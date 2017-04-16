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

// History
// 	5/2/2001 Lee Meador This is a new pane to show events while figuring
//			out why things don't get updated and so forth when selecting
//			stuff but a table cell is being edited. It uses the debug
//			message listener stuff to get messages from here and there
//			without too much effort.
//	6/26/2001 lmm	- simplify code

package tinyplanet.docwiz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import tinyplanet.gui.ConcreteAppPane;

/**
 * A pane to show debug events
 *
 * @author Lee Meador
 * @since 5/21/2001
 */
public class EventAppPane extends ConcreteAppPane
			implements ItemChangedListener, DebugMessageListener
{
	private JTextArea textArea1 = new JTextArea();
  	private GridLayout lm = new GridLayout(1,1);
	private JScrollPane jScrollPane1 = new JScrollPane();

	public EventAppPane()
	{
		this.setLayout(lm);
		this.add(jScrollPane1);

		textArea1.setEditable(false);
		jScrollPane1.getViewport().add(textArea1);

		this.setTitle("Events");
	}


	public void itemChanged(ItemChangedEvent ie)
	{
  		CommentableCode commentableCode = ie.getCurrentCode();
		String codeName = "";
		if (commentableCode != null) {
			codeName = commentableCode.toString();		// was getName()
		}
  		CompilationUnit compilationUnit = ie.getCompilationUnit();
		String fileName = "";
		if (compilationUnit != null) {
			fileName = compilationUnit.getFileName();
		}
		textArea1.append("Item changed event, " + codeName + ", " + fileName + "\n");
	}

  	private int debugMessageCount = 1;
	public void debugMessage(DebugMessageEvent debugMessageEvent)
	{

		String message = debugMessageEvent.getMessage();
		textArea1.append(debugMessageCount++ + ": " + message + "\n");
	}

	public void setDisplayText(String s)
	{
		textArea1.setText(s);
	}

}
