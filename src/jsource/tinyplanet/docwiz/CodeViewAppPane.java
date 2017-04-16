/*
DocWiz: Easy Javadoc documentation
Copyright (C) 2000 Simon Arthur

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

// History:
//	6/20/2001 lmm	- Set the tab size in the display pane to match the users configuration.
//	6/26/2001 lmm	- change to courier and simplify building page
//	7/25/2001 lmm	- call HighlightCode class to color code. (NOTE: Tabs are messed up.)

package tinyplanet.docwiz;

import tinyplanet.gui.ConcreteAppPane;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/** AppPane for displaying the source code to be commented.
 *
 * @author Simon Arthur
 * @version $Id: CodeViewAppPane.java,v 1.5 2001/07/25 22:59:13 lmeador Exp $
 */
public class CodeViewAppPane extends ConcreteAppPane  implements ItemChangedListener {
  /** Scroll pane for scrolling the viewer
   */
  JScrollPane jScrlPnCodeView = new JScrollPane();
  /** Text area which displays the code.
   */
//  JTextArea jTxtArCodeViewer = new JTextArea();
  JEditorPane jTxtArCodeViewer = new JEditorPane();

  /** Layout of the viewer, one single large box.
   */
  GridLayout lm = new GridLayout(1,1);

  /** Build the GUI, set up the AppPane
   */
  public CodeViewAppPane() {

//    ConfigurationService configurationService = ConfigurationService.getConfigurationService();

    this.setLayout(lm);
    this.add(jScrlPnCodeView);

    jTxtArCodeViewer.setEditable(false);
    jTxtArCodeViewer.setAutoscrolls(false);
    jTxtArCodeViewer.setText("");
	jTxtArCodeViewer.setFont(new Font("Courier", 0, 12));
//	jTxtArCodeViewer.setTabSize(configurationService.getTabSize());

    jScrlPnCodeView.getViewport().add(jTxtArCodeViewer);
    this.setTitle("Current Code");
  }

  /** When the user selected item changes, change the display.
   *
   * @param ie ItemChangedEvent with information about the change.
   */
  public void itemChanged(ItemChangedEvent ie)
  {
    CommentableCode currentCode = ie.getCurrentCode();
    String declString = currentCode.getDeclarationString();

	// Convert tabs to spaces
    ConfigurationService configurationService = ConfigurationService.getConfigurationService();
	int tabSize = configurationService.getTabSize();
	if (tabSize < 1) {
		tabSize = 1;
	}

	StringBuffer itemWithTabsExpanded = new StringBuffer();
	int col = 0;
	for (int i=0 ; (i < declString.length()) ; ++i) {
		char chr = declString.charAt(i);
		if (chr == '\r' || chr == '\n') {
			col = 0;
			itemWithTabsExpanded.append(chr);
		}
		else if (chr == '\t') {
			do {
				itemWithTabsExpanded.append(' ');
				col++;
			} while ((col % tabSize) != 0);
		}
		else  {
			itemWithTabsExpanded.append(chr);
			col++;
		}
	}
	declString = itemWithTabsExpanded.toString();

	// Put converted string to the screen.
	try  {
		jTxtArCodeViewer.setContentType("text/html");
	    jTxtArCodeViewer.setText(new HighlightCode().processCode(declString));
    }
	catch (IOException e)  {
		e.printStackTrace();
		jTxtArCodeViewer.setContentType("text/plain");
	    jTxtArCodeViewer.setText(declString);
	}
    jTxtArCodeViewer.setSelectionStart(1);
    jTxtArCodeViewer.setSelectionEnd(1); // scroll to top
  }
}
