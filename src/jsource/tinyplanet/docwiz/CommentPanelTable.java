// Source file: com/tinyplanet/docwiz/CommentPanelTable.java
// $Id: CommentPanelTable.java,v 1.4 2001/08/15 16:43:29 lmeador Exp $
//
// $Log: CommentPanelTable.java,v $
// Revision 1.4  2001/08/15 16:43:29  lmeador
// Remove implements since base class already implements the same interface.
//
// Revision 1.3  2001/07/17 16:51:39  lmeador
// -small enhancement removes duplicate code line.
//
// Revision 1.2  2001/06/26 22:20:15  lmeador
// - Most changes have to do with allowing a subclass to override some
// things related to how the drop-down boxes work. The unknown tag
// pane needs to have different drop down lists in each of two editable
// columns. This means we delay hooking in the cell editor and
// renderer until there are columns to tie them to. It means get and set
// methods for some attributes. Many other related changes.
// - Don't let minimum height be more than preferred height.
//
// Revision 1.1  2001/06/18 16:39:16  lmeador
// New swing-like components change the way the Field, Class
// and Executable panels work. This consolidates common code
// between the three panels and fixes some things related to clicking
// on one field while anothe field is being edited.
//
//

// History:
//	6/21/2001 lmm	- Fix a bug where deleting a row might leave the cell editor open and thus
//					the correct row was deleted but the value from the cell editor got copied
//					over the top of the row that rolled up.
//	7/17/2001 lmm	- don't rebuild the cell editor, use the one from the init.

package tinyplanet.docwiz;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/** A class to hold a JLabel, a JTable and, possibly, a pair of buttons to click and
 *		add or subtract rows.
 *
 * @author Lee Meador
 * @since  05/21/2001 9:09:37 AM
 */
public class CommentPanelTable extends CommentPanelBaseComponent
{

// Attributes:

	public static final int PLAIN_CELL_EDITOR = 1;
	public static final int COMBO_BOX_CELL_EDITOR = 2;

	protected JLabel aloneLabel;
	protected JLabel plusMinusLabel;
	protected TableNavMenuBar plusMinusBar = new TableNavMenuBar();
	protected JTable table = new JTable();
	protected JScrollPane scrollPane = new JScrollPane();

	protected Box boxPlusMinus = Box.createHorizontalBox();
	protected Component plusMinusSpacer = Box.createVerticalStrut(componentGap);

	protected JComboBox comboBox = new JComboBox();
	protected TableCellEditor tableCellEditor = new EditableComboBoxCellEditor(comboBox);

	protected DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			// Never draw anything but the last column as selected or having focus.
			//  There should be a better way to say that a column can not have any of
			//  its cells selected.
			if (column < table.getModel().getColumnCount() - 1) {
				return super.getTableCellRendererComponent(table,
					value, false, false, row, column);
			}
			else {
				return super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			}
		}
	};

	protected String tagName;

	protected boolean hasPlusMinusBar = false;
	private int cellEditorType = COMBO_BOX_CELL_EDITOR;

// Debug
//	private TableModelListener debugTableChangedListener = new TableModelListener()  {
//
//				public void tableChanged(TableModelEvent e)
//				{
//					String msg = "table changed: "
//						+ "Rows " + e.getFirstRow() + " to " + e.getLastRow() + ", "
//						+ "Col = " + e.getColumn();
//
//					switch (e.getType()) {
//						case e.UPDATE :
//							msg += ", UPDATE";
//							break;
//						case e.DELETE :
//							msg += ", DELETE";
//							break;
//						case e.INSERT :
//							msg += ", INSERT";
//							break;
//						default :
//							break;
//					}
//
//					changeDispatcher.fireDebugMessage("ClassPanel:TableModelListener:See " + msg);
//				}
//			};
// Debug

// Constructors:

	/**
	 * Default Constructor
	 */
	public CommentPanelTable()
	{
		this("", 100, null);
	}

	public CommentPanelTable(CommentPanelModel commentPanelModel)
	{
		this("", 100, null);
		setModel(commentPanelModel);
	}

	public CommentPanelTable(String title, int preferredHeight, String tagName, CommentPanelModel commentPanelModel)
	{
		this(title, preferredHeight, tagName);
		setModel(commentPanelModel);
	}

	public CommentPanelTable(String title, int preferredHeight, String tagName)
	{
		super(BoxLayout.Y_AXIS);			// like Box.createVerticalBox()

		setTagName(tagName);

		aloneLabel = new CommentPanelLabel(title, CommentPanelLabel.ALONE_ON_LINE);

		plusMinusLabel = new CommentPanelLabel(title, CommentPanelLabel.NOT_ALONE_ON_LINE);

		getPlusMinusBar().setTable(table);		// Always tie this to the table, only show this sometimes

		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
//    	table.addFocusListener(new java.awt.event.FocusAdapter() {
//
//      	public void focusLost(FocusEvent e) {
//        		table.doneEditing(e);
//      	}
//    	});

		scrollPane.getViewport().add(table);
		scrollPane.setMinimumSize(new Dimension(100, (preferredHeight < 50) ? preferredHeight : 50));
		scrollPane.setPreferredSize(new Dimension(453, preferredHeight));
		scrollPane.setMaximumSize(new Dimension(3000, preferredHeight*10));

		boxPlusMinus.add(plusMinusLabel);
		boxPlusMinus.add(Box.createRigidArea(new Dimension(componentGap, 1)));
		boxPlusMinus.add(getPlusMinusBar());
		boxPlusMinus.add(Box.createHorizontalGlue());

		//Set up the editor for the parameter cells.
		comboBox.setEditable(true);
		comboBox.addItem("<nothing>");
		comboBox.setMaximumRowCount(8);
// Debug
//		comboBox.addItemListener(new ItemListener()  {
//				public void itemStateChanged(ItemEvent e)
//				{
//				    if (e.getStateChange() == ItemEvent.SELECTED) {
//						changeDispatcher.fireDebugMessage("ClassPanel:ItemListener:See:ComboBox isSelected <" + e.getItem().toString() + ">");
//				    } else {
//						changeDispatcher.fireDebugMessage("ClassPanel:ItemListener:See:ComboBox deSelected <" + e.getItem().toString() + ">");
//				    }
//				}
//
//			}
//		);
//		comboBox.addActionListener(new ActionListener()  {
//				public void actionPerformed(ActionEvent e)
//				{
//					changeDispatcher.fireDebugMessage("ClassPanel:ActionListener:See:ComboBox action performed <" + e.getActionCommand().toString() + ">");
//				}
//			}
//		);
// Debug

//		tableCellEditor = new EditableComboBoxCellEditor(comboBox);

// Debug
//		tableCellEditor.addCellEditorListener(new CellEditorListener()  {
//
//				public void editingCanceled(ChangeEvent e)  {
//					changeDispatcher.fireDebugMessage("ClassPanel:CellEditorListener:See editing cancelled <" + e.getSource().toString() + ">");
//				}
//
//				public void editingStopped(ChangeEvent e)  {
//					Object source = e.getSource();
//					changeDispatcher.fireDebugMessage("ClassPanel:CellEditorListener:See editing stopped<" + e.getSource().toString() + ">");
//				}
//
//			}
//		);
// Debug

// Debug
//    	comboBox.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter() {
//    	  	public void focusLost(FocusEvent e) {
//				changeDispatcher.fireDebugMessage("ClassPanel:FocusListener:See:ComboBox:Editor:Component focus lost <" + e.getSource().toString() + ">");
//	      	}
//    	});
// Debug

//		table.setDefaultEditor(String.class, tableCellEditor);
//		int lastColumn = table.getColumnCount() - 1;
//		TableColumn lastTableColumn = table.getColumnModel().getColumn(lastColumn);
//		lastTableColumn.setCellEditor(tableCellEditor);

		//Set up tool tips for the parameter cells.
		getTableCellRenderer().setToolTipText("Click for combo box");
//		table.setDefaultRenderer(String.class, getTableCellRenderer());
//		lastTableColumn.setCellRenderer(getTableCellRenderer());

		setCellEditorType(COMBO_BOX_CELL_EDITOR);
//		setCellEditor(COMBO_BOX_CELL_EDITOR);

//		//Set up tool tip for the parameter column header.
//		TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();
//		if (headerRenderer instanceof DefaultTableCellRenderer) {
//			((DefaultTableCellRenderer)headerRenderer).setToolTipText(
//			    "Click the " + name + " comment to see a list of choices");
//		}

		add(aloneLabel);
		add(scrollPane);
	}

	public CommentPanelTable(String title, int preferredHeight, String tagName, int cellEditorType, CommentPanelModel commentPanelModel)
	{
		this(title, preferredHeight, tagName);
		setCellEditorType(cellEditorType);
		setModel(commentPanelModel);
	}

	public CommentPanelTable(String title, int preferredHeight, String tagName, int cellEditorType)
	{
		this(title, preferredHeight, tagName);
		setCellEditorType(cellEditorType);
	}

// Methods:

	/**
	 * Set the title, which is the text for the JCheckBox.
	 *
	 * @since 05/17/2001 5:49:34 PM
	 * @param title The new title to go into the text of the JCheckBox.
	 */
	public void setTitle(String title)
	{
		aloneLabel.setText(" " + title);
		plusMinusLabel.setText(" " + title);
		return;
	}

	/**
	 * Set the text of the JTextField.
	 *
	 * @since 05/17/2001 6:04:20 PM
	 * @param text The new text to put into the text field.
	 */
	public void setText(String text)
	{
		return;
	}

	/**
	 * Set the next focusable component for the internal JTextField.
	 *
	 * @param component The new, next component in the focus chain.
	 */
	public void setNextFocusableComponent(Component component)
	{
		table.setNextFocusableComponent(component);
	}

	/**
	 * Set the focusable fields to be enabled or not.
	 *
	 * @since 05/18/2001 12:00:25 PM
	 * @param isEnabled true if the fields should be enabled,
	 *		false otherwise.
	 * @return
	 */
	public void setEnabled(boolean isEnabled)
	{
		table.setEnabled(isEnabled);
		return;
	}

	/**
	 * Unselect whatever is selected in the table.
	 *
	 * @since 05/21/2001 9:29:28 AM
	 */
	public void clearSelection()
	{
		table.clearSelection();
		return;
	}

	/**
	 * Have the table size the columns correctly.
	 *
	 * @since 05/21/2001 9:34:19 AM
	 * @param col See {jTable#sizeColumnsToFit} for the full parameter meaning. It is mainly
	 *		the column that changed size.
	 * @see jTable#sizeColumnsToFit
	 */
	public void sizeColumnsToFit(int col)
	{
		if (table.getColumnCount() > 0) { 			// No use sizeing if no columns
			if (table.getColumnCount() > col) {
				table.sizeColumnsToFit(col);		// Size valid column
			}
			else  {
				table.sizeColumnsToFit(-1);			// Size all columns
			}
		}
		return;
	}

	/**
	 * Returns the TableColumn object for the column in the jTable object at
	 *		the column index given.
	 *
	 * @since 05/21/2001 9:37:31 AM
	 * @param identifier the index of the column desired.
	 * @return the TableColumn object that is at the given column.
	 */
	public TableColumn getColumn(int identifier)
	{
		return table.getColumnModel().getColumn(identifier);
	}

	/**
	 * Tells how many columns in the JTable.
	 *
	 * @since 05/21/2001 9:37:31 AM
	 * @return The number of columns of data.
	 */
	public int getColumnCount()
	{
		return table.getModel().getColumnCount();
	}

	/**
	 * Tells how many rows in the JTable.
	 *
	 * @since 05/21/2001 9:37:31 AM
	 * @return The number of rows of data.
	 */
	public int getRowCount()
	{
		return table.getModel().getRowCount();
	}

	/**
	 * Add the plus/minus tool bar to appear right above the table
	 *
	 * @since 05/21/2001 5:38:07 PM
	 */
	public void addPlusMinusBar()
	{
		if (!isPlusMinusBar()) {
			remove(0);
			add(plusMinusSpacer, 0);		// becomes 2nd component
			add(boxPlusMinus, 0);			// is first component
		}
		hasPlusMinusBar = true;

		return;
	}

	/**
	 * Remove the plus/minus tool bar to appear right above the table
	 *
	 * @since 05/21/2001 5:38:07 PM
	 */
	public void removePlusMinusBar()
	{
		if (isPlusMinusBar()) {
			remove(0);						// remove 1st two components
			remove(0);
			add(aloneLabel, 0);
		}
		hasPlusMinusBar = false;

		return;
	}

	/**
	 * Tell if the plus/minus tool bar appears right above the table
	 *
	 * @since 05/21/2001 5:38:07 PM
	 * @return true if it is there, false if not.
	 */
	public boolean isPlusMinusBar()
	{
		return hasPlusMinusBar;
	}

	/**
	 * Change the cell editor to the one specified.
	 *
	 * @since 05/21/2001 6:25:15 PM
	 * @param type should be one of 1) PLAIN_CELL_EDITOR or 2) COMBO_BOX_CELL_EDITOR.
	 */
	public void setCellEditor(int type)
	{
		int lastColumn = table.getColumnCount() - 1;

		if (type == PLAIN_CELL_EDITOR) {
			if (lastColumn >= 0) {
				TableColumn lastTableColumn = table.getColumnModel().getColumn(lastColumn);
				lastTableColumn.setCellEditor(null);
				lastTableColumn.setCellRenderer(null);
			}

//			// By clearing out the cell editor on String, we make it choose the cell
//			//  editor on Object, which is the normal one. Similarly for the renderer.
//			table.setDefaultEditor(String.class, null);
//			table.setDefaultRenderer(String.class, null);
			setCellEditorType(PLAIN_CELL_EDITOR);
		}
		else  {
			if (lastColumn >= 0) {
				TableColumn lastTableColumn = table.getColumnModel().getColumn(lastColumn);
				lastTableColumn.setCellEditor(tableCellEditor);
				lastTableColumn.setCellRenderer(getTableCellRenderer());
			}

//			// The editor/renderer on String is used instead of the normal one on Object.
//			table.setDefaultEditor(String.class, tableCellEditor);
//			table.setDefaultRenderer(String.class, getTableCellRenderer());
			setCellEditorType(COMBO_BOX_CELL_EDITOR);
		}

		return;
	}

	/**
	 * Come up with a printable string for this object.
	 *
	 *return A printable string.
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer(this.getClass().getName());
		buf.append("@");
		buf.append(this.hashCode());

		return buf.toString();
	}

// Event things:

	/**
	 *  Stop the cell editor that may be running on the JTable
	 *   in response to the specified focus lost or gained event. Stopping
	 *	 the cell editor will copy the screen contents to the table model
	 *	 and remove the highlighting on the cell that is being edited.
	 *
	 * @since 05/21/2001 9:30:34 AM
	 * @param e The FocusEvent object that triggered this action.
	 */
	public void doneEditing(FocusEvent e)
	{
		if (e != null && e.isTemporary()) {
			return;
		}
		CellEditor ce = null;
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
		return;
	}

	/**
	 * Implement ChangeListener interface. This method tells me that my model changed
	 *  and I need to update my screen image.
	 *
	 * @param e The ChangeEvent object.
	 */
	public void stateChanged(ChangeEvent e)
	{
		CodeComment comment = getCurrentComment();
		String tagName = getTagName();
		if (comment != null && tagName != null) {
			TableModel tagModel = (TableModel)comment.getTagEntry(tagName);
			if (tagModel == null) {
				System.out.println("Null tagModel in " + this);
			}

//			table.setModel(new ParamTagModel()); // workaround to avoid stomping on values when changing between methods
			table.setModel(tagModel);

			setCellEditor(getCellEditorType());

			if (table.getAutoCreateColumnsFromModel()) {
				table.setAutoCreateColumnsFromModel(false);
			}

			if (isPlusMinusBar()) {
				getPlusMinusBar().updateEnabledState();
			}
			else {
				if (getRowCount() > 0) {
					setEnabled(true);
				}
				else  {
					setEnabled(false);
				}
			}

			sizeColumnsToFit(0);

			// Set up the things in the combo box drop down list.

			comboBox.removeAllItems();

			Vector dropDownList = comment.getCompilationUnitTagList(tagName);

			if (dropDownList != null && dropDownList.size() > 0) {

				// NOTE: If you need this to run with JDK 1.1 you will need to replace this
				//  or comment it out.
				Collections.sort(dropDownList);

				// First put them all in a hash table to remove the duplicates. This
				//  really acts more like a Set.
				Hashtable hash = new Hashtable();
				for (Enumeration enum = dropDownList.elements(); enum.hasMoreElements() ; ) {
					Object parm = enum.nextElement();
					if (parm != null) {
						hash.put(parm, "X");
					}
				}
				// Now put them in the list
				for (Enumeration enum = hash.keys(); enum.hasMoreElements() ; ) {
					Object parm = enum.nextElement();
					if (parm != null) {
						comboBox.addItem(parm);
					}
				}
			}
		}

		return;
    }

// Get/Set Methods:

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link tagName }.
	 * @since  05/19/2001 1:42:44 PM
	 */
	public String getTagName()
	{
		return this.tagName;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link tagName }.
	 * @since  05/19/2001 1:42:44 PM
	 */
	public void setTagName(String val)
	{
		this.tagName = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link cellEditorType }.
	 * @since  05/21/2001 6:29:26 PM
	 */
	public int getCellEditorType()
	{
		return this.cellEditorType;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link cellEditorType }.
	 * @since  05/21/2001 6:29:26 PM
	 */
	public void setCellEditorType(int val)
	{
		this.cellEditorType = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link tableCellRenderer }.
	 * @since  06/20/2001 7:06:43 PM
	 */
	public DefaultTableCellRenderer getTableCellRenderer()
	{
		return this.tableCellRenderer;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link tableCellRenderer }.
	 * @since  06/20/2001 7:06:43 PM
	 */
	public void setTableCellRenderer(DefaultTableCellRenderer val)
	{
		this.tableCellRenderer = val;
	}

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link plusMinusBar }.
	 * @since  06/20/2001 7:09:01 PM
	 */
	public TableNavMenuBar getPlusMinusBar()
	{
		return this.plusMinusBar;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link plusMinusBar }.
	 * @since  06/20/2001 7:09:01 PM
	 */
	public void setPlusMinusBar(TableNavMenuBar val)
	{
		this.plusMinusBar = val;
	}

}