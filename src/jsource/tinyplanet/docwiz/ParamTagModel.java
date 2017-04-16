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

//Title:        DocWiz
//Version:
//Copyright:    Copyright (c) 1997
//Author:       Simon Arthur
//Company:
//Description:  Easy Javadoc documentation

// History
//	3/22/2001 Lee Meador (LeeMeador@usa.net) 1) Handle listeners in super class.
//	6/15/2001 lmm - remove the 'implements' stuff since it is in super class already.
//				add toString() method to print contents for debug purposes.

package tinyplanet.docwiz;

import javax.swing.table.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;
import javax.swing.event.*;
import tinyplanet.docwiz.TagSet;
 /** A model for storing method and constructor parameters
  *
  * @author Simon Arthur
  */
public class ParamTagModel extends TwoPartTagModel
{

  public ParamTagModel() {
  }

 /** Create a new ParamTagModel based upon the given ExecutableElement
  *
  * @param e The ExecutableElement which contains the parameters
  */
  public ParamTagModel(ExecutableElement e) {
//    setCodeToTag(e);
	super(e);
  }

	/**
	 * @param paramLine
	 */
	public void addValue(String paramLine)
	{
		super.addValue(paramLine);
	}

 /** The name of the tag, in this case, "param"
  *
  * @return The String "Param"
  */
  public String getTagName () {
    return "param";
  }
  /**
  */
  public void setTagName(String tagName) {
  // doesn't matter, we know the name
  }

 /** Get a vector containing the names of all the parameters
  *
  * @return a Vector containing all the names of the parameters
  */
  public Vector getNameVector() {
    ExecutableElement m = (ExecutableElement)codeToTag;
    Vector actualParams = m.getParameterNames();
    return  actualParams;
  }

// The TableModel interface methods begin here **********************************************

 /** Find the column names, in this case "Parameter" for the first column and "Description"
  * for the second column
  *
  * @param columnIndex The column to look up
  * @return The name of the column requested
  */
  public String getColumnName(int columnIndex) {
    String returnValue = "";
    switch (columnIndex) {
      case NAMECOLUMN: returnValue = "Parameter";
        break;
      case DESCRIPTIONCOLUMN: returnValue = "Description";
        break;
    }
    return returnValue;
  }

//     /** The listeners for the tableModel events
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
		buf.append(", size: " + getColumnCount() + " X " + getRowCount() + ", contents: ");
		for (Iterator iter = getNameVector().iterator() ; (iter.hasNext()) ; ) {
			buf.append(", ");
			buf.append(iter.next());
		}

		return buf.toString();
	}

}