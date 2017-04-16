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
//	4/30/2001 Lee Meador - Add getTagStringList() to fill in drop down list
//				of comments.
//				- Add code to force vector long enough in case it is too
//				short to take a new value.
//				- Reformat javadocs with DocWiz and code with astyle.
//	5/3/2001 Lee Meador - add removeElementAt method here so that subclass doesn't
//				need to access the vector v.
//	6/20/2001 lmm	- add removeTagAt() method to work with unknown tags.

package tinyplanet.docwiz;

import tinyplanet.docwiz.TagSet;
import java.util.*;
import javax.swing.text.*;

/**
 * The default container for most JavaDoc tags. "@exception" and "@param" tags
 * are treated specially, so they're not included here. Most other kinds of
 * single string based tags, such as "@author" and "@see" end up here.
 *
 * @author Simon Arthur
 */
public class DefaultTagModel extends javax.swing.text.PlainDocument
			implements TagSet
{

	/**
	 * A container for all the lines which contain the current tag.
	 */
	Vector v = new Vector(3);

	/**
	 * Convert the tag to a JavaDoc string. The DefaultTagModel doesn't know
	 * which type of @ tag it's tracking, so that needs to be passed in as
	 * a String
	 *
	 * @param tagType A String representing the name of the "@" tag this
	 *     DefaultTagModel represents
	 * @return The current tag, as a JavaDoc string
	 */
	public String toJavaDocString(String tagType)
	{
		TagFormatter tf = new ValuedTagFormatter(this, tagType);
		return tf.getJavaDocString();
	}

	/**
	 * Change a particular vaule within this list. If the value doesn't
	 * yet exist, it will be created.
	 *
	 * @param location The position in the list of the tag
	 * @param value The new value for the tag entry
	 */
	public void setTagAt(int location, String value)
	{
		while (location >= v.size()) {
			v.add("");
		}
		v.setElementAt(value, location);
	}

	/**
	 * Remove an entry from this list.
	 *
	 * @param location The position in the list of the tag to be removed
	 */
	public void removeTagAt(int location)
	{
		if  (location < v.size()) {
			v.removeElementAt(location);
		}
	}


	/**
	 * Find the tag at a particular location
	 *
	 * @param i The location to find the tag
	 * @return The tag at the given location
	 */
	public String tagAt(int i)
	{
		Object o = null;
		if (i <= v.size() - 1) {
			o = v.elementAt(i);
		}
		else {
			o = null;
		}
		String rtn = (o == null) ? "" : o.toString();
		return rtn ;
	}

	/**
	 * Create a new DefaultTagModel
	 *
	 */
	public DefaultTagModel()
	{}

	/**
	 * Create a new DefaultTagModel and associate it with a given
	 * CommentableCode object
	 *
	 * @param e The CommentableCode to tag
	 */
	public DefaultTagModel(CommentableCode e)
	{
		setCodeToTag(e);
	}

	/**
	 * Remove a value from the list of tags.
	 *
	 * @param location The index of the tag to remove.
	 */
	public void removeElementAt(int location)
	{
		v.removeElementAt(location);
	}

	/**
	 * Add a new value to the list of tags
	 *
	 * @param paramLine The value to add
	 */
	public void addValue(String paramLine)
	{
		v.addElement(paramLine);
	}

	/**
	 * Convert the TagSet to a list (Vector) consisting of the comments that can be changed.
	 *
	 * @return A list of the comment strings.
	 */
	public Vector getTagStringList()
	{
		return v;
	}

	/**
	 * Remove all tags from this DefaultTagModel
	 *
	 */
	public void clear()
	{
		v.setSize(0);
	}

	/**
	 * Determine the number of entries in this DefaultTagModel
	 *
	 * @return The number of entries in this DefaultTagModel
	 */
	public int size()
	{
		return v.size();
	}

	/**
	 * Change which CommentableCode Java source code entity
	 * this DefaultTagModel is associated with
	 *
	 * @param newCodeToTag The code entity to associate to the DefaultTagModel
	 */
	public void setCodeToTag(CommentableCode newCodeToTag)
	{
		codeToTag = newCodeToTag;
	}

	/**
	 * Find out which CommentableCode Java source element this object is
	 * associated with
	 *
	 * @return The CommentableCode for this DefaultTagModel
	 */
	public CommentableCode getCodeToTag()
	{
		return codeToTag;
	}
	/**
	 * The CommentableCode Java source element associated with this DefaultTagModel
	 */
	private CommentableCode codeToTag;

	private String tagName;

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public String getTagName()
	{
		return tagName;
	}
}
