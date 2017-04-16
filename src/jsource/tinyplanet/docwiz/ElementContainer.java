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
//	7/17/2001 lmm	- add a method here to figure out how much code items of
//					this class are completed being commented.

package tinyplanet.docwiz;

import tinyplanet.javaparser.*;

 /** An ElementContainer is a Java source code entity which can contain other
  * Java entities. An example is the Class, which can contain methods and fields.
  *
  * @author Simon Arthur
  */
public class ElementContainer extends CommentableCode {

 /** Create a new ElementContainer based upon the SimpleNode produced
  * by the Java parser and given as  a parameter.
  *
  * @param n The SimpleNode to build the ElementContainer from
  */
public ElementContainer (SimpleNode n) {
 super(n);
 name = null;
 findName(n);
}

 /** Search through the given SimpleNode to find the name of the
  * ElementContainer
  * and set the name field.
  *
  * @param n The SimpleNode of the ElementContainer
  */
  void findName(SimpleNode n) {
    SimpleNode currentChild;
    String currentChildName;
    if (n instanceof ASTUnmodifiedClassDeclaration || n instanceof ASTUnmodifiedInterfaceDeclaration) {
     name =  n.first_token.next.image;
     return;
    } else {
      if (n.jjtGetNumChildren() != 0) {
        for (int i = 0; i < n.jjtGetNumChildren(); ++i) {
          currentChild = (SimpleNode)(n.jjtGetChild(i));
          if (currentChild != null) {
            findName(currentChild);
            if (name != null) {
              // we've found the name
              return;
            }
          }
        }
      }
    }
}

 /**
  * Return the name of the ElementContainer
  *
  * @return The ElementContainer's name
  */
public String  getUnqualifiedName(){
  // TODO getName and getUnqualifiedName should really be different
  return getName();
}

	// No javadoc needed because of parent class.
	public int getCommentLevel(boolean isBodyPresent)
	{
		// check for any things required beyond the body.

		// NOTE: There might should be some preferences allowing to add the requirement
		//		for an author, since or version tag.

		if (isBodyPresent) {
			return 4;
		}
		else  {
			return 0;
		}
	}

}