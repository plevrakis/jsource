package jsource.console;


/**
 * ConsoleListDir.java	07/08/03
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

import java.io.File;
import java.io.IOException;

import java.util.Date;
import java.util.StringTokenizer;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;

import jsource.gui.*;
import jsource.io.*;
import jsource.util.*;
import jsource.io.localization.*;

/**
 * <code>ConsoleListDir</code> provides an emulation of ls function for Java Shell.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class ConsoleListDir {
	private static XMLResourceBundle bundle = null;
    private static Console2 parent = null;
    private static int indentSize = 0;
    private static String indent = new String();
    private static String pattern = new String();
    private static boolean moreInfos = false;
    private static boolean fullNames = false;
    private static boolean longDates = false;
    private static boolean hiddenFiles = false;
    private static boolean noDates = false;
    private static boolean onlyDirs = false;
    private static boolean onlyFiles = false;
    private static boolean recursive = false;
    private static boolean noInfos = false;
    private static boolean kiloBytes = false;
    private static boolean sort = false;
    private static boolean canList = true;

    /**
     * Sets the <code>XMLResourceBundle</code> for this class.
     * @param bdl the <code>XMLResourceBundle</code>
     */
	public static void setXMLResourceBundle(XMLResourceBundle bdl) {
		bundle = bdl;
	}

    /**
     * Exec the equivalent of system's 'ls' or 'dir' command.
     * @param cparent Console which executed the command
     * @param args The command arguments
     */
    public static void list(Console2 cparent, String args) {
        parent = cparent;
        boolean list = true;

        if (buildFlags(args)) {
            String old = System.getProperty("user.dir");

            run();

            if (recursive || !canList)
                System.getProperties().put("user.dir", old);

            // we reset flags
            sort = kiloBytes = recursive = onlyFiles = onlyDirs = noDates
                    = moreInfos = hiddenFiles = longDates = fullNames = false;
            pattern = "";
            canList = true;
            indentSize = 0;
        }
    }

    /**
     * Output a string in the parent console.
     * @param print the string to output
     */
    private final static void print(String print) {
        parent.append(print + JSConstants.LINE_SEP, parent.outputColor);
    }

    /**
     * Determines which options are enabled.
     * @param args the arguments containing the option flags
     */
    private static boolean buildFlags(String arg) {
        if (arg == null)
            return true;

        StringTokenizer tokens = new StringTokenizer(arg);
        String argument;

        while (tokens.hasMoreTokens()) {
            argument = tokens.nextToken();

            if (argument.startsWith("-")) {
                if (argument.equals("-help")) {
                    help();
                    return false;
                }

                for (int j = 1; j < argument.length(); j++) {
                    switch (argument.charAt(j)) {
                    case 'h':               // hidden files to be shown
                        hiddenFiles = true;
                        break;

                    case 'm':               // display full infos
                        moreInfos = true;
                        break;

                    case 'l':               // use long dates format
                        longDates = true;
                        break;

                    case 'f':               // display full names (don't cut with '...')
                        fullNames = true;
                        break;

                    case 'n':               // don't show last modified dates
                        noDates = true;
                        break;

                    case 'd':               // lists dirs only
                        onlyDirs = true;
                        break;

                    case 'a':               // lists files only
                        onlyFiles = true;
                        break;

                    case 'r':               // lists subdirectories
                        recursive = true;
                        break;

                    case 'i':               // don't show infos
                        noInfos = true;
                        break;

                    case 'k':               // display file sizes in kb instead of bytes
                        kiloBytes = true;
                        break;

                    case 's':               // alphabetically sort files
                        sort = true;
                        break;
                    }
                }
            } else
                pattern = argument;
        }

        return true;
    }

    /**
     * List according to the options flag activated.
     */
    private final static void run() {
        // these instances are used to improve speed of dates calculations
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        FieldPosition field = new FieldPosition(0);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss");

        // default pattern used is '*'
        File[] files = Utilities.listFiles(
                Utilities.getWildCardMatches(pattern.equals("") ? "*" : pattern, sort), true);

        long totalSize;
        int totalDir, totalFiles;

        totalSize = totalFiles = totalDir = 0;

        // if canList, then we have to browse a subdirectory
        if (canList && pattern.indexOf("*") == -1 && pattern.indexOf("?") == -1) {
            File curr = new File(Utilities.constructPath(pattern));

            if (curr == null || !curr.isDirectory()) {
                parent.error("ls: Invalid directory");
                return;
            }
            canList = false;
            pattern = "*";
            System.getProperties().put("user.dir", Utilities.constructPath(curr.getAbsolutePath()));
            run();
            return;
        }

        print("");

        for (int i = 0; i < files.length; i++) {
            StringBuffer display = new StringBuffer();
            File current = files[i];
            String currentName = current.getName();

            if (!fullNames)
                currentName = Utilities.getShortStringOf(currentName, 24);
            int amountOfSpaces = 32 - currentName.length();

            int sub = 0;

            if (amountOfSpaces > 6)
                sub = 6;
            else if
                    (amountOfSpaces >= 0) sub = amountOfSpaces;
            else
                sub = 0;
            // we found a directory
            if (current.isDirectory()) {
                display.append(currentName).append(Utilities.createWhiteSpace(amountOfSpaces).substring(sub)).append("<DIR>");
                if (moreInfos)
                    display = (new StringBuffer("   ")).append(Utilities.createWhiteSpace(8)).append(display);
                totalDir++;
            } else if (current.isFile()) {
                // and this is a file
                display.append(currentName).append(Utilities.createWhiteSpace(amountOfSpaces)).append(current.length());
                totalSize += current.length();
                if (moreInfos) {
                    StringBuffer info = new StringBuffer();

                    info.append(current.canWrite() ? 'w' : '-');         // file is writable
                    info.append(current.canRead() ? 'r' : '-');          // file is readable
                    info.append(current.isHidden() ? 'h' : '-');         // file is hidden
                    info.append(Utilities.createWhiteSpace(8));
                    display = info.append(display);
                }
                totalFiles++;
            }

            StringBuffer time = new StringBuffer();

            if (!noDates) {
                date.setTime(current.lastModified());

                if (longDates) {
                    // we display long dates format
                    time.append(date.toString());
                } else {
                    // we display short dates
                    buffer.setLength(0);
                    time.append(formatter.format(date, buffer, field));
                }
                time.append(Utilities.createWhiteSpace(8));
            }

            // determine if we must show or not (according to flags) found file
            if ((hiddenFiles && current.isHidden()) || !current.isHidden()) {
                if ((current.isDirectory() && !onlyFiles)
                        || (current.isFile() && !onlyDirs)
                        || (onlyDirs && onlyFiles)) {
                    if (Utilities.match(pattern, current.getName()))
                        print(indent + time.toString() + display.toString());
                }
            }

            // if we are dealing with a dir and -r flag is set, we browse it
            if (recursive && current.isDirectory()) {
                System.getProperties().put("user.dir", Utilities.constructPath(current.getAbsolutePath()));
                indent = createIndent(++indentSize);
                run();
                if (!onlyDirs)
                    print("");
            }
        }

        // display summary infos
        StringBuffer size = new StringBuffer();

        if (kiloBytes)
            size.append(formatNumber(Long.toString(totalSize / 1024))).append('k');
        else
            size.append(formatNumber(Long.toString(totalSize))).append("bytes");

        if (!noInfos)
            print("\n" + indent + totalFiles + " files - " + totalDir + " directories - " + size.toString());
        indent = createIndent(--indentSize);
    }

    /**
     * Format a number from 12000123 to 12 000 123.
     * @param number the number to be formatted
     */
    private final static String formatNumber(String number) {
        StringBuffer formatted = new StringBuffer(number);

        for (int i = number.length(); i > 0; i -= 3)
            formatted.insert(i, ' ');
        return formatted.toString();
    }

    /**
     * Creates the indent for the recursive option.
     * An indent unit adds two '-'.
     * @param len the length of indentation
     */
    private final static String createIndent(int len) {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < len; i++) {
            buf.append('-');
            buf.append('-');
        }
        return buf.toString();
    }

    /**
     * Display command help in the console.
     */
    public static void help() {
        parent.help(bundle.getValueOf("console.ls.help"));
    }
}
