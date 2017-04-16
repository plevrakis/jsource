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

// History
// 3/22/2001 Lee Meador (LeeMeador@usa.net) 1) Remove the focus listeners from the parameter
//			and exceptions lists since that (on my machine) causes you to be unable to
//			select any text within the cells and you can only type at the end. (java 1.3,
//			Windows 2000 sp1) 2) Also the focus listener was being set twice for the parameter
//			list. 3) Removed the calls to 'editingStopped' on the parameter and exception
//			lists at the end of 'setMethodControls()', which helps load a new set of data
//			into those lists. 4) Use the single selection model for the parm and exception
//			tables.
// 4/6/2001	Lee Meador - Add drop-down combo box with all the other param comments to the
//			cell for entering the @param comments.
// 4/11/2001 Lee Meador - Rearrange the layout. All the extra space goes to the comment and the tables.
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
//import borland.jbcl.layout.*;
import java.awt.event.*;
import javax.swing.border.*;


public class ExecutableElementPanel extends CommentPanel
{

// Attributes:

	protected CommentPanelTextField textFieldSince;
	protected CommentPanelReturnDescription textFieldReturnDescription;
	protected CommentPanelTable tableParameters;
	protected CommentPanelExceptionTable tableExceptions;

// Constructors:

	/**
	 * Default constructor is all done in super class.
	 */
	public ExecutableElementPanel()
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
		textAreaBody = new CommentPanelTextArea("Description", "Enter the description here", this);

		labelType = new CommentPanelType("Return Type: ", this);

		textFieldReturnDescription = new CommentPanelReturnDescription("Return Description", "return", this);

		tableParameters = new CommentPanelTable("Parameters", 100, "param", this);

		tableExceptions = new CommentPanelExceptionTable("Exceptions", 50, this);

		textFieldSince = new CommentPanelTextField("Since", "since", this);

		tableSee = new CommentPanelTable("See", 50, "see", this);
		tableSee.addPlusMinusBar();

		textFieldDeprecated = new CommentPanelDeprecated(this);

		//----------------------------------------------------------------------------

		textAreaBody.setNextFocusableComponent(textFieldReturnDescription);
		textFieldReturnDescription.setNextFocusableComponent(tableParameters);
		tableParameters.setNextFocusableComponent(tableExceptions);
		tableExceptions.setNextFocusableComponent(textFieldSince);
		textFieldSince.setNextFocusableComponent(tableSee);
		tableSee.setNextFocusableComponent(textFieldDeprecated);
		textFieldDeprecated.setNextFocusableComponent(textAreaBody);

		//----------------------------------------------------------------------------

		JSeparator separator1 = new JSeparator();
		separator1.setMaximumSize(new Dimension(9999, 13));
		JSeparator separator2 = new JSeparator();
		separator2.setMaximumSize(new Dimension(9999, 13));
		JSeparator separator3 = new JSeparator();
		separator3.setMaximumSize(new Dimension(9999, 13));

		boxControls = Box.createVerticalBox();
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(textAreaBody);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(labelType);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(separator1);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(textFieldReturnDescription);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(separator2);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(tableParameters);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(tableExceptions);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(textFieldSince);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(separator3);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(tableSee);
		boxControls.add(Box.createVerticalStrut(componentGap));
		boxControls.add(textFieldDeprecated);
		boxControls.add(Box.createVerticalStrut(componentGap));

		//----------------------------------------------------------------------------

		scrollPaneControls.getViewport().add(boxControls);
		scrollPaneControls.setAutoscrolls(true);
		scrollPaneControls.setPreferredSize(new Dimension(300, 200));

		//----------------------------------------------------------------------------

		this.setLayout(borderLayoutControls);
		this.add(scrollPaneControls, BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(22, 100));
		this.setPreferredSize(new Dimension(300, 200));

		return;
	}

	/**
	 * Force all the data in the components to be written to their models.
	 */
	public void doneEditing()
	{
		if (tableSee != null) {
			tableSee.doneEditing(null);
			tableSee.clearSelection();
		}

		if (tableExceptions != null) {
			tableExceptions.doneEditing(null);
			tableExceptions.clearSelection();
		}

		if (tableParameters != null) {
			tableParameters.doneEditing(null);
			tableParameters.clearSelection();
		}
	}

}