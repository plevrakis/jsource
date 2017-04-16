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
//	8/2/2001 lmm	- add an else to make it a tiny bit clearer
//	8/9/2001 lmm	- remove WritableCode interface since parent class already implements it.

package tinyplanet.docwiz;

import tinyplanet.javaparser.*;

 /** This class represents a field within a class or interface
  *
  * @author Simon Arthur
  */
public class Field extends ClassMember {
 /** The field's type
  *
  */
String fieldType;

 /** Create a new field object based upon the given SimpleNode from the Java parser
  *
  * @param n The SimpleNode for the field
  */
public Field (SimpleNode n) {
  super(n);
  name = null;
  fieldType = null;
  findNameAndType(n);
}

 /** Get the field's declaration type
  *
  * @return The field's type
  */
public String getType() {
  return fieldType;
}

 /** Extract the name and fieldType fields from the given SimpleNode
  *
  * @param n The node for the field
  */
void findNameAndType(SimpleNode n) {
  SimpleNode currentChild;
  String currentChildName;
  if (n instanceof ASTVariableDeclarator) {
   name =  n.first_token.image;
   return;
  }
  else if (n instanceof ASTType) {
       // seek out the entire type, including package
       int endCol = n.last_token.endColumn;
       Token currentToken =  n.first_token;
       fieldType = "";
       do {
         fieldType += currentToken.image;
         currentToken = currentToken.next;
       } while (currentToken.endColumn <= endCol);
   return;
  }
  else {
    if (n.jjtGetNumChildren() != 0) {
      for (int i = 0; i < n.jjtGetNumChildren(); ++i) {
        currentChild = (SimpleNode)(n.jjtGetChild(i));
        if (currentChild != null) {
          if ((name == null) || (fieldType == null)) {
            findNameAndType(currentChild);
          }
        }
      }
    }
  }
}

	// No javadoc needed because of parent class.
	public boolean usesSingleLineComment()
	{
		ConfigurationService config = ConfigurationService.getConfigurationService();
		return config.getFieldUsesSingleLineComment();
	}

	// No javadoc needed because of parent class.
	public int getAppearanceInList()
	{
		ConfigurationService config = ConfigurationService.getConfigurationService();
	  	return config.getFieldAppearanceInList();
	}

 /** Give some information about the field, including its line number
  *
  * @return Summary information about the field, including its line number
  */
public String toString() {
  return getConfiguredScope() + "Field: " + this.name + ", line " + this.line_number;
}


}