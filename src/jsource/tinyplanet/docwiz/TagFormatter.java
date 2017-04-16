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

import java.util.StringTokenizer;

/** Abstract class for outputting a tagged paragraph (TagSet object)as a String.
 *
 * @see TagSet
 * @author Simon Arthur
 */
public abstract class TagFormatter {

  /** The TagSet to format
   */
  private tinyplanet.docwiz.TagSet tagSet;
  /** The name (@ tag) of the tagged paragraph.
   */
  private String tagName;
  /** Where to wrap when printing tags
   */
  static final int MAXLINELENGTH = 72;

  /** Build a new TagFormatter
   */
  public TagFormatter() {
  }

  /** Build a new TagFormatter for the given TagSet and tag name.
   *
   * @param tagName The name associated with the tagged paragraph
   * @param t The TagSet to format
   */
  public TagFormatter(TagSet t, String tagName) {
    setTagSet(t);
    setTagName(tagName);
  }

  /** Find the TagSet to be formatted.
   *
   * @return the TagSet to be formatted
   */
  public tinyplanet.docwiz.TagSet getTagSet() {
    return tagSet;
  }

  /** Change the TagSet to format
   *
   * @param newTagSet The TagSet to format
   */
  public void setTagSet(tinyplanet.docwiz.TagSet newTagSet) {
    tagSet = newTagSet;
  }

  /**    *
   * @param newTagName
   */
  public void setTagName(String newTagName) {
    tagName = newTagName;
  }

  /**    */
  public String getTagName() {
    return tagName;
  }

   /** Format the tagged paragraphs associated with this TagFormatter
    *
    * @return The TagSet for this TagFormatter, formatted as a String
    */
   public abstract String getJavaDocString();
}