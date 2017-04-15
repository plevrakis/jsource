package jsource.io;


/**
 * FileReader.java	12/27/02
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
 * <code>FileReader</code> handles file reading operations.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class FileReader implements JSConstants {

	private JSEditor editor = null;
	private MainFrame mainFrame = null;
	private boolean isDir = false;
	private boolean isAPI = false;
	private boolean isJAR = false;
	private boolean toolSetup = false;
    private boolean searchForEXE = false;

	public FileReader() {
		isDir = true;
	}

	public FileReader(JSEditor editor, MainFrame mainFrame) {
		this.editor = editor;
		this.mainFrame = mainFrame;
	}

    /**
     * Sets if the file chooser searches for browser.
     * This method actually sets back the default file selection mode.
     * @param the boolean searchForBrowser
     */
	public void setSearchForEXE(boolean searchForEXE) {
	    this.searchForEXE = searchForEXE;
	    toolSetup = true;
	}

    /**
     * Gets if the file chooser searches for browser.
     * This method actually returns whether the default selection model is used.
     * @return the boolean searchForBrowser
     */
	public boolean getSearchForEXE() {
	    return searchForEXE;
	}

    /**
     * Sets if the file chooser searches for a Javadoc index.html file.
     * This method actually sets back the default file selection mode.
     * @param the boolean isAPI
     */
	public void setIsAPI(boolean isAPI) {
	    this.isAPI = isAPI;
	    toolSetup = true; // prevents setting tokenmarker if setting path to a tool
	    if (isAPI == true) {
			isDir = false;
		}
	}

    /**
     * Gets if the file chooser searches for a Javadoc index.html file.
     * This method actually returns whether the default selection model is used.
     * @return the boolean isAPI
     */
	public boolean getIsAPI() {
	    return isAPI;
	}

    /**
     * Sets if the file chooser searches for .jar files.
     * @param the boolean isJAR
     */
	public void setIsJAR(boolean isJAR) {
	    this.isJAR = isJAR;
	    if (isJAR == true) {
			isDir = false;
		}
	}

    /**
     * Gets if the file chooser searches for .jar files.
     * @return the boolean isJAR
     */
	public boolean getIsJAR() {
	    return isJAR;
	}

    /**
     * Get a file to open
     * @return the File to open
     */
    public File openFile() {
        File file = null;
        String currentDir = mainFrame.currentDir;
        JSourceFileFilter filter = null;
        JFileChooser filechooser = new JFileChooser(currentDir);
        if (isAPI) {
			String extensions[] = new String[2];
			extensions[0] = "html";
			extensions[1] = "htm";
			filter = new JSourceFileFilter(extensions, "Web pages");
		    filechooser.addChoosableFileFilter(filter);
	    }
        if (isJAR) {
			String extensions[] = new String[1];
			extensions[0] = "jar";
			filter = new JSourceFileFilter(extensions, "Compressed files");
		    filechooser.addChoosableFileFilter(filter);
	    }
        if (isDir && !searchForEXE) {
			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}

        int replycode = filechooser.showOpenDialog(editor);
        if (replycode == JFileChooser.APPROVE_OPTION) {
            file = filechooser.getSelectedFile();
            if (!isDir && !toolSetup) {
            	mainFrame.currentDir = file.getParent();
            	detectFileType(file.getName());
			}
        }
        return file;
    }

    /**
     * Gets a build .xml file.
     * @return the build <code>File</code>
     */
    public File getBuildFile() {
		File buildFile = null;
        JSourceFileFilter filter = null;
        String currentDir = mainFrame.currentDir;
        JFileChooser filechooser = new JFileChooser(currentDir);
		String extensions[] = new String[1];
		extensions[0] = "xml";
	    filter = new JSourceFileFilter(extensions, "xml files");
		filechooser.addChoosableFileFilter(filter);
        int replycode = filechooser.showOpenDialog(editor);
        if (replycode == JFileChooser.APPROVE_OPTION) {
            buildFile = filechooser.getSelectedFile();
		}
		return buildFile;
	}

    /**
     * Detect file type and set the appropriate token marker
     * @param filename the name of the file
     */
	public void detectFileType(String filename) {
        if (FileUtilities.isFileExtension("java", filename)) {
			editor.setTokenMarker(new JavaTokenMarker());
			mainFrame.isJavaFile = true;
		} else if (FileUtilities.isFileExtension("xml", filename)) {
			editor.setTokenMarker(new XMLTokenMarker());
			mainFrame.isWebPage = true; // can view an xml file in web browser
	    } else if (FileUtilities.isFileExtension("html", filename) || FileUtilities.isFileExtension("htm", filename)) {
			editor.setTokenMarker(new HTMLTokenMarker());
			mainFrame.isWebPage = true;
		} else if (FileUtilities.isFileExtension("js", filename)) {
			editor.setTokenMarker(new JavaScriptTokenMarker());
		} else if (FileUtilities.isFileExtension("cpp", filename)) {
			editor.setTokenMarker(new CCTokenMarker());
		} else if (FileUtilities.isFileExtension("php", filename)) {
			editor.setTokenMarker(new PHPTokenMarker());
		} else if (FileUtilities.isFileExtension("txt", filename)) {
			editor.setTokenMarker(null); // no syntax highlighting for .txt files is needed
		}
	}

    /**
     * Save as file name and file type selected by the user
     * @return the saved File
     */
    public File saveAsFile() {
        File file = null;
        File currentdirectory = new File(USER_DIR);
        JFileChooser filechooser = new JFileChooser(currentdirectory);
        int replycode = filechooser.showSaveDialog(editor);

        if (replycode == JFileChooser.APPROVE_OPTION) {
            file = filechooser.getSelectedFile();
            String f = filechooser.getName(file);

            /*if (file != null)
                currentFile = f;
            if (file.getName().endsWith(".java"))
                isJavaFile = true;*/
        }
        return file;
    }
} // end class