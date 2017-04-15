package jsource.gui;


/**
 * AntWorkMessage.java 07/28/03
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


public class AntWorkMessage {
    private String mAbsoluteFilename = null;
    private String mFilename = null;
    private int mLine = -1;
    private int mColumn = -1;
    private String mMessage = null;
    boolean mIsWarning;

    public AntWorkMessage(String filename, String line, String message) {
        this(filename, line, message, null);
    }

    public AntWorkMessage(String filename,
            String line, String message, String tooltip) {
        this(filename, Integer.parseInt(line), message, tooltip, -1);
    }

    public AntWorkMessage(String filename, int line, int column, String message) {
        this(filename, line, message, null, column);
    }

    public AntWorkMessage(String filename,
            int line, String message, String tooltip, int column) {
        mAbsoluteFilename = filename;
        mFilename = filename.substring(filename.lastIndexOf(File.separatorChar) + 1);
        mLine = line;
        mColumn = column;
        mMessage = message.trim();
        mIsWarning = mMessage.toLowerCase().startsWith("warning:");
    }

    public String getAbsoluteFilename() {
        return mAbsoluteFilename;
    }

    public int getLine() {
        return mLine;
    }

    public int getColumn() {
        return mColumn;
    }

    public void setColumn(int column) {
        mColumn = column;
    }

    public boolean isWarning() {
        return mIsWarning;
    }

    public String toString() {
        if (mIsWarning) {
            return "\"" + mFilename + "\" Warning: " + mMessage + " at line "
                    + mLine + ", column=" + mColumn;
        } else {
            return "\"" + mFilename + "\" Error: " + mMessage + " at line "
                    + mLine + ", column=" + mColumn;
        }
    }

}
