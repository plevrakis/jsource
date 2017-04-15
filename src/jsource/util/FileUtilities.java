package jsource.util;


/**
 * FileUtilities.java	01/03/03
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
import java.nio.*;
import java.nio.channels.*;
import jsource.io.FileUnit;


/**
 * <code>FileUtilities</code> holds some useful file manipulation utilities.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class FileUtilities {

    public final static String java = "java";
    public final static String cpp = "cpp";
    public final static String html = "html";
    public final static String htm = "htm";
    public final static String xml = "xml";

    /**
     * Get the extension of a file
     * @return a String with the file extension
     */
    public static String getExtension(FileUnit unit) {
        String ext = null;
        String s = unit.getFileName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Check filename extension ignoring case
     * @return true if the file ends with the given extension
     */
    public static boolean isFileExtension(String extension, String filename) {
        return filename.endsWith("." + extension)
                || filename.endsWith("." + extension.toUpperCase());
    }

    /**
     * Remove a file extension from a file and replace slash characters with periods
     * @param name the file name
     */
    public static String formatFileToRun(String name) {
        char[] fileName = name.toCharArray();

        for (int i = fileName.length - 6; i >= 0; i--)
            if (fileName[i] == '/')
                fileName[i] = '.';
        return new String(fileName, 0, fileName.length - 5);
    }

    /**
     * Reverses slashes in a file name.
     * @param name the file name
     */
    public static String reverseSlashes(String name) {
		return name.replace('\\', '/');
    }

    /**
     * Creates a copy of a file to a specified location.
     * @param from the location and name of the file to be copied
     * @param to the location and name of the file copy
     */
    public static void copyFile(String from, String to) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fcin = null;
        FileChannel fcout = null;

        try { // do the file copy
            fis = new FileInputStream(from);
            fos = new FileOutputStream(to);
            fcin = fis.getChannel();
            fcout = fos.getChannel();
            fcin.transferTo(0, fcin.size(), fcout);
        } catch (IOException ex) {
            GUIUtilities.showExceptionErrorMessage("IOException: " + ex.getMessage());
        } finally { // close streams and channels
            close(fcin);
            close(fcout);
            close(fis);
            close(fos);
        }
    }

    /**
     * Closes the specified <code>OutputStream</code>, swallowing any exceptions.
     * @param out the <code>OutputStream</code> to close. May be null.
     */
    public static void close(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                GUIUtilities.showExceptionErrorMessage("IOException: " + e.getMessage());
                Log.log(e);
            }
        }
    }

    /**
     * Closes the specified <code>InputStream</code>, swallowing any exceptions.
     * @param in the <code>InputStream</code> to close. May be null.
     */
    public static void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                GUIUtilities.showExceptionErrorMessage("IOException: " + e.getMessage());
                Log.log(e);
            }
        }
    }

    /**
     * Closes the specified <code>Writer</code>, swallowing any exceptions.
     * @param out the <code>Writer</code> to close. May be null.
     */
    public static void close(Writer out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                GUIUtilities.showExceptionErrorMessage("IOException: " + e.getMessage());
                Log.log(e);
            }
        }
    }

    /**
     * Closes the specified <code>Reader</code>, swallowing any exceptions.
     * @param in the <code>Reader</code> to close. May be null.
     */
    public static void close(Reader in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                GUIUtilities.showExceptionErrorMessage("IOException: " + e.getMessage());
                Log.log(e);
            }
        }
    }

    /**
     * Closes the specified <code>Channel</code>, swallowing any exceptions.
     * @param channel the <code>Channel</code> to close. May be null.
     */
    public static void close(Channel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                GUIUtilities.showExceptionErrorMessage("IOException: " + e.getMessage());
                Log.log(e);
            }
        }
    }
}
