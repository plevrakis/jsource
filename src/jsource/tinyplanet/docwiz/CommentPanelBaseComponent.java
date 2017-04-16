// Source file: com/tinyplanet/docwiz/CommentPanelBaseComponent.java
// $Id: CommentPanelBaseComponent.java,v 1.1 2001/06/18 16:39:16 lmeador Exp $
//
// $Log: CommentPanelBaseComponent.java,v $
// Revision 1.1  2001/06/18 16:39:16  lmeador
// New swing-like components change the way the Field, Class
// and Executable panels work. This consolidates common code
// between the three panels and fixes some things related to clicking
// on one field while anothe field is being edited.
//
//

package tinyplanet.docwiz;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/** Abstract class to form a base for various classes that hold a few components
 *		for placement on the comment panels.
 *
 * @author Lee Meador
 * @since  05/18/2001 3:16:25 PM
 */
public abstract class CommentPanelBaseComponent extends Box implements ChangeListener
{

// Attributes:

	protected static final int componentGap = CommentPanel.componentGap;

	/**
	 * The model where updates to the text area are stored.
	 * @since  05/17/2001 7:16:49 PM
	 */
	protected CommentPanelModel commentPanelModel;

// Constructors:

	/**
	 * Same Constructor as Box class. No Default Constructor.
	 *
	 * @param axis Indicates box direction either as BoxLayout.X_AXIS or BoxLayout.Y_AXIS.
	 */
	public CommentPanelBaseComponent(int axis)
	{
		super(axis);
	}

	/**
	 * Create one with the model in place.
	 *
	 * @param axis Indicates box direction either as BoxLayout.X_AXIS or BoxLayout.Y_AXIS.
	 * @param commentPanelModel The CommentPanelModel object that will be used to update
	 *		my fields and to which I will be a registered ChangeListener.
	 */
	public CommentPanelBaseComponent(int axis, CommentPanelModel commentPanelModel)
	{
		this(axis);
		setModel(commentPanelModel);
	}

// Methods:

	/**
	 * Set the text of the label for this component.
	 *
	 * @since 05/17/2001 6:04:20 PM
	 * @param text The new text to put in the label.
	 */
	abstract public void setTitle(String title);

	/**
	 * Set the text of the component holding the information. This would
	 *		be the JTextArea, JTextField, a JLable, or some such.
	 *
	 * @since 05/17/2001 6:04:20 PM
	 * @param text The new text to put in.
	 */
	abstract public void setText(String text);

	/**
	 * Called when the comment changes. This would be when the user
	 *		clicks on a different Method, Field, etc. Implements the
	 *		ChangeListener interface.
	 *
	 * @since 05/17/2001 6:04:20 PM
	 * @param e The ChangeEvent object with information about the change.
	 */
	abstract public void stateChanged(ChangeEvent e);

	/**
	 * Send debug message to std out
	 *
	 * @since 05/17/2001 6:49:41 PM
	 * @param msg The debug message to print.
	 */
	protected void debug(String msg)
	{
		System.out.println(this.getClass().getName() + ": " + msg + "\n");
		return;
	}

// Get/Set Methods:

	/**
	 * Retreive the current JavaDoc comment associated with the panel.
	 *
	 * @return The current JavaDoc stored in the CodeComment object.
	 */
	public CodeComment getCurrentComment()
	{
		return getModel().getCurrentComment();
	}

	/**
	 * getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link commentPanelModel }.
	 * @since  05/17/2001 7:17:06 PM
	 */
	public CommentPanelModel getModel()
	{
		return this.commentPanelModel;
	}

	/**
	 * setter method changes the current value of the attribute. Calling with <pre>null</pre>
	 * 		as the parameter will remove this as a ChangeListener.
	 *
	 * @param val The new value of the attribute {@link commentPanelModel }.
	 * @since  05/17/2001 7:17:06 PM
	 */
	public void setModel(CommentPanelModel val)
	{
		if (this.commentPanelModel != null) {
			commentPanelModel.removeChangeListener(this);
		}

		this.commentPanelModel = val;

		if (this.commentPanelModel != null) {
			commentPanelModel.addChangeListener(this);
		}
	}

}