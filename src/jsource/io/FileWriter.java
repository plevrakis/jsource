package jsource.io;


/**
 * FileWriter.java	12/27/02
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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jsource.*;
import jsource.syntax.*;
import jsource.syntax.tokenmarker.*;
import jsource.util.*;
import jsource.gui.*;

/**
 * <code>FileWriter</code> handles file writing
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class FileWriter implements JSConstants {

	private JSEditor editor = null;
    private MainFrame mainFrame = null;
    private java.io.FileWriter filewriter = null;
    private java.io.FileReader filereader = null;
    private BufferedReader bufferedreader = null;

	public FileWriter() {
	}

	public FileWriter(JSEditor editor, MainFrame mainFrame) {
		this.editor = editor;
		this.mainFrame = mainFrame;
	}

	/**
	 * Displays the contents of a file to the editor.
	 * @param fileUnit the <code>FileUnit</code> that encapsulates the file to be read.
	 */
    public void writeFileToEditor(FileUnit fileUnit) {
		File file = fileUnit.getFile();
		StringBuffer buffer = new StringBuffer();
		editor.setText("");

        try {
            filereader = new java.io.FileReader(file);
            bufferedreader = new BufferedReader(filereader);
            String lineread = "";
            while ((lineread = bufferedreader.readLine()) != null) {
				  checkForMainOrAppletTags(lineread);
                  buffer.append(lineread + LINE_SEP);
            }
            editor.setText(buffer.toString());
            editor.setCaretPosition(0);
        } catch(IOException ex) {
			GUIUtilities.showExceptionErrorMessage("IOException: " + ex.getMessage());
		} finally {
			FileUtilities.close(filereader);
			filereader = null;
		}
    }

	/**
	 * Displays the specified text to the text area
	 * @param text the text to display
	 */
    public void writeStringToEditor(String text) {
		editor.setText(text);
		editor.setCaretPosition(0);
    }

    /**
     * Updates the contents of the current file with the current text in the editor.
     * @param fileUnit the <code>FileUnit</code> that encapsulates the file to be updated.
     * @param text the <code>String</code> that updates the contents the file.
     */
    public void writeStringToFile(FileUnit fileUnit, String text) {
		File file = fileUnit.getFile();
        try {
            filewriter = new java.io.FileWriter(file);
            StringReader stringreader = new StringReader(text);
            bufferedreader = new BufferedReader(stringreader);
            String lineread = "";

            while ((lineread = bufferedreader.readLine()) != null) {
				checkForMainOrAppletTags(lineread);
                filewriter.write(lineread + LINE_SEP);
            }
        } catch(IOException ex) {
			GUIUtilities.showExceptionErrorMessage("IOException: " + ex.getMessage());
		} finally {
			FileUtilities.close(filewriter);
			filewriter = null;
		}
    }

    /**
     * A private utility method that checks a line if it contains
     * the signature of the main method or the opening applet tag
     * @param line the <code>String</code> of the line to be checked.
     */
    private void checkForMainOrAppletTags(String line) {
		// detect if this file contains the main method
		if (line.trim().startsWith("public static void main") ||
			line.trim().startsWith("static public void main")) {
				mainFrame.hasMain = true;
		}
		// detect if this file contains applet tags
		if (line.trim().startsWith("<applet") ||
			line.trim().startsWith("<APPLET")) {
				mainFrame.hasAppletTags = true;
				mainFrame.isWebPage = true;
		}
	}
} // end class