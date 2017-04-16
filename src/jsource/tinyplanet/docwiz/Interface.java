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

import tinyplanet.javaparser.*;

 /** Representation of a Java interface
  *
  * @author Simon Arthur
  */
public class Interface extends ElementContainer {

 /** Create a new Interface, based upon the SimpleNode for the Interface from the Java
  * parser
  *
  * @param n The SimpleNode for the Interface
  */
  public Interface(SimpleNode n) {
    super(n);
  }

	// No javadoc needed because of parent class.
	public boolean usesSingleLineComment()
	{
		ConfigurationService config = ConfigurationService.getConfigurationService();
		return config.getInterfaceUsesSingleLineComment();
	}

	// No javadoc needed because of parent class.
	public int getAppearanceInList()
	{
		ConfigurationService config = ConfigurationService.getConfigurationService();
	  	return config.getInterfaceAppearanceInList();
	}

 /**
  * Provide some information about the Interface, including the line on which it is declared.
  *
  * @return Summary information for the interface
  */
public String toString() {
  return getConfiguredScope() + "Interface: " + this.name + ", line " + this.line_number;
}

}