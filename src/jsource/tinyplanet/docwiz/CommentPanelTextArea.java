// Source file: com/tinyplanet/docwiz/CommentPanelTextArea.java
// $Id: CommentPanelTextArea.java,v 1.2 2001/08/15 16:43:29 lmeador Exp $
//
// $Log: CommentPanelTextArea.java,v $
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

/** A class to provide a single swing component to hold a JLabel, a JTextArea
 *	 in a JScrollPane all lined up in a Box such that the JTextArea expands
 *	 vertically and horizontally as the enclosing pane gets larger but the
 *	 other portions do not.
 *
 * @author Lee Meador
 * @since  05/17/2001 5:41:26 PM
 */
public class CommentPanelTextArea extends CommentPanelBaseComponent
{

// Attributes:

	protected JLabel label;
	protected JTextArea textArea = new JTextArea();
	protected JScrollPane scrollPane = new JScrollPane();

// Constructors:

	/**
	 * Default Constructor
	 */
	public CommentPanelTextArea()
	{
		this("", null);
	}

	/**
	 * Default Constructor
	 */
	public CommentPanelTextArea(String title)
	{
		this(title, null);
	}

	/**
	 * Constructor sets up everything.
	 *
	 * @param title The title for this component. It appears above and to the left of
	 *		the text area.
	 * @param msg The tool tip that appear when the cursor hovers over the text area.
	 * @param theCurrentComment This CodeComment object will be modified as the user
	 *		moves the cursor around. The 'title' is changed and the file is marked
	 *		modified.
	 */
	public CommentPanelTextArea(String title, String msg)
	{
		super(BoxLayout.Y_AXIS);

		label = new CommentPanelLabel(title, CommentPanelLabel.ALONE_ON_LINE);

		setText("fill in text");
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setColumns(60);
		textArea.setFont(new Font("Courier", 0, 12));
		setToolTipText(msg);

		textArea.addCaretListener(new javax.swing.event.CaretListener() {
			public void caretUpdate(CaretEvent e) {
				textCaretUpdate(e);
			}
		});
//		textArea.setMinimumSize(new Dimension(100, 50));
//		textArea.setMinimumSize(new Dimension(100, 75));
//		textArea.setPreferredSize(new Dimension(200, 100));

		scrollPane.getViewport().add(textArea);
		scrollPane.setAutoscrolls(true);
		scrollPane.setMinimumSize(new Dimension(100, 50));
		scrollPane.setPreferredSize(new Dimension(450, 100));
		scrollPane.setMaximumSize(new Dimension(3000, 100*30));

		add(label, null);
		add(scrollPane, null);

		return;
	}

	public CommentPanelTextArea(String title, String msg, CommentPanelModel commentPanelModel)
	{
		this(title, msg);
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
		label.setText(title);
		return;
	}

	/**
	 * Set the text of the JTextArea.
	 *
	 * @since 05/17/2001 6:04:20 PM
	 * @param text The new text to put into the text area.
	 */
	public void setText(String text)
	{
		textArea.setText(text);
		return;
	}

	/**
	 * Set the tool tip message for when the cursor hovers over the text area.
	 *
	 * @since 05/17/2001 6:24:50 PM
	 * @param msg The new tool tip message.
	 */
	public void setToolTipText(String msg)
	{
		textArea.setToolTipText(msg);
		return;
	}

	/**
	 * Set the next focusable component for the internal JTextArea.
	 *
	 * @param component The new, next component in the focus chain.
	 */
	public void setNextFocusableComponent(Component component)
	{
		textArea.setNextFocusableComponent(component);
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
	 * This method is called when the JTextArea object has its cursor moved. It updates the
	 *  the text in my model if the text changed.
	 *
	 * @param e The CaretEvent that caused this to be called.
	 */
	protected void textCaretUpdate(javax.swing.event.CaretEvent e)
	{
		JTextComponent t = (JTextComponent)e.getSource();
		String theText = t.getText();
		CodeComment comment = getCurrentComment();
		if (comment != null &&
				!theText.equals(comment.getTitle())
				) {
			comment.setTitle(theText);
			comment.getCompilationUnit().touchFile();
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
			String theText = comment.getTitle();
			setText(theText);
		}
    }

// Get/Set Methods:

}