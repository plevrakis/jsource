// Source file: com/tinyplanet/docwiz/CommentPanelDeprecated.java
// $Id: CommentPanelDeprecated.java,v 1.2 2001/08/15 16:43:29 lmeador Exp $
//
// $Log: CommentPanelDeprecated.java,v $
// Revision 1.2  2001/08/15 16:43:29  lmeador
// Remove implements since base class already implements the same interface.
//
// Revision 1.1  2001/06/18 16:39:16  lmeador
// New swing-like components change the way the Field, Class
// and Executable panels work. This consolidates common code
// between the three panels and fixes some things related to clicking
// on one field while anothe field is being edited.
//
//

package tinyplanet.docwiz;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

/** A class to provide a single swing component containing a JCheckBox and a JTextField
 *		lined up in a Box such that the text field expands horizontally but neither one
 *		changes height.
 *
 * @author Lee Meador
 * @since  05/18/2001 11:41:11 AM
 */
public class CommentPanelDeprecated extends CommentPanelBaseComponent
{

// Attributes:

	protected JCheckBox checkBox = new JCheckBox();
	protected JTextField textField = new JTextField();
	protected Document document;
	protected Component glue = Box.createHorizontalGlue();

// Constructors:

	/**
	 * Default Constructor
	 */
	public CommentPanelDeprecated(CommentPanelModel commentPanelModel)
	{
		this();
		setModel(commentPanelModel);
	}

	public CommentPanelDeprecated()
	{
		super(BoxLayout.X_AXIS);			// like Box.createHorizontalBox()

		setTitle("Deprecated");
		checkBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkBox_actionPerformed(e);
			}
		});
		checkBox.setNextFocusableComponent(textField);

		setText("");
		textField.setMaximumSize(new Dimension(9999, textField.getPreferredSize().height));

		add(Box.createRigidArea(new Dimension(componentGap/2, 1)));
		add(checkBox, null);
		add(Box.createRigidArea(new Dimension(componentGap, 1)));
		add(textField, null);

		document = textField.getDocument();
		document.addDocumentListener(new javax.swing.event.DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                document_changedUpdate(e);
            }

            public void insertUpdate(DocumentEvent e) {
                document_changedUpdate(e);
            }

            public void removeUpdate(DocumentEvent e) {
                document_changedUpdate(e);
            }
    	});
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
		checkBox.setText(" " + title);
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
		textField.setText(text);
		return;
	}

	/**
	 * Set the next focusable component for the internal JTextField.
	 *
	 * @param component The new, next component in the focus chain.
	 */
	public void setNextFocusableComponent(Component component)
	{
		textField.setNextFocusableComponent(component);
	}

	/**
	 * Identify whether the checkbox is checked or not.
	 *
	 * @since 05/18/2001 11:57:47 AM
	 * @return true if the check box is checked, false if it is not checked.
	 */
	public boolean isSelected()
	{
		return checkBox.isSelected();
	}

	/**
	 * Set whether the check box is selected (checked) or not.
	 *
	 * @since 05/18/2001 12:08:04 PM
	 * @param isChecked true to make it selected, false to make it unselected
	 */
	public void setSelected(boolean isChecked)
	{
		checkBox.setSelected(isChecked);
		return;
	}

	/**
	 * Set the text field to be enabled or not.
	 *
	 * @since 05/18/2001 12:00:25 PM
	 * @param isEnabled true if the text field should be enabled,
	 *		false otherwise.
	 * @return
	 */
	public void setEnabled(boolean isEnabled)
	{
		textField.setEnabled(isEnabled);
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

	protected void document_changedUpdate(DocumentEvent e)
	{
		if (getCurrentComment() != null) {
			getCurrentComment().updateExistentialTag(e, "deprecated");
		}
	}

	protected void checkBox_actionPerformed(ActionEvent e)
	{
		boolean isChecked = isSelected();
		setEnabled(isChecked);

		CodeComment comment = getCurrentComment();
		if (comment != null) {
			if (isChecked) {
				showTextField();
				comment.updateTag(textField.getText(), "deprecated");
			}
			else {
				// clear out any text in the deprecated description if it isn't enabled
				setText("");
				hideTextField();
				// user has chosen to remove deprecated tag
				comment.clearTagEntries("deprecated");
			}
		}
	}

	/**
	 * Implement ChangeListener interface. This method tells me that my model changed
	 *  and I need to update my screen image.
	 *
	 * @param e The ChangeEvent object.
	 */
	public void stateChanged(ChangeEvent e) {
		CodeComment comment = getCurrentComment();
		if (comment != null) {

			DefaultTagModel theDeprecated = (DefaultTagModel)comment.getTagEntry("deprecated", false);
			if (theDeprecated != null) {
				setSelected(true);
				showTextField();
				setText(theDeprecated.tagAt(0));
			}
			else {
				setSelected(false);
				setText("");
				hideTextField();
			}
		}
    }

	/**
	 * Show the text field.
	 */
	protected void showTextField()
	{
		remove(glue);
//		add(textField);
		setEnabled(true);
		textField.setVisible(true);
	}

	/**
	 * Don't show the text field but make the check move to the left.
	 */
	protected void hideTextField()
	{
		setEnabled(false);
		textField.setVisible(false);
//		remove(textField);
		add(glue);
	}

// Get/Set Methods:

}