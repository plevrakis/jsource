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
// 4/6/2001 Lee Meador (LeeMeador@usa.net)
//				Minimum size is no longer zero.
// 5/26/2001 lmm	- when resizing the panes the extra space goes into the right or top
//					splits rather than the default (right and bottom) ones.

package tinyplanet.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.ArrayList;

/** ConcreteRack is an implementation of the Rack interface using a JFrame as its base.
 * A rack is a container for Shelfs, which, in turn, contain AppPanes.
 *
 */
public class ConcreteRack extends JFrame implements Rack {
  /**    */
  List splitPaneList = new ArrayList();

  /**    */
  JPanel contentPane;
  /**    */
  BorderLayout borderLayout1 = new BorderLayout();
  /**    */
  Shelf mainShelf = new Shelf();

  /**    */
  public Shelf splitLeft(Shelf s) {
    //System.out.println("CR: splitLeft");
    return split(s, JSplitPane.LEFT, JSplitPane.RIGHT);
  }
  /**    */
  public Shelf splitRight(Shelf s) {
    return split(s, JSplitPane.RIGHT, JSplitPane.LEFT);
  }

  /**    */
  public Shelf splitUp(Shelf s) {
    return split(s, JSplitPane.TOP, JSplitPane.BOTTOM);
  }
  /**    */
  public Shelf splitDown(Shelf s) {
    return split(s, JSplitPane.BOTTOM, JSplitPane.TOP);
  }
  /** "@param splitPanePosition position to add the new pane, see javax.swing.JSplitPane
   */
  Shelf split (Shelf s, String newShelfPosition, String oldShelfPosition) {
    // contentpane itself might contain 1 shelf
    Container containingSplitPane = null;
    Component[] componentArray = contentPane.getComponents();
    for (int i = 0 ; i < componentArray.length; i++)
      if (componentArray[i] instanceof Shelf)
        containingSplitPane = contentPane;

    String positionInParent = JSplitPane.BOTTOM;
    // iterate thru splitPanes to get splitPane for shelf, store in containingSplitPane
    // also record position of splitPane in isContainedOnTop
    for (int i = 0 ; i < splitPaneList.size(); i++) {
      JSplitPane currentSplitPane = (JSplitPane)splitPaneList.get(i);
      Component tc = currentSplitPane.getTopComponent();
      Component bc = currentSplitPane.getBottomComponent();
      // determine section of split pane which contains shelf
      if (tc == s) {
        positionInParent = JSplitPane.TOP;
      }
      if (tc == s || bc == s) {
        containingSplitPane = currentSplitPane;
        break;
      }
    }

    // remove shelf from split pane
    containingSplitPane.remove(s);
    // create new split pane with correct new orientation
    int newOrientation = JSplitPane.VERTICAL_SPLIT;
	double resizeWeight = 1.0;							// resize changes right split's size
    if (JSplitPane.LEFT.equals(newShelfPosition) ||
        JSplitPane.RIGHT.equals(newShelfPosition)) {
      newOrientation = JSplitPane.HORIZONTAL_SPLIT;
	  resizeWeight = 0.0;								// resize changes top split's size
    }
    JSplitPane newSplitPane = new JSplitPane(newOrientation);
    newSplitPane.setBackground(SystemColor.control);
    newSplitPane.setForeground(SystemColor.control);
    addToPaneList(newSplitPane);
    // create new shelf
    Shelf newShelf = new Shelf();
	newShelf.setMinimumSize(new Dimension(0,0));

    // add new split pane to containing split pane in correct new position
    if (containingSplitPane == contentPane) {
      //System.out.println("CR: adding to  contentPane");
      contentPane.add(newSplitPane);
    } else {
      // not at top level of panes
      containingSplitPane.add(newSplitPane, positionInParent);
    }

    //System.out.println("CR: adding "+   newSplitPane);

    //System.out.println("CR: adding 2"+   s);
    // add old shelf to new split pane in correct position
      newSplitPane.add(s, oldShelfPosition);


    // add new shelf to new split pane in correct position
    //System.out.println("CR: adding newShelf"+  newShelf);
    newSplitPane.add(newShelf, newShelfPosition);
    newSplitPane.setDividerLocation(100);					// default width/height in pixels
    try {
      newSplitPane.setResizeWeight(resizeWeight);
    } catch (NoSuchMethodError nsme) {
		  // this is a fallback for JDK < 1.3
      System.err.println("Can't set resizeWeight for SplitPane.");
    }
    this.getRootPane().revalidate();
    return newShelf;
  }

  /**    */
  public Shelf[] getShelfs() {
    ArrayList list = new ArrayList();
    // from split panes
    for (int i = 0 ; i < splitPaneList.size(); i++) {
      JSplitPane currentSplitPane = (JSplitPane)splitPaneList.get(i);
      Component lc = currentSplitPane.getLeftComponent();
      Component rc = currentSplitPane.getRightComponent();
      if (lc != null  && lc instanceof Shelf)
        list.add(lc);
      if (rc != null && rc instanceof Shelf)
        list.add(rc);
    }

    // contentpane itself might contain 1 shelf
    Component[] componentArray = contentPane.getComponents();
    for (int i = 0 ; i < componentArray.length; i++)
      if (componentArray[i] instanceof Shelf)
        list.add(componentArray[i]);

    Object[] o = list.toArray();
    // Convert to Shelf[]
    Shelf[] shArray = new Shelf[o.length];
    for (int i = 0; i<o.length; i++) {
        shArray[i] = (Shelf)o[i];
    }
    return shArray;
  }

  /**    */
  public void removeShelf(Shelf s) throws ShelfNotEmptyException {
    throw new ShelfNotEmptyException();
  }

  /**    */
  public ConcreteRack()  {
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout1);
    this.setBackground(SystemColor.control);
    this.setSize(new Dimension(400, 300));
    contentPane.add(mainShelf);
  }

  //Overridden so we can exit when window is closed
  /**    */
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  /**    */
  void addToPaneList(JSplitPane js) {
    //System.out.println("CR: adding to pane list");
    splitPaneList.add(js);
  }
}
