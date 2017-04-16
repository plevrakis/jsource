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
import java.util.*;

 /** A representation of a Constructor entity within a Java source file
  *
  * @author Simon Arthur
  */
public class Constructor extends ExecutableElement {

 /** Create a new Constructor based upon the given SimpleNode
  *
  * @param n The base node produced by the parser for the Constructor
  */
  public Constructor(SimpleNode n) {
    super(n);
    SimpleNode currentNode;
    name = null;
    for(int i = 0; i < n.jjtGetNumChildren(); i ++) {
      currentNode =  (SimpleNode)(n.jjtGetChild(i)) ;
      if (currentNode instanceof ASTConstructorDeclaration) {
        // We've found the name
        this.name = currentNode.first_token.image;
        break;
      }
    }
    //findParams(n);
  }

	// No javadoc needed because of parent class.
	public boolean usesSingleLineComment()
	{
		ConfigurationService config = ConfigurationService.getConfigurationService();
		return config.getConstructorUsesSingleLineComment();
	}

	// No javadoc needed because of parent class.
	public int getAppearanceInList()
	{
		ConfigurationService config = ConfigurationService.getConfigurationService();
	  	return config.getConstructorAppearanceInList();
	}

 /** Provide some summary information about the constructor, including
  * its line number
  *
  * @return Summary information about the constructor
  */
public String toString() {
  return getConfiguredScope() + "Constructor: line " + this.line_number;
}

}