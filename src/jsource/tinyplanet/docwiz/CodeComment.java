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
//  3/22/2001 Lee Meador (LeeMeador@usa.net) Changed the way comments are formatted to
//			effect the following: 1) No blank line ever appears between the text and
//			the parms if one of those is missing. 2) No comment appears if there is no
//			text and there are no parameters. 3) Added properties file options to
//			allow putting the "/**" on a line by itself, to remove the blank line
//			between the text and params in all cases, and to remove any blank lines
//			at the top of the text comment. 4) added an entry to the properties file
//			to tell how many columns comprise one tab and made the generated comments
//			use tabs at the front of each line (if the indent is great enough).
//	4/30/2001 Lee Meador - add method to get the compilation unit for this code comment.
//	5/17/2001 Lee Meador - Add accesssor method for the CommentableCode object (codeToComment).
//				Test for tag changed before setting the flag that the file is different.
//	7/12/2001 lmm	- Add a new format for the comments generated (a single line comment)
//					that will be generated when the preferences indicate to do so and the
//					comment is one line only and has no @tag lines.
//	7/15/2001 lmm	- Call the code item object and ask it how well it is commented. This
//					allows different criteria for each type. (eg - methods need the param
//					tags filled in, fields do not)

package tinyplanet.docwiz;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import tinyplanet.javaparser.*;
import java.util.*;

 /**
  * This class contains all the information about a JavaDoc comment, including the
  * title and all @ tags.
  *
  * @see
  * @since
  * @author Simon Arthur
  * @version
  */
public class CodeComment
{
 /** A Hashtable of all the tags within the CodeComment
  */
   protected Hashtable tags = new Hashtable(18);

 /** The Title string of the JavaDoc comment
  */
   protected String title;

 /** The complete, original JavaDoc string
  */
   protected String body;

 /** The line on which the code comment begins
  */
   public int beginLine, beginColumn, endLine, endColumn;

 /** The Java entity which the CodeComment applies to
  */
	public CommentableCode codeToComment;

 /** Create a new CodeComment for the given CommentableCode Java entity
  *
  * @param newCodeToComment The entity that we are commenting
  */
   public CodeComment(CommentableCode newCodeToComment) {
	  title = "";
	  setCodeToComment(newCodeToComment);
	  beginLine = -1; beginColumn = -1; endLine = -1; endColumn = -1;
	  return;
   }

 /** Create a new CodeComment for the given CommentableCode and the Token
  * for the comment, as produced by the  parser
  *
  * @param newCodeToComment The code which we are going to add comments to
  * @param t The token for the existing comment.
  */
   public CodeComment(CommentableCode newCodeToComment, Token t) {


	 beginLine = -1; beginColumn = -1; endLine = -1; endColumn = -1;
	 setCodeToComment(newCodeToComment);
	 String currentLine;
	 this.body = "";
	 String wholeComment = findJavaDoc(t);
	 String LS = System.getProperty("line.separator");
	 if (!wholeComment.startsWith("/**")) {
	   // it's not a JavaDoc comment
	   beginLine = -1; beginColumn = -1; endLine = -1; endColumn = -1;
	   title = "";
	   return;
	 } else {
	   wholeComment = wholeComment.substring(2); // trim off "/**"
	   if (!wholeComment.endsWith("*/")) {
		 // it's not a JavaDoc comment, this should never happen
		 title = "";
	   } else {
		 wholeComment = wholeComment.substring(0,wholeComment.length()-3); // trim off "*/"
	   }
	 }

	 StringTokenizer messageTokens = new StringTokenizer (wholeComment,"\n\r"); // bust up the comment by lines
	 if (messageTokens.hasMoreTokens() ){
	   currentLine = messageTokens.nextToken().trim();
	 } else {
	   // there's nothing there
	   title = "";
	   return;
	 }
	 currentLine = delLeadingChars(currentLine);
	 this.body += currentLine;
	 boolean isTag = false;
	 String tagSection;
	 while (messageTokens.hasMoreTokens()) {
	   // eat up lines before the @ tags, place them in body
	   currentLine = messageTokens.nextToken();
	   currentLine = delLeadingChars(currentLine);
	   currentLine = currentLine.trim();

	   if (currentLine.startsWith("@") ) {
		 if (messageTokens.hasMoreTokens()) {
		   // grab rest of comment.
		   // add in a \n because StringTokenizer.hasMoreTokens() has the
		   // undocumented side-effect of advancing its internal pointer past the token
		   // delimiter provided in the constructor call.
		   tagSection = currentLine + "\n" + messageTokens.nextToken("\u0000");
		 } else {
		   // line with @ tag is last line
		   tagSection = currentLine;
		 }
		 processTags( tagSection );
		 break;
	   }

	   if (!messageTokens.hasMoreTokens() && currentLine.equals("")) {
	   } else {
		 this.body += LS + currentLine;
	   }

	 } //while
   }

  /** Locate the JavaDoc attached to the token */
  private String findJavaDoc(Token currentToken) {
	String currentImage;
	Token currentSpecialToken;
	if (currentToken != null) {
	  currentSpecialToken = currentToken.specialToken;
	  currentImage =  currentToken.image;
	  if (currentImage.startsWith("/**")) {
		// we've found the JavaDoc.
		 beginLine = currentToken.beginLine;
		 beginColumn = currentToken.beginColumn;
		 endLine = currentToken.endLine;
		 endColumn = currentToken.endColumn;

		return currentToken.image;
	  } else {
		// recurively check all special tokens
		return findJavaDoc(currentSpecialToken);
	  }
	} else {
	   return "";
	}
  }

 /** Process all the tags for a comment
  *
  * @param tagSection The @ tag section with all tags
  */
   public void processTags(String tagSection) {
	 StringTokenizer messageTokens = new StringTokenizer (tagSection,"\n\r"); // bust up the comment by lines
	 String currentLine;
	 String currentTag;
	 currentLine = messageTokens.nextToken();
	 currentLine = delLeadingChars(currentLine);
	 currentTag = currentLine;
	 while(messageTokens.hasMoreTokens()) {
	   currentLine = messageTokens.nextToken();
	   currentLine = delLeadingChars(currentLine);
	   if (!currentLine.startsWith("@")) {
		 currentTag += " " +currentLine;
	   }
	   else {
		 //we've reached the end of that tag...
		 processATag(currentTag);
		   //if (messageTokens.hasMoreTokens()) {
		   //currentLine = messageTokens.nextToken();
		   //currentLine = delLeadingChars(currentLine);
		 currentTag = currentLine;
		   //  break;
		   //}
	   } //if
	 } //while
	 processATag(currentTag);
   }

 /** Get the TagSet for a given @ javadoc entry
  *  If no tag exists by that name, create it
  *
  * @param key The @ tag to look up
  * @return The set of values for the given @ tag
  */
   public TagSet getTagEntry(String key) {
	 return getTagEntry(key, true);
   }

  /** Get ALL created tag entries.
   * @return enumeration of TagSets
   */
  public Enumeration getTagEntries() {
	return getTags().elements();
  }

  public UnknownTagModel getUnknownTagEntry()
  {
		UnknownTagModel tagModel = new UnknownTagModel(this);
		return tagModel;
  }

 /** Get the TagSet for a given @ javadoc entry
  *
  *
  * @param key The @ tag to look up
  * @param createIfNeeded  If true, and no tag exists by that name, create it
  * @return The set of values for the given @ tag */

   public TagSet getTagEntry(String key, boolean createIfNeeded) {
	 TagSet returnValue;
	 returnValue = getTagSet(key);
	 // we want to build a new TagSet if there's no TagSet currently available
	 // for the Tag
	 if (returnValue==null && createIfNeeded) {
	   returnValue = TagModelFactory.tagSetCreateFromTag(getCodeToComment(), key);
	   addTagSet(key, returnValue);
	 }
	 return returnValue;
   }

  /** For tags which mean something by their very existence, i.e. deprecated
  */
  void updateExistentialTag(DocumentEvent e, String tagName) {
	TagSet theExistential = this.getTagEntry(tagName, false);

	if (theExistential != null) {
	  this.updateTag(e,tagName);
	} else {
	  // there is no existentialtag
	  this.clearTagEntries(tagName);
	}
  }

  /**   */
  //cd
  void updateTag(DocumentEvent e, String tagName) {
	Document doc = e.getDocument();
	String newValue = null;
	try {
	  newValue = doc.getText(0,doc.getLength());
	} catch (javax.swing.text.BadLocationException bse) {
	  System.err.println("CodeComment.updateTag:\n");
	  System.err.println(bse);
	  newValue = "";
	}
	updateTag(newValue, tagName);
  }

  void updateTag(String newValue, String tagName) {
	TagSet ts;
	ts = this.getTagEntry(tagName);
	if (ts.size() < 1) {
		ts.addValue(newValue);
		this.getCompilationUnit().touchFile();
	} else {
		if (!newValue.equals(ts.tagAt(0))) {
			ts.setTagAt(0,newValue);
			this.getCompilationUnit().touchFile();
		}
	}
}

 /** Store an @ tag entry for the given @ line and tokenizer
  *
  * @param currentLine The line containing the @ tag
  */
   void processATag(String currentLine) {
	 String tag = "", tagBody = "";
	 StringTokenizer tagTokens;
	 tagTokens = new StringTokenizer (currentLine,"* \t");
	 if (tagTokens.hasMoreTokens()) {
	   tag = tagTokens.nextToken();
	   try {
		 tagBody = tagTokens.nextToken("\r\n"); // get the rest of the line
		 tagBody = tagBody.trim();
	   } catch (NoSuchElementException nsee) {
		 // there's nothing left on the line
	   } ///catch
	 }  //if (tagTokens.hasMoreTokens()) {
	 // strip @ from tag
	 tag = tag.substring(1);
	 addTagEntry(tag,tagBody);
   }

 /** Add a new tag entry for the given @ tag
  *
  * @param type The kind of tag to add, without the "@"
  * @param value The value of the tag
  */
   public void addTagEntry(String type, String value) {
	 value = value.trim();
	 TagSet t = getTagSet(type);
	 if (t == null) {
	   t = TagModelFactory.tagSetCreateFromLine(getCodeToComment(), type + " " + value);
	   addTagSet(type, t);
	 }
	 t.addValue(value);
   }

   /**
    * store one tag set into where we store those for this comment.
	*
	 * @param type The type indicates which tag set to retreive. This is typically the string
	 *		that follows the @-sign in the java doc itself.
	* @param t The tag set to add.
	*/
   public void addTagSet(String type, TagSet t) {
	 getTags().put(type,t);
   }

	/**
	 * Retreive one tag set from those for this comment.
	 *
	 * @since 07/10/2001 1:42:52 PM
	 * @param type The type indicates which tag set to retreive. This is typically the string
	 *		that follows the @-sign in the java doc itself.
	 * @return The requested tag set.
	 */
	protected TagSet getTagSet(Object type)
	{
	 	TagSet t = (TagSet)getTags().get(type);
		return t;
	}

 /** Remove all the @ tag entries for the given tag
  *
  * @param type The type of @ tag to remove, without the "@"
  */
   public void clearTagEntries(String type) {
  //   TagSet  t = TagModelFactory.tagSetCreateFromTag(getCodeToComment(), type);
  //     addTagSet(type,t);
	 getTags().remove(type);
   }

 /**
  * remove the leading spaces and *'s from the line
  *
  * @param line The line from which we will remove the *'s and spaces
  * @return The given line, without leading spaces and asterisks
  */
   String delLeadingChars(String line) {
	 String newLine = "";
	 StringTokenizer messageTokens = new StringTokenizer (line,"* \t");
	 if (messageTokens.hasMoreElements()) {
	   newLine += messageTokens.nextElement();
	   try {
		 newLine +=  messageTokens.nextToken("\u0000"); // get the rest of the line
	   } catch (NoSuchElementException nsee) {
		 // there's nothing left on the line
	   }
	 }
	 return newLine;
   }


 /** Get the main JavaDoc title
  *
  * @return The title for the JavaDoc comment
  */
   public String getTitle() {
	 return this.body;
   }

 /** Change the title for the JavaDoc comment
  *
  * @param newtitle The new title for the comment
  */
   public void setTitle(String newtitle) {
	 this.body = newtitle;
   }

   /**
    * Get the compilation unit that this code comment is within
	*
	* @return The compilation unit to which this CodeComment belongs.
	*/
   public CompilationUnit getCompilationUnit() {
     return getCodeToComment().getCompilationUnit();
   }

	/**
	 * Get a list of all the comments for the specified tag in this compilation unit. This is
	 *	used to build a drop down list of existing comments to allow selecting to use the
	 *	same comment again.
	 * @param tag The tag that we want to see about. (e.g. 'param' would return a list of
	 *		the comments for all 'param' tags in this compilation unit.
	 * @return A list of all the comments for the specified tag.
	 */
	public Vector getCompilationUnitTagList(String tag)
	{
		return getCompilationUnit().getCompilationUnitTagList(tag);
	}

	/**
	 * Get a list of all the comments for all tags in this compilation unit. This is
	 *	used to build a drop down list of existing comments to allow selecting to use the
	 *	same comment again.
	 * @param tag The tag that we want to see about. (e.g. 'param' would return a list of
	 *		the comments for all 'param' tags in this compilation unit.
	 * @return A list of all the comments for the specified tag.
	 *
	 * @since 06/21/2001 12:38:20 PM
	 * @param
	 * @return
	 */
	public Vector getCompilationUnitTagList()
	{
		return getCompilationUnit().getCompilationUnitTagList();
	}

	/**
	 * Get a list of all the unique tags in this compilation unit. This is
	 *	used to build a drop down list of existing tags to allow selecting to use the
	 *	same tag again.
	 * @return A list of all the tags.
	 */
	public Vector getCompilationUnitTags()
	{
		return getCompilationUnit().getCompilationUnitTags();
	}

	/**
	 * Get an indication of how completely commented this class, field, etc is.
	 *
	 * @return 	0 means not commented. 4 means completely done.
	 */
   public int getCommentLevel() {
     return getCodeToComment().getCommentLevel(this.body != null && this.body.length() > 2);

//	 if (this.body != null && this.body.length() > 2) {
//	   return 4;
//	 } else {
//	   return 0;
//	 }
   }

 /** Convert the CodeComment to a JavaDoc comment, ready for embedding
  * in code.
  *
  * @return The JavaDoc comment for the current CodeComment
  */
   public String toJavaDocString() {
	String LS = System.getProperty("line.separator");
    ConfigurationService config = ConfigurationService.getConfigurationService();

	String currentLine;
	String theTitle = getTitle();

	String indentString = "";
	int indentLevel = getCodeToComment().getIndentLevel() - 1;
	int tabSize = config.getTabSize();
    int i = 0;
	while (i+tabSize-1 < indentLevel) {			// use tabs where possible
	 	indentString += "\t";
	 	i += tabSize;
	}
	for (;i<indentLevel;i++) {
		indentString += " ";
	}
	StringBuffer theDoc = new StringBuffer();

	// Use a buffered reader to read the Title a line at a time
	try {
	  if (theTitle == null) {
	  	theTitle = "";
	  };
	  java.io.LineNumberReader br =
		new java.io.LineNumberReader(new java.io.StringReader(theTitle));

	  Object currentKey;
	  TagSet currentValues;
	  Enumeration theKeys;
	  boolean singleLine = false;
	  boolean hasParams = true;

	  CommentableCode theCode = getCodeToComment();
	  // Todo: Each one of these types should have a flag to enable it in preferences.
	  if (theCode.usesSingleLineComment()) {
//	  if ((theCode instanceof tinyplanet.docwiz.Field && config.getFieldUsesSingleLineComment()) ||
//	  		(theCode instanceof tinyplanet.docwiz.Class && config.getClassUsesSingleLineComment()) ||
//	  		(theCode instanceof tinyplanet.docwiz.Interface && config.getInterfaceUsesSingleLineComment()) ||
//	  		(theCode instanceof tinyplanet.docwiz.Method && config.getMethodUsesSingleLineComment()) ||
//			(theCode instanceof tinyplanet.docwiz.Constructor && config.getConstructorUsesSingleLineComment())
//	  		) {
		  hasParams = false;
		  theKeys = getTags().keys();
		  while (theKeys.hasMoreElements()) {
			currentKey = theKeys.nextElement();
			currentValues = getTagSet(currentKey);
			String paramStr = currentValues.toJavaDocString(currentKey.toString());
			if (paramStr.trim().length() > 0) {
				hasParams = true;
				break;
			}
		  }

		  if (!hasParams) {
			  	java.io.LineNumberReader br2 =
					new java.io.LineNumberReader(new java.io.StringReader(theTitle));
				int lineCount = 0;
				do {
					currentLine = br2.readLine();
					if (currentLine != null && currentLine.trim().length() > 0) {
						++lineCount;
						if (lineCount == 2) {
							break;
						}
					}
				} while (currentLine != null);
				singleLine = (lineCount == 1);
		  }
	  }
//	  System.out.println("CodeComment.toJavaDocString: hasParams = " + hasParams + ", singleLine = " + singleLine);

	  String frontOfLine;
	  if (!singleLine && config.getExtraLineAtJavaDocTop()) {
		  // If you desire the style where every javadoc has the first line without text ...
		  frontOfLine = indentString + "/**" + LS + indentString + " * ";
	  }
	  else  {
		  // If you desire the style where every javadoc has text on the first line.
		  frontOfLine = indentString + "/** ";
	  }

	  boolean firstLine = true;
	  do {
		currentLine = br.readLine();
		if (currentLine != null) {
										// Check if we allow the user to add a blank
										//  line at the top of the javadoc comment.
			if (!config.getIgnoreBlankLinesAtJavaDocTop()
					|| !firstLine
					|| currentLine.trim().length() > 0
					) {					// Ignore blank lines on front if option set
				firstLine = false;
				theDoc.append(frontOfLine
					+ ((currentLine.startsWith("@")) ? "\"" : "") 	// leading @ sign is confusing to parser
					+ currentLine
					+ (singleLine ? "" : LS)
					);
				frontOfLine = indentString + " * ";
			}
		}
	  } while (currentLine != null);

	  // Now do params. First see if there are any parms. Then add a blank line (if there are
	  //  and then add the parms.

	  theKeys = getTags().keys();
	  boolean firstTime = true;
	  while (theKeys.hasMoreElements()) {
		currentKey = theKeys.nextElement();
		currentValues = getTagSet(currentKey);
		String paramStr = currentValues.toJavaDocString(currentKey.toString());
		if (paramStr.trim().length() > 0) {
			if (firstTime) {			// When we find a param to add on, preceed with blank line
				firstTime = false;
				if (theDoc.length() > 0		// Only add blank line if there was any comment text.
						&& config.getAddBlankLineBeforeParms() // ... and if the option is set.
						) {
					theDoc.append(frontOfLine + LS);
					frontOfLine = indentString + " * ";
				}
			}
			theDoc.append(frontOfLine + paramStr);
			frontOfLine = indentString + " * ";
		}
	  }
	} catch (java.io.IOException ioe) {
	  System.err.println(ioe);
	}
	if (theDoc.length() > 0) {
		theDoc.append(indentString+ " */");
	}
	return theDoc.toString();
   }

// Get/Set methods:

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link codeToComment }.
	 * @since  05/17/2001 9:51:41 PM
	 */
	public CommentableCode getCodeToComment()
	{
		return this.codeToComment;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link codeToComment }.
	 * @since  05/17/2001 9:51:41 PM
	 */
	public void setCodeToComment(CommentableCode val)
	{
		this.codeToComment = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link tags }.
	 * @since  07/10/2001 1:38:31 PM
	 */
	public Hashtable getTags()
	{
		return this.tags;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link tags }.
	 * @since  07/10/2001 1:38:31 PM
	 */
	public void setTags(Hashtable val)
	{
		this.tags = val;
	}

}