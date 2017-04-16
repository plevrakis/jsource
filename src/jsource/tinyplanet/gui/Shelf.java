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
//	7/16/2001 lmm	- add setSelectedIndex method to directly set the current pane.

package com.tinyplanet.gui;

import javax.swing.JPanel;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.*;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.Transferable;
import java.awt.GridLayout;
import java.awt.Component;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class Shelf extends JPanel 
//implements DropTargetListener, DragGestureListener, DragSourceListener 
{
  protected DraggableTabbedPane tabPane;
//  protected DragSource dragSource;
//  /**
//   * enables this component to be a dropTarget
//   */
//
//  DropTarget dropTarget = null;

  public Shelf() {
    setToolTipText("Aw yeah");
//    dropTarget = new DropTarget (this, this);
//    dragSource = DragSource.getDefaultDragSource();
//    dragSource.createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_MOVE, this);
    this.setLayout(new GridLayout(1,1));
	
      tabPane = new DraggableTabbedPane();
      this.add(tabPane);
  }

//  /**
//   * is invoked when the user changes the dropAction
//   *
//   */
//
//  public void dropActionChanged ( DragSourceDragEvent event) {
//    System.out.println( "shelf dropActionChanged");
//  }

  /**
   *  similar to DraggableTabbedPane.addAddAppPane, but addAddAppPane() is an
   *  implementation detail intended to be hidden. Conceptually, AppPanes
   * are always added to Shelfs. However, Some coupling necessary here.
   */
  public synchronized void addPane(AppPane a) {
//    if (tabPane == null) {
//      // Shelf is initially empty, then we add the DraggableTabbedPane
//      // to it.
//      //System.out.println( "shelf creating new DTP");
//      tabPane = new DraggableTabbedPane();
//      this.add(tabPane);
//    }
    tabPane.addElement(a);
  }

	/**
	 * Does the same thing as this same function for JTabbedPane.
	 *
	 * @since 07/16/2001 10:08:37 AM
	 * @param
	 * @return
	 */
	public void setSelectedIndex(int tabIndex)
	{
//		List panes = getPaneList();
//		for (Iterator iter = panes.iterator(); iter.hasNext() ; ) {
//			Component thePane = (Component)iter.next();
//			thePane.setVisible(false);
//		}
		if (tabPane.getTabCount() > 0) {  
			tabPane.setSelectedIndex(tabIndex);
		}
		return;
	}

  public List getPaneList() {
    List theList = null;
	if (tabPane != null) {
		theList = tabPane.paneList;
	}
	if (theList == null) {
		theList = new LinkedList();
	} 
	return theList;
  }

//   /**
//   * a drop has occurred
//   *
//   */
//
//
//  public void drop (DropTargetDropEvent event) {
//    System.out.println( "shelf drop");
//    try {
//        Transferable transferable = event.getTransferable();
//        DataFlavor objectFlavor = AppPaneTransferrable.localApFlavor;
//        if (transferable.isDataFlavorSupported(objectFlavor)){
//            System.out.println( "shelf accepting drop");
//            event.acceptDrop(DnDConstants.ACTION_MOVE);
//            Object o = transferable.getTransferData (objectFlavor);
//            if (o instanceof AppPane) {
//               addPane((AppPane)o);
//               event.getDropTargetContext().dropComplete(true);
//            }
//        }
//        else {
//          event.rejectDrop();
//        }
//    }
//    catch (IOException exception) {
//        exception.printStackTrace();
//        System.err.println( "Exception" + exception.getMessage());
//        event.rejectDrop();
//    }
//    catch (UnsupportedFlavorException ufException ) {
//      ufException.printStackTrace();
//      System.err.println( "Exception" + ufException.getMessage());
//      event.rejectDrop();
//    }
//    /*catch (ClassNotFoundException cnfe) {
//      cnfe.printStackTrace();
//      System.err.println( "Exception" + cnfe.getMessage());
//      event.rejectDrop();
//    }*/
//  }
//
//  /**
//   * is invoked if the use modifies the current drop gesture
//   *
//   */
//
//
//  public void dropActionChanged ( DropTargetDragEvent event ) {
//    System.err.println("Shelf: dropActionChanged()");
//  }
//
//  /**
//   * is invoked when a drag operation is going on
//   *
//   */
//
//  public void dragOver (DropTargetDragEvent event) {
//    //System.err.println( "Shelf dragOver(DropTargetDragEvent event)");
//  }
//  /**
//   * is invoked when you are exit the DropSite without dropping
//   *
//   */
//
//  public void dragExit (DropTargetEvent event) {
//    System.out.println( "Shelf dragExit(DropTargetEvent event)");
//
//  }
//
//   /**
//   * is invoked when you are dragging over the DropSite
//   *
//   */
//
//  public void dragEnter (DropTargetDragEvent event) {
//
//    // debug messages for diagnostics
//    System.out.println( "shelf dragEnter");
//    event.acceptDrag (DnDConstants.ACTION_MOVE);
//  }
//
//    /**
//   * a drag gesture has been initiated
//   *
//   */
//
//  public void dragGestureRecognized( DragGestureEvent event) {
//    System.out.println( "shelf dragGestureRecognized");
//    /*Object selected = getSelectedComponent();
//    if ( selected != null ){
//        StringSelection text = new StringSelection( selected.toString());
//
//        // as the name suggests, starts the dragging
//        dragSource.startDrag (event, DragSource.DefaultMoveDrop, text, this);
//    } else {
//        System.out.println( "nothing was selected");
//    }*/
//  }
//
//  /**
//   * this message goes to DragSourceListener, informing it that the dragging is currently
//   * ocurring over the DropSite
//   *
//   */
//
//  public void dragOver (DragSourceDragEvent event) {
//    //System.out.println( "shlf dragOver, source");
//
//  }
//
//  /**
//   * this message goes to DragSourceListener, informing it that the dragging
//   * has exited the DropSite
//   *
//   */
//
//  public void dragExit (DragSourceEvent event) {
//    System.out.println( "shlf dragExit");
//
//  }
//
//    /**
//   * this message goes to DragSourceListener, informing it that the dragging
//   * has entered the DropSite
//   *
//   */
//
//  public void dragEnter (DragSourceDragEvent event) {
//    System.err.println( "shlf dragEnter");
//  }
//  /**
//   * this message goes to DragSourceListener, informing it that the dragging
//   * has ended
//   *
//   */
//
//  public void dragDropEnd (DragSourceDropEvent event) {
//    System.err.println( "shlf ddend");
//    if ( event.getDropSuccess()){
//        System.err.println( "shlf ddendsuccess");
//    }
//  }
}