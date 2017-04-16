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
//	7/16/2001 lmm	- add methods to query preferences that relate to this class

package tinyplanet.docwiz;

import java.util.Vector;
import tinyplanet.javaparser.*;

/** A representation of a Java class, including all the elevant
 * information needed for producing
 * JavaDoc comments.
 *
 * @author Simon Arthur
 */
public class Class extends ElementContainer {

/** Create a new Class representation based upon the SimpleNode produced
 * by the parser
 *
 * @param n The SimpleNode for the class from the javaparser
 */
public Class (SimpleNode n) {
 super(n);
}

	// No javadoc needed because of parent class.
	public boolean usesSingleLineComment()
	{
		ConfigurationService config = ConfigurationService.getConfigurationService();
		return config.getClassUsesSingleLineComment();
	}

	// No javadoc needed because of parent class.
	public int getAppearanceInList()
	{
		ConfigurationService config = ConfigurationService.getConfigurationService();
	  	return config.getClassAppearanceInList();
	}

/** Produce a string which gives some information about the class.
 * This is used in the user interface so the user can see the name
 * and line number of the class.
 */
public String toString() {
  return getConfiguredScope() + "Class: " + this.name + ", line " + this.line_number;
}


}