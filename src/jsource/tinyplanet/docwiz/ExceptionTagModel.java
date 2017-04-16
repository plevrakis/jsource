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

// History
//	3/22/2001 Lee Meador (LeeMeador@usa.net) 1) Handle listeners in super class. 2) Use
//			superclass in constructor 3) Leave off 'implements' that is now in superclass.

package tinyplanet.docwiz;

import java.util.*;
import tinyplanet.docwiz.TagSet;
import javax.swing.table.*;
import javax.swing.event.*;

 /** A TagSet for tracking information about a particular method's exceptions,
  * including the type of exception and the description of when the exception
  * is thrown.
  *
  * @author Simon Arthur
  */
public class ExceptionTagModel extends TwoPartTagModel
{

  /** This is either 'throws' or 'exception' depending on what they put in.
  */
  private String tagName;

 /** Create a new set of exception tags for the given ExecutableElement
  * (i.e. Class or Method).
  *
  * @param e The ExecutableElement which contains exceptions
  */
  public ExceptionTagModel(ExecutableElement e) {
//	    setCodeToTag(e);
	super(e);
  }

  public ExceptionTagModel() {
  }

 /**
  * Return the type of "@" tag, should be the value "exception" (or "throws")
  *
  * @return The value "exception"/"throws"
  */
  public String getTagName () {
    return this.tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

 /** Gives a vector of the various exception names associated with this
  * ExceptionTagModel
  *
  * @return The names of the exceptions for this ExceptionTagModel
  */
  public  Vector getNameVector() {
    ExecutableElement m = (ExecutableElement)codeToTag;
    Vector actualExceptions = m.getExceptionNames();
    return actualExceptions;
  }

// The TableModel interface methods begin here **********************************************

 /** Return the name of the requested column. Gives either "Throws" if the
  * column containing the exception names is requested or "When" if the
  * exception description column is requested.
  *
  * @param columnIndex The requested column
  * @return The name of the requested column, "Throws" or "When"
  */
  public String getColumnName(int columnIndex) {
    String returnValue = "";
    switch (columnIndex) {
      case NAMECOLUMN: returnValue = "Throws";
        break;
      case DESCRIPTIONCOLUMN: returnValue = "When";
        break;
    }
    return returnValue;
  }

//    /** The listeners for the tableModel events
//   */
//  private java.util.Vector tableModelListeners;
//    /**    */
//  public void addTableModelListener(TableModelListener l) {
//    java.util.Vector v = tableModelListeners == null ? new Vector(2) : (Vector) tableModelListeners.clone();
//    if (!v.contains(l)) {
//      v.addElement(l);
//      tableModelListeners = v;
//    }
//  }
//
//  /**    */
//  public void removeTableModelListener(TableModelListener l) {
//     if (tableModelListeners != null && tableModelListeners.contains(l)) {
//      java.util.Vector v = (java.util.Vector) tableModelListeners.clone();
//      v.removeElement(l);
//      tableModelListeners = v;
//    }
//  }

// The TableModel interface methods ends here **********************************************

}