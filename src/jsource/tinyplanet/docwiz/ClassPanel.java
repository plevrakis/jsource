/*
DocWiz: Easy Javadoc documentation
Copyright (C) 1998  Simon Arthur

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

//Title:        DocWiz
//Version:
//Copyright:    Copyright (c) 1997
//Author:       Simon Arthur
//Company:
//Description:  Easy Javadoc documentation

// History:
// 	4/6/2001 Lee Meador (LeeMeador@usa.net)
//				remove the focus lost listener stuff for the see and author fields.
// 4/12/2001	Lee Meador - Add drop-down combo box with all the other param comments to the
//			cell for entering the @param comments. Rearrange the layout. All the extra space
//			goes to the comment and the tables.
// 5/4/2001 Add code to make tabs move from place to place reasonably. (Still messed up if the
//				key focus goes to a table with zero rows in place.)
// 5/14/2001 Lee Meador - Add EditableComboBoxCellEditor to fix the bug where you type
//				some characters in the See table cell and then click another component and
//				the changes are ignored.
// 5/16/2001 Lee Meador - Create a base class for ClassPanel, FieldPanel and ExecutableElementPanel
//			It contains common methods and attributes. It would extend JPanel.

package tinyplanet.docwiz;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;

/** GUI panel which contains controls for manipulating the JavaDoc associated with a class or interface.
 *
 * @since
 * @author Simon Arthur
 * @version
 */
public class ClassPanel extends CommentPanel
{

// Attributes:

	protected CommentPanelTextField textFieldSince;
	protected CommentPanelTextField textFieldVersion;
	protected CommentPanelTable tableAuthor;
	protected ChangeDispatcher changeDispatcher = ChangeDispatcher.getChangeDispatcher();

// Constructors:

	/**
	 * Default constructor is all done in super class.
	 */
	public ClassPanel()
	{
		super();
	}

// Methods:

	/**
	 *  Construct all the things that go in this panel and
	 *		add them to this.
	 *
	 * @exception Exception If there is some error building it.
	 */
	protected void jbInit() throws Exception
	{
		textAreaBody = new CommentPanelTextArea("Description", "Enter the class description here", this);

		tableAuthor = new CommentPanelTable("Author", 50, "author", CommentPanelTable.PLAIN_CELL_EDITOR, this);
		tableAuthor.addPlusMinusBar();

		textFieldVersion = new CommentPanelTextField("Version", "version", this);

		textFieldSince = new CommentPanelTextField("Since", "since", this);

		tableSee = new CommentPanelTable("See", 50, "see", this);
		tableSee.addPlusMinusBar();

		textFieldDeprecated = new CommentPanelDeprecated(this);
		//----------------------------------------------------------------------------

		textAreaBody.setNextFocusableComponent(tableAuthor);
		tableAuthor.setNextFocusableComponent(textFieldVersion);
		textFieldVersion.setNextFocusableComponent(textFieldSince);
		textFieldSince.setNextFocusableComponent(tableSee);
		tableSee.setNextFocusableComponent(textFieldDeprecated);
		textFieldDeprecated.setNextFocusableComponent(textAreaBody);

		//----------------------------------------------------------------------------

		JSeparator separator1 = new JSeparator();
		separator1.setMaximumSize(new Dimension(9999, 13));
		JSeparator separator2 = new JSeparator();
		separator2.setMaximumSize(new Dimension(9999, 13));

		boxControls = Box.createVerticalBox();
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(textAreaBody, null);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(tableAuthor, null);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(textFieldVersion, null);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(separator1);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(textFieldSince, null);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(separator2);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(tableSee, null);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(textFieldDeprecated, null);
		boxControls.add(Box.createVerticalStrut(componentGap));

		//----------------------------------------------------------------------------

		scrollPaneControls.getViewport().add(boxControls, null);
		scrollPaneControls.setAutoscrolls(true);
		scrollPaneControls.setPreferredSize(new Dimension(300, 200));

		//----------------------------------------------------------------------------

		this.setLayout(borderLayoutControls);
		this.add(scrollPaneControls, BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(22, 100));
		this.setPreferredSize(new Dimension(300, 200));
	}

	/**
	 *  Force all the data in the table components to be written to their models
	 */
	protected void doneEditing()
	{
//// Debug
//		changeDispatcher.fireDebugMessage("ClassPanel:DoneEditing entry");
//// Debug
		if (tableSee != null) {
			tableSee.doneEditing(null);
			tableSee.clearSelection();
		}

		if (tableAuthor != null) {
			tableAuthor.doneEditing(null);
			tableAuthor.clearSelection();
		}
//// Debug
//		changeDispatcher.fireDebugMessage("ClassPanel:DoneEditing exit");
//// Debug
	}

}