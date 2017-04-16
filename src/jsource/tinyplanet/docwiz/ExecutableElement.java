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
//	15 Jun 2001 Lee Meador	Change to initialize all the vectors and the hash table before
//							filling in the parameters and exceptions. The bug this fixed was
//							the one where the parameters were doubled when an exception was
//							present.

package tinyplanet.docwiz;

import java.util.*;
import tinyplanet.javaparser.*;
 /**
  * This class represents a Java entity which contains executable code:
  * generally, either a method or a constructor. It contains various accessor methods
  * to find the parameters and exceptions.
  *
  * @author Simon Arthur
  */
public class ExecutableElement extends ClassMember
{
 /** This Hashtable stores the parameters of the ExecutableElement as keys and
  * the parameter types as values.
  */
private Hashtable parameter = null;
 /** A vector containing the names of the parameters, in the order in which they
  * were declared.
  *
  */
private Vector paramVector = null;
 /** A vector containing the names of the exceptions for the ExecutableElement
  *
  */
private Vector exception = null;

 /** Create a new ExecutableElement based upon the SimpleNode for the
  * element, as produced by the parser.
  *
  * @param n The node for the ExecutableElement
  */
public ExecutableElement(SimpleNode n) {
  super(n);
  createParamsAndExceptions(n);
}

 /** Return the parameter's type as a String based upon the given parameter
  * name.
  *
  * @param paramName The name of the parameter to look up
  * @return The given parameter's type
  */
public String getTypeOfParameter(String paramName) {
  String returnType = (String)parameter.get(paramName);
  return returnType;
}

 /** Find the names of the exceptions for the current ExecutableElement
  *
  * @return A Vector of Strings for the ExecutableElement
  */
public Vector getExceptionNames() {
  if  (exception == null) {
    createParamsAndExceptions(this.baseNode);
  }
  return exception;
}

 /** Get the names of the parameters for the current ExecutableElement
  *
  * @return A Vector of Strings containing the various parameters to the ExecutableElement
  */
public Vector getParameterNames() {
  if (paramVector == null) {
    createParamsAndExceptions(this.baseNode);
  }
  return paramVector;
}

 /** Initialize the vectors and hash table and then determine this
  * object's parameters and exceptions from the SimpleNode
  * for the ExecutableElement. Changes both the parameter and exception
  * fields.
  *
  * @param n The SimpleNode for the ExcecutableElement
  */
private void createParamsAndExceptions(SimpleNode n)
{
//	System.out.println("ExecutableElement.createParamsAndExceptions: Entry");
	parameter = new Hashtable(10);
	paramVector = new Vector(10);
	exception = new Vector(3);

	findParamsAndExceptions(n);

//	System.out.println("ExecutableElement.createParamsAndExceptions: Exit");
	return;
}

 /** Determine this object's parameters and exceptions from the SimpleNode
  * for the ExecutableElement. Changes both the parameter and exception
  * fields.
  *
  * @param n The SimpleNode for the ExcecutableElement
  */
private void findParamsAndExceptions(SimpleNode n)
{
    SimpleNode currentChild;
    String currentParamType, currentParamName;
    if (n instanceof ASTNameList) {
       // get the exceptions
       findExceptionName(n);
    }
    else if (n instanceof ASTFormalParameters) {
       findParamName(n);
    }
    else if (n.jjtGetNumChildren() != 0) {
      for (int i = 0; i < n.jjtGetNumChildren(); ++i) {
        currentChild = (SimpleNode)(n.jjtGetChild(i));
        if (currentChild != null  && !(currentChild instanceof ASTBlock) ) {
          findParamsAndExceptions(currentChild);
        }
      }
    }
	return;
}

 /** Set up the parameter field with the names of the parameters for this
  * ExecutableElement
  *
  * @param n
  */
private void findParamName(SimpleNode n) {
    SimpleNode currentChild;
    String currentParamType, currentParamName;
    if (n instanceof ASTFormalParameter) {
     currentParamName = ""; currentParamType = "";
     // Retrieve the name and type of the parameter
     for (int j = 0 ; j < n.jjtGetNumChildren(); ++j) {
       currentChild = (SimpleNode)(n.jjtGetChild(j));
       if (currentChild instanceof ASTType) {
         // seek out the entire type, including package
         int endCol = currentChild.last_token.endColumn;
         int endLine = currentChild.last_token.endLine;
         Token currentToken =  currentChild.first_token;
         currentParamType = "";
         do {
           currentParamType += currentToken.image;
           currentToken = currentToken.next;
         } while (currentToken != null &&
                (
                (currentToken.endLine < endLine) ||
                (currentToken.endLine == endLine && currentToken.endColumn <= endCol)
                )
                );
       }
       if (currentChild instanceof ASTVariableDeclaratorId) {
         currentParamName = currentChild.first_token.image;
         paramVector.addElement(currentParamName);
         parameter.put(currentParamName,currentParamType);
       }
     }
    }
	else {
      if (n.jjtGetNumChildren() != 0) {
        for (int i = 0; i < n.jjtGetNumChildren(); ++i) {
          currentChild = (SimpleNode)(n.jjtGetChild(i));
          if (currentChild != null) {
            findParamName(currentChild);
          }
        }
      }
    }
}

 /** Find the list of exceptions for the current ExecutableElement and
  * set up the parameter Vector
  *
  * @param n The SimpleNode for the parameter list
  */
private void findExceptionName(SimpleNode n) {
  SimpleNode currentChild;
  String currentExceptionName;
  currentExceptionName = "";
   // Retrieve  type of the exception
   for (int j = 0 ; j < n.jjtGetNumChildren(); ++j) {
     currentChild = (SimpleNode)(n.jjtGetChild(j));
     if (currentChild instanceof ASTName) {
       // seek out the entire type, including package
       int endCol = currentChild.last_token.endColumn;
       int endLine = currentChild.last_token.endLine;
       Token currentToken =  currentChild.first_token;
       currentExceptionName = "";
       do {
         currentExceptionName += currentToken.image;
         currentToken = currentToken.next;
       } while (currentToken != null &&
                (
                (currentToken.endLine < endLine) ||
                (currentToken.endLine == endLine && currentToken.endColumn <= endCol)
                )
                );
       exception.addElement(currentExceptionName);
     }
   }
   return;
}

	// No javadoc needed because of parent class.
	public int getCommentLevel(boolean isBodyPresent)
	{
//		System.out.println("ExecutableElement.getCommentLevel: Entry");
		// check for all param and exception tags filled
		CodeComment comment = getCodeComment();

		Set params = new HashSet(getParameterNames());
		TwoPartTagModel ts = (TwoPartTagModel)comment.getTagEntry("param", false);
		if (ts != null) {
			for (int i = 0; (i < ts.size()) ; ++i) {
				if (ts.tagValueAt(i).trim().length() > 0) {
					params.remove(ts.tagNameAt(i).trim());
				}
			}
		}
		if (params.size() == 0) {
			params.addAll(getExceptionNames());
			ts = (TwoPartTagModel)comment.getTagEntry("exception", false);
			if (ts != null) {
				for (int i = 0; (i < ts.size()) ; ++i) {
					if (ts.tagValueAt(i).trim().length() > 0) {
						params.remove(ts.tagNameAt(i).trim());
					}
				}
			}
		}
		boolean paramsAndExceptionsPresent = (params.size() == 0);

		int retVal = 0;
		if (isBodyPresent && paramsAndExceptionsPresent) {
			retVal = 4;
		}
		else if (isBodyPresent || paramsAndExceptionsPresent) {
			retVal = 2;
		}
//		System.out.println("ExecutableElement.getCommentLevel: exit " + retVal);
		return retVal;
	}

	/**
	 * Form a string suitable for debugging
	 *
	 * @since 06/15/2001 8:03:48 AM
	 * @return A string suitable for debugging
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer(this.getClass().getName());
		buf.append("@");
		buf.append(this.hashCode());
		// Hashtable parameter;
		buf.append(", parameters: ");
		for (Iterator iter = paramVector.iterator() ; (iter.hasNext()) ; ) {
			buf.append(", ");
			buf.append(iter.next());
		}
		buf.append(", exceptions: ");
		for (Iterator iter = exception.iterator() ; (iter.hasNext()) ; ) {
			buf.append(", ");
			buf.append(iter.next());
		}

		return buf.toString();
	}

}