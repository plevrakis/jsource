// Source file: com/tinyplanet/docwiz/EdittableComboBoxCellEditor.java
// $Id: EditableComboBoxCellEditor.java,v 1.1 2001/06/18 17:45:12 lmeador Exp $
//
// $Log: EditableComboBoxCellEditor.java,v $
// Revision 1.1  2001/06/18 17:45:12  lmeador
// Used with drop down combo boxes attached as the editor for JTable
// cells. This fixes a bug in the Swing library which only shows if the
// combo box is editable.
//
//

package tinyplanet.docwiz;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/**
 * EdittableComboBoxCellEditor - A class to fix a bug in DefaultCellEditor when used
 *   with an editable JComboBox. In the JDK 1.3 version if you type some changes into
 *   the editable combo box and then click on another row (cell?) the changes you typed
 *	 will be lost.
 *
 * @author Lee Meador
 * @since  05/03/2001 4:24:16 PM
 */
public class EditableComboBoxCellEditor extends DefaultCellEditor
{

// Attributes:

// Constructors:

    /**
     * Constructs a subclass of a DefaultCellEditor object that uses a combo box
	 *  that is set editable.
     *
     * @param x  a JComboBox object ...
     */
    public EditableComboBoxCellEditor(final JComboBox comboBox) {
		super(comboBox);
		comboBox.removeActionListener(delegate);

		delegate = new EditorDelegate() {

//			ChangeDispatcher changeDispatcher = ChangeDispatcher.getChangeDispatcher();

			public void setValue(Object value) {
				comboBox.setSelectedItem(value);
			}

			public Object getCellEditorValue() {
//				changeDispatcher.fireDebugMessage("EditabelcomboBoxCellEditor:EditorDelegate:getCellEditorValue");
				if (comboBox.isEditable()) {				// This is the changed part
					return comboBox.getEditor().getItem();	// This is the changed part
				}											// This is the changed part
				else  { 									// This is the changed part
					return comboBox.getSelectedItem();
				}											// This is the changed part
			}

			public boolean shouldSelectCell(EventObject anEvent) {
				if (anEvent instanceof MouseEvent) {
					MouseEvent e = (MouseEvent)anEvent;
					return e.getID() != MouseEvent.MOUSE_DRAGGED;
				}
				return true;
			}
		};

		comboBox.addActionListener(delegate);
		comboBox.setEditable(true);
	}

// Get/Set Methods:

}