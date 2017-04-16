/*
DocWiz: Easy Javadoc documentation
Copyright (C) 2000 Simon Arthur

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

package tinyplanet.docwiz;

import tinyplanet.userconfig.UserProperties;


public class ConfigurationService
{

// Attributes:

	/**
	 * whether or not to start the GUI, based upon the
	 * given options
	 */
	public boolean startGUI = true;

	/**
	 * whether or not to show the file scan info
	 */
	public boolean doScan = false;

	/**
	 * Max line length for comments
	 */
	public int iMaxLineLength = 78;

	/**
	 * Whether or not to use end-of-line comments for field javadoc
	 */
	public static boolean bGrabFieldComments = false;

	/**
	 * whether or not to fill and exit
	 */
	public boolean doFill = false;

	/**
	 * use word wrap on tag titles?
	 */
	public boolean bFormatTagTitle = false;

	private static final String SIZEX_KEY = "windowSizeX";
	int sizeX = 600;

	private static final String SIZEY_KEY = "windowSizeY";
	int sizeY = 400;

	private static final String EDITPANE_KEY = "editControlsPane";
	int editPaneLocation = 1;				// 1 is the top of the horizontal divider

	private static final String UNKTAGPANE_KEY = "unknownTagPane";
	int unknownTagPaneLocation = 1;

	private static final String ITEMPANE_KEY = "itemListPane";
	int itemPaneLocation = 0;				// 0 is the left side of the vertical divider

	private static final String CODEPANE_KEY = "codeViewPane";
	int codePaneLocation = 2;				// 2 is the bottom of the horiz divider

	private static final String COMMENTPANE_KEY = "commentDisplayPane";
	int commentPaneLocation = 2;

	private static final String SHELF1_KEY = "shelfSize1";
	int shelf1Size = 150;

	private static final String SHELF0_KEY = "shelfSize0";
	int shelf0Size = 100;

	private static final String LASTDIR_KEY = "lastDirectory";
	String lastDirectory = ".";

	/**
	 * The number of columns between tabs on the input and output.
	 *
	 * @since 3/22/2001 11:26:12 AM
	 */
	private static final String TABSIZE_KEY = "tabSize";
	private int tabSize = 8;

	/**
	 * Some people like to have the first line of the javadoc with only the slash-star-star
	 * on it and some like to start the comment text on that line.
	 * true will leave off the text, false will put the text there.
	 *
	 * @since 3/22/2001 12:14:44 PM
	 */
	private static final String EXTRALINEATJAVADOCTOP_KEY = "extraLineAtJavaDocTop";
	private boolean extraLineAtJavaDocTop = false;

	/**
	 * Some people like to turn {@link extraLineAtJavaDocTop} off and turn this on so that
	 * they can put the extra line at the top when they want to.
	 * true will remove any blank lines at the top of a javadoc, false will preserve them.
	 * (blank lines may have an indentation and star at the front but no text.)
	 *
	 * @since 3/22/2001 12:17:43 PM
	 */
	private static final String IGNOREBLANKLINESATJAVADOCTOP_KEY = "ignoreBlankLinesAtJavaDocTop";
	private boolean ignoreBlankLinesAtJavaDocTop = false;

	/**
	 * Some people want a blank line between the text and the parameters. (Note: No blank
	 * line is ever generated if there are no parameters or if there is no text.)
	 * true will put a blank line (with an indented star) between the comment text and the
	 * parameters, false will not put the blank line there.
	 *
	 * @since 3/22/2001 12:20:10 PM
	 */
	private static final String ADDBLANKLINEBEFOREPARMS_KEY = "addBlankLineBeforeParms";
	private boolean addBlankLineBeforeParms = true;

	/**
	 * Some people want to see whether a method or field is public, private, etc. in the
	 *		list pane.
	 *
	 * @since  07/11/2001 6:05:43 PM
	 */
	private static final String SHOWSCOPEINLIST_KEY = "showScopeInList";
	private boolean showScopeInList = false;

	/**
	 * Some people want to show a single line comment for the javadoc on any method
	 *		that has only one line of comment and no @tag entries.
	 *
	 * @since  07/11/2001 6:12:21 PM
	 */
	private static final String METHODUSESSINGLELINECOMMENT_KEY = "methodUsesSingleLineComment";
	private boolean methodUsesSingleLineComment = false;

	/**
	 * Some people want to show a single line comment for the javadoc on any attribute/field
	 *		that has only one line of comment and no @tag entries.
	 *
	 * @since  07/11/2001 6:12:21 PM
	 */
	private static final String FIELDUSESSINGLELINECOMMENT_KEY = "fieldUsesSingleLineComment";
	private boolean fieldUsesSingleLineComment = false;

	/**
	 * Some people want to show a single line comment for the javadoc on any constructor
	 *		that has only one line of comment and no @tag entries.
	 *
	 * @since  07/11/2001 6:12:21 PM
	 */
	private static final String CONSTRUCTORUSESSINGLELINECOMMENT_KEY = "constructorUsesSingleLineComment";
	private boolean constructorUsesSingleLineComment = false;

	/**
	 * Some people want to show a single line comment for the javadoc on any class
	 *		that has only one line of comment and no @tag entries.
	 *
	 * @since  07/11/2001 6:12:21 PM
	 */
	private static final String CLASSUSESSINGLELINECOMMENT_KEY = "classUsesSingleLineComment";
	private boolean classUsesSingleLineComment = false;

	/**
	 * Some people want to show a single line comment for the javadoc on any interface
	 *		that has only one line of comment and no @tag entries.
	 *
	 * @since  07/11/2001 6:12:21 PM
	 */
	private static final String INTERFACEUSESSINGLELINECOMMENT_KEY = "interfaceUsesSingleLineComment";
	private boolean interfaceUsesSingleLineComment = false;

	/**
	 * Some people want to show all methods in the list pane, some want to show
	 *		private methods with a greyed out appearance, some want to not show
	 *		private methods at all.
	 *
	 * @since  07/11/2001 6:12:21 PM
	 */
	private static final String METHODAPPEARANCEINLIST_KEY = "methodAppearanceInList";
	private int methodAppearanceInList = 0;

	/**
	 * Some people want to show all fields in the list pane, some want to show
	 *		private fields with a greyed out appearance, some want to not show
	 *		private fields at all.
	 *
	 * @since  07/11/2001 6:12:21 PM
	 */
	private static final String FIELDAPPEARANCEINLIST_KEY = "fieldAppearanceInList";
	private int fieldAppearanceInList = 0;

	/**
	 * Some people want to show all constructors in the list pane, some want to show
	 *		private constructors with a greyed out appearance, some want to not show
	 *		private constructors at all.
	 *
	 * @since  07/11/2001 6:12:21 PM
	 */
	private static final String CONSTRUCTORAPPEARANCEINLIST_KEY = "constructorAppearanceInList";
	private int constructorAppearanceInList = 0;

	/**
	 * Some people want to show all classes in the list pane, some want to show
	 *		private classes with a greyed out appearance, some want to not show
	 *		private classes at all.
	 *
	 * @since  07/11/2001 6:12:21 PM
	 */
	private static final String CLASSAPPEARANCEINLIST_KEY = "classAppearanceInList";
	private int classAppearanceInList = 0;

	/**
	 * Some people want to show all interfaces in the list pane, some want to show
	 *		private interfaces with a greyed out appearance, some want to not show
	 *		private interfaces at all.
	 *
	 * @since  07/11/2001 6:12:21 PM
	 */
	private static final String INTERFACEAPPEARANCEINLIST_KEY = "interfaceAppearanceInList";
	private int interfaceAppearanceInList = 0;

	/**
	 * The version of this release
	 */
	public static final String VERSION_NAME = "0.68";

	/**
	 * String which contains information about copyright, license, and version
	 */
	public static final String COPYRIGHT_NOTICE =
	    "DocWiz version " + VERSION_NAME + "\n" +
	    "Easy JavaDoc documentation: \n" +
	    "http://www.mindspring.com/~chroma/docwiz/\n" +
	    "Copyright (C) 1998-2001 Simon Arthur\n\n" +
	    "DocWiz comes with ABSOLUTELY NO WARRANTY;\n" +
	    "for details see the file LICENSE.txt or \n" +
	    "www.gnu.org. \n" +
	    "This is free software, and you are welcome\n" +
	    "to redistribute it under certain conditions;\n" +
	    "see LICENSE.txt or www.gnu.org for details.\n";

	/**
	 * String to which describes the command line usage
	 */
	public static final String HELP_NOTICE =
	    "DocWiz version " + VERSION_NAME + "\n" +
	    "Usage:\n" +
	    "  java docwiz [options] [filename] [filename2]\n" +
	    "Options:\n" +
	    "  --help, -h show command-line help\n" +
	    "  --version, -v show version information\n" +
	    "  --scan-comments, -s print some information about files\n" +
	    "  --fill-in, -f fill in template comments and exit\n" +
	    //"  --grab-end, -g grab field end-of-line comments as the field description\n"+
	    "  --line-length=num, -l num  maxmum line length\n" +
	    //"  --no-title-format, -n do not format tag titles\n"+
	    "\n";

	static ConfigurationService theConfigurationService;

// Constructors:

	/**
	 * Private to prevent direct instantiation
	 */
	private ConfigurationService()
	{}

// Methods:

	public static synchronized ConfigurationService getConfigurationService()
	{
		if (theConfigurationService == null) {
			theConfigurationService = new ConfigurationService();
		}

		return theConfigurationService;
	}


	public void loadConfiguration()
	{
		UserProperties up = new UserProperties("docwiz");
		try {
			up.loadProperties("properties");
		}
		catch (java.io.IOException ioe) {
			System.err.println("Can't load properties from file");
			return ;
		}

		setSizeX(up.getIntProperty(SIZEX_KEY, sizeX));
		setSizeY(up.getIntProperty(SIZEY_KEY, sizeY));

		setEditPaneLocation(up.getIntProperty(EDITPANE_KEY, editPaneLocation));
		setUnknownTagPaneLocation(up.getIntProperty(UNKTAGPANE_KEY, unknownTagPaneLocation));
		setItemPaneLocation(up.getIntProperty(ITEMPANE_KEY, itemPaneLocation));
		setCodePaneLocation(up.getIntProperty(CODEPANE_KEY, codePaneLocation));
		setCommentPaneLocation(up.getIntProperty(COMMENTPANE_KEY, commentPaneLocation));

		setShelf0Size(up.getIntProperty(SHELF0_KEY, shelf0Size));
		setShelf1Size(up.getIntProperty(SHELF1_KEY, shelf1Size));

		setLastDirectory(up.getProperty(LASTDIR_KEY, "."));

		setTabSize(up.getIntProperty(TABSIZE_KEY, tabSize));

		setExtraLineAtJavaDocTop(up.getBooleanProperty(EXTRALINEATJAVADOCTOP_KEY, extraLineAtJavaDocTop));
		setIgnoreBlankLinesAtJavaDocTop(up.getBooleanProperty(IGNOREBLANKLINESATJAVADOCTOP_KEY, ignoreBlankLinesAtJavaDocTop));
		setAddBlankLineBeforeParms(up.getBooleanProperty(ADDBLANKLINEBEFOREPARMS_KEY, addBlankLineBeforeParms));

		setShowScopeInList(up.getBooleanProperty(SHOWSCOPEINLIST_KEY, showScopeInList));

		setMethodUsesSingleLineComment(up.getBooleanProperty(METHODUSESSINGLELINECOMMENT_KEY, methodUsesSingleLineComment));
		setFieldUsesSingleLineComment(up.getBooleanProperty(FIELDUSESSINGLELINECOMMENT_KEY, fieldUsesSingleLineComment));
		setConstructorUsesSingleLineComment(up.getBooleanProperty(CONSTRUCTORUSESSINGLELINECOMMENT_KEY, constructorUsesSingleLineComment));
		setClassUsesSingleLineComment(up.getBooleanProperty(CLASSUSESSINGLELINECOMMENT_KEY, classUsesSingleLineComment));
		setInterfaceUsesSingleLineComment(up.getBooleanProperty(INTERFACEUSESSINGLELINECOMMENT_KEY, interfaceUsesSingleLineComment));

		setMethodAppearanceInList(up.getIntProperty(METHODAPPEARANCEINLIST_KEY, methodAppearanceInList));
		setFieldAppearanceInList(up.getIntProperty(FIELDAPPEARANCEINLIST_KEY, fieldAppearanceInList));
		setConstructorAppearanceInList(up.getIntProperty(CONSTRUCTORAPPEARANCEINLIST_KEY, constructorAppearanceInList));
		setClassAppearanceInList(up.getIntProperty(CLASSAPPEARANCEINLIST_KEY, classAppearanceInList));
		setInterfaceAppearanceInList(up.getIntProperty(INTERFACEAPPEARANCEINLIST_KEY, interfaceAppearanceInList));

	}

	public void saveConfiguration()
	{
		UserProperties up = new UserProperties("docwiz");

		up.setProperty(SIZEX_KEY, getSizeX());
		up.setProperty(SIZEY_KEY, getSizeY());
		up.setProperty(EDITPANE_KEY, getEditPaneLocation());
		up.setProperty(UNKTAGPANE_KEY, getUnknownTagPaneLocation());
		up.setProperty(ITEMPANE_KEY, getItemPaneLocation());
		up.setProperty(CODEPANE_KEY, getCodePaneLocation());
		up.setProperty(COMMENTPANE_KEY, getCommentPaneLocation());
		up.setProperty(SHELF0_KEY, getShelf0Size());
		up.setProperty(SHELF1_KEY, getShelf1Size());
		up.setProperty(LASTDIR_KEY, getLastDirectory());
		up.setProperty(TABSIZE_KEY, getTabSize());

		up.setProperty(EXTRALINEATJAVADOCTOP_KEY, getExtraLineAtJavaDocTop());
		up.setProperty(IGNOREBLANKLINESATJAVADOCTOP_KEY, getIgnoreBlankLinesAtJavaDocTop());
		up.setProperty(ADDBLANKLINEBEFOREPARMS_KEY, getAddBlankLineBeforeParms());

		up.setProperty(SHOWSCOPEINLIST_KEY, getShowScopeInList());

		up.setProperty(METHODUSESSINGLELINECOMMENT_KEY, getMethodUsesSingleLineComment());
		up.setProperty(FIELDUSESSINGLELINECOMMENT_KEY, getFieldUsesSingleLineComment());
		up.setProperty(CONSTRUCTORUSESSINGLELINECOMMENT_KEY, getConstructorUsesSingleLineComment());
		up.setProperty(CLASSUSESSINGLELINECOMMENT_KEY, getClassUsesSingleLineComment());
		up.setProperty(INTERFACEUSESSINGLELINECOMMENT_KEY, getInterfaceUsesSingleLineComment());

		up.setProperty(METHODAPPEARANCEINLIST_KEY, getMethodAppearanceInList());
		up.setProperty(FIELDAPPEARANCEINLIST_KEY, getFieldAppearanceInList());
		up.setProperty(CONSTRUCTORAPPEARANCEINLIST_KEY, getConstructorAppearanceInList());
		up.setProperty(CLASSAPPEARANCEINLIST_KEY, getClassAppearanceInList());
		up.setProperty(INTERFACEAPPEARANCEINLIST_KEY, getInterfaceAppearanceInList());

		try {
			up.storeProperties("properties");
		}
		catch (java.io.IOException ioe) {
			System.err.println(ioe);
		}
	}


	private int propertyToInt(String s, int theDefault)
	{
		try {
			if (s != null) {
				return Integer.parseInt(s.trim());
			}
			else {
				return theDefault;
			}
		}
		catch (NumberFormatException nfe) {
			System.err.println("Cannot convert String '" + s + "' to integer in " +
			                   "configuration file");
			return theDefault;
		}
	}

	/**
	 * Convert the string from the property file to a boolean value. A string that evaluates to 0 is false.
	 * all other values give true. (These are stored at 0 and 1.
	 *
	 * @since 3/22/2001 12:28:11 PM
	 * @param s The string to evaluate.
	 * @return true if the string value isn't 0 and false if it is.
	 */
	private boolean propertyToBoolean(String s, boolean theDefault)
	{
		return (propertyToInt(s, (theDefault ? 1 : 0)) != 0);
	}


// Get/Set routines:

	public boolean getDebug()
	{
		return false;
	}


	public boolean getStartGUI()
	{
		return startGUI;
	}


	public void setStartGUI(boolean newStartGUI)
	{
		startGUI = newStartGUI;
	}


	public boolean getFormatTagTitle()
	{
		return bFormatTagTitle;
	}


	public void setFormatTagTitle(boolean newFormatTagTitle)
	{
		bFormatTagTitle = newFormatTagTitle;
	}


	public boolean getGrabFieldComments()
	{
		return bGrabFieldComments;
	}


	public void setGrabFieldComments(boolean newGrabFieldComments)
	{
		bGrabFieldComments = newGrabFieldComments;
	}


	public boolean getDoFill()
	{
		return doFill;
	}


	public void setDoFill(boolean newDoFill)
	{
		doFill = newDoFill;
	}


	public boolean getDoScan()
	{
		return doScan;
	}


	public void setDoScan(boolean newDoScan)
	{
		doScan = newDoScan;
	}


	public int getMaxLineLength()
	{
		return iMaxLineLength;
	}


	public void setMaxLineLength(int newMaxLineLength)
	{
		iMaxLineLength = newMaxLineLength;
	}


	public void setShelf1Size(int newShelf1Size)
	{
		this.shelf1Size = newShelf1Size;
	}


	public void setShelf0Size(int newShelf0Size)
	{
		this.shelf0Size = newShelf0Size;
	}


	public void setSizeX(int newSizeX)
	{
		this.sizeX = newSizeX;
	}


	public void setSizeY(int newSizeY)
	{
		this.sizeY = newSizeY;
	}


	public void setEditPaneLocation(int newEditPaneLocation)
	{
		this.editPaneLocation = newEditPaneLocation;
	}


	public void setUnknownTagPaneLocation(int newUnknownTagPaneLocation)
	{
		this.unknownTagPaneLocation = newUnknownTagPaneLocation;
	}


	public void setItemPaneLocation(int newItemPaneLocation)
	{
		this.itemPaneLocation = newItemPaneLocation;
	}


	public void setCodePaneLocation(int newCodePaneLocation)
	{
		this.codePaneLocation = newCodePaneLocation;
	}


	public void setCommentPaneLocation(int newCommentPaneLocation)
	{
		this.commentPaneLocation = newCommentPaneLocation;
	}


	public int getShelf1Size()
	{
		return this.shelf1Size;
	}


	public int getShelf0Size()
	{
		return this.shelf0Size;
	}


	public void setLastDirectory(String newLastDirectory)
	{
		this.lastDirectory = newLastDirectory;
	}


	public int getSizeX()
	{
		return sizeX;
	}


	public int getSizeY()
	{
		return sizeY;
	}


	public int getEditPaneLocation()
	{
		return editPaneLocation;
	}


	public int getUnknownTagPaneLocation()
	{
		return unknownTagPaneLocation;
	}


	public int getItemPaneLocation()
	{
		return itemPaneLocation;
	}


	public int getCodePaneLocation()
	{
		return codePaneLocation;
	}


	public int getCommentPaneLocation()
	{
		return commentPaneLocation;
	}


	public String getLastDirectory()
	{
		return this.lastDirectory;
	}


	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @since 3/22/2001 11:26:24 AM
	 * @return The current value of the attribute {@link tabSize }.
	 */
	public int getTabSize()
	{
		return this.tabSize;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @since 3/22/2001 11:26:24 AM
	 * @param val The new value of the attribute {@link tabSize }.
	 */
	public void setTabSize(int val)
	{
		this.tabSize = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @since 3/22/2001 12:14:51 PM
	 * @return The current value of the attribute {@link extraLineAtJavaDocTop }.
	 */
	public boolean getExtraLineAtJavaDocTop()
	{
		return this.extraLineAtJavaDocTop;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @since 3/22/2001 12:14:51 PM
	 * @param val The new value of the attribute {@link extraLineAtJavaDocTop }.
	 */
	public void setExtraLineAtJavaDocTop(boolean val)
	{
		this.extraLineAtJavaDocTop = val;
	}


	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @since 3/22/2001 12:17:47 PM
	 * @return The current value of the attribute {@link
	 *     ignoreBlankLinesAtJavaDocTop }.
	 */
	public boolean getIgnoreBlankLinesAtJavaDocTop()
	{
		return this.ignoreBlankLinesAtJavaDocTop;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @since 3/22/2001 12:17:47 PM
	 * @param val The new value of the attribute {@link
	 *     ignoreBlankLinesAtJavaDocTop }.
	 */
	public void setIgnoreBlankLinesAtJavaDocTop(boolean val)
	{
		this.ignoreBlankLinesAtJavaDocTop = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @since 3/22/2001 12:20:13 PM
	 * @return The current value of the attribute {@link addBlankLineBeforeParms
	 *     }.
	 */
	public boolean getAddBlankLineBeforeParms()
	{
		return this.addBlankLineBeforeParms;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @since 3/22/2001 12:20:13 PM
	 * @param val The new value of the attribute {@link addBlankLineBeforeParms
	 *     }.
	 */
	public void setAddBlankLineBeforeParms(boolean val)
	{
		this.addBlankLineBeforeParms = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link showScopeInList }.
	 * @since  07/11/2001 6:05:51 PM
	 */
	public boolean getShowScopeInList()
	{
		return this.showScopeInList;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link showScopeInList }.
	 * @since  07/11/2001 6:05:51 PM
	 */
	public void setShowScopeInList(boolean val)
	{
		this.showScopeInList = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link methodUsesSingleLineComment }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public boolean getMethodUsesSingleLineComment()
	{
		return this.methodUsesSingleLineComment;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link methodUsesSingleLineComment }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public void setMethodUsesSingleLineComment(boolean val)
	{
		this.methodUsesSingleLineComment = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link fieldUsesSingleLineComment }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public boolean getFieldUsesSingleLineComment()
	{
		return this.fieldUsesSingleLineComment;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link fieldUsesSingleLineComment }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public void setFieldUsesSingleLineComment(boolean val)
	{
		this.fieldUsesSingleLineComment = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link constructorUsesSingleLineComment }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public boolean getConstructorUsesSingleLineComment()
	{
		return this.constructorUsesSingleLineComment;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link constructorUsesSingleLineComment }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public void setConstructorUsesSingleLineComment(boolean val)
	{
		this.constructorUsesSingleLineComment = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link classUsesSingleLineComment }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public boolean getClassUsesSingleLineComment()
	{
		return this.classUsesSingleLineComment;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link classUsesSingleLineComment }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public void setClassUsesSingleLineComment(boolean val)
	{
		this.classUsesSingleLineComment = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link interfaceUsesSingleLineComment }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public boolean getInterfaceUsesSingleLineComment()
	{
		return this.interfaceUsesSingleLineComment;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link interfaceUsesSingleLineComment }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public void setInterfaceUsesSingleLineComment(boolean val)
	{
		this.interfaceUsesSingleLineComment = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link methodAppearanceInList }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public int getMethodAppearanceInList()
	{
		return this.methodAppearanceInList;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link methodAppearanceInList }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public void setMethodAppearanceInList(int val)
	{
		this.methodAppearanceInList = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link fieldAppearanceInList }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public int getFieldAppearanceInList()
	{
		return this.fieldAppearanceInList;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link fieldAppearanceInList }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public void setFieldAppearanceInList(int val)
	{
		this.fieldAppearanceInList = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link constructorAppearanceInList }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public int getConstructorAppearanceInList()
	{
		return this.constructorAppearanceInList;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link constructorAppearanceInList }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public void setConstructorAppearanceInList(int val)
	{
		this.constructorAppearanceInList = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link classAppearanceInList }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public int getClassAppearanceInList()
	{
		return this.classAppearanceInList;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link classAppearanceInList }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public void setClassAppearanceInList(int val)
	{
		this.classAppearanceInList = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link interfaceAppearanceInList }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public int getInterfaceAppearanceInList()
	{
		return this.interfaceAppearanceInList;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link interfaceAppearanceInList }.
	 * @since  07/11/2001 6:12:38 PM
	 */
	public void setInterfaceAppearanceInList(int val)
	{
		this.interfaceAppearanceInList = val;
	}

}
