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
//	7/12/2001 lmm	- make it send a change event to the JList if a change comes
//					in from the change dispatcher in which the compilation unit is
//					the same.

package tinyplanet.docwiz;

import tinyplanet.gui.ConcreteAppPane;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.*;

/** AppPane which shows the list of commentable items in the current compilation unit.
 */
public class ItemListAppPane extends ConcreteAppPane implements ItemChangedListener {

  LayoutManager lm = new GridLayout(1,1);

  JScrollPane jScrlPnItemList = new JScrollPane();

  JList lstCodeElement = new JList();

  CodeCommentListCellRenderer cclcrrenderer = new  CodeCommentListCellRenderer();

  CompilationUnit currentUnit;

  ChangeDispatcher changeDispatcher = ChangeDispatcher.getChangeDispatcher();
    /**
	 * Holds the last value selected in the JList lstCodeElement so we can filter out
	 *  multiple changes to the same guy. (We get one change event on mouse down and
	 *  a second on mouse up on my machine.)
	 */
	Object previousSelection = null;

  public ItemListAppPane() {
    this.setLayout(lm);
    jScrlPnItemList.getViewport().add(lstCodeElement, null);
    this.add(jScrlPnItemList);
    lstCodeElement.setCellRenderer(this.cclcrrenderer);
    lstCodeElement.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    lstCodeElement.setModel(new WritableListModel());
    lstCodeElement.setToolTipText("Select the code element you want to comment here");
    lstCodeElement.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      /**        *
       * @param e
       */
      public void valueChanged(ListSelectionEvent e) {
        lstCodeElement_valueChanged(e);
      }
    });
    this.setTitle("Item List");
  }

  /** This event fires when the user chooses a new code element from the list of
   * code elements within the CompilationUnit. This method then sets up the controls so that
   * the correct values are displayed for the current element by calling setCurrentElementControls() and
   * and validates the container containing the controls.
   *
   * @param e
   */
  void lstCodeElement_valueChanged(ListSelectionEvent e) {
    /*setCurrentElementControls();
    Object o = e.getSource();
    Component c = (Component) o;
    c.validate(); // make sure the component is refreshed
    pnlEditControls.validate();
    checkAndUpdatePreview();*/

	// Only fire the change if the selected element is different this time.
    Object o = lstCodeElement.getSelectedValue();
	if (previousSelection != o) {
		previousSelection = o;		// Save for next time
	    CommentableCode cc = (CommentableCode) o;
    	changeDispatcher.fireItemChanged(currentUnit, cc);
	}
  }

  public void itemChanged(ItemChangedEvent ie) {
    CompilationUnit newUnit = ie.getCompilationUnit();
    if (newUnit != this.currentUnit) {
      lstCodeElement.removeAll();
      lstCodeElement.setModel(newUnit);
      lstCodeElement.setSelectionInterval(0,0);
      lstCodeElement.revalidate();
      currentUnit = newUnit;
    }
	else  {
	  this.currentUnit.fireContentsChanged();
	}
  }
}
