package jsource.gui;


/*
 * AntWorkListener.java
 * modified by Copyright (C) 2001 Klaus Hartlage from
 * org.apache.tools.ant.Main;
 * webmaster@hartmath.org
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999, 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

import org.apache.tools.ant.*;
import jsource.console.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.Color;


public class AntWorkListener extends Thread {

    /** The default build file name */
    public static final String DEFAULT_BUILD_FILENAME = "build.xml";

    /** Our current message output status. Follows Project.MSG_XXX */
    private int msgOutputLevel = Project.MSG_INFO;

    /** File that we are using for configuration */
    private File buildFile = null;

    /** Stream that we are using for logging */
    private Writer out = null;

    /** Stream that we are using for logging error messages */
    private Writer err = null;

    /** We are using the Jext console for message output */
    private Console console = null;

    private AntWorkPanel antWorkPanel = null;
    private AntWorkMessage antWorkMessage = null;

    /** The build targets */
    private Vector targets = new Vector(5);

    /** Set of properties that can be used by tasks */
    private Properties definedProps = new Properties();

    /** Names of classes to add as listeners to project */
    private Vector listeners = new Vector(5);

    /**
     * The Ant logger class. There may be only one logger. It will have the right to use
     * the 'out' BufferedWriter. The class must implements the BuildLogger interface.
     */
    private String loggerClassname = null;

    /**
     * Indicates if this ant should be run.
     */
    private boolean readyToRun = false;

    /**
     * Indicates we should only parse and display the project help information
     */
    private boolean projectHelp = false;

    /**
     * Prints the message of the Throwable if it's not null.
     */
    private static void printMessage(Throwable t) {
        String message = t.getMessage();

        if (message != null) {
            System.err.println(message + "\n");
        }
    }

    /**
     * Command line entry point. This constructor kicks off the building
     * of a project object and executes a build using either a given
     * target or the default target.
     *
     * @param args Command line args.
     */
    protected AntWorkListener(MainFrame main,
            String[]  args,
            Console   cons) throws BuildException {
        // cycle through given args
        console = cons;
        antWorkPanel = new AntWorkPanel(main);
        antWorkMessage = null;
        out = console.getStdOut();
        err = console.getStdErr();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equals("-help")) {
                printUsage(console);
                return;
            } else if (arg.equals("-version")) {
                printVersion();
                return;
            } else if (arg.equals("-quiet") || arg.equals("-q")) {
                msgOutputLevel = Project.MSG_WARN;
            } else if (arg.equals("-verbose") || arg.equals("-v")) {
                printVersion();
                msgOutputLevel = Project.MSG_VERBOSE;
            } else if (arg.equals("-debug")) {
                printVersion();
                msgOutputLevel = Project.MSG_DEBUG;
            } else if (arg.equals("-buildfile") || arg.equals("-file")
                    || arg.equals("-f")) {
                try {
                    buildFile = new File(args[i + 1]);
                    i++;
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    String msg = "You must specify a buildfile when "
                            + "using the -buildfile argument";

                    System.out.println(msg);
                    return;
                }
            } else if (arg.equals("-listener")) {
                try {
                    listeners.addElement(args[i + 1]);
                    i++;
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    String msg = "You must specify a classname when "
                            + "using the -listener argument";

                    System.out.println(msg);
                    return;
                }
            } else if (arg.startsWith("-D")) {

                /* Interestingly enough, we get to here when a user
                 * uses -Dname=value. However, in some cases, the JDK
                 * goes ahead * and parses this out to args
                 *   {"-Dname", "value"}
                 * so instead of parsing on "=", we just make the "-D"
                 * characters go away and skip one argument forward.
                 *
                 * I don't know how to predict when the JDK is going
                 * to help or not, so we simply look for the equals sign.
                 */

                String name = arg.substring(2, arg.length());
                String value = null;
                int posEq = name.indexOf("=");

                if (posEq > 0) {
                    value = name.substring(posEq + 1);
                    name = name.substring(0, posEq);
                } else if (i < args.length)
                    value = args[++i];

                definedProps.put(name, value);
            } else if (arg.equals("-logger")) {
                if (loggerClassname != null) {
                    System.out.println("Only one logger class may be specified.");
                    return;
                }
                loggerClassname = args[++i];
            } else if (arg.equals("-projecthelp")) {
                // set the flag to display the targets and quit
                projectHelp = true;
            } else if (arg.startsWith("-")) {
                // we don't have any more args to recognize!
                String msg = "Unknown arg: " + arg;

                System.out.println(msg);
                printUsage(console);
                return;
            } else {
                // if it's no other arg, it may be the target
                targets.addElement(arg);
            }

        }

        // if buildFile was not specified on the command line,
        // then search for it
        if (buildFile == null) {
            buildFile = findBuildFile(DEFAULT_BUILD_FILENAME);
        }

        // make sure buildfile exists
        if (!buildFile.exists()) {
            throw new BuildException("Build failed");
        }

        // make sure it's not a directory (this falls into the ultra
        // paranoid lets check everything category)
        if (buildFile.isDirectory()) {
            throw new BuildException("Build failed");
        }

        readyToRun = true;
    }

    protected void finalize() throws Throwable {
        out.flush();
        out.close();
    }

    /**
     * Helper to get the parent file for a given filename.
     *
     * <P>Added to simulate File.getParentFile() from JDK 1.2.
     *
     * @param filename  File name
     * @return          Parent file or null if none
     */
    private File getParentFile(String filename) {
        return getParentFile(new File(filename));
    }

    /**
     * Helper to get the parent file for a given file.
     *
     * <P>Added to simulate File.getParentFile() from JDK 1.2.
     *
     * @param file   File
     * @return       Parent file or null if none
     */
    private File getParentFile(File file) {
        String filename = file.getAbsolutePath();

        file = new File(filename);
        filename = file.getParent();

        if (filename != null && msgOutputLevel >= Project.MSG_VERBOSE) {
            System.out.println("Searching in " + filename);
        }

        return (filename == null) ? null : new File(filename);
    }

    /**
     * Search parent directories for the build file.
     *
     * <P>Takes the given target as a suffix to append to each
     *    parent directory in seach of a build file.  Once the
     *    root of the file-system has been reached an exception
     *    is thrown.
     *
     * @param suffix    Suffix filename to look for in parents.
     * @return          A handle to the build file
     *
     * @exception BuildException    Failed to locate a build file
     */
    private File findBuildFile(String suffix) throws BuildException {
        if (msgOutputLevel >= Project.MSG_INFO) {
            System.out.println("Searching for " + suffix + " ...");
        }

        File parent = getParentFile(suffix);
        File file = new File(parent, suffix);

        // check if the target file exists in the current directory
        while (!file.exists()) {
            // change to parent directory
            parent = getParentFile(parent);

            // if parent is null, then we are at the root of the fs,
            // complain that we can't find the build file.
            if (parent == null) {
                throw new BuildException("Could not locate a build file!");
            }

            // refresh our file handle
            file = new File(parent, suffix);
        }

        return file;
    }

    /**
     * Executes the build.
     */
    public void run() throws BuildException {
        try {
            if (!readyToRun) {
                return;
            }
            // track when we started
            if (msgOutputLevel >= Project.MSG_INFO) {
                System.out.println("Buildfile: " + buildFile);
            }

            Project project = new Project();

            Throwable error = null;

            addBuildListeners(project, out, console);

            project.init();

            // set user-define properties
            Enumeration e = definedProps.keys();

            while (e.hasMoreElements()) {
                String arg = (String) e.nextElement();
                String value = (String) definedProps.get(arg);

                project.setUserProperty(arg, value);
            }

            project.setUserProperty("ant.file", buildFile.getAbsolutePath());

            // first use the ProjectHelper to create the project object
            // from the given build file.
            try {
                Class.forName("javax.xml.parsers.SAXParserFactory");
                ProjectHelper.configureProject(project, buildFile);
            } catch (NoClassDefFoundError ncdfe) {
                throw new BuildException("No JAXP compliant XML parser found. See http://java.sun.com/xml for the\nreference implementation.", ncdfe);
            } catch (ClassNotFoundException cnfe) {
                throw new BuildException("No JAXP compliant XML parser found. See http://java.sun.com/xml for the\nreference implementation.", cnfe);
            } catch (NullPointerException npe) {
                throw new BuildException("No JAXP compliant XML parser found. See http://java.sun.com/xml for the\nreference implementation.", npe);
            }

            // make sure that we have a target to execute
            if (targets.size() == 0) {
                targets.addElement(project.getDefaultTarget());
            }

            // if (projectHelp) {
            // printTargets(project);
            // } else {
            // actually do some work
            project.executeTargets(targets);
            // }
        } catch (org.apache.tools.ant.BuildException exp) {
            console.append("\nBuildException: " + exp.toString(), Color.red);
            // exp.printStackTrace( System.out );
        } catch (RuntimeException exc) {
            console.append("\nRuntimeException: " + exc.toString(), Color.red);
            exc.printStackTrace(System.out);
        } catch (Error err) {
            console.append("\nError: " + err.toString(), Color.red);
            err.printStackTrace(System.out);
        } finally {// buildFinished(error);
        }
        console.displayPrompt();
    }

    protected void addBuildListeners(Project project,
            Writer output, Console console) {
        // Add the default listener
        project.addBuildListener(createLogger(output, console));

        for (int i = 0; i < listeners.size(); i++) {
            String className = (String) listeners.elementAt(i);

            try {
                BuildListener listener =
                        (BuildListener) Class.forName(className).newInstance();

                project.addBuildListener(listener);
            } catch (Exception exc) {
                throw new BuildException("Unable to instantiate listener " + className,
                        exc);
            }
        }
    }

    /**
     *  Creates the default build logger for sending build events to the ant log.
     */
    private BuildLogger createLogger(Writer output, Console console) {
        BuildLogger logger = null;

        logger = new AntWorkLogger(output, console);

        logger.setMessageOutputLevel(msgOutputLevel);

        return logger;
    }

    /**
     * Prints the usage of how to use this class to System.out
     */
    private static void printUsage(Console console) {
        String lSep = System.getProperty("line.separator");
        StringBuffer msg = new StringBuffer();

        msg.append("ant [options] [target [target2 [target3] ...]]" + lSep);
        msg.append("Options: " + lSep);
        msg.append("  -help                  print this message" + lSep);
        msg.append("  -projecthelp           print project help information" + lSep);
        msg.append("  -version               print the version information and exit" + lSep);
        msg.append("  -quiet                 be extra quiet" + lSep);
        msg.append("  -verbose               be extra verbose" + lSep);
        msg.append("  -debug                 print debugging information" + lSep);
        // msg.append("  -emacs                 produce logging information without adornments" + lSep);
        // msg.append("  -logfile <file>        use given file for log" + lSep);
        // msg.append("  -logger <classname>    the class which is to perform logging" + lSep);
        // msg.append("  -listener <classname>  add an instance of class as a project listener" + lSep);
        msg.append("  -buildfile <file>      use given buildfile" + lSep);
        msg.append("  -D<property>=<value>   use value for given property" + lSep);
        console.append(msg.toString(), Color.black);
    }

    private static void printVersion() {
        try {
            Properties props = new Properties();
            InputStream in = Main.class.getResourceAsStream("/org/apache/tools/ant/version.txt");

            props.load(in);
            in.close();

            String lSep = System.getProperty("line.separator");
            StringBuffer msg = new StringBuffer();

            msg.append("Ant version ");
            msg.append(props.getProperty("VERSION"));
            msg.append(" compiled on ");
            msg.append(props.getProperty("DATE"));
            msg.append(lSep);
            System.out.println(msg.toString());
        } catch (IOException ioe) {
            System.err.println("Could not load the version information.");
            System.err.println(ioe.getMessage());
        } catch (NullPointerException npe) {
            System.err.println("Could not load the version information.");
        }
    }

    /**
     * Print out a list of all targets in the current buildfile
     */
    public static void printTargets(Project project,
            DefaultListModel resultModel) {
        // find the target with the longest name
        int maxLength = 0;
        Enumeration ptargets = project.getTargets().elements();
        String targetName;
        String targetDescription;
        Target currentTarget;
        // split the targets in top-level and sub-targets depending
        // on the presence of a description
        Vector topNames = new Vector();
        Vector topDescriptions = new Vector();
        Vector subNames = new Vector();

        while (ptargets.hasMoreElements()) {
            currentTarget = (Target) ptargets.nextElement();
            targetName = currentTarget.getName();
            targetDescription = currentTarget.getDescription();
            // maintain a sorted list of targets
            if (targetDescription == null) {
                int pos = findTargetPosition(subNames, targetName);

                subNames.insertElementAt(targetName, pos);
            } else {
                int pos = findTargetPosition(topNames, targetName);

                topNames.insertElementAt(targetName, pos);
                topDescriptions.insertElementAt(targetDescription, pos);
                if (targetName.length() > maxLength) {
                    maxLength = targetName.length();
                }
            }
        }
        addTargets(resultModel, topNames, topDescriptions, "Maintarget");
        addTargets(resultModel, subNames, null, "Subtarget");
    }

    /**
     * Search for the insert position to keep names a sorted list of Strings
     */
    private static int findTargetPosition(Vector names, String name) {
        int res = names.size();

        for (int i = 0; i < names.size() && res == names.size(); i++) {
            if (name.compareTo((String) names.elementAt(i)) < 0) {
                res = i;
            }
        }
        return res;
    }

    private static void addTargets(DefaultListModel resultModel,
            Vector names,
            Vector descriptions,
            String heading) {
        StringBuffer msg;

        for (int i = 0; i < names.size(); i++) {
            msg = new StringBuffer();
            msg.append(names.elementAt(i));
            msg.append(" - [");
            msg.append(heading);
            msg.append("]");
            resultModel.addElement(msg.toString());
        }
    }

    /**
     * Output a formatted list of target names with an optional description
     */
    private static String printTargets(Vector names,
            Vector descriptions,
            String heading, int maxlen) {
        // now, start printing the targets and their descriptions
        String lSep = System.getProperty("line.separator");
        // got a bit annoyed that I couldn't find a pad function
        String spaces = "    ";

        while (spaces.length() < maxlen) {
            spaces += spaces;
        }
        StringBuffer msg = new StringBuffer();

        msg.append(heading + lSep + lSep);
        for (int i = 0; i < names.size(); i++) {
            msg.append(" ");
            msg.append(names.elementAt(i));
            if (descriptions != null) {
                msg.append(spaces.substring(0,
                        maxlen - ((String) names.elementAt(i)).length() + 2));
                msg.append(descriptions.elementAt(i));
            }
            msg.append(lSep);
        }
        return msg.toString();
    }

    protected static String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;

        if (minutes > 0) {
            return Long.toString(minutes) + " minute"
                    + (minutes == 1 ? " " : "s ") + Long.toString(seconds % 60)
                    + " second" + (seconds % 60 == 1 ? "" : "s");
        } else {
            return Long.toString(seconds) + " second"
                    + (seconds % 60 == 1 ? "" : "s");
        }

    }

    protected static String lSep = System.getProperty("line.separator");

    /**
     *  Writes build event to a BufferedWriter. Currently, it
     *  only writes which targets are being executed, and
     *  any messages that get logged.
     *  If a javac error is produced it writes the error to the AntWork error-list
     */
    private class AntWorkLogger implements BuildLogger {

        protected Writer out;
        protected Writer err;
        protected int msgOutputLevel;
        private long startTime = System.currentTimeMillis();
        Console console;
        PrintWriter print_err;

        public AntWorkLogger(Writer output, Console cons) {
            out = output;
            err = output;
            console = cons;
            print_err = new PrintWriter(cons.getStdErr());
        }

        /**
         * Set the msgOutputLevel this logger is to respond to.
         *
         * Only messages with a message level lower than or equal to the given level are
         * output to the log.
         *
         * @param level the logging level for the logger.
         */
        public void setMessageOutputLevel(int level) {
            this.msgOutputLevel = level;
        }

        /**
         * Undefined method.
         * @param output the output stream for the logger.
         */
        public void setOutputPrintStream(PrintStream output) {}

        public void setOutputBufferedWriter(BufferedWriter output) {
            this.out = output;
        }

        /**
         * Undefined method.
         * @param err the error stream for the logger.
         */
        public void setErrorPrintStream(PrintStream output) {}

        public void setErrorBufferedWriter(BufferedWriter err) {
            this.err = err;
        }

        /**
         * This method is undefined.
         *
         * @param emacsMode true if output is to be unadorned so that emacs and other
         * editors can parse files names, etc.
         */
        public void setEmacsMode(boolean emacsMode) {}

        public void buildStarted(BuildEvent event) {
            startTime = System.currentTimeMillis();
        }

        /**
         *  Prints whether the build succeeded or failed, and
         *  any errors that occured during the build.
         */
        public void buildFinished(BuildEvent event) {
            try {
                Throwable error = event.getException();

                if (error == null) {
                    out.write(lSep + "BUILD SUCCESSFUL\n");
                } else {
                    err.write(lSep + "BUILD FAILED" + lSep + "\n");

                    if (error instanceof BuildException) {
                        err.write("\n" + error.toString() + "\n");

                        Throwable nested = ((BuildException) error).getException();

                        if (nested != null) {
                            nested.printStackTrace(print_err);
                        }
                    } else {
                        error.printStackTrace(print_err);
                    }
                }
                out.write(lSep + "Total time: " + formatTime(System.currentTimeMillis() - startTime));
            } catch (IOException ioe) {
                console.append("buildFinished::IOException: " + ioe.toString(),
                        Color.red);
            }
        }

        public void targetStarted(BuildEvent event) {
            try {
                if (msgOutputLevel <= Project.MSG_INFO) {
                    out.write(lSep + event.getTarget().getName() + ":");
                }
            } catch (IOException ioe) {
                console.append("targetStarted::IOException: " + ioe.toString(),
                        Color.red);
            }
        }

        public void targetFinished(BuildEvent event) {}

        public void taskStarted(BuildEvent event) {}

        public void taskFinished(BuildEvent event) {}

        public void messageLogged(BuildEvent event) {
            // Filter out messages based on priority
            if (event.getPriority() <= msgOutputLevel) {
                // Determine if we are in the javac task
                if (event.getTask() != null) {
                    String name = event.getTask().getTaskName();

                    if (name.compareTo("javac") == 0) {
                        String line = event.getMessage();

                        if (isMessageLine(line)) {
                            antWorkMessage = parseJavacMessage(line);
                        } else {
                            int column = getLocationPos(line);

                            if ((column >= 0) && (antWorkMessage != null)) {
                                antWorkMessage.setColumn(column);
                                antWorkPanel.append(antWorkMessage);
                                antWorkMessage = null;
                            }
                        }
                        return;
                    }
                }
                // Print the message to the console:
                console.append(event.getMessage() + "\n", Color.red);
            }
        }

        private AntWorkMessage parseJavacMessage(String line) {
            int p1 = line.indexOf(".java:");

            if (p1 >= 0) p1 += 5;
            int p2 = line.indexOf(":", p1 + 1);

            if (p1 >= 0 && p2 >= 0) {
                int p0 = line.indexOf("[javac]");

                if (p0 >= 0) p0 += 7;
                // only if ".java:" and a second ":" are found
                // the line includes a correct message to display

                // extract the filename
                String f = line.substring(p0 >= 0 ? p0 : 0, p1).trim();
                // get the line number
                String l = line.substring(p1 + 1, p2).trim();
                // and finally get the message itself
                String m = line.substring(p2 + 1).trim();

                // return a new error message for the error list:
                return new AntWorkMessage(f, Integer.parseInt(l), 1, m);
            }
            return null;
        }

        // returns true if the passed string includes a '.java:' token followed
        // by an integer followed by a ':'
        // thats the way simple messages are formatted by javac
        private boolean isMessageLine(String line) {
            int p1 = line.indexOf(".java:");

            if (p1 >= 0) p1 += 5;
            int p2 = line.indexOf(":", p1 + 1);

            if (p1 >= 0 && p2 >= 0) {
                try {
                    Integer.parseInt(line.substring(p1 + 1, p2).trim());
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            // check for last line of the format "LineNr String" like "2 warnings"
            int x = line.indexOf(' ');

            try {
                if (x > 0)
                    Integer.parseInt(line.substring(0, x)); // line starts with a number??
                String s = line.substring(x + 1).toLowerCase();

                if (s.startsWith("error") || s.startsWith("warning"))
                    return true;
            } catch (NumberFormatException e) {}
            return false;
        }

        private int getLocationPos(String line) {
            return line.indexOf('^');
        }
    }
}
