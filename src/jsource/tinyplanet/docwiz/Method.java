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
//import tinyplanet.docwiz.WritableCode;
import tinyplanet.javaparser.*;
import java.util.*;


public class Method extends ExecutableElement
{

String returnType;

public Method (SimpleNode n) {
  super(n);
  //n.dump("+");
  SimpleNode currentNode;
  name = null;  returnType = null;
  for(int i = 0; i < n.jjtGetNumChildren(); i ++) {
    currentNode =  (SimpleNode)(n.jjtGetChild(i)) ;
    if (currentNode instanceof ASTMethodDeclarator) {
      // We've found the name
      this.name = currentNode.first_token.image;
    }
    if (currentNode instanceof ASTResultType) {
      // We've found the return type
      this.returnType = currentNode.first_token.image;
    }
    if ((name != null) && (returnType != null)) {
      break;
      }
  }
  //findParams(n);
}

	// No javadoc needed because of parent class.
	public int getCommentLevel(boolean isBodyPresent)
	{
//		System.out.println("Method.getCommentLevel: Entry");
		int retVal = super.getCommentLevel(isBodyPresent);
//		System.out.println("Method.getCommentLevel: super returns " + retVal);
		if (retVal == 4) {
			// check for return (if not void)
//			System.out.println("Method.getCommentLevel: type is " + getType());
			if (!("void".equals(getType()))) {
				retVal = 2;
				// check for return tag filled in.
				CodeComment comment = getCodeComment();
				TagSet ts = comment.getTagEntry("return", false);
//				System.out.println("Method.getCommentLevel: return tagset is " + ts);
				if (ts != null) {
					String tag = ts.tagAt(0);
//					System.out.println("Method.getCommentLevel: tag 0 is <" + tag + ">");
					if (tag.trim().length() > 0) {
						retVal = 4;
					}
				}
			}
		}
//		System.out.println("Method.getCommentLevel: exit " + retVal);
		return retVal;
	}

	// No javadoc needed because of parent class.
	public boolean usesSingleLineComment()
	{
		ConfigurationService config = ConfigurationService.getConfigurationService();
		return config.getMethodUsesSingleLineComment();
	}

	// No javadoc needed because of parent class.
	public int getAppearanceInList()
	{
		ConfigurationService config = ConfigurationService.getConfigurationService();
	  	return config.getMethodAppearanceInList();
	}

public String toString() {
  return getConfiguredScope() + "Method: " + this.name + ", line " + this.line_number;
}


public String getType() {
  return returnType;
}



}