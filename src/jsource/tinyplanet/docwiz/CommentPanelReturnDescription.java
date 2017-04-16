// Source file: com/tinyplanet/docwiz/CommentPanelReturnDescription.java
// $Id: CommentPanelReturnDescription.java,v 1.2 2001/08/15 16:43:29 lmeador Exp $
//
// $Log: CommentPanelReturnDescription.java,v $
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
 *		This is specific to the return description because of the way the state change
 *		is done.
 *
 * @author Lee Meador
 * @since  05/19/2001 1:23:12 PM
 */
public class CommentPanelReturnDescription extends CommentPanelTextField
{

// Attributes:

// Constructors:

	/**
	 * Default Constructor
	 */
	public CommentPanelReturnDescription()
	{
		this("", null);
	}

	public CommentPanelReturnDescription(String title, String tagName)
	{
		super(title, tagName);
	}

	public CommentPanelReturnDescription(String title, String tagName, CommentPanelModel commentPanelModel)
	{
		super(title, tagName, commentPanelModel);
	}

// Methods:

// Event things:

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
				if ("void".equals(((Method)(comment.getCodeToComment())).getType())) {
					setVisible(false);
				}
				else {
					String tagName = getTagName();
					if (tagName != null) {
						setVisible(true);
						DefaultTagModel theTagModel = (DefaultTagModel)comment.getTagEntry(tagName, false);
						if (theTagModel != null) {
							setText(theTagModel.tagAt(0));
						}
						else {
							setText("");
						}
					}
				}

			}
		}
    }

// Get/Set Methods:

}
