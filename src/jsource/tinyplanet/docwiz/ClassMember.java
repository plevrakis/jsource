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


package tinyplanet.docwiz;
import tinyplanet.javaparser.*;
import java.util.*;

/** An abstraction of any of the several things which can appear within a class and can
 * be given JavaDoc comments: methods, constructors or fields.
 *
 * @author Simon Arthur
 */
public class ClassMember extends CommentableCode {
/** The name of the class which contains the ClassMember
 */
String parentName;

/** Create a new ClassMember object based upon the given SimpleNode.
 *
 * @param n The SimpleNode for the ClassMember, produced by the Java parser
 */
public ClassMember (SimpleNode n) {
  super(n);
  parentName = null;
  findParentName(n);
}

/** Get the name of the class which contains the ClassMember
 *
 * @return The name of the class which contains the ClassMember
 */
public String getParentName() {
  return parentName;
}

/** Set up the parentName property by looking through the parse tree for the
 * given SimpleNode
 *
 * @param n The SimpleNode for the ClassMember
 */
void findParentName(SimpleNode n) {
  if (n instanceof ASTClassDeclaration || n instanceof  ASTInterfaceDeclaration) {
     tinyplanet.docwiz.Class c;
     c = new tinyplanet.docwiz.Class(n);
     parentName = c.getName();
     return;
  }
  if (parentName == null) {
    findParentName((SimpleNode)n.jjtGetParent());
  }

}





}