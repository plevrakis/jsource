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

// History:
//	7/10/2001 lmm	- change to order the special declarations correctly (they are stored in
//					reverse order) so comments appear in the correct order in display.
//	7/12/2001 lmm	- add an attribute to hold the scope and set it in constructor
//					- add method to show the tokens for a SimpleNode for debugging
//					- add method to show scope IF preferences allow it for this type of
//					code item.
//	7/16/2001 lmm	- add methods to query preferences that relate to the code type so
//					they can be overridden in the final subclasses.
//	8/2/2001 lmm	- fix bug where certain commentable code items would hang the JVM
//					if they didn't have the scope (public, private) and have it first
//					on the line. The while loop wasn't moving its current pointer.


package tinyplanet.docwiz;
import java.util.Vector;
import java.util.Enumeration;
import tinyplanet.javaparser.*;

 /** An abstraction for a piece of code which can be given a JavaDoc comment
  *
  * @author Simon Arthur
  */
public class CommentableCode implements WritableCode
{
	/** Constant for use with {@link #getAppearanceInList} */
	public static final int PRIVATE_SHOW = 0;

	/** Constant for use with {@link #getAppearanceInList} */
	public static final int PRIVATE_GREY = 1;

	/** Constant for use with {@link #getAppearanceInList} */
	public static final int PRIVATE_NO_SHOW = 2;

 /** The line on which the code is declared */
   int line_number, column_number;

 /** The name of the entity which this class represents */
   String name;

 /** The CodeComment which describes this CommentableCode */

   CodeComment comment;
 /** The SimpleNode for the current CommentableCode, produced by the Java parser */

   SimpleNode baseNode;

   CompilationUnit currentUnit;

	/**
	 * This is either "blank", "package", "protected", "public" or "private".
	 * @since  07/10/2001 6:20:32 PM
	 */
	private String scope;

 /** Create a new CommentableCode
  *
  */
   public CommentableCode () {
   }

   public CompilationUnit getCompilationUnit() {
     return currentUnit;
   }

   public void setCompilationUnit(CompilationUnit u) {
     currentUnit = u;
   }

 /** Give a summary description of the code entity which this object describes,
  * including the line number
  *
  * @return A summary description of the CommentableCode
  */
   public String toString() {
     java.lang.Class c = this.getClass();
     String commentableTypeName = c.toString();
     return scope + " " + commentableTypeName  + " " + name + ": " + line_number;
   }

 /** Create a new CommentableCode entity based upon the given SimpleNode.
  *
  * @param n The root node for the entity, as produced by the Java parser.
  */
   public CommentableCode (SimpleNode n) {
//  System.out.println("CommentableCode.<init>: entry");
     this.baseNode = n;

/*     System.out.println("CommentableCode: " + this + " ... " + getCodeWithoutSpecials());
	 System.out.println(n.getClass().getName() + ":" + n.first_token.image);
     for (int i = 0; i < n.jjtGetNumChildren(); ++i) {
		SimpleNode n1 = (SimpleNode)n.jjtGetChild(i);
		if (n1 != null) {
	  		System.out.print(n1.getClass().getName() + ":" + n1.first_token.image+ ", ");
		}
     }
	 System.out.println("");
*/
//  System.out.println("CommentableCode.<init>: 1");
	 scope = "";
	 Token currentToken = n.first_token;
	 if (n.jjtGetNumChildren() > 0) {
	 	SimpleNode child = (SimpleNode)n.jjtGetChild(0);
		if (child != null) {
			Token endToken = child.first_token;
			while (currentToken != endToken && currentToken != null) {
				String tokenString = currentToken.image;
				if (tokenString.equals("private")) {
					scope = tokenString;
					break;
				}
				else if (tokenString.equals("public")) {
					scope = tokenString;
					break;
				}
				else if (tokenString.equals("protected")) {
					scope = tokenString;
					break;
				}
				currentToken = currentToken.next;
			}
		}
	 }
	 //System.out.println("this is " + scope);

//  System.out.println("CommentableCode.<init>: 2");
     line_number = n.first_token.beginLine;
     column_number = n.first_token.beginColumn;
     setupCodeComment(n);
//  System.out.println("CommentableCode.<init>: exit");
   }

   public int getIndentLevel() {
    return  baseNode.first_token.beginColumn;
   }

 /** Produce a CodeComment for this entity
  *
  * @param n The root node for the current entity
  */
   void setupCodeComment (SimpleNode n) {
     if (n.first_token.specialToken != null) {
       comment = new CodeComment(this, n.first_token.specialToken);
     } else {
       comment = new CodeComment(this);
     }
   }

 /** Give the name of the current Java code entity which this object represents
  *
  * @return The name of the current entity
  */
   public String getName() {
     return name;
   }

 /** Convience method for getting the title of the current entity's
  * code comment
  *
  * @return The title description for the code entity
  */
   public String getCodeCommentTitle() {
     return comment.getTitle();
   }

 /** Convenience method to access an entry for a given @ tag
  * Creates a new one if tag doesn't exist
  * @param type The @ tag to find, without "@"
  * @return The TagSet for the given @ tag
  */
  public TagSet getTagEntry(String type) {
     return getTagEntry(type, true);
   }

 /** Convenience method to access an entry for a given @ tag
  *
  * @param type The @ tag to find, without "@"
  * @param  createNew whether to create a new entry if a tag was not found by that name
  * @return The TagSet for the given @ tag
  */
  public TagSet getTagEntry(String type, boolean createNew) {
     TagSet entry;
     entry = comment.getTagEntry(type,createNew);
     return entry;
   }

 /** Convenience method to access all tag entries
  *
  * @return An Enumeration of all the TagSet's
  */
  public Enumeration getTagEntry() {
     return comment.getTagEntries();
   }

 /** The line number on which the declaration for the entity begins
  *
  * @return The line number where this Java entity's declaration begins
  */
  public int getLineNumber() {
    return this.line_number;
  }
 /** Gets the column on which the declaration for this entity begins
  *
  * @return The column number where the declaration for this entity begins
  */
  public int getColumnNumber() {
    return this.column_number;
  }
 /** Get the CodeComment object which describes the JavaDoc comment for
  * the current entity
  *
  * @return The CodeComment for the current entity
  */
  public CodeComment getCodeComment() {
    return this.comment;
  }


 /** Modify the current Java entity's CodeComment to be the given
  * CodeComment object
  *
  * @param c The CodeComment which will be used to describe the current entity.
  */
  public void setCodeComment(CodeComment c) {
    this.comment =  c;
  }

  private String _declarationString = null;

  /** Give the full string representation of the code to be commented
  */
  public String getDeclarationString() {
    if (_declarationString==null) {
      StringBuffer newDecl = new StringBuffer();
      // seek out the entire declatration
      Token currentToken =  baseNode.first_token;
      Token spToken;
      //int currentLine = currentToken.beginLine;
      boolean isFirst = true;
      while  (true /* currentToken != baseNode.last_token*/) {
        if (currentToken.specialToken != null) {
          if (isFirst) {
//System.out.println("CommentableCode.getDeclarationString: First token begin");
            newDecl.append(getSpecialTokenDeclatations(currentToken.specialToken, false));
//System.out.println("CommentableCode.getDeclarationString: First token end");
            isFirst = false;
          } else {
           newDecl.append(getSpecialTokenDeclatations(currentToken.specialToken, true));
          }
        }
        newDecl.append( currentToken.image);
		if (currentToken == baseNode.last_token) {
			break;
		}
        currentToken = currentToken.next;
      }
//      newDecl.append(currentToken.image);

		// TODO: This shouldn't really trim(). It should remove all the whitespace off the
		//		front unless there are newlines in that whitespace. In that case it should
		//		leave any whitespace after the last newline in that whitespace. This keeps
		//		the first line indented correctly. (The whitespace on the end can be removed
		//		or not.)

      _declarationString = newDecl.toString().trim();
    }

    return _declarationString;

  }

  	public String getCodeWithoutSpecials()
	{
      StringBuffer newDecl = new StringBuffer();
      Token currentToken =  baseNode.first_token;
      while  (true) {
		newDecl.append("<");
        newDecl.append( currentToken.image + ":" + currentToken.kind);
		newDecl.append(">");
		if (currentToken == baseNode.last_token) {
			break;
		}
        currentToken = currentToken.next;
      }
      return newDecl.toString();
	}

  /**
  * Recursively grab all the special tokens (whitespace and comments) which precede
  * a given token
  * @param currentToken the token to look up
  * @param showJavaDoc whether or not to include JavaDoc special tokens
  */
  private String getSpecialTokenDeclatations(Token currentToken, boolean showJavaDoc)
  {
    String currentImage;
    Token currentSpecialToken;
    if (currentToken != null) {
      currentSpecialToken = currentToken.specialToken;
      currentImage =  currentToken.image;
      if (!showJavaDoc) {
//System.out.println("CommentableCode.getSpecialTokenDeclatations: <" + currentImage + ">");
	  	if (currentImage.startsWith("/**")) {		// Ignore javadoc

        	currentImage = "";
      	}
//        else if (currentImage.trim().equals("")) {
//     		currentImage = "";
//      	}
	  }
       // recursively get all special tokens
//      return  currentImage + getSpecialTokenDeclatations(currentSpecialToken, showJavaDoc);
      return  getSpecialTokenDeclatations(currentSpecialToken, showJavaDoc) + currentImage;
    } else {
       return "";
    }
  }

	/**
	 * Check the preferences and tell if this code item allows the single-line format for
	 *		javadoc comments. Normally, this method is overridden for the classes on the
	 *		end of the class hierarchy tree branches.
	 *
	 * @since 07/16/2001 1:26:15 PM
	 * @return true if this type of code item allows single-line comments.
	 */
	public boolean usesSingleLineComment()
	{
//		if ((theCode instanceof tinyplanet.docwiz.Field && config.getFieldUsesSingleLineComment()) ||
		return false;
	}

	/**
	 * Return a constant indicating how private code items will appear in the list of code
	 *		items.
	 *
	 * @since 07/16/2001 1:38:32 PM
	 * @return One of the constants defined in CommentableCode.
	 *	<ul>
	 *		<li>PRIVATE_SHOW
	 *		<li>PRIVATE_GREY
	 *		<li>PRIVATE_NO_SHOW
	 *	</ul>
	 */
	public int getAppearanceInList()
	{
//		ConfigurationService config = ConfigurationService.getConfigurationService();
//	  	return config.getFieldAppearanceInList();
		return PRIVATE_SHOW;
	}

	/**
	 * Tell if this code item is shown (fully) when it is private.
	 *
	 * @since 07/16/2001 1:46:26 PM
	 * @return true if this code item is to be shown when private.
	 * @see #isPrivateNoShow
	 * @see #isPrivateGrey
	 */
	public boolean isPrivateShow()
	{
		return getAppearanceInList() == PRIVATE_SHOW;
	}

	/**
	 * Tell if this code item is shown in grey when it is private.
	 *
	 * @since 07/16/2001 1:46:26 PM
	 * @return true if this code item is to be greyed out (but active) when private.
	 * @see #isPrivateNoShow
	 * @see #isPrivateShow
	 */
	public boolean isPrivateGrey()
	{
		return getAppearanceInList() == PRIVATE_GREY;
	}

	/**
	 * Tell if this code item is NOT shown when it is private.
	 *
	 * @since 07/16/2001 1:46:26 PM
	 * @return true if this code item is to be NOT shown when private.
	 * @see #isPrivateShow
	 * @see #isPrivateGrey
	 */
	public boolean isPrivateNoShow()
	{
		return getAppearanceInList() == PRIVATE_NO_SHOW;
	}

	/**
	 * Get an indication of how completely commented this class, field, etc is.
	 *
	 * @since 07/16/2001 2:16:01 PM
	 * @param isBodyPresent true if there is a comment (body) for this code item.
	 * @return 	0 means not commented. 4 means completely done.
	 */
	public int getCommentLevel(boolean isBodyPresent)
	{
		if (isBodyPresent) {
			return 4;
		}
		else  {
			return 0;
		}
	}

// Get/Set methods:

	/**
	 * Get the scope string (public, private, protected, etc.) if the preferences allow
	 *		showing it.
	 *
	 * @return The scope string with a space on the end or the empty string depending on
	 *		the setting in the preferences.
	 */
	public String getConfiguredScope()
	{
	    ConfigurationService config = ConfigurationService.getConfigurationService();
		if (config.getShowScopeInList()) {
			return getScope() + " ";
		}
		else  {
			return "";
		}
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link scope }.
	 * @since  07/10/2001 6:20:36 PM
	 */
	public String getScope()
	{
		return this.scope;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link scope }.
	 * @since  07/10/2001 6:20:36 PM
	 */
	public void setScope(String val)
	{
		this.scope = val;
	}

}