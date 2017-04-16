// Source file: com/tinyplanet/docwiz/CommentPanelLabel.java
// $Id: CommentPanelLabel.java,v 1.1 2001/06/18 16:39:16 lmeador Exp $
//
// $Log: CommentPanelLabel.java,v $
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

/** A subclass of JLabel that configures things the way I want and
 *   has constructors to build the two major variants - with size set
 *	 to never expand X or Y - with size set to expand horizontally only.
 *
 * @author Lee Meador
 * @since  05/17/2001 1:51:39 PM
 */
public class CommentPanelLabel extends JLabel {

// Attributes:

	public static final int ALONE_ON_LINE = 1;
	public static final int NOT_ALONE_ON_LINE = 2;

	/**
	 * true if the size is set to let the horizonal dimension stretch.
	 * @since  05/17/2001 5:59:27 PM
	 */
	private boolean aloneOnLine = false;

// Constructors:

	/**
	 * Default Constructor just sets the colors, etc.
	 */
	public CommentPanelLabel()  {
		super();
		setBackground(SystemColor.control);
		setForeground(SystemColor.controlText);
		setAlignmentX((float) 0.5);
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	/**
	 * Constructor makes a label that has something after it on the line.
	 */
	public CommentPanelLabel(String text) {
		this();
		setText(text);
	}

	/**
	 * Constructor makes label that has something after it on the line or is alone. The
	 *		maximum height is always set to the height of the text. This is designed to
	 *		be used with <pre>BoxLayoutManager</pre> so the label doesn't expand too
	 *		big.
	 *
	 * @param type ALONE_ON_LINE - makes maximum width very large<br>
	 *		NOT_ALONE_ON_LINE - makes maximum width equal to the text size.
	 */
	public CommentPanelLabel(String text, int type)  {
		this();
		setAloneOnLine(type == ALONE_ON_LINE);
		setText(text);
	}

// Methods:

	/**
	 * Set the text for the label and change the sizes to match.
	 *
	 * @since 05/17/2001 5:52:17 PM
	 * @param text The new text for this label.
	 */
	public void setText(String text)
	{
		super.setText(" " + text);
		if (isAloneOnLine()) {
			setMinimumSize(new Dimension(90, getPreferredSize().height));
			setMaximumSize(new Dimension(9986, getPreferredSize().height));
		}
		else  {
	   		setMinimumSize(getPreferredSize());
   			setMaximumSize(getPreferredSize());
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
		buf	.append("," + this.getText());

		return buf.toString();
	}

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

	/**
	 * Non-standard getter method for boolean retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link aloneOnLine }.
	 * @since  05/17/2001 5:59:31 PM
	 */
	public boolean isAloneOnLine()
	{
		return getAloneOnLine();
	}

// Get/Set Methods:

	/**
	 * Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link aloneOnLine }.
	 * @since  05/17/2001 5:59:31 PM
	 */
	public boolean getAloneOnLine()
	{
		return this.aloneOnLine;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link aloneOnLine }.
	 * @since  05/17/2001 5:59:31 PM
	 */
	public void setAloneOnLine(boolean val)
	{
		this.aloneOnLine = val;
	}

}