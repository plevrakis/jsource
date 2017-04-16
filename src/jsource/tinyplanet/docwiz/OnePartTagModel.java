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
//	3/22/2001 Lee Meador (LeeMeador@usa.net) 1) Changed the firing of change events
//			to avoid having then generated when the TableModel methods are called.
//			Further, change the rest of the code to never call the TableModel
//			methods but to change the underlying data and then fire a change.
//			2) Use an EventListenerList object to keep the list of listeners and
//			to fire events to them. Remove the local list of listeners.
//	5/3/2001 Lee Meador - don't access the vector (v) in the super class directly
//			from here. Keep all references in the super class.
//	6/20/2001 lmm	- add removeTagAt() method

package tinyplanet.docwiz;

import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableModel;
import javax.swing.table.*;
import java.util.Vector;
import java.lang.RuntimeException;

/** This class is a TableModel for data which is stored as a list and which can be represented
 * by a single column in a JTable.
 *
 * @author Simon Arthur
 */
public class OnePartTagModel extends DefaultTagModel implements TableModel
{
	EventListenerList listenerList = new EventListenerList();
	TableModelEvent tableModelEvent = null;

	//  /** The listeners for the tableModel events are stored in here. The methods are
	//   * needed since {@link javax.swing.table.AbstractTableModel} is abstract. They
	//   * are not used.
	//   */
	//  private AbstractTableModel tableModel = new AbstractTableModel()  {
	//  	/** not used but required since @link javax.swing.table.AbstractTableModel} is abstract. */
	//  	public int getRowCount()  {
	//		return 1;
	//	};
	//  	/** not used but required since @link javax.swing.table.AbstractTableModel} is abstract. */
	//	public int getColumnCount() {
	//		return 2;
	//	};
	//  	/** not used but required since @link javax.swing.table.AbstractTableModel} is abstract. */
	//	public Object getValueAt(int row, int column)  {
	//		return "n/a";
	//	};
	//  };
	//
	////  private java.util.Vector tableModelListeners;

	/** Build a new OnePartTagModel for the given piece of CommentableCode
	 *
	 * @param e The CommentableCode upon which the model will be based
	 */
	public OnePartTagModel(CommentableCode e)
	{
		super(e);
	}

	public void removeElementAt(int location)
	{
		super.removeElementAt(location);
		TableModelEvent e = new TableModelEvent(this, location, location,
			TableModelEvent.ALL_COLUMNS,
			TableModelEvent.DELETE);
		this.fireTableChanged(e);
//		this.tableModel.fireTableRowsDeleted(location, location);
	}

	// Begin TableModel interface methods. ***************************************************

	public java.lang.Class getColumnClass(int columnIndex)
	{
		return String.class;
	}

	public int getRowCount()
	{
		return super.size();
	}

	public int getColumnCount()
	{
		return 1;
	}

	public String getColumnName(int columnIndex)
	{
		return "";
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Object retval = this.tagAt(rowIndex);
		debug("getValueAt(" + rowIndex + "," + columnIndex + ") -> " + retval); // Debug
		return retval;
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{

		debug("setValueAt(" + rowIndex + "," + columnIndex + ") = " + aValue); // Debug
		new RuntimeException().printStackTrace();
		if (aValue == null) {	// this is a kludge
			return ;
		}
		// NOTE: Use super because it does not fire any table changed stuff.
		super.setTagAt(rowIndex,  /* aValue==null ? "" :*/ aValue.toString());
		//    TableModelEvent e = new TableModelEvent(this, rowIndex, rowIndex, columnIndex);
		//    this.fireTableChanged(e);
	}

	/*
	public void addTableModelListener(TableModelListener l) {
		this.tableModel.addTableModelListener(l);
	//    java.util.Vector v = tableModelListeners == null ? new Vector(2) : (Vector) tableModelListeners.clone();
	//    if (!v.contains(l)) {
	//      v.addElement(l);
	//      tableModelListeners = v;
	//    }
}
	 */

	/*
	public void removeTableModelListener(TableModelListener l) {
		this.tableModel.removeTableModelListener(l);
	//     if (tableModelListeners != null && tableModelListeners.contains(l)) {
	//      java.util.Vector v = (java.util.Vector) tableModelListeners.clone();
	//      v.removeElement(l);
	//      tableModelListeners = v;
	//    }
}
	 */

	/*
	protected void fireTableChanged(TableModelEvent e) {
		this.tableModel.fireTableChanged(e);
	//    if (tableModelListeners != null) {
	//      Vector listeners = tableModelListeners;
	//      int count = listeners.size();
	//      for (int i = 0; i < count; i++)
	//        ((TableModelListener) listeners.elementAt(i)).tableChanged(e);
	//    }
}
	 */


	public void addTableModelListener(TableModelListener l)
	{
		listenerList.add(TableModelListener.class, l);
	}

	public void removeTableModelListener(TableModelListener l)
	{
		listenerList.remove(TableModelListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for
	 *  notification on this event type.  The event instance
	 *  is lazily created using the parameters passed into
	 *  the fire method.
	 * @param e The event.
	 */
	protected void fireTableChanged(TableModelEvent e)
	{
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TableModelListener.class) {
				// Lazily create the event:
				tableModelEvent = e;
				((TableModelListener)listeners[i + 1]).tableChanged(e);
			}
		}
	}

	// end TableModel interface methods. ***************************************************

	public void addValue(String parm1)
	{
		super.addValue( parm1 );
		int row = this.size() - 1;
		TableModelEvent e = new TableModelEvent(this, row, row,
			TableModelEvent.ALL_COLUMNS,
			TableModelEvent.INSERT);
		this.fireTableChanged(e);
//		this.tableModel.fireTableRowsInserted(row, row);
	}

	public void setTagAt(int location, String value)
	{
		super.setTagAt(location, value);
		TableModelEvent e = new TableModelEvent(this, location, location, 0);
		this.fireTableChanged(e);
//		this.tableModel.fireTableCellUpdated(location, 0);
	}

	public void removeTagAt(int location)
	{
		super.removeTagAt(location);
		TableModelEvent e = new TableModelEvent(this, location, location,
			TableModelEvent.ALL_COLUMNS,
			TableModelEvent.DELETE);
		this.fireTableChanged(e);
	}

	private void debug(String msg)
	{
//	    ConfigurationService configurationService = ConfigurationService.getConfigurationService();
//    	if (configurationService.getDebug()) {
//			ChangeDispatcher.getChangeDispatcher().fireDebugMessage("OnePartTagModel:" + msg);
//    	}
	}

}
