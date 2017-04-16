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
//	5/2/2001 Lee Meador - Change the sizes so that the buttons do not get larger
//			and larger as the window does. Don't let them get the focus when they
//			are clicked upon. Realign the text and buttons a bit.
// 5/14/2001 Lee Meador - Change to delete the last row if the bottom, right cell
//			is blank and no other row is currently selected.
// 5/21/2001 Lee Meador - disable the table when the last element is removed, enable it
//			when the one is added back. Add a border to the buttons and a space between.
// 6/20/2001 lmm	- change some stuff to protected so we can subclass for the unknown
//					tags pane.
//	6/21/2001 lmm	- close down the cell editor before deleting that row in the table.

package tinyplanet.docwiz;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.*;

/** This is a special GUI component for manipulating the contents of a JTable. It contains buttons
 * labeled "+" and "-". If the JTable is associated with a OnePartTagModel, the buttons will add
 * a new element and erase the selected element.
 *
 * @author Simon Arthur
 */
public class TableNavMenuBar extends JPanel {

  /** The "+" (add) button is a standard JButton that will not take the focus.
   */
  JButton jBtnAdd = new JButton()  {
		public boolean isFocusTraversable()
		{
			return false;
		}
  	};

  /** The "-" (delete) button is a standard JButton that will not take the focus.
   */
  JButton jBtnDelete = new JButton()  {
		public boolean isFocusTraversable()
		{
			return false;
		}
  	};

  /** The JTable which this object will manipulate
   */
  private javax.swing.JTable table;

//  GridLayout gridLayout1 = new GridLayout(1,2);

  public TableNavMenuBar() {
    try  {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
//    this.setLayout(new BorderLayout());

	jBtnAdd.setBorder(BorderFactory.createRaisedBevelBorder());
    jBtnAdd.setText("+");
    jBtnAdd.setBackground(SystemColor.control);
    jBtnAdd.setFont(new Font("Monospaced", 1, 14));
	jBtnAdd.setMaximumSize(new Dimension(50, 50));
	jBtnAdd.setPreferredSize(new Dimension(50, 50));
	jBtnAdd.setRequestFocusEnabled(false);
    jBtnAdd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jBtnAdd_actionPerformed(e);
      }
    });
	jBtnAdd.setBorderPainted(true);

    jBtnDelete.setBorder(BorderFactory.createRaisedBevelBorder());
    jBtnDelete.setText("-");
    jBtnDelete.setBackground(SystemColor.control);
    jBtnDelete.setFont(new Font("Monospaced", 1, 16));
	jBtnDelete.setMaximumSize(new Dimension(50, 50));
	jBtnDelete.setPreferredSize(new Dimension(50, 50));
	jBtnDelete.setRequestFocusEnabled(false);
    jBtnDelete.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jBtnDelete_actionPerformed(e);
      }
    });

//    jToolBar.setLayout(new BoxLayout(jToolBar, BoxLayout.X_AXIS));
//    jToolBar.setMaximumSize(new Dimension(100, 50));
//    jToolBar.add(jBtnAdd, null);
//    jToolBar.add(jBtnDelete, null);
//
//    this.setMaximumSize(new Dimension(100, 50));
//    this.add(jToolBar, BorderLayout.WEST);

    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.setMaximumSize(new Dimension(100, 50));
    this.add(jBtnAdd, null);
	this.add(Box.createHorizontalStrut(5));
    this.add(jBtnDelete, null);

  }

  public void setTable(javax.swing.JTable newTable) {
    table = newTable;

    TableModel tableModel = table.getModel();
	enableDisableStuff(tableModel.getRowCount());
  }

  public javax.swing.JTable getTable() {
    return table;
  }

  protected void jBtnAdd_actionPerformed(ActionEvent e) {
    if (table != null) {
      TableModel t = table.getModel();
      if (t instanceof OnePartTagModel) {
        OnePartTagModel tableModel = (OnePartTagModel ) t;
        tableModel.addValue("");
		enableDisableStuff(tableModel.getRowCount());
        table.revalidate();
      }
    }
  }

  protected void jBtnDelete_actionPerformed(ActionEvent e) {
    if (table != null) {
      TableModel t = table.getModel();
      if (t instanceof OnePartTagModel) {
        OnePartTagModel tableModel = (OnePartTagModel ) t;
        int selectedRow = table.getSelectedRow();
        if ( selectedRow  != -1 ) {

			CellEditor ce = null;				// Stop the editor before removing the row
			if (table.isEditing()) {
				int column = table.getSelectedColumn();
				int row = table.getSelectedRow();
				if (column >= 0 && row >= 0) {
					ce = table.getCellEditor(row, column);
				}
			}
			if (ce != null) {
				ce.stopCellEditing();
			}

	        tableModel.removeElementAt(selectedRow);
        }
		else  {
			int rows = tableModel.getRowCount();
			if (rows > 0) {
				int lastCol = tableModel.getColumnCount();
				if (lastCol > 0) {
					lastCol--;
				}
				String val = tableModel.getValueAt(rows-1, lastCol).toString();
				if (val.trim().length() == 0) {
					tableModel.removeElementAt(rows-1);
				}
			}
		}
		enableDisableStuff(tableModel.getRowCount());
        table.revalidate();
      }
    }
  }

  protected void enableDisableStuff(int tableRows) {
		if (tableRows > 0) {
			table.setEnabled(true);
//			jBtnDelete.setEnabled(true);		// This makes sense but looks funny
		}
		else  {
			table.setEnabled(false);
//			jBtnDelete.setEnabled(false);		// This makes sense but looks funny
		}
  }

  public void updateEnabledState() {
    if (table != null) {
      TableModel tableModel = table.getModel();
	  enableDisableStuff(tableModel.getRowCount());
      table.revalidate();
    }
  }


}