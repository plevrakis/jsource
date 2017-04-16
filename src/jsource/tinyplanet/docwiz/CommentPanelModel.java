// Source file: com/tinyplanet/docwiz/CommentPanelModel.java
// $Id: CommentPanelModel.java,v 1.1 2001/06/18 16:39:16 lmeador Exp $
//
// $Log: CommentPanelModel.java,v $
// Revision 1.1  2001/06/18 16:39:16  lmeador
// New swing-like components change the way the Field, Class
// and Executable panels work. This consolidates common code
// between the three panels and fixes some things related to clicking
// on one field while anothe field is being edited.
//
//

package tinyplanet.docwiz;

import java.util.*;
import javax.swing.event.*;

/** An interface for communicating the CodeComment object associated with this panel.
 *
 * @author Lee Meador
 * @since  05/17/2001 7:12:07 PM
 */
public interface CommentPanelModel
{

// Attributes:

// Methods:

	/**
	 * Must have a method to let me get the currently associated CodeComment object.
	 */
	public CodeComment getCurrentComment();

	public void addChangeListener(ChangeListener x);
	public void removeChangeListener(ChangeListener x);

}