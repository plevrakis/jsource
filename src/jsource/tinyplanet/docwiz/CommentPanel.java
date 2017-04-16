// Source file: com/springbow/notification/CommentPanel.java
// $Id: CommentPanel.java,v 1.1 2001/06/18 16:39:16 lmeador Exp $
//
// $Log: CommentPanel.java,v $
// Revision 1.1  2001/06/18 16:39:16  lmeador
// New swing-like components change the way the Field, Class
// and Executable panels work. This consolidates common code
// between the three panels and fixes some things related to clicking
// on one field while anothe field is being edited.
//
//

// History
// 5/15/2001 Lee Meador Begin with new base class. Copy things into it from
//				ClassPanel, FieldPanel and ExecutableElementPanel.

package tinyplanet.docwiz;

import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * CommentPanel - A class to be the base class for the various panels that
 * allow editing the field/attribute, method/constructor or class comments.
 *
 * @since 05/15/2001 2:47:26 PM
 * @author Lee Meador
 * @author Simon Arthur
 */
public abstract class CommentPanel extends JPanel implements CommentPanelModel
{

// Attributes:

	/**
	 * The size in pixels between components that fall too close together when
	 * no spacing is added. This may be used to space horizontally or vertically.
	 */
	public static final int componentGap = 10;

	/**
	 * The JavaDoc code comment that this panel modifies. This would contain the information
	 * from the JavaDoc comment preceeding one Class, Field, Method or Constructor.
	 */
	protected CodeComment currentComment;

	/**
	 * Hold the list of listeners for when ChangeEvent to occur. When that event occurs
	 * a call should be made to {@link fireChange} and all Listeners will be notified.
	 */
	protected EventListenerList changeListeners = new EventListenerList();

	/**
	 * The Component holding the JLabel for the title and the JTextArea for the main JavaDoc comment.
	 */
	protected CommentPanelTextArea textAreaBody;
	/**
	 * The Component holding the JLabel, two JButton objects, and a JTable to hold the list of
	 * "see also" links.
	 */
	protected CommentPanelTable tableSee;
	/**
	 * The Component holding a JCheckBox and a JTextField to hold the deprecated
	 * information.
	 */
	protected CommentPanelDeprecated textFieldDeprecated;
	/**
	 * The Component holding two JLabel objects lined up to show the
	 * return type of a Method or the type of a Field.
	 */
	protected CommentPanelType labelType;

	/**
	 * The Box object holding all the controls for the whole panel.
	 */
	protected Box boxControls;
	/**
	 * The JScrollPane object holding the Box containing all the controls for this panel.
	 */
	protected JScrollPane scrollPaneControls = new JScrollPane();
	/**
	 * The BorderLayout object for the entire panel containing the scroll pane, ...
	 */
	protected BorderLayout borderLayoutControls = new BorderLayout();

// Constructors:

	/**
	 * Default Constructor sets things up, builds the panel and sets up any controls.
	 */
	public CommentPanel() {
		init();
		try {
			jbInit();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		setDocumentControls();
	}

// Methods:

	/**
	 * Give initial values to any attributes declared in
	 * this class. This facilitates the constructor being
	 * mainly in the super class. It is called first in the
	 * Constructor.
	 */
	protected void init()
	{
	}

	/**
	 * Construct all the things that go in this panel and
	 * add them to this.
	 *
	 * @exception Exception If there is some error building it.
	 */
	abstract protected void jbInit() throws Exception;

	/**
	 * Set up additional Listeners on the Document objects inside the
	 * JTextField and JTextArea components to handle updating
	 * the String attributes in the CodeComment object we are
	 * editing.
	 */
	protected void setDocumentControls()
	{
	}

	/**
	 * Force all the data in the table components to be written to their models.
	 */
	abstract protected void doneEditing();

	/**
	 * Set up the controls for the newly loaded comment object
	 *  which is supplied as the parameter.
	 *
	 * @since 05/19/2001 8:31:03 PM
	 * @param c The comment object to populate the panel.
	 */
	protected void setControls(CodeComment c)
	{
	}

	/**
	 * Implements CommentPanelModel interface to notify listeners.
	 *
	 * @param x The ChangeListener object that wishes to be notified when this
	 *     object's CodeComment changes
	 * @see CommentPanelModel#addChangeListener
	 * @see currentComment
	 */
	public void addChangeListener(ChangeListener x) {
		changeListeners.add (ChangeListener.class, x);

		// bring it up to date with current state
		x.stateChanged(new ChangeEvent(this));
	}

	/**
	 * Implements CommentPanelModel interface to quit notifying Listeners.
	 *
	 * @param x The ChangeListener object that no longer wishes to be notified
	 *     when this object's CodeComment changes
	 * @see CommentPanelModel#removeChangeListener
	 * @see currentComment
	 */
	public void removeChangeListener(ChangeListener x) {
		changeListeners.remove (ChangeListener.class, x);
	}

	/**
	 * Implements CommentPanelModel interface to issue the notifications.
	 *
	 * @see CommentPanelModel#fireChange
	 */
	protected void fireChange() {
		// Create the event:
		ChangeEvent c = new ChangeEvent(this);
		// Get the listener list
		Object[] listeners =
				changeListeners.getListenerList();
		// Process the listeners last to first
		// List is in pairs, Class and instance
		for (int i
				= listeners.length-2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				ChangeListener cl =
						(ChangeListener)listeners[i+1];
				cl.stateChanged(c);
			}
	    }
	}

// Event stuff:

// Get/Set Methods:

	/**
	 * Retreive the current JavaDoc comment associated with the panel.
	 * Implement CommentPanelModel interface.
	 *
	 * @return The entire current JavaDoc stored in this panel's CodeComment
	 *     object.
	 * @see CommentPanelModel#addChangeListener
	 */
	public CodeComment getCurrentComment()
	{
		return this.currentComment;
	}

	/**
	 * Change the Javadoc comment associated with the panel
	 *
	 * @param c The new CodeComment object holding the JavaDoc comment.
	 */
	public void setCurrentComment(CodeComment c)
	{
		doneEditing();
		setControls(c);
		this.currentComment = c;
		fireChange();
	}

}
