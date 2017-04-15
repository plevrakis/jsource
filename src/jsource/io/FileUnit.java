package jsource.io;


/**
 * FileUnit.java 05/31/03
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

/**
 * <code>FileUnit</code> is the class that gives modification information about a file.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class FileUnit {

    /** Open file last modified time */
    long lastModTime = 0;

    /** True if the user modified the file since it was opened */
    boolean currentFileModified = false;

    /** Directory of the current file */
    String fileDir = null;

    /** Name of the current file without path */
    String fileName = null;

    /** Name of the current file with full path */
    String fullFileName = null;

    /** The current file */
    File file = null;

    /**
     * Create a new <code>FileUnit</code>, opening up the given source file.
     *
     * @param file the <code>File</code> that is currently visible in the editor
     */
    public FileUnit(File file) {
        this.file = file;
        fileDir = file.getParent();
        fileName = file.getName();
        File f = new File(fileDir, fileName);
        long modTime = f.lastModified();
        setCurrentFileInfo(modTime, fileDir, fileName);
    }

    /**
     * Find out if the current file has had any programmatic modifications since it was opened
     *
     * @return True if the file has been touched, false otherwise.
     */
    public boolean getCurrentFileModified() {
        return currentFileModified;
    }

    /**
     * Get the working directory for the file.
     *
     * @return the working directory for the file
     */
    public String getFileDir() {
        return fileDir;
    }

    /**
     * Get the file name
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the file
     *
     * @return the File
     */
    public File getFile() {
        return file;
    }

    /**
     * Find the last modification time, on the disk, for the file.
     *
     * @return The last modification time, in seconds since the epoch.
     */
    public synchronized long getLastModTime() {
        return this.lastModTime;
    }

    /**
     * Reload the modification time for the file from the file on the disk.
     */
    public synchronized void resetModTime() {
        File f = new File(fileDir, fileName);
        setModTime(f.lastModified());
    }

    /**
     * Change the internal representation of the file modification time to the given time.
     *
     * @param currentModTime The new modification time, in seconds since the epoch
     */
    public synchronized void setModTime(long currentModTime) {
        this.lastModTime = currentModTime;
    }

    /**
     * Modify the internal file information.
     *
     * @param modificationTime The last modification time for the file
     * @param directory The directory for the file
     * @param fileName The file name
     */
    public synchronized void setCurrentFileInfo(long modificationTime, String directory, String fileName) {
        setModTime(modificationTime);
        this.fileDir = directory;
        this.fileName = fileName;
    }

    /**
     * Set the current file to appear to be modified.
     */
    public void touchFile() {
        currentFileModified = true;
    }

    /**
     * Set modification status for the current file.
     */
    public void setFileModified(boolean newModified) {
        currentFileModified = newModified;
    }

    /**
     * Check to see if the file has been externally modified since it was loaded.
     */
    public boolean isFileModified() {
        File theFile = new File(fileDir, fileName);
        long currentModTime = theFile.lastModified();

        if (currentModTime != getLastModTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Reload the current file.
     */
    public void reloadModTime() {
        File timeFile = new File(this.fileDir, this.fileName);
        long modTime = timeFile.lastModified();
        setCurrentFileInfo(modTime, fileDir, fileName);
        this.currentFileModified = false;
    }

    /**
     * String representation of this <code>FileUnit</code> object.  This is just the
     * string returned by the <code>getPath()</code> method of <code>File</code>.
     *
     * @return  The string form of this <code>FileUnit</code>
     */
    public String toString() {
		return file.getPath();
    }
}
