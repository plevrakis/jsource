// Source file: com/tinyplanet/docwiz/CommentPanelType.java
// $Id: CommentPanelType.java,v 1.2 2001/08/15 16:43:29 lmeador Exp $
//
// $Log: CommentPanelType.java,v $
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
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

/** A class to put some JTextLabel objects in a component.
 *
 * @author Lee Meador
 * @since  05/18/2001 5:01:45 PM
 */
public class CommentPanelType extends CommentPanelBaseComponent
{

// Attributes:

	protected JLabel labelLabel;
	protected JLabel label;

// Constructors:

	/**
	 * Default Constructor
	 */
	public CommentPanelType()
	{
		this("");
	}

	/**
	 * Constructor
	 */
	public CommentPanelType(String title) {

		super(BoxLayout.X_AXIS);

		labelLabel = new CommentPanelLabel(title);

		label = new CommentPanelLabel("type", CommentPanelLabel.ALONE_ON_LINE);

		add(labelLabel);
		add(label);
		add(Box.createHorizontalGlue());
	}

	public CommentPanelType(String title, CommentPanelModel commentPanelModel)
	{
		this(title);
		setModel(commentPanelModel);
	}

// Methods:

	/**
	 * Set the title, which is the text for the label.
	 *
	 * @since 05/17/2001 5:49:34 PM
	 * @param title The new title to go into the text of the label.
	 */
	public void setTitle(String title)
	{
		labelLabel.setText(title);
		return;
	}

	/**
	 * Set the text of the 2nd JLabel.
	 *
	 * @since 05/17/2001 6:04:20 PM
	 * @param text The new text to put into the 2nd label.
	 */
	public void setText(String text)
	{
		label.setText(text);
		return;
	}

	/**
	 * Set whether the label is visible or not. if not, blank out the type.
	 *
	 * @since 05/18/2001 12:08:04 PM
	 * @param isChecked true to make it visible, false to make it invisible.
	 */
	public void setVisible(boolean isVisible)
	{
		labelLabel.setVisible(isVisible);
		if (!isVisible) {
			label.setText(" ");
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

	/**
	 * Implement ChangeListener interface. This method tells me that my model changed
	 *  and I need to update my screen image.
	 *
	 * @param e The ChangeEvent object.
	 */
	public void stateChanged(ChangeEvent e) {
		CodeComment comment = getCurrentComment();
		if (comment != null) {
			if (comment.getCodeToComment() instanceof Constructor) {
				setVisible(false);
			}
			else if (comment.getCodeToComment() instanceof Method) {
				setVisible(true);
				setText(((Method)(comment.getCodeToComment())).getType());
			}
			else if (comment.getCodeToComment() instanceof Field) {
				setVisible(true);
				setText(((Field)(comment.getCodeToComment())).getType());
			}
		}
    }

// Get/Set Methods:

}