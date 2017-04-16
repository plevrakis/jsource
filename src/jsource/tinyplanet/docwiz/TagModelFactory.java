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

//Title:        DocWiz
//Version:
//Copyright:    Copyright (c) 1997
//Author:       Simon Arthur
//Company:
//Description:  Easy Javadoc documentation

// History:
//	7/12/2001 lmm	- when creating a tag set, catch a class cast exception and return a null
//					to show the tag set could not be created for this code item.
//	7/13/2001 lmm	- when creating a tag set from a line, trim the tag before creating TagSet.

package tinyplanet.docwiz;

import java.util.*;

 /** This class contains static methods which create TagSets based upon the type of tag passed to
  * them
  *
  * @author Simon Arthur
  */
public class TagModelFactory {

 /** Make a new TagSet based upon the given "@" tag
  *
  * @param c The CommentableCode object which will be associated with the tag
  * @param tag The @ tag line
  * @return The appropriate tag set for the tag which is passed in
  */
  public static TagSet tagSetCreateFromTag(CommentableCode c, String tag) {
    // "throws" -> "exception" is fixed somewhere else
    TagSet returnTagSet = null;
	try {
	     if (tag.equals("param")) {
	       returnTagSet = new ParamTagModel((ExecutableElement)c);
	     } /* (tag.equals("param")*/ else {
	       if (tag.equals("exception")) {
	         returnTagSet = new ExceptionTagModel((ExecutableElement)c);
	       } /*tag.equals("exception"*/ else {
	         if (tag.equals("see")  || tag.equals("author"))  {
	           returnTagSet = new OnePartTagModel(c);
	         } else {
	           if (tag.equals("deprecated") ) {
	             returnTagSet = new StandaloneTagModel(c);
	           } else {
	             returnTagSet = new DefaultTagModel(c);
	           }
	         }
	       } // ! ( tag.equals("exception))
	     } // !(tag.equals("param")*/
     returnTagSet.setTagName(tag);
	}
	catch (ClassCastException e)  {
	}
     return returnTagSet;
  }

 /** Create a new TagSet based upon the complete "@" tag line passed in, without the "@"
  *
  * @param c The CommentableCode which will be associated with the tag
  * @param tagline The complete line for the tag, without the "@"
  * @return The appropriate TagSet for the tagline
  */
  public static TagSet tagSetCreateFromLine(CommentableCode c, String tagline) {
    StringTokenizer  st = new StringTokenizer (tagline, " ");
    String value = "";
    String tag = "";
    try {
      tag = st.nextToken();
      value = st.nextToken("\u0000");
    } catch (NoSuchElementException nsee) {
      // just leave tag and value at defaults.
    }

    value = value.trim();
    return tagSetCreateFromTag(c, tag.trim());
  }

}