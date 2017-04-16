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

// History
//	3/22/2001 Lee Meador (LeeMeador@usa.net) Use super class in constructor.

package tinyplanet.docwiz;

/**
* This class represents a tag that has all the attributes of a standard tagged
* paragraph, except that it will print as a JavaDoc string, even if it has
* no content.
*/

public class StandaloneTagModel extends DefaultTagModel {
  public StandaloneTagModel(CommentableCode c) {
	super(c);
//    setCodeToTag(c);
  }

 /** Convert the tag to a JavaDoc string. The DefaultTagModel doesn't know
  * which type of @ tag it's tracking, so that needs to be passed in as
  * a String
  *
  * @param tagType A String representing the name of the "@" tag this DefaultTagModel represents
  * @return The current tag, as a JavaDoc string
  */
 public String toJavaDocString(String tagType) {
    TagFormatter tf = new ExistTagFormatter(this, tagType);
    return tf.getJavaDocString();
 }

}