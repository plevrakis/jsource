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
//	8/13/2001 lmm	- force a space between the parm name and the comment proper if it is
//					not there. (It won't be there any more due to another fix.)

package tinyplanet.docwiz;

import java.util.*;
import tinyplanet.docwiz.TagSet;
import javax.swing.table.*;
import javax.swing.event.*;


/**
 * A model for holding JavaDoc "@" tags which can have two distinct pieces of data, with a space
 * delimiter. For example:
 * <p>
 * <pre>@exception IOException There has been a problem writing to the file
 * </pre>
 * <p>The above entry contains two distinct pieces of information: the name of the exception, and the
 * exception's description.
 *
 *
 * @author Simon Arthur
 */
public abstract class TwoPartTagModel extends AbstractTableModel implements TagSet, TableModel
{
	// LMM made this extend AbstractTableModel.

	/**
	 * The column in which the first part of the tag will be displayed
	 */
	static final int NAMECOLUMN = 0;

	/**
	 * The column in which the second part of the column will be displayed
	 */
	static final int DESCRIPTIONCOLUMN = 1;

	/**
	 * This Hashtable relates the first part of the tag, as the key, to the second part of the tag,
	 * as the value.
	 */
	Hashtable typeToDesc = new Hashtable (9);

	/** This holds the names for tags that are not actual parameters or exceptions */
	private Vector missingTagNames = new Vector(9);

	/**
	 * Which code element is described by this TagModel
	 */
	protected tinyplanet.docwiz.CommentableCode codeToTag;

	/**
	 * Default Constructor
	 */
	public TwoPartTagModel()
	{}

	/**
	 * Create a new one based upon the given ExecutableElement
	 *
	 * @param e The ExecutableElement which contains the parameters.
	 */
	public TwoPartTagModel(ExecutableElement e)
	{
		setCodeToTag(e);
	}


	public abstract Vector getNameVector();

	/**
	 * @param paramLine
	 */
	public void addValue(String paramLine)
	{
//		System.out.println("TwoPartTagModel.addValue: entry: " + paramLine);
		String type = "", description = "";
		StringTokenizer st = new StringTokenizer(paramLine, " \t");
		// split up line into type and description
		try {

			type = st.nextToken();
			description = st.nextToken("\u0000");
			description = description.trim();
		}
		catch (NoSuchElementException nsee) {
			//System.err.println(nsee);
		}
		if (type.trim().length() > 0) {
			typeToDesc.put(type, description);

			Vector actualNames = new Vector(getNameVector());
			actualNames.addAll(missingTagNames);
			int rowIndex = actualNames.indexOf(type);
			if (rowIndex < 0 ) {			// Handle this being a new name but not a real parameter
				missingTagNames.add(type);
				actualNames = new Vector(getNameVector());
				actualNames.addAll(missingTagNames);
				rowIndex = actualNames.indexOf(type);
			}
			fireTableRowsInserted(rowIndex, rowIndex);
		}
//		System.out.println("TwoPartTagModel.addValue: exit");
	}

	public String tagAt(int i)
	{
		String parmName = tagNameAt(i);
		String between = " ";
		/*if (parmName.lastIndexOf(' ') != parmName.length()-1) {
			between = " ";
			} */
		String rtn = parmName + between + tagValueAt(i);
		return rtn;
	}


	/**
	 * Get the argument name for the indicated tag in this set. If the tag were
	 *		"param count the number of items" then the name is "count".
	 *
	 * @since 07/16/2001 3:11:08 PM
	 * @param i The index in the tag set of the desired name.
	 * @return The name of the argument for the indicated tag.
	 */
	public String tagNameAt(int i)
	{
		Object o = getValueAt(i, NAMECOLUMN);
		String rtn = (o == null) ? "" : o.toString();
		return rtn;
	}

	/**
	 * Get the value for the indicated tag in this set. If the tag were
	 *		"param count the number of items" then the value is "the number of times".
	 *
	 * @since 07/16/2001 3:11:08 PM
	 * @param i The index in the tag set of the desired tag.
	 * @return The value for the indicated tag.
	 */
	public String tagValueAt(int i)
	{
		Object o = getValueAt(i, DESCRIPTIONCOLUMN);
		String rtn = (o == null) ? "" : o.toString();
		return rtn;
	}

	public void clear()
	{
		typeToDesc = new Hashtable (9);
		missingTagNames = new Vector(9);

		fireTableDataChanged();
	}

	/**
	 * @param location
	 * @param value
	 */
	public void setTagAt(int location, String value)
	{
		//    this.setValueAt(value, location, DESCRIPTIONCOLUMN);

		String type = null;
		try {
			int nameVectorSize = getNameVector().size();
			if (location < nameVectorSize) {
				type = (String)getNameVector().elementAt(location);
			}
			else  {
				type = (String)missingTagNames.elementAt(location - nameVectorSize);
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {}

		if (type == null) {
			type = "";
		}
		typeToDesc.put(type, value);

		fireTableCellUpdated(location, 1);
	}

	public void removeTagAt(int location)
	{
		String type = null;
		try {
			int nameVectorSize = getNameVector().size();
			if (location < nameVectorSize) {
				type = (String)getNameVector().elementAt(location);
				if (type == null) {
					type = "";
				}
				typeToDesc.remove(type);
			}
			else  {
				type = (String)missingTagNames.elementAt(location - nameVectorSize);
				if (type == null) {
					type = "";
				}
				typeToDesc.remove(type);
				missingTagNames.removeElementAt(location - nameVectorSize);
			}

		}
		catch (ArrayIndexOutOfBoundsException e) {}
		fireTableRowsDeleted(location, 	location);
	}

	/**
	 * @param newCodeToTag
	 */
	public void setCodeToTag(tinyplanet.docwiz.CommentableCode newCodeToTag)
	{
		clear();
		codeToTag = newCodeToTag;
		fireTableDataChanged();
	}


	public tinyplanet.docwiz.CommentableCode getCodeToTag()
	{
		return codeToTag;
	}


	public int size()
	{
//		System.out.println("TwoPartTagModel.size: " +  getNameVector().size() + ", " + missingTagNames.size());
		return getNameVector().size() + missingTagNames.size();
	}


	/**
	 * @param tagType
	 */
	public String toJavaDocString(String tagType)
	{
		TagFormatter tf = new ValuedTagFormatter(this, tagType);
		return tf.getJavaDocString();
	}

	/**
	 * Convert the TagSet to a list (Vector) consisting of the comments that can be changed.
	 *
	 * @return A list of the comment strings.
	 */
	public Vector getTagStringList()
	{
		Vector actualNames = new Vector(getNameVector());
		actualNames.addAll(missingTagNames);
		Vector result = new Vector();
		for (Enumeration enum = actualNames.elements() ; enum.hasMoreElements() ; ) {
			String val = (String)enum.nextElement();
			if (val != null) {
				result.add(typeToDesc.get(val));
			}
		}
		return result;
	}

	// Begin TableModel interface methods. ****************************************

	/**
	 * Set the value at the specified row and column. (Due to some editing done.)
	 *
	 * @param aValue The new value for the cell.
	 * @param rowIndex Row of the cell to change.
	 * @param columnIndex Column of the cell to change.
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{

		//	System.out.println("TwoPartModel.setValueAt(" + rowIndex + "," + columnIndex + ") = " + aValue); // Debug
		if (aValue == null) {	// this is a kludge
			return ;
		}

		switch (columnIndex) {
			case NAMECOLUMN :
				// we're looking for Exception/parameter name
				// we can't change that!

				break;
			case DESCRIPTIONCOLUMN :
				// we're looking for Exception/parameter description

				String type = getValueAt(rowIndex, 0).toString();
				String description = aValue.toString();
				typeToDesc.put(type, description);
				break;
		}
	}

	/**
	 * Get the value at the specified row and column
	 *
	 * @param rowIndex The row for which the value is desired.
	 * @param columnIndex The column for which the value is desired.
	 * @return The value for the cell.
	 */
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		//	System.out.println("TwoPartModel.getValueAt(" + rowIndex + "," + columnIndex + ")"); // Debug
		int i, describedIndex;
		Vector actualNames = new Vector(getNameVector());
		actualNames.addAll(missingTagNames);
		//  Vector describedParams = m.getTagEntry("param");
		String currentParam, currentDescription;
		String parameterName;
		String returnValue = "";
		Object o;
		if (rowIndex < actualNames.size()) {
			o = actualNames.elementAt(rowIndex);
		}
		else {
			o = null;
		}
		String tag = (o == null) ? "" : o.toString();
		if (actualNames.size() > 0) {
			// there is at least one param/exception to the method
			switch (columnIndex) {
				case NAMECOLUMN :
					// we're looking for parameter/exception name

					returnValue = tag;
					break;

				case DESCRIPTIONCOLUMN :
					// we're looking for parameter/exception description

					o = typeToDesc.get(tag);
					returnValue = (o == null) ? "" : o.toString();
					break;
			}
			return returnValue;
		}
		else {
			return "";
		} //  else ! (actualExceptions.size() > 0)
	}

	/**
	 * Can a given cell be edited?
	 * In this case, since the first column is a static description, no.
	 * The second column is OK to edit, though.
	 *
	 * @param rowIndex
	 * @param columnIndex
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{

		if (columnIndex == DESCRIPTIONCOLUMN) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * How many rows are there in the table.
	 *
	 * @return The number of rows.
	 */
	public int getRowCount()
	{
		return size();
	}

	/**
	 * Get the name of the class of object occupying this column. Allows
	 * swing to use different editors for different types.
	 *
	 * @param columnIndex the column number (0-n) in question
	 * @return The name of the class. (eg. java.lang.String)
	 */
	public java.lang.Class getColumnClass(int columnIndex)
	{
		try {
			return java.lang.Class.forName("java.lang.String");
		}
		catch (ClassNotFoundException cfe) {
			return null;
		}
	}

	/**
	 * There are two columns, the first being the name, the second the description
	 *
	 * @return 2
	 */
	public int getColumnCount()
	{
		return 2;
	}

	//  /**    *
	//   * @param l
	//   */
	//  public synchronized void removeTableModelListener(TableModelListener l) {
	//    if (tableModelListeners != null && tableModelListeners.contains(l)) {
	//      Vector v = (Vector) tableModelListeners.clone();
	//      v.removeElement(l);
	//      tableModelListeners = v;
	//    }
	//  }
	//
	//  /**    *
	//   * @param l
	//   */
	//  public synchronized void addTableModelListener(TableModelListener l) {
	//    Vector v = tableModelListeners == null ? new Vector(2) : (Vector) tableModelListeners.clone();
	//    if (!v.contains(l)) {
	//      v.addElement(l);
	//      tableModelListeners = v;
	//    }
	//  }
	//   /**     */
	//   private transient Vector tableModelListeners;

	// /**   *
	//  * @param e
	//  */
	// protected void fireTableChanged(TableModelEvent e) {
	//    if (tableModelListeners != null) {
	//      Vector listeners = tableModelListeners;
	//      int count = listeners.size();
	// /**   */
	//      for (int i = 0; i < count; i++)
	//        ((TableModelListener) listeners.elementAt(i)).tableChanged(e);
	//    }
	//  }

	// End of TableModel interface methods. ********************************************


	/**
	 * Give info about this object
	 *
	 * @since 05/21/2001 11:51:13 AM
	 * @return A debug string to show info about this object.
	 */
	public String toString()
	{
		String result = super.toString() + "size = " + getColumnCount() + ", " + getRowCount() + "";
		return result;
	}

}
