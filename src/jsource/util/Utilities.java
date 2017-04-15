package jsource.util;


/**
 * Utilities.java 07/08/03
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

import jsource.syntax.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

/**
 * <code>Utilities</code> is a class that holds general useful utilities.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class Utilities {

    /**
     * Creates a blank string made of spaces.
     * @param len the amount of spaces contained in the string
     * @return a blank string
     */
    public static String createWhiteSpace(int len) {
        return createWhiteSpace(len, 0);
    }

    /**
     * Creates a blank string made of tabs.
     * @param len the amount of spaces contained in the string
     * @param tabSize tabulation size
     * @return a blank string
     */
    public static String createWhiteSpace(int len, int tabSize) {
        StringBuffer buf = new StringBuffer();

        if (tabSize == 0) {
            while (len-- > 0)
                buf.append(' ');
        } else {
            int count = len / tabSize;

            while (count-- > 0)
                buf.append('\t');

            count = len % tabSize;
            while (count-- > 0)
                buf.append(' ');
        }

        return buf.toString();
    }

    /**
     * Returns the number of leading white space characters in the specified string.
     * @param str the string
     */
    public static int getLeadingWhiteSpace(String str) {
        int whitespace = 0;

        loop:
        for (; whitespace < str.length();) {
            switch (str.charAt(whitespace)) {
            case ' ':
            case '\t':
                whitespace++;
                break;

            default:
                break loop;
            }
        }
        return whitespace;
    }

    public static int getRealLength(String str, int tabSize) {
        int pos = 0;

        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
            case '\t':
                pos += tabSize;
                break;

            default:
                pos++;
            }
        }
        return pos;
    }

    /**
     * Some strings can be too long to be correctly displayed on the screen.
     * Mainly when it is a path to a file. This method truncates a string.
     * @param longString the string to be truncated
     * @param maxLength the maximum length of the string
     * @return the truncated string
     */
    public static String getShortStringOf(String longString, int maxLength) {
        int len = longString.length();

        if (len <= maxLength)
            return longString;
        else if (longString.indexOf('\\') == -1 && longString.indexOf('/') == -1) {
            StringBuffer buff = new StringBuffer(longString.substring(longString.length() - maxLength));

            for (int i = 0; i < 3; i++)
                buff.setCharAt(i, '.');
            return  buff.toString();
        } else {
            int first = len / 2;
            int second = first;

            for (int i = first - 1; i >= 0; i--) {
                if (longString.charAt(i) == '\\' || longString.charAt(i) == '/') {
                    first = i;
                    break;
                }
            }

            for (int i = second + 1; i < len; i++) {
                if (longString.charAt(i) == '\\' || longString.charAt(i) == '/') {
                    second = i;
                    break;
                }
            }

            loop:
            while ((len - (second - first)) > maxLength) {
                out:
                for (int i = first - 1; i >= 0; i--) {
                    switch (longString.charAt(i)) {
                    case '\\':
                    case '/':
                        first = i;
                        break out;
                    }
                }

                if ((len - (second - first)) < maxLength)
                    break loop;

                out2:
                for (int i = second + 1; i < len; i++) {
                    switch (longString.charAt(i)) {
                    case '\\':
                    case '/':
                        second = i;
                        break out2;
                    }
                }
            }

            return longString.substring(0, first + 1) + "..."
                    + longString.substring(second);
        }
    }

    /**
     * Constructs a new path from the current user path. This is an easy way to get a path
     * if the user specified, for example, "..\Java" as new path. This method will return
     * the argument if this one is a path to a root (i.e, if <code>change</code> is equal
     * to C:\Jdk, constructPath will return C:\Jdk).
     * @param change the modification to apply to the path
     */
    public static String constructPath(String change) {
        StringBuffer newPath = new StringBuffer(System.getProperty("user.dir"));
        char current;
        char lastChar = '\0';
        boolean toAdd = false;

        if (beginsWithRoot(change)) {
            return change;
		}
        if (change.equalsIgnoreCase("cd..")) {
			String parent = (new File(newPath.toString())).getParent();
            if (parent != null) newPath = new StringBuffer(parent);
            return newPath.toString();
		}
        if (change.equalsIgnoreCase("cd~")) {
			newPath = new StringBuffer(getHomeDirectory());
            return newPath.toString();
		}

        change = change.trim();
        StringBuffer buf = new StringBuffer(change.length());

        for (int i = 0; i < change.length(); i++) {
            switch ((current = change.charAt(i))) {
            case '.':
                if (lastChar == '.') {
                    String parent = (new File(newPath.toString())).getParent();

                    if (parent != null) newPath = new StringBuffer(parent);
                } else if ((lastChar != '\0' && lastChar != '\\'
                        && lastChar != '/')
                        || (i < change.length() - 1
                        && change.charAt(i + 1) != '.'))
                    buf.append('.');
                lastChar = '.';
                break;

            case '\\':
            case '/':
                if (lastChar == '\0') {
                    newPath = new StringBuffer(getRoot(newPath.toString()));
                } else {
                    char c = newPath.charAt(newPath.length() - 1);

                    if (c != '\\' && c != '/')
                        newPath.append(File.separator).append(buf.toString());
                    else
                        newPath.append(buf.toString());
                    buf = new StringBuffer();
                    toAdd = false;
                }
                lastChar = '\\';
                break;

            case '~':
                if (i < change.length() - 1) {
                    if (change.charAt(i + 1) == '\\'
                            || change.charAt(i + 1) == '/')
                        newPath = new StringBuffer(getHomeDirectory());
                    else
                        buf.append('~');
                } else if (i == 0)
                    newPath = new StringBuffer(getHomeDirectory());
                else
                    buf.append('~');
                lastChar = '~';
                break;

            default:
                lastChar = current;
                buf.append(current);
                toAdd = true;
                break;
            }
        }

        if (toAdd) {
            char c = newPath.charAt(newPath.length() - 1);

            if (c != '\\' && c != '/')
                newPath.append(File.separator).append(buf.toString());
            else
                newPath.append(buf.toString());
        }

        return newPath.toString();
    }

    /**
     * It can be necessary to check if a path specified by the user is absolute.
     * @param path the path to check
     * @return <code>true</code> if path begins with a root name
     */
    public static boolean beginsWithRoot(String path) {
        if (path.length() == 0)
            return false;

        File file = new File(path);
        File[] roots = file.listRoots();

        for (int i = 0; i < roots.length; i++)
            if (path.regionMatches(true, 0, roots[i].getPath(), 0, roots[i].getPath().length()))
                return true;
        return false;
    }

    /**
     * Gets user's home directory.
     * @return a string that represents the home directory of the current path.
     */
    public static String getHomeDirectory() {
        return System.getProperty("user.home");
    }

    /**
     * Returns the width of the leading white space in the specified string.
     * @param str the string
     * @param tabSize the tab size
     */
    public static int getLeadingWhiteSpaceWidth(String str, int tabSize) {
        int whitespace = 0;

        loop:
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
            case ' ':
                whitespace++;
                break;

            case '\t':
                whitespace += (tabSize - whitespace % tabSize);
                break;

            default:
                break loop;
            }
        }
        return whitespace;
    }

    /**
     * When the user has to specify file names, wildcards (*, ?) can be used.
     * This method handles the usage of these wildcards.
     * @param s wilcards
     * @param sort if <code>true</code> will sort file names
     * @return a string array that contains all files matching s in current directory.
     */
    public static String[] getWildCardMatches(String s, boolean sort) {
        return getWildCardMatches(null, s, sort);
    }

    /**
     * When the user has to specify file names, wildcards (*, ?) can be used.
     * This method handles the usage of these wildcards.
     * @param path the path where to search
     * @param s wilcards
     * @param sort if <code>true</code> will sort file names
     * @return a string array that contains all files matching s in current directory.
     */
    public static String[] getWildCardMatches(String path, String s, boolean sort) {
        if (s == null)
            return null;

        String files[];
        String filesThatMatch[];
        String args = new String(s.trim());
        ArrayList filesThatMatchVector = new ArrayList();

        if (path == null)
            path = System.getProperty("user.dir");

        files = (new File(path)).list();
        if (files == null)
            return null;

        for (int i = 0; i < files.length; i++) {
            if (match(args, files[i])) {
                File temp = new File(System.getProperty("user.dir"), files[i]);

                filesThatMatchVector.add(new String(temp.getName()));
            }
        }

        Object[] o = filesThatMatchVector.toArray();

        filesThatMatch = new String[o.length];
        for (int i = 0; i < o.length; i++)
            filesThatMatch[i] = o[i].toString();
        o = null;
        filesThatMatchVector = null;

        if (sort)
            Arrays.sort(filesThatMatch);

        return filesThatMatch;
    }

    /**
     * This method can determine if a string matches a pattern of wildcards
     * @param pattern the pattern used for comparison
     * @param string the string to be checked
     * @return <code>true</code> if the string matches pattern
     */
    public static boolean match(String pattern, String string) {
        for (int p = 0;; p++) {
            for (int s = 0;; p++, s++) {
                boolean sEnd = (s >= string.length());
                boolean pEnd = (p >= pattern.length()
                        || pattern.charAt(p) == '|');

                if (sEnd && pEnd)
                    return true;
                if (sEnd || pEnd)
                    break;
                if (pattern.charAt(p) == '?')
                    continue;
                if (pattern.charAt(p) == '*') {
                    int i;

                    p++;
                    for (i = string.length(); i >= s; --i)
                        if (match(pattern.substring(p), string.substring(i))) return true;
                    break;
                }
                if (pattern.charAt(p) != string.charAt(s))
                    break;
            }
            p = pattern.indexOf('|', p);
            if (p == -1)
                return false;
        }
    }

	/**
	 * It can be necessary to determine which is the root of a path.
	 * For example, the root of D:\Projects\Java is D:\.
	 * @param path the path used to get a root
	 * @return the root which contais the specified path
	 */
	public static String getRoot(String path) {
		File file = new File(path);
		File[] roots = file.listRoots();
		for (int i = 0; i < roots.length; i++)
	  	if (path.startsWith(roots[i].getPath()))
			return roots[i].getPath();
		return path;
	}

	/**
	 * Lists content of a directory.
	 * @param names the names of the files
	 * @param construct <code>true</code> if names do not contain full paths
	 * @return a <code>File</code> array
	 */
	public static File[] listFiles(String[] names, boolean construct) {
		return listFiles(names, System.getProperty("user.dir"), construct);
	}

	/**
	 * Lists content of a directory.
	 * @param names the names of the files
	 * @param path the base path for files
	 * @param construct <code>true</code> if names do not contain full paths
	 * @return a <code>File</code> array
	 */
	public static File[] listFiles(String[] names, String path, boolean construct) {
		File[] files = new File[names.length];

		if (construct) {
	    	if (!path.endsWith(File.separator))
			path += File.separator;
		}

		for (int i = 0; i < files.length; i++) {
	  		if (construct)
				files[i] = new File(path + names[i]);
	  		else
				files[i] = new File(names[i]);
		}

		return files;
	}

	/**
	 * Turns a Un*x glob filter to regexp one
	 * @param glob globbed filter
	 */
	public static String globToRE(String glob) {
		char c = '\0';
		boolean escape = false, enclosed = false;
		StringBuffer _buf = new StringBuffer(glob.length());

		for (int i = 0; i < glob.length(); i++) {
	  		c = glob.charAt(i);

	  		if (escape) {
				_buf.append('\\');
				_buf.append(c);
				escape = false;
				continue;
	  		}

	  		switch(c) {
				case '*':
			  		_buf.append('.').append('*');
			  		break;
				case '?':
			  		_buf.append('.');
			  		break;
				case '\\':
			  		escape = true;
			  		break;
				case '.':
			  		_buf.append('\\').append('.');
			  		break;
				case '{':
			  		_buf.append('(');
			  		enclosed = true;
			  		break;
				case '}':
			  		_buf.append(')');
			  		enclosed = false;
			  		break;
				case ',':
			  		if (enclosed)
					_buf.append('|');
			  	else
					_buf.append(',');
			  	break;
				default:
			  		_buf.append(c);
	  		}
		}
		return _buf.toString();
	}
}