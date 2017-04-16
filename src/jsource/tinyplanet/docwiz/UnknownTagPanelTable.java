// Source file: com/tinyplanet/docwiz/UnknownTagPanelTable.java
// $Id: UnknownTagPanelTable.java,v 1.2 2001/07/17 17:12:59 lmeador Exp $
//
// $Log: UnknownTagPanelTable.java,v $
// Revision 1.2  2001/07/17 17:12:59  lmeador
// - fix the contents of the drop down box on the name (left) column.
// - it shows all current tag names in the file.
// - it shows all tags allowed by javadoc standards on this code item
// - multiples are removed
// - it works right now.
//
// Revision 1.1  2001/06/26 22:48:18  lmeador
// Unknown tag processing is almost totally rewritten. (Although some
// really good code got copied from the old to the new.) The new table
// model is structured as two lists that point into the TagSet objects that
// hold the real tags and comments. When the user changes the table,
// the change is reflected into the TagSet objects via the two lists. We
// also subclass existing classes to generate the +/- bar and to handle
// the table updating, drop down lists and formatting.
//
// Revision 1.1  2001/06/18 16:39:16  lmeador
// New swing-like components change the way the Field, Class
// and Executable panels work. This consolidates common code
// between the three panels and fixes some things related to clicking
// on one field while anothe field is being edited.
//
//

// History:
//	7/17/2001 lmm	- Fix drop down contents for the tag name (left column) to
//					reflect only tags allowed for the item type and other tags
//					present in the file without duplicates.

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
public class UnknownTagPanelTable extends CommentPanelTable
{

// Attributes:

	/** An array of the tags allowed for methods */
	private static final String [] methodTags = new String[]  {
		"deprecated",
		"exception",
		"param",
		"return",
		"see",
		"since",
		"throws"
		};
	/** A Collection of the tags allowed for methods */
	private static final Collection methodTagList = Arrays.asList(methodTags);

	/** An array of the tags allowed for methods */
	private static final String [] fieldTags = new String[]  {
		"deprecated",
		"see",
		"since"
		};
	/** A Collection of the tags allowed for methods */
	private static final Collection fieldTagList = Arrays.asList(fieldTags);

	/** An array of the tags allowed for methods */
	private static final String [] classTags = new String[]  {
		"author",
		"deprecated",
		"see",
		"since",
		"version"
		};
	/** A Collection of the tags allowed for methods */
	private static final Collection classTagList = Arrays.asList(classTags);

	/** An array of the tags allowed for methods */
	private static final String [] interfaceTags = new String[]  {
		"author",
		"deprecated",
		"see",
		"since",
		"version"
		};
	/** A Collection of the tags allowed for methods */
	private static final Collection interfaceTagList = Arrays.asList(interfaceTags);

	/** An array of the tags allowed for methods */
	private static final String [] constructorTags = new String[]  {
		"deprecated",
		"exception",
		"param",
		"see",
		"since",
		"throws"
		};
	/** A Collection of the tags allowed for methods */
	private static final Collection constructorTagList = Arrays.asList(constructorTags);

	/** A Map holding all the List's of tag strings indexed by the Class of the code item */
	private static final Map tagsAllowed = new HashMap();
	static  {
		tagsAllowed.put(tinyplanet.docwiz.Class.class, 		 classTagList);
		tagsAllowed.put(tinyplanet.docwiz.Interface.class, 	 interfaceTagList);
		tagsAllowed.put(tinyplanet.docwiz.Field.class, 		 fieldTagList);
		tagsAllowed.put(tinyplanet.docwiz.Method.class, 	 methodTagList);
		tagsAllowed.put(tinyplanet.docwiz.Constructor.class, constructorTagList);
	};

	protected JComboBox tagComboBox = new JComboBox();
	protected TableCellEditor tagTableCellEditor = new EditableComboBoxCellEditor(tagComboBox);

	protected DefaultTableCellRenderer tableCellRenderer = null;

	protected TableNavMenuBar plusMinusBar = null;

// Constructors:

	/**
	 * Default Constructor
	 */
	public UnknownTagPanelTable()
	{
		super("Unknown Tags", 50, "unknown");
		this.addPlusMinusBar();

		//Set up the editor for the tag column cells.
		tagComboBox.setEditable(true);
		tagComboBox.addItem("<nothing>");
		tagComboBox.setMaximumRowCount(8);
// Debug
//		tagComboBox.addItemListener(new ItemListener()  {
//				public void itemStateChanged(ItemEvent e)
//				{
//				    if (e.getStateChange() == ItemEvent.SELECTED) {
//						changeDispatcher.fireDebugMessage("ClassPanel:ItemListener:See:tagComboBox isSelected <" + e.getItem().toString() + ">");
//				    } else {
//						changeDispatcher.fireDebugMessage("ClassPanel:ItemListener:See:tagComboBox deSelected <" + e.getItem().toString() + ">");
//				    }
//				}
//
//			}
//		);
//		tagComboBox.addActionListener(new ActionListener()  {
//				public void actionPerformed(ActionEvent e)
//				{
//					changeDispatcher.fireDebugMessage("ClassPanel:ActionListener:See:tagComboBox action performed <" + e.getActionCommand().toString() + ">");
//				}
//			}
//		);
// Debug

//		tagTableCellEditor = new EditableComboBoxCellEditor(tagComboBox);

// Debug
//		tagTableCellEditor.addCellEditorListener(new CellEditorListener()  {
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
//    	tagComboBox.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter() {
//    	  	public void focusLost(FocusEvent e) {
//				changeDispatcher.fireDebugMessage("ClassPanel:FocusListener:See:tagComboBox:Editor:Component focus lost <" + e.getSource().toString() + ">");
//	      	}
//    	});
// Debug

//		TableColumn firstTableColumn = table.getColumnModel().getColumn(0);
//		firstTableColumn.setCellEditor(tagTableCellEditor);
//		firstTableColumn.setCellRenderer(getTableCellRenderer());
	}

	public UnknownTagPanelTable(CommentPanelModel commentPanelModel)
	{
		this();
		setModel(commentPanelModel);
	}

	public UnknownTagPanelTable(int cellEditorType, CommentPanelModel commentPanelModel)
	{
		this();
		setCellEditorType(cellEditorType);
		setModel(commentPanelModel);
	}

	public UnknownTagPanelTable(int cellEditorType)
	{
		this();
		setCellEditorType(cellEditorType);
	}

// Methods:

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
	 * Implement ChangeListener interface. This method tells me that my model changed
	 *  and I need to update my screen image.
	 *
	 * @param e The ChangeEvent object.
	 */
	public void stateChanged(ChangeEvent e)
	{
		CodeComment comment = getCurrentComment();
		CommentableCode theCode = (comment == null) ? null : comment.getCodeToComment();
	    Enumeration enum;

		if (comment != null) {
			TableModel tagModel = (TableModel)comment.getUnknownTagEntry();
			if (tagModel == null) {
				System.out.println("Null tagModel in " + this);
			}

			table.setModel(tagModel);

			setCellEditor(getCellEditorType());			// These are called after the model is set
			setTagCellEditor(getCellEditorType());		// .. since they use the table columns
														// .. which don't exist unless the model
														// .. says they do.

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

			// Set up the things in the parameter column combo box drop down list.

			comboBox.removeAllItems();

			Vector dropDownList = comment.getCompilationUnitTagList();

			if (dropDownList != null && dropDownList.size() > 0) {

				// NOTE: If you need this to run with JDK 1.1 you will need to replace this
				//  or comment it out.
				Collections.sort(dropDownList);

				// First put them all in a hash table to remove the duplicates. This
				//  really acts more like a Set.
				Hashtable hash = new Hashtable();
				for (enum = dropDownList.elements(); enum.hasMoreElements() ; ) {
					Object parm = enum.nextElement();
					if (parm != null) {
						hash.put(parm, "X");
					}
				}
				// Now put them in the list
				for (enum = hash.keys(); enum.hasMoreElements() ; ) {
					Object parm = enum.nextElement();
					if (parm != null) {
						comboBox.addItem(parm);
					}
				}
			}

			// Set up the things in the tag column combo box drop down list.

			tagComboBox.removeAllItems();

			dropDownList = comment.getCompilationUnitTags();
			if (theCode != null) {
				Collection theTags = (Collection)tagsAllowed.get(theCode.getClass());
				if (theTags != null) {
					for (Iterator iter = theTags.iterator() ; (iter.hasNext()) ; ) {
						maybeAdd(dropDownList, iter.next().toString());
					}
				}
			}
			maybeAdd(dropDownList, "serial");			// Add the serializable tags added in JDK 1.2
			maybeAdd(dropDownList, "serialData");
			maybeAdd(dropDownList, "serialField");

			if (dropDownList != null && dropDownList.size() > 0) {

				// NOTE: If you need this to run with JDK 1.1 you will need to replace this
				//  or comment it out.
				Collections.sort(dropDownList);

				// Now put them in the list
				for (enum = dropDownList.elements(); enum.hasMoreElements() ; ) {
					Object parm = enum.nextElement();
					if (parm != null) {
						tagComboBox.addItem(parm);
					}
				}
			}

		}

		return;
    }

	/**
	 * Add the string to the vector if it isn't already there.
	 *
	 * @since 06/26/2001 5:05:43 PM
	 * @param list A Vector to which the string should be added.
	 * @param str A String to add to the Vector if it isn't there yet.
	 */
	private void maybeAdd(Vector list, String str)
	{
		if (!list.contains(str)) {
			list.addElement(str);
		}
		return;
	}

	/**
	 * Change the cell editor for the tag column to the one specified.
	 *
	 * @since 05/21/2001 6:25:15 PM
	 * @param type should be one of 1) PLAIN_CELL_EDITOR or 2) COMBO_BOX_CELL_EDITOR.
	 */
	public void setTagCellEditor(int type)
	{
		if (table.getColumnCount() > 0) {
			TableColumn firstTableColumn = table.getColumnModel().getColumn(0);

			if (type == PLAIN_CELL_EDITOR) {
				firstTableColumn.setCellEditor(null);
				firstTableColumn.setCellRenderer(null);
			}
			else  {
				firstTableColumn.setCellEditor(tagTableCellEditor);
				firstTableColumn.setCellRenderer(getTableCellRenderer());
			}
		}

		return;
	}

// Get/Set Methods:

	/**
	 * Standard getter method retrieves current value of the attribute. if null,
	 *		sets it to the default value and then returns it.
	 *
	 * @return The current value of the attribute {@link tableCellRenderer }.
	 * @since  06/20/2001 7:06:43 PM
	 */
	public DefaultTableCellRenderer getTableCellRenderer()
	{
		if (this.tableCellRenderer == null) {
			setTableCellRenderer(new DefaultTableCellRenderer());
		}
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
	 * Standard getter method retrieves current value of the attribute. If null,
	 *		sets it to the default value and then returns it.
	 *
	 * @return The current value of the attribute {@link plusMinusBar }.
	 * @since  06/20/2001 7:09:01 PM
	 */
	public TableNavMenuBar getPlusMinusBar()
	{
		if (this.plusMinusBar == null) {
			plusMinusBar = new TableNavMenuBar() {

				protected void jBtnAdd_actionPerformed(ActionEvent e) {
					System.out.println("UnknownTagPanelTable.TableNavMenuBar.add: hit");
					if (table != null) {
						TableModel t = table.getModel();
						if (t instanceof UnknownTagModel) {
							UnknownTagModel tableModel = (UnknownTagModel)t;

						   	tableModel.addTag();
							enableDisableStuff(tableModel.getRowCount());
						    table.revalidate();
						}
			  		}
				}

			  	protected void jBtnDelete_actionPerformed(ActionEvent e) {
					System.out.println("UnknownTagPanelTable.TableNavMenuBar.delete: hit");
				    if (table != null) {
				      TableModel t = table.getModel();
				      if (t instanceof UnknownTagModel) {
				        UnknownTagModel tableModel = (UnknownTagModel) t;
				        int selectedRow = table.getSelectedRow();
				        if ( selectedRow  != -1 ) {
						  doneEditing(null);
				          tableModel.removeElementAt(selectedRow);
				        }
//						else  {
//							int rows = tableModel.getRowCount();
//							if (rows > 0) {
//								int lastCol = tableModel.getColumnCount();
//								if (lastCol > 0) {
//									lastCol--;
//								}
//								String val = tableModel.getValueAt(rows-1, lastCol).toString();
//								if (val.trim().length() == 0) {
//									tableModel.removeElementAt(rows-1);
//								}
//							}
//						}
						enableDisableStuff(tableModel.getRowCount());
				        table.revalidate();
				      }
				    }
			  	}
			};

		}
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
