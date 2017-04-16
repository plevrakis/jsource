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
//	4/30/2001 Lee Medor - Add method to get a list of all the comments to put
//			in the drop down list.
//			Reformat javadocs using Docwiz and the code using astyle.

package tinyplanet.docwiz;

import java.util.*;

/**
 * A set of DocWiz comments with a common "@" tag
 *
 * @author Simon Arthur
 */
public interface TagSet
{
	/**
	 * Add a new tag line to the set of tags
	 *
	 * @param paramLine The complete tag line, without the "@"
	 */
	public void addValue(String paramLine);

	/**
	 * Remove all the tags from the TagSet
	 */
	public void clear();

	/**
	 * Find a particular tag within the TagSet
	 *
	 * @param location The position of the tag within the TagSet
	 * @return The tag at the given position
	 */
	public String tagAt(int location);

	/**
	 * Change a particular tag to have a new value
	 *
	 * @param location The location of the tag to change
	 * @param value The value to set the tag to
	 */
	public void setTagAt(int location, String value);

	/**
	 * Remove a particular tag from the set
	 *
	 * @param location The location of the tag to change
	 * @param value The value to set the tag to
	 */
	public void removeTagAt(int location);

	/**
	 * Get the number of tags in this TagSet
	 *
	 * @return The number of tags in this TagSet
	 */
	public int size();

	/**
	 * Convert the TagSet to a JavaDoc-style set of comments
	 *
	 * @param tagType The type of tag that this will display as
	 * @return The tag set, suitable for embedding within a JavaDoc comment
	 */
	public String toJavaDocString(String tagType);

	/**
	 * Convert the TagSet to a list (Vector) consisting of the comments that can be changed.
	 *
	 * @return A list of the comment strings.
	 */
	public Vector getTagStringList();

	/**
	 * Return the code to which the TagSet applies.
	 */
	public CommentableCode getCodeToTag();

	/**
	 * set the name of the  @ tag
	 *
	 * @param tagName
	 */
	public void setTagName(String tagName);

	/**
	 * Get the name of the  @ tag
	 */
	public String getTagName();

}
