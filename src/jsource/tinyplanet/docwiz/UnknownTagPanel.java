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


public class UnknownTagPanel extends CommentPanel
{

// Attributes:

	protected UnknownTagPanelTable table;

// Constructors:

	/**
	 * Default constructor is all done in super class.
	 */
	public UnknownTagPanel()
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
	  	table = new UnknownTagPanelTable(this);

		//----------------------------------------------------------------------------

		scrollPaneControls.getViewport().add(table);
		scrollPaneControls.setAutoscrolls(true);
		scrollPaneControls.setPreferredSize(new Dimension(300, 100));

		//----------------------------------------------------------------------------

		this.setLayout(borderLayoutControls);
		this.add(Box.createVerticalStrut(5)	, BorderLayout.NORTH);
		this.add(scrollPaneControls, BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(22, 100));
		this.setPreferredSize(new Dimension(300, 100));

		return;
	}

	/**
	 * Force all the data in the components to be written to their models.
	 */
	public void doneEditing()
	{
		if (table != null) {
			table.doneEditing(null);
			table.clearSelection();
		}
	}

}
