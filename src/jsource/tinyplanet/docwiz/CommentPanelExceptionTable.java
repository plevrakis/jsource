// Source file: com/tinyplanet/docwiz/CommentPanelExceptionTable.java
// $Id: CommentPanelExceptionTable.java,v 1.2 2001/08/15 16:43:29 lmeador Exp $
//
// $Log: CommentPanelExceptionTable.java,v $
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
import javax.swing.*;
import javax.swing.event.*;


/** A class to hold a table etc for exceptions
 *
 * @author Lee Meador
 * @since  05/21/2001 10:24:43 AM
 */
public class CommentPanelExceptionTable extends CommentPanelTable {

// Attributes:

// Constructors:

	/**
	 * Default Constructor
	 */
	public CommentPanelExceptionTable()
	{
		super();
	}

	public CommentPanelExceptionTable(CommentPanelModel commentPanelModel)
	{
		super(commentPanelModel);
	}

	public CommentPanelExceptionTable(String title, int preferredHeight, CommentPanelModel commentPanelModel)
	{
		super(title, preferredHeight, "exception", commentPanelModel);
	}

	public CommentPanelExceptionTable(String title, int preferredHeight)
	{
		super(title, preferredHeight, "exception");
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
	 *  	and I need to update my screen image. In this case I just move any entries from
	 *		the 'throws' param to the 'exception' param	and let the super class do the
	 *		work.
	 *
	 * @param e The ChangeEvent object.
	 */
	public void stateChanged(ChangeEvent e)
	{
		CodeComment comment = getCurrentComment();
		if (comment != null) {
			ExceptionTagModel tagModel = (ExceptionTagModel)comment.getTagEntry(getTagName());

			// Merge tags from "throws" synonym
			DefaultTagModel tmSynonym = (DefaultTagModel)comment.getTagEntry("throws", false);
			if (tmSynonym != null) {
				for (int i = 0 ; i < tmSynonym.size(); i++) {
					String throwsValue = tmSynonym.tagAt(i).toString();
					//getValueAt(i,tmSynonym.DESCRIPTIONCOLUMN).toString();
					tagModel.addValue(throwsValue);
				}
				comment.clearTagEntries("throws");
			}
		}

		super.stateChanged(e);

		return;
    }

// Get/Set Methods:

}