package jsource.console;


/**
 * Console.java	07/08/03
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 */

import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.BadLocationException;

import jsource.gui.*;
import jsource.io.*;
import jsource.util.*;
import jsource.io.localization.*;

/**
 * <code>Console</code> is an internal JSource console which provides different kinds of
 * prompts and allows the execution of both internal and external (OS specific) commands.
 * The console is embedded in a <code>JScrollPane</code> and handles itself.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class Console2 extends JScrollPane {

    /** DOS prompt */
    public static final int DOS_PROMPT = 0;

    /** JSource prompt */
    public static final int JSOURCE_PROMPT = 1;

    /** Linux prompt */
    public static final int LINUX_PROMPT = 2;

    /** SunOS prompt */
    public static final int SUNOS_PROMPT = 3;

    /** Default prompt types: DOS, JSource, Linux and SunOS **/
    public static final String[] DEFAULT_PROMPTS = { "$p >", "$u@$p >", "$u@$h$$ ", "$h% " };

    // current separators used in command lines
    private static final String COMPLETION_SEPARATORS = " \t;:/\\\"\'";

    // commands
    private Command currentCmd = null;
    private Command firstCmd = null;
    private Command evalCom = null;
    // parent
    private MainFrame parent = null;
    // processes specific
    private Process process = null;
    private String processName = null;
    private StdoutThread stdout = null;
    private StderrThread stderr = null;
    // private fields
    private String cmd = null;
    private String current = null;
    private XMLResourceBundle bundle = null;
    private Document outputDocument = null;
    private ConsoleTextPane textArea = null;
    private HistoryModel historyModel = new HistoryModel(25);
    private int userLimit = 0, typingLocation = 0, index = -1;
    // colors
    public Color errorColor = Color.red;
    public Color promptColor = Color.blue;
    public Color outputColor = Color.black;
    public Color infoColor = new Color(0, 165, 0);
    // prompt
    private boolean displayPath = false;
    private String prompt, hostName, oldPath = new String();
    private String promptPattern = DEFAULT_PROMPTS[DOS_PROMPT];

    /**
     * Instanciates a new console displaying prompt.
     * @param parent <code>MainFrame</code> parent
     */
    public Console2(MainFrame parent, XMLResourceBundle bundle) {
        this(parent, bundle, true);
    }

    /**
     * Creates a new console, embedding it in a <code>JScrollPane</code>.
     * @param parent <code>MainFrame</code> parent
     * @param display if set on true, prompt is displayed
     */
    public Console2(MainFrame parent, XMLResourceBundle bundle, boolean display) {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.parent = parent;
        this.bundle = bundle;

        // load commands from previous sessions
        load();

        textArea = new ConsoleTextPane(this);
        textArea.setFont(JSConstants.TERMINAL_FONT);
        outputDocument = textArea.getDocument();
        String welcome = bundle.getValueOf("console.welcome");
		setErrorColor(GUIUtilities.parseColor(bundle.getValueOf("console.errorColor")));
		setPromptColor(GUIUtilities.parseColor(bundle.getValueOf("console.promptColor")));
		setOutputColor(GUIUtilities.parseColor(bundle.getValueOf("console.outputColor")));
		setInfoColor(GUIUtilities.parseColor(bundle.getValueOf("console.infoColor")));
		setBgColor(GUIUtilities.parseColor(bundle.getValueOf("console.bgColor")));
		setSelectionColor(GUIUtilities.parseColor(bundle.getValueOf("console.selectionColor")));
        append(welcome, infoColor, false, false);

        if (display)
            displayPrompt();

        getViewport().setView(textArea);
        FontMetrics fm = getFontMetrics(textArea.getFont());

        setPreferredSize(new Dimension(40 * fm.charWidth('m'), 6 * fm.getHeight()));
        setMinimumSize(getPreferredSize());
        setMaximumSize(getPreferredSize());

        initCommands();
    }

    /**
     * Returns the parent of this <code>Console</code>.
     * @return parent the <code>MainFrame</code> parent
     */
    public MainFrame getParentFrame() {
        return parent;
    }

    /**
     * Return the <code>Document</code> in which output is performed.
     */
    public Document getOutputDocument() {
        return outputDocument;
    }

    /**
     * Adds a command to the linked list of commands.
     * @param command the <code>Command</code> to add
     */
    public void addCommand(Command command) {
        if (command == null)
            return;

        currentCmd.next = command;
        currentCmd = command;
    }

    /**
     * Return true if the command is built-in. If the command is built-in, it is also executed.
     * @param command the <code>Command</code> to check and execute
     */
    private boolean builtInCommand(String command) {
        boolean ret = false;
        Command _currentCmd = firstCmd;

        while (_currentCmd != null) {
            if (_currentCmd.handleCommand(this, command)) {
                ret = true;
                break;
            }
            _currentCmd = _currentCmd.next;
        }

        return ret;
    }

    private void initCommands() {
        firstCmd = currentCmd = new HelpCommand(bundle);
        addCommand(new ChangeDirCommand(bundle));
        addCommand(new ClearCommand(bundle));
        addCommand(new ListCommand(bundle));
        addCommand(new PwdCommand(bundle));
        addCommand(new ExitCommand(parent, bundle));
        addCommand(new HomeCommand(bundle));
    }

    /**
     * Sets the background color of the console.
     * @param color <code>Color</code> to be used
     */
    public void setBgColor(Color color) {
        textArea.setBackground(color);
    }

    /**
     * Sets console error color.
     * @param color <code>Color</code> to be used
     */
    public void setErrorColor(Color color) {
        errorColor = color;
    }

    /**
     * Sets console prompt color.
     * @param color <code>Color</code> to be used
     */
    public void setPromptColor(Color color) {
        promptColor = color;
    }

    /**
     * Sets console output color.
     * @param color <code>Color</code> to be used
     */
    public void setOutputColor(Color color) {
        outputColor = color;
        textArea.setForeground(color);
        textArea.setCaretColor(color);
    }

    /**
     * Sets console info color.
     * @param color <code>Color</code> to be used
     */

    public void setInfoColor(Color color) {
        infoColor = color;
    }

    /**
     * Sets console selection color.
     * @param color <code>Color</code> to be used
     */
    public void setSelectionColor(Color color) {
        textArea.setSelectionColor(color);
    }

    /**
     * Saves the history.
     */
    public void save() {
        for (int i = 0; i < historyModel.getSize(); i++)
            MainFrame.setProperty("console.history." + i, historyModel.getItem(i));
    }

    /**
     * Loads the last saved history.
     */
    public void load() {
        String historyItem = null;

        for (int i = 24; i >= 0; i--) {
            historyItem = MainFrame.getProperty("console.history." + i);
            if (historyItem != null)
                historyModel.addItem(historyItem);
        }
    }

    /**
     * Sets the prompt pattern.
     * @param type The prompt pattern
     */
    public void setPromptPattern(String prompt) {
        if (prompt == null)
            return;

        promptPattern = prompt;
        displayPath = false;
        buildPrompt();
    }

    /**
     * Gets prompt pattern.
     */
    public String getPromptPattern() {
        return promptPattern;
    }

    /**
     * Displays the prompt according to the current selected prompt type.
     */
    public void displayPrompt() {
        if (prompt == null || displayPath)
            buildPrompt();

        append('\n' + prompt, promptColor);
        typingLocation = userLimit = outputDocument.getLength();
    }

    // builds the prompt according to the prompt pattern
    private void buildPrompt() {
        if (displayPath && oldPath.equals(JSConstants.USER_DIR))
            return;

        displayPath = false;
        StringBuffer buf = new StringBuffer();

        if (hostName == null) {
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException uhe) {}
        }

        for (int i = 0; i < promptPattern.length(); i++) {
            char c = promptPattern.charAt(i);

            switch (c) {
            case '$':
                if (i == promptPattern.length() - 1)
                    buf.append(c);
                else {
                    switch (promptPattern.charAt(++i)) {
                    case 'p':                    // current path
                        buf.append(System.getProperty("user.dir"));
                        displayPath = true;
                        break;

                    case 'u':                    // user name
                        buf.append(JSConstants.USER_NAME);
                        break;

                    case 'h':                    // host name
                        buf.append(hostName);
                        break;

                    case '$':
                        buf.append('$');
                        break;
                    }
                }
                break;

            default:
                buf.append(c);
            }
        }

        prompt = buf.toString();
    }

    /**
     * Appends text in the text area.
     * @param text the text to append
     * @param color the <code>Color</code> of the text
     * @param italic set to true will append italic text
     * @param bold set to true will append bold text
     */
    private void append(String text, Color color, boolean italic, boolean bold) {
        SimpleAttributeSet style = new SimpleAttributeSet();

        if (color != null)
            style.addAttribute(StyleConstants.Foreground, color);
        StyleConstants.setBold(style, bold);
        StyleConstants.setItalic(style, italic);

        try {
            outputDocument.insertString(outputDocument.getLength(), text, style);
        } catch (BadLocationException bl) {}

        textArea.setCaretPosition(outputDocument.getLength());
    }

    /**
     * Appends text in the text area.
     * @param text the text to append in the text area
     * @param color the <code>Color</code> of the text to append
     */
    public void append(String text, Color color) {
        append(text, color, false, false);
    }

    /**
     * Adds a command to the history.
     * @param command the command to add in the history
     */
    public void addHistory(String command) {
        historyModel.addItem(command);
        index = -1;
    }

    /**
     * Removes a char from current command line.
     * Stands for BACKSPACE action.
     */
    public void removeChar() {
        try {
            int pos = textArea.getCaretPosition();

            if (pos <= typingLocation && pos > userLimit) {
                outputDocument.remove(pos - 1, 1);
                typingLocation--;
            }
        } catch (BadLocationException ble) {}
    }

    /**
     * Deletes a char from command line.
     * Stands for DELETE action.
     */
    public void deleteChar() {
        try {
            int pos = textArea.getCaretPosition();

            if (pos == outputDocument.getLength()) return;
            if (pos < typingLocation && pos >= userLimit) {
                outputDocument.remove(pos, 1);
                typingLocation--;
            }
        } catch (BadLocationException ble) {}
    }

    /**
     * Adds a <code>String</code> to the current command line.
     * @param add <code>String</code> to be added
     */
    public void add(String add) {
        try {
            int pos = textArea.getCaretPosition();

            if (pos <= typingLocation && pos >= userLimit)
                outputDocument.insertString(pos, add, null);
            typingLocation += add.length();
        } catch (BadLocationException ble) {}
    }

    /**
     * Returns the position in characters at which
     * user is allowed to type his commands.
     * @return beginning of user typing space
     */
    public int getUserLimit() {
        return userLimit;
    }

    /**
     * Returns the position of the end of the console prompt.
     */
    public int getTypingLocation() {
        return typingLocation;
    }

    /**
     * Completes current filename if possible.
     */
    public void doCompletion() {
        int index = 0;
        int caret = textArea.getCaretPosition() - userLimit;

        String wholeText = getText();
        String text;

        try {
            text = outputDocument.getText(userLimit, caret);
        } catch (BadLocationException ble) {
            return;
        }

        for (int i = text.length() - 1; i >= 0; i--) {
            if (COMPLETION_SEPARATORS.indexOf(text.charAt(i)) != -1) {
                if (i == index)
                    return;
                index = i + 1;
                break;
            }
        }

        String current = text.substring(index);

        java.util.ArrayList matching = new java.util.ArrayList();
        String[] files = Utilities.getWildCardMatches("*", true);

        if (files == null)
            return;

        for (int i = 0; i < files.length; i++) {
            if (files[i].startsWith(current)) {
                matching.add(files[i]);
            }
        }

        if (matching.size() == 0)
            return;

        int length = 0;
        int _length = 0;
        int mIndex = 0;

        for (int i = 0; i < matching.size(); i++) {
            _length = ((String) matching.get(i)).length();
            length = length < _length ? _length : length;
            if (length == _length)
                mIndex = i;
        }

        char c;
        boolean isSame = true;
        int diffIndex = length;

        String compare;
        String source = (String) matching.get(mIndex);

        for (int i = 0; i < length; i++) {
            c = source.charAt(i);

            for (int j = 0; j < matching.size(); j++) {
                if (j == mIndex)
                    continue;

                compare = (String) matching.get(j);
                if (i >= compare.length())
                    continue;

                isSame = (compare.charAt(i) == c);
            }

            if (!isSame) {
                diffIndex = i;
                break;
            }
        }

        compare = text.substring(0, index) + source.substring(0, diffIndex);
        setText(compare + wholeText.substring(caret));
        textArea.setCaretPosition(userLimit + compare.length());
    }

    /**
     * Searches backward in the history for a matching command,
     * according to the command typed in the user typing space.
     */
    public void doBackwardSearch() {
        String text = getText();

        if (text == null) {
            historyPrevious();
            return;
        }

        for (int i = index + 1; i < historyModel.getSize(); i++) {
            String item = historyModel.getItem(i);

            if (item.startsWith(text)) {
                setText(item);
                index = i;
                return;
            }
        }
    }

    /**
     * Gets previous item in the history list.
     */
    public void historyPrevious() {
        if (index == historyModel.getSize() - 1)
            getToolkit().beep();
        else if (index == -1) {
            current = getText();
            setText(historyModel.getItem(0));
            index = 0;
        } else {
            int newIndex = index + 1;

            setText(historyModel.getItem(newIndex));
            index = newIndex;
        }
    }

    /**
     * Gets next item in the history list.
     */
    public void historyNext() {
        if (index == -1)
            getToolkit().beep();
        else if (index == 0)
            setText(current);
        else {
            int newIndex = index - 1;

            setText(historyModel.getItem(newIndex));
            index = newIndex;
        }
    }

    /**
     * Sets user's command line content.
     * @param text the text to be put on command line.
     */
    public void setText(String text) {
        try {
            outputDocument.remove(userLimit, typingLocation - userLimit);
            outputDocument.insertString(userLimit, text, null);
            typingLocation = outputDocument.getLength();
            index = -1;
        } catch (BadLocationException ble) {}
    }

    /**
     * Returns current command line.
     */
    public String getText() {
        try {
            return outputDocument.getText(userLimit, typingLocation - userLimit);
        } catch (BadLocationException ble) {}
        return null;
    }

    /**
     * Displays a message using output color.
     * @param display <code>String</code> to be displayed
     */
    public void output(String display) {
        append(JSConstants.LINE_SEP + display, outputColor, false, false);
    }

    /**
     * Displays console help.
     */
    public void help() {
        Command _current = firstCmd;
        StringBuffer buf = new StringBuffer();

        while (_current != null) {
			String cmdName = _current.getCommandName();
			if (!cmdName.equals("help")) {
            	buf.append("   - ").append(cmdName);
            	buf.append(Utilities.createWhiteSpace(30 - cmdName.length())).append('(');
			}
            buf.append(_current.getCommandSummary());
            if (!cmdName.equals("help")) {
				buf.append(')');
			}
			buf.append(JSConstants.LINE_SEP);
            _current = _current.next;
        }

        help(buf.toString());
    }

    /**
     * Displays a message using help color.
     * @param display <code>String</code> to be displayed
     */
    public void help(String display) {
        append(JSConstants.LINE_SEP + display, infoColor, true, false);
    }

    /**
     * Display a message using error color.
     * @param display <code>String</code> to be displayed
     */
    public void error(String display) {
        append(JSConstants.LINE_SEP + display, errorColor, false, false);
    }

    /**
     * Stops current task.
     */
    public void stop() {
        if (stdout != null) {
            stdout.interrupt();
            stdout = null;
        }

        if (stderr != null) {
            stderr.interrupt();
            stderr = null;
        }

        if (process != null) {
            process.destroy();
            Object[] args = { processName };

            error(bundle.getValueOf("console.killed"));
            processName = null;
            process = null;
        }
    }

    /**
     * Parse a command. Replace internal variables by their values.
     * @param command Command to be parsed
     */
    public String parseCommand(String command) {
        String file;
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < command.length(); i++) {
            char c = command.charAt(i);

            switch (c) {
            case '$':
                if (i == command.length() - 1)
                    buf.append(c);
                else {
                    switch (command.charAt(++i)) {
                    case 'f':                    // current opened file (absolute path)
                        file = parent.currFile.getFile().getAbsolutePath();
                        if (file != null)
                            buf.append(file);
                        break;

                    case 'd':                    // user directory
                        buf.append(parent.currentDir);
                        break;

                    case 'p':                    // current opened file name
                        buf.append(parent.currFile.getFileName());
                        break;

                    case 'e':                    // current opened file name without extension
                        file = parent.currFile.getFileName();
                        int index = file.lastIndexOf('.');

                        if (index != -1 && index + 1 < file.length())
                            buf.append(file.substring(0, index));
                        else
                            buf.append(file);
                        break;

                    case 'n':                    // current opened file directory
                        file = parent.currFile.getFileDir();
                        if (file != null)
                            buf.append(file.substring(0, file.lastIndexOf(File.separator)));
                        break;

                    case 'h':                    // home dir
                        buf.append(JSConstants.USER_HOME);
                        break;

                    case 'j':                    // jsource dir
                        buf.append(JSConstants.USER_DIR);
                        break;

                    case 's':                    // selected text
                        buf.append(parent.editor.getSelectedText());
                        break;

                    case '$':
                        buf.append('$');
                        break;
                    }
                }
                break;

            default:
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * Execute command. First parse it then check if command
     * is built-in. At last, a process is created and threads
     * which handle output streams are started.
     * @param command Command to be execute
     */
    public void execute(String command) {
		cmd = command;
        stop();
        if (command.length() == 0 || command == null)
            return;

        // check to see if in "jython" mode...
        // boolean isJython = Jext.getBooleanProperty("console.jythonMode");

        // if in jython mode, look for '!' as first charater...means
        // treat it like a "normal console" command
        /* if (isJython) {
         if (!command.startsWith("!")) {
         if (command.startsWith("?")) {  // kludge shorthand for 'print'
         String ts = command.substring(1);

         command = "print " + ts;
         } else if (command.startsWith("exit")) {
         Jext.setProperty("console.jythonMode", "off");
         displayPrompt();
         return;
         }

         evalCom.handleCommand(this, "eval:" + command);
         displayPrompt();
         return;

         } else {
         String ts = command.substring(1);

         command = ts;
         }
         }*/

        int index = command.indexOf(' ');

        if (index != -1)
            processName = command.substring(0, index);
        else
            processName = command;

        command = parseCommand(command);
        if (command == null || command.length() == 0)
            return;

        if (builtInCommand(command)) {
            displayPrompt();
            return;
        }

        append(JSConstants.LINE_SEP + "> " + command, infoColor);
        try {
            if (JSConstants.JAVA_VERSION.charAt(2) < '3')
                process = Runtime.getRuntime().exec(command);
            else
                process = Runtime.getRuntime().exec(command, null, new File(JSConstants.USER_DIR));
            process.getOutputStream().close();
        } catch (IOException ioe) {
            error(bundle.getValueOf("console.error"));
            displayPrompt();
            return;
        }

        stdout = new StdoutThread();
        stderr = new StderrThread();

        if (process == null)
            displayPrompt();
    }

    class StdoutThread extends Thread {
        StdoutThread() {
            super("----thread: stout: jsource----");
            start();
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;

                while ((line = in.readLine()) != null)
                    output(line);
                in.close();

                int exitCode = process.waitFor();
                Object[] args = { processName, new Integer(exitCode) };

                append(JSConstants.LINE_SEP + bundle.getValueOf("console.exited") + " " + cmd, infoColor);

                Thread.sleep(500);
                process.destroy();
                processName = null;
                process = null;

                displayPrompt();
            } catch (IOException io) {} catch (InterruptedException ie) {} catch (NullPointerException npe) {}
        }
    }


    class StderrThread extends Thread {
        StderrThread() {
            super("----thread: stderr: jsource----");
            start();
        }

        public void run() {
            try {
                if (process == null)
                    return;
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                String line;

                while ((line = in.readLine()) != null)
                    append(JSConstants.LINE_SEP + line, errorColor);
                in.close();
            } catch (IOException io) {} catch (NullPointerException npe) {}
        }
    }

  private Writer writerSTDOUT = new Writer()
  {
    public void close() { }

    public void flush()
    {
      repaint();
    }

    public void write( char cbuf[], int off, int len )
    {
      append(new String(cbuf, off, len), outputColor);
    }
  };

  private Writer writeSTDERR = new Writer()
  {
    public void close() { }

    public void flush()
    {
      repaint();
    }

    public void write( char cbuf[], int off, int len )
    {
      append(new String(cbuf, off, len), errorColor);
    }
  };

  /**
   * Returns a writer in which external classes can send
   * <code>String</code> to make them being displayed in the
   * console as standard output.
   */

  public Writer getStdOut()
  {
    return writerSTDOUT;
  }

  /**
   * Returns a writer in which external classes can send
   * <code>String</code> to make them being displayed in the
   * console as error output.
   */

  public Writer getStdErr()
  {
    return writeSTDERR;
  }

    protected void finalize() throws Throwable {
        super.finalize();

        currentCmd = null;
        firstCmd = null;

        parent = null;

        process = null;
        processName = null;
        stdout = null;
        stderr = null;

        current = null;
        outputDocument = null;
        textArea = null;
        historyModel = null;

        infoColor = null;

        prompt = null;
        hostName = null;
        oldPath = null;
        promptPattern = null;
    }
}
