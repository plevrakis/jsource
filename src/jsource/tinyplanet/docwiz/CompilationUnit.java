/*
DocWiz: Easy Javadoc documentation
Copyright (C) 1998  Simon Arthur

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

// History
//	4/30/2001 Lee Meador - add getTagFile() method to retreive a list of all the
//				comments for some tag. This allows adding the drop down lists of
//				other comments when entering new comments.
//	7/12/2001 lmm	- add the concept of an invisible code item that is not counted or
//					shown in the list pane when the preferences indicate such. These
//					are all private.
//					- trim the tag pieces that are filled into lists for the drop down boxes

package tinyplanet.docwiz;

import tinyplanet.javaparser.ParseException;
import java.io.*;
import java.util.*;
import javax.swing.ListModel;
import javax.swing.event.*;
import tinyplanet.javaparser.ParseException;

/**
 * This class gives information about a compilation unit, aka a Java source
 * file. It can include classes, methods, fields, and the like. Note that it
 * implements ListModel so that you can tie it to a JList.
 *
 * @author Simon Arthur
 */
public class CompilationUnit implements ListModel
{
	/** Open file last modified time
	 */
	long lastModTime;
	/** Has the user modified the file since it was opened?
	 */
	boolean currentFileModified = false;
	/** Directory of the current file
	 */
	String fileDir;
	/** Name of the current file (without path)
	 */
	String fileName;
	/** The entire Java source file, as a String
	 */
	String wholeFile;
	/** The parser instance for the current Java file
	 */
	tinyplanet.javaparser.JavaParser parse;
	/** A vector with entries for the JavaDoc commentable entities within the CompilationUnit
	 */
	Vector summaryInfo;
	/** The root parser node for the Java source file
	 */
	tinyplanet.javaparser.SimpleNode topNode;
	/** The listeners for ListData events
	 */
	private transient Vector listDataListeners;

	/** Create a new CompilationUnit, opening up the given Java source file in the given directory.
	 *
	 * @exception ParseException The Java file doesnt parse correctly.
	 * @exception IOException Theres an IO problem with the file
	 * @param fileName the name of the file to open
	 * @param fileDir The dirrectory in which the file is located
	 */
	public CompilationUnit(String fileDir, String fileName) throws ParseException, IOException
	{
		this.fileDir = fileDir;
		this.fileName = fileName;
		doOpenFile();
	}

	/** Find out if the current file has had any programmatic modifications since it was opened
	 *
	 * @return True if the file has been touched, false otherwise.
	 */
	public boolean getCurrentFileModified()
	{
		return currentFileModified;
	}

	/** Get the working directory for the file.
	 *
	 * @return the working directory for the file
	 */
	public String getFileDir()
	{
		return fileDir;
	}

	/** Get the file name
	 *
	 * @return the file name
	 */
	public String getFileName()
	{
		return fileName;
	}

	/** Get a vector with the commentable entities within the Java Source file
	 *
	 * @return A vector of the CommentableCode objects within the Java source
	 *     file
	 */
	public Vector getSummaryInfo()
	{
		return summaryInfo;
	}

	/** Load in a new Java source file and parse it.
	 *
	 * @exception IOException There's a problem reading from the given Reader:
	 *     file problems, etc.
	 * @exception tinyplanet.javaparser.ParseException There's a problem,
	 *      such as a syntax error, parsing the given Java source
	 * @param r A reader for the current Java source file
	 */
	public void load(Reader r) throws IOException, tinyplanet.javaparser.ParseException
	{
		char currentChar;
		String currentLine;

		String LS = System.getProperty("line.separator");
		StringBuffer wholeInput = new StringBuffer();
		LineNumberReader input = new LineNumberReader(r);
		currentLine = input.readLine();
		while (currentLine != null) {
			wholeInput.append(currentLine);
			wholeInput.append(LS);
			currentLine = input.readLine();
		}
		// to do: fire an event for updating when this gets some lines
		this.wholeFile = wholeInput.toString();
		StringReader sr = new StringReader(this.wholeFile);

		parse = new tinyplanet.javaparser.JavaParser(sr);
		topNode = parse.CompilationUnit();

		summaryInfo = topNode.getSummaryInfo();
		for (int i = 0; i < summaryInfo.size(); i++) {
			CommentableCode c = (CommentableCode)summaryInfo.elementAt(i);
			c.setCompilationUnit(this);
		}
		//topNode.dump( " " );
		//ASTCompilationUnit s = parser.CompilationUnit();
		this.fireContentsChanged();
//		this.fireContentsChanged(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, this.getSize()));
//		this.fireContentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, this.getSize()));
	}

	/** Find the last modification time, on the disk, for the file.
	 *
	 * @return The last modification time, in seconds since the epoch.
	 */
	synchronized long getLastModTime ()
	{
		return this.lastModTime;
	}

	/** Open up the file based upon the given parameters, parse it, and display the file
	 * in the user interface.
	 *
	 * @exception java.io.IOException There's an error reading the file.
	 * @exception tinyplanet.javaparser.ParseException The file could not be
	 *      parsed.
	 * @param directory Directory to look in for the file
	 * @param fileName The name of the file
	 * @param bis An opened Reader for the file
	 */
	void doOpenReader(Reader bis, String directory, String fileName) throws
				java.io.IOException, tinyplanet.javaparser.ParseException
	{
		try {
			//this.jLblStatus.setText("Loading file...");
			//this.jLblStatus.repaint();
			this.load(bis);
		}
		catch (tinyplanet.javaparser.ParseException pe) {
			// note that the file isn't open, rethrow exception
			this.fileName = null;
			this.currentFileModified = false;
			throw pe;
		}
		finally {
		}


	}

	/** Reload the modification time for the file from the file on the disk.
	 */
	synchronized void resetModTime()
	{
		File f = new File(fileDir , fileName);
		setModTime(f.lastModified());
	}

	/** Change the internal representation of the file modification time to the given time.
	 *
	 * @param currentModTime The new modification time, in seconds since the
	 *     epoch
	 */
	synchronized void setModTime(long currentModTime)
	{
		this.lastModTime = currentModTime;
	}

	/** Modify the internal file information.
	 *
	 * @param directory The directory for the file
	 * @param fileName The file name
	 * @param modificationTime The last modification time for the file
	 */
	synchronized void setCurrentFileInfo(long modificationTime, String directory,
										 String fileName)
	{
		setModTime(modificationTime);
		this.fileDir = directory;
		this.fileName = fileName;
	}

	/**
	 * Open the file with the given file name and directory
	 *
	 * @exception FileNotFoundException The file couldnt be found
	 * @exception IOException There was some other IO problem reading the file
	 * @exception ParseException The file couldnt be parsed
	 */
	void doOpenFile()
	throws FileNotFoundException, IOException, ParseException
	{
		BufferedReader bis = null;
		FileReader fis = null;
		ConfigurationService cs = ConfigurationService .getConfigurationService();
		cs.setLastDirectory(fileDir);
		try {
			String fullFileName = fileDir + fileName;
			long modTime;
			File f;
			f = new File(fileDir , fileName);
			modTime = f.lastModified();
			setCurrentFileInfo(modTime, fileDir, fileName);
			fis = new FileReader(fullFileName);
			bis = new BufferedReader(fis);
			doOpenReader(bis, fileDir, fileName);
		}
		finally {
			if (fis != null)
				fis.close();
			if (bis != null)
				bis.close();
		}
	}

	/** Set the current file to appear to be modified.
	 */
	void touchFile()
	{
		currentFileModified = true;
	}
	public void setFileModified(boolean newModified)
	{
		currentFileModified = newModified;
	}
	/** Check to see if the file has been externally modified since it was
	 * loaded
	 */
	boolean isFileModified()
	{
		File theFile = new File(this.fileDir , this.fileName);
		long currentModTime = theFile.lastModified();
		//System.err.println("CompilationUnit: currentModTime="+currentModTime);
		//System.err.println("CompilationUnit: getLastModTime()="+getLastModTime());
		if (currentModTime != getLastModTime()) {
			return true;
		}
		else {
			return false;
		}
	}

	/** Save the file to the disk, making a backup copy.
	 *
	 * @exception IOException Theres an IO problem while writing the file
	 */
	void doSave () throws IOException
	{
		BufferedWriter output = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileDir + fileName + ".new");
			output = new BufferedWriter(fw);
			save(output);
			File f;
			// Delete original .BAK file, if it exists.
			f = new File(this.fileDir, this.fileName + ".BAK");
			f.delete();
			// rename original file to .BAK
			f = new File(this.fileDir, this.fileName);
			f.renameTo(new File(this.fileDir, this.fileName + ".BAK"));
			// rename new file to original name
			f = new File(this.fileDir, this.fileName + ".new");
			f.renameTo(new File(this.fileDir, this.fileName));

			// Re-load the modification time
			File timeFile = new File(this.fileDir, this.fileName);
			long modTime = timeFile.lastModified();
			setCurrentFileInfo(modTime, fileDir, fileName);

			// re-read the file so that the line numbers are right now.
			this.currentFileModified = false;
		}
		finally {
			if (output != null)
				output.close();
			if (fw != null)
				fw.close();
		}
	}

	/** Save the CompilationUnit, including all CodeComments
	 *
	 *
	 * @exception IOException There's an IO problem while writing the file: disk
	 *      space, permissions, and the like.
	 * @param output The Writer to which the Java source will be sent
	 */
	public void save(Writer output) throws IOException
	{
		String newText = "";
		try {
			String LS = System.getProperty("line.separator");
			StringReader sr = new StringReader (this.wholeFile);
			LineNumberReader lr = new LineNumberReader(sr);
			int currentDataStructureNumber = 0;
			//int currentStructureStartLine;
			int currentLineNumber = 0;
			//int oldCommentStart, oldCommentEnd;
			//int commentSearchIndex;
			CommentableCode currentDataStructure = (CommentableCode)(summaryInfo.elementAt(currentDataStructureNumber));
			String currentLine = lr.readLine();
			currentLineNumber = lr.getLineNumber();
			CodeComment ccInfo;
			int currentSearchLine = currentDataStructure.getCodeComment().beginLine;
			if (currentSearchLine == -1) {
				// there's no pre-existing comment
				currentSearchLine = currentDataStructure.getLineNumber();
			}
			boolean oldCommentFound = false, doneSearching = false;
			String commentSearchToken = "";
			while (currentLine != null) { // while the end of the file has not been reached
				//System.out.println("cl "+currentLineNumber+": "+currentLine);
				if (currentLineNumber == currentSearchLine) {
					// time to insert a comment
					if (currentDataStructure.getCodeComment().beginLine == -1) {
						// there's no pre-existing comment
						//System.out.println("dta str" + currentDataStructure.getLineNumber() + ": " + currentDataStructure.getName());
					}
					else {
						// This will fix the dropped comment after the javadoc bug. It will
						//		also fix errors that may happen if non-whitespace preceeds
						//		the start of the java doc (slash-star-star) or follows the
						//		end of it (star-slash) on the same lines.
						// TODO: Collect the characters before the old javadoc upto the
						//		starting column. Ignore them if they are whitespace.
						// TODO: Seek up to the ending line and column of the javadoc.
						//		collect any characters from there to the end of the line
						//		and save them if they are not whitespace.
						// TODO: Quit seeking all the way to the declaration.

						// there was a pre-existing comment
						// seek forward to find the end of javadoc or declaration
						currentSearchLine = currentDataStructure.getCodeComment().endLine + 1;
						if (currentSearchLine == 0) {
							currentSearchLine = currentDataStructure.getLineNumber();
						}
						while ((currentLine != null) && (currentSearchLine != currentLineNumber)) {
							currentLine = lr.readLine();
							currentLineNumber = lr.getLineNumber();
						} //while
					} // else

					String newJavaDoc = currentDataStructure.getCodeComment().toJavaDocString();
					if (newJavaDoc.length() > 0) {
						newText += newJavaDoc + LS;
					}
					currentDataStructureNumber++;
					if (currentDataStructureNumber < summaryInfo.size()) {
						doneSearching = false;
						currentDataStructure = (CommentableCode)(summaryInfo.elementAt(currentDataStructureNumber));
					}
					else {
						doneSearching = true;
					}
					// TODO: If there was non-whitespace after the javadoc, add it to the
					//		newText now.
				}
				newText += currentLine + LS;
				currentSearchLine = currentDataStructure.getCodeComment().beginLine;
				if (currentSearchLine == -1) {
					// there's no pre-existing data structure
					currentSearchLine = currentDataStructure.getLineNumber();
				}
				currentLine = lr.readLine();
				currentLineNumber = lr.getLineNumber();
			} // while

			// write the String Buffer to the output stream
			output.write(newText);
		}
		finally {
			// close new file output
			if (output != null)
				output.close();
		}
		try {
			load(new StringReader(newText)); //, this.fileName
		}
		catch (tinyplanet.javaparser.ParseException pse) {
			// bad shit, there should never be a parse exception here, we're parsing
			// a file that we just created
			System.err.println(pse);
		}
	}


	/**
	 * Get the number of JavaDoc commentable elements within the compilation unit
	 *
	 * @return The number of CommentableCode entities within the source
	 */
	public int getSize()
	{
		int count = 0;
		for (Enumeration enum = summaryInfo.elements(); enum.hasMoreElements();) {
			Object dummy = enum.nextElement();
			if (isVisibleCode((CommentableCode) dummy)) {
				++count;
			}
		}
		return count;
//		return summaryInfo.size();
	}

	/** Find a particular CommentableCode object based upon its order
	 * within the list. These are ordered by their declaration within the Java
	 * source file.
	 *
	 * @param index The index of the CommentableCode to look up
	 * @return The CommentableCode object at the given index
	 */
	public Object getElementAt(int index)
	{
		Object element = null;
		int count = 0;
		for (Enumeration enum = summaryInfo.elements(); enum.hasMoreElements();) {
			element = enum.nextElement();
			if (isVisibleCode((CommentableCode) element)) {
				if (count >= index) {
					break;
				}
				++count;
			}
		}
		return (count == index) ? element : null;
//		return summaryInfo.elementAt(index);
	}

	/**
	 * Tell if the CommentableCode object is invisible because it is private and
	 *		the preferences indicate this type of code should not be shown.
	 *
	 * @since 07/12/2001 12:20:58 PM
	 * @param theCode The CommentableCode object that we are testing against.
	 * @return true if it should NOT be shown, false if it should be shown.
	 */
	protected boolean isVisibleCode(CommentableCode theCode)
	{
	  String scope = theCode.getScope();
      ConfigurationService config = ConfigurationService.getConfigurationService();
	  boolean retVal = true;
	  if (scope.equals("private") && theCode.isPrivateNoShow()) {
//	  	if ((theCode instanceof tinyplanet.docwiz.Field && config.getFieldAppearanceInList() == 2) ||
//		  	(theCode instanceof tinyplanet.docwiz.Class && config.getClassAppearanceInList() == 2) ||
//		  	(theCode instanceof tinyplanet.docwiz.Interface && config.getInterfaceAppearanceInList() == 2) ||
//		  	(theCode instanceof tinyplanet.docwiz.Method && config.getMethodAppearanceInList() == 2) ||
//			(theCode instanceof tinyplanet.docwiz.Constructor && config.getConstructorAppearanceInList() == 2)
//			) {
		  retVal = false;
	  }
		return retVal;
	}

	/*
	 * Get a list of all the comments for the specified tag in this compilation unit. This is
	 *	used to build a drop down list of existing comments to allow selecting to use the
	 *	same comment again.
	 * @param tag The tag that we want to see about. (e.g. 'param' would return a list of
	 *		the comments for all 'param' tags in this compilation unit.
	 * @return A list of all the comments for the specified tag.
	*/
	public Vector getCompilationUnitTagList(String tag)
	{
		Vector list = new Vector();

		for (int i = 0; i < summaryInfo.size(); i++) {
			CommentableCode c = (CommentableCode)summaryInfo.elementAt(i);
			if (c != null) {
				TagSet tagSet = c.getTagEntry(tag, false);
				if (tagSet != null) {
					Vector theList = tagSet.getTagStringList();
					if (theList != null) {
						for (Enumeration enum = theList.elements(); (enum.hasMoreElements()) ; ) {
							Object item = enum.nextElement();
							if (item != null && !list.contains(item.toString().trim())) {
								list.add(item.toString().trim());
							}
						}
					}
				}
			}
		}
		return list;
	}

	/*
	 * Get a list of all the comments for all tags in this compilation unit. This is
	 *	used to build a drop down list of existing comments to allow selecting to use the
	 *	same comment again when editing the Unknown tags.
	 * @return A list of all the comments for the specified tag.
	*/
	public Vector getCompilationUnitTagList()
	{
		Vector list = new Vector();

		for (int i = 0; i < summaryInfo.size(); i++) {
			CommentableCode c = (CommentableCode)summaryInfo.elementAt(i);
			if (c != null) {
				Enumeration enumOuter = c.getTagEntry();
				while (enumOuter.hasMoreElements()) {
					TagSet tagSet = (TagSet)enumOuter.nextElement();
					if (tagSet != null) {
						Vector theList = tagSet.getTagStringList();
						if (theList != null) {
							for (Enumeration enum = theList.elements(); (enum.hasMoreElements()) ; ) {
								Object item = enum.nextElement();
								if (item != null && !list.contains(item.toString().trim())) {
									list.add(item.toString().trim());
								}
							}
						}
					}
				}
			}
		}
		return list;
	}

	/*
	 * Get a list of all the unique tags in this compilation unit. This is
	 *	used to build a drop down list of existing tags to allow selecting to use the
	 *	same tag again when editing the Unknown tags.
	 * @return A list of all the tags.
	*/
	public Vector getCompilationUnitTags()
	{
		Vector list = new Vector();

		// Traverse the list of commentable things.
		for (int i = 0; i < summaryInfo.size(); i++) {
			CommentableCode c = (CommentableCode)summaryInfo.elementAt(i);
			if (c != null) {
				// For each commentable thing, traverse the list of tag sets, pulling
				//	out any unique tags.
				Enumeration enumOuter = c.getTagEntry();
				while (enumOuter.hasMoreElements()) {
					TagSet tagSet = (TagSet)enumOuter.nextElement();
					if (tagSet != null) {
						String name = tagSet.getTagName();
						if (tagSet.size() > 0 &&
								tagSet.tagAt(0).trim().length() > 0 &&
								!list.contains(name)
								) {
							list.add(name);
						}
					}
				}
			}
		}
		return list;
	}

	/** Get rid of a data listener
	 *
	 * @param l The listener to remove
	 */
	public synchronized void removeListDataListener(ListDataListener l)
	{
		if (listDataListeners != null && listDataListeners.contains(l)) {
			Vector v = (Vector) listDataListeners.clone();
			v.removeElement(l);
			listDataListeners = v;
		}
	}

	/** Add a new DataListener to be called every time the data is modified
	 *
	 * @param l The listener to add
	 */
	public synchronized void addListDataListener(ListDataListener l)
	{
		Vector v = listDataListeners == null ? new Vector(2) : (Vector) listDataListeners.clone();
		if (!v.contains(l)) {
			v.addElement(l);
			listDataListeners = v;
		}
	}

	/** This method should be called when a new group of elements is added to the
	 * list. It, in turn, fires the intervalAdded method of the listeners
	 *
	 * @param e The event to give to the listeners
	 */
	protected void fireIntervalAdded(ListDataEvent e)
	{
		if (listDataListeners != null) {
			Vector listeners = listDataListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++)
				((ListDataListener) listeners.elementAt(i)).intervalAdded(e);
		}
	}

	/** This method should be called when objects are removed from the list of
	 * code elements. This method invokes intervalRemoved on all of the listeners.
	 *
	 * @param e The event to pass to the listeners
	 */
	protected void fireIntervalRemoved(ListDataEvent e)
	{
		if (listDataListeners != null) {
			Vector listeners = listDataListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++)
				((ListDataListener) listeners.elementAt(i)).intervalRemoved(e);
		}
	}

	/** This method should be invoked whenever the contents of the list change.
	 * It fires the listeners for the contentsChanged event.
	 *
	 * @param e The event to pass to the listeners
	 */
	protected void fireContentsChanged(ListDataEvent e)
	{
		if (listDataListeners != null) {
			Vector listeners = listDataListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++)
				((ListDataListener) listeners.elementAt(i)).contentsChanged(e);
		}
	}

	/**
	 * This method should be invoked whenever the entire contents of the list change.
	 *  	It fires the listeners for the contentsChanged event indicating a big change
	 *		encompassing the whole list.
	 *
	 * @since 07/12/2001 2:41:46 PM
	 */
	public void fireContentsChanged()
	{
	  this.fireContentsChanged(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0,   this.getSize()));
	  this.fireContentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, this.getSize()));
	}


}
