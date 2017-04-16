// Source file: com/tinyplanet/docwiz/CommentPanelTextField.java
// $Id: CommentPanelTextField.java,v 1.2 2001/08/15 16:43:29 lmeador Exp $
//
// $Log: CommentPanelTextField.java,v $
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

/** A class to implement a component containing a JLabel and a JTextField in one unit.
 *
 * @author Lee Meador
 * @since  05/19/2001 1:23:12 PM
 */
public class CommentPanelTextField extends CommentPanelBaseComponent
{

// Attributes:

	protected JLabel label;
	protected JTextField textField = new JTextField();
	protected Document document;

	protected String tagName;

// Constructors:

	/**
	 * Default Constructor
	 */
	public CommentPanelTextField()
	{
		this("", null);
	}

	public CommentPanelTextField(String title, String tagName)
	{
		super(BoxLayout.X_AXIS);			// like Box.createHorizontalBox()

		label = new CommentPanelLabel(title);

		textField.setText("fill in");
		textField.setMaximumSize(new Dimension(9999, textField.getPreferredSize().height));

		add(label, null);
		add(Box.createRigidArea(new Dimension(componentGap, 1)));
		add(textField, null);

		setTagName(tagName);

		document = textField.getDocument();
		document.addDocumentListener(new javax.swing.event.DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
			   document_changedUpdate(e);
			}

			public void removeUpdate(DocumentEvent e) {
			   document_changedUpdate(e);
			}

			public void changedUpdate(DocumentEvent e) {
			   document_changedUpdate(e);
			}
		});

	}

	public CommentPanelTextField(String title, String tagName, CommentPanelModel commentPanelModel)
	{
		this(title, tagName);
		setModel(commentPanelModel);
	}

	/**
	 * Set the title, which is the text for the JLabel.
	 *
	 * @since 05/17/2001 5:49:34 PM
	 * @param title The new title to go into the text of the JLabel.
	 */
	public void setTitle(String title)
	{
		label.setText(" " + title);
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
	 * Set whether the label is visible or not. if not, blank out the type.
	 *
	 * @since 05/18/2001 12:08:04 PM
	 * @param isChecked true to make it visible, false to make it invisible.
	 */
	public void setVisible(boolean isVisible)
	{
		label.setVisible(isVisible);
		textField.setVisible(isVisible);
		if (!isVisible) {
			textField.setText(" ");
		}
		return;
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

	/** When the 'since' tag changes, update the model for that tag
	 */
	private void document_changedUpdate(DocumentEvent e)
	{
		CodeComment codeComment = getCurrentComment();
		String tagName = getTagName();
		if (codeComment != null && tagName != null) {
			codeComment.updateTag(e, tagName);
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
		String tagName = getTagName();
		if (comment != null && tagName != null) {

			DefaultTagModel theTagModel = (DefaultTagModel)comment.getTagEntry(tagName, false);
			if (theTagModel != null) {
				setText(theTagModel.tagAt(0));
			}
			else {
				setText("");
			}
		}
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

}