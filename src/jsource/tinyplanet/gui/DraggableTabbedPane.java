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

// History
//	6/27/2001 lmm	- Make the drag and drop work. The three listeners are broken into
//					inner classes. Various checking and accepting is done as it is
//					dragged through various objects. And many more changes, which did
//					not make it work. The 'drop' method was never called even though
//					all else seemed to be working right. It began to work when I quit
//					using the AppPaneTransferrable class and started just dragging a
//					string containing the name from the tab while the actual pane was stored
//					in the locking object.
//					-- Make the cursor change to show places that it would like to
//					drop something and make the background turn green when a pane was
//					ready to accept the drop.
//					-- There is still a bug when you get a single tab-pane in one of the
//					split panes, sometimes, it will not update but remains blank. Adding
//					another tab-pane there and clicking it then going back to the problem
//					tab-pane and clicking clears up the problem.

package com.tinyplanet.gui;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DnDConstants;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;

/** The GUI implementation on which ConcreteAppPane is based. Allows dragging of
 * tabsbetween DraggableTabbedPanes.
 *
 */
public class DraggableTabbedPane extends JTabbedPane implements	DNDComponentInterface
{

// Attributes:

	protected List paneList = new LinkedList();

	private static PaneLock paneLock = new DraggableTabbedPane().new PaneLock();

	/**
	 * enables this component to be a dropTarget
	 */
	private DropTarget dropTarget;

	private DropTargetListener dropTargetListener;

	/**
	 * enables this component to be a Drag Source
	 */
	private DragSource dragSource;

	private DragGestureListener dragGestureListener;

	private DragSourceListener dragSourceListener;

	/** These are the actions we allow */
	private static final int acceptableActions = DnDConstants.ACTION_MOVE;

// Constructors:

	public DraggableTabbedPane()
	{
		dropTargetListener = new MyDropTargetListener();
		dropTarget = new DropTarget (this, acceptableActions, dropTargetListener, true);

		dragSource = DragSource.getDefaultDragSource();

		dragGestureListener = new MyDragGestureListener();
		dragSourceListener = new MyDragSourceListener();
		dragSource.createDefaultDragGestureRecognizer( this, acceptableActions, dragGestureListener);
	}

// Methods:
	
	/**
	 * implements DNDComponentInterface - adds AppPanes to this tab pane.
	 */
	public void addElement( Object s )
	{
		if (s instanceof AppPane) {
			// Check to see if it's already added.
			AppPane a = (AppPane)s;
			if (containsAppPane(a)) {
				//already added
				return ;
			}
			else {
				Container con = a.getParent();
				if (con != null && con instanceof DNDComponentInterface) {
//					System.out.println("DraggableTabbedPane.addElement: about to removeDraggingElement()");
					((DNDComponentInterface)con).removeDraggingElement(a);
				}

				// Add to  list of Panes

				this.paneList.add(s);
//				System.out.println("DraggableTabbedPane.addElement: "+this.hashCode()+": calling addTab with "+s);
				this.addTab(a.getTitle(), null, (Component)s, "");
				
//				if (getSelectedIndex() == -1) 
				if (getTabCount() == 1)
				{ 
//					setSelectedComponent((Component)s);
					((Component)s).setVisible(true);
				}
//
				((JComponent)s).invalidate();
//				
//				Shelf parent = (Shelf)this.getParent();
////				parent.revalidate();
//        		parent.repaint();
			}
		}
	}

	private boolean containsAppPane(AppPane a)
	{
		return (indexOfComponent((Component)a) != -1);
//		return (a.getParent() == this);
	}

//	public void removeTabAt(int i) {
//	 // no removals allowed while we're dragging
//	  synchronized (paneLock) {
//	  if (paneLock.isLocked()) {
//		System.out.println("DraggableTabbedPane.removeTabAt: "+hashCode()+": removed "+i);
//
//			super.removeTabAt(i);
//	  }
//	else {
//
//		System.out.println("DraggableTabbedPane.remooveTabAt: "+hashCode()+": not locked, not removing "+i);
//	  }
//	  }
//
//	}

	/**
	 * implements DNDComponentInterface - removes a pane from this tab pane
	 */
	public void removeDraggingElement(Object o)
	{
		// get selected tab
//		System.out.println("DraggableTabbedPane.removeDraggingElement: Entry");
		if (o instanceof AppPane) {
			AppPane pane = (AppPane)o;
			// remove from list of AppPanes
			paneList.remove(pane);
			// remove tab
			remove((Component)pane);
		}
	}

	protected Component makeTextPanel(String text)
	{
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}

// Inner classes:

	/**
	 * This class implements the <code>DragGestureListener</code> interface and is supplied
	 *	to the drag source so that it can be used by the drag gesture recognizer. It is
	 *	called to handle the case when a drag operation is beginning in this component.
	 *
	 */
	private class MyDragGestureListener implements DragGestureListener
	{

		/**
		 * a drag gesture has been initiated
		 */
		public void dragGestureRecognized( DragGestureEvent event)
		{
//			System.out.println("\n***************************************************************\n");
//			System.out.println("DraggableTabbedPane$MyDragGestureListener.dragGestureRecognized: Entry");
			synchronized (paneLock) {
				if (!paneLock.isLocked()) {
					// We only want to drag if it's not locked
					paneLock.setLocked(true);
					Component selected = getSelectedComponent();
					AppPane selectedAppPane = null;
					if (selected instanceof AppPane) {
						selectedAppPane = (AppPane)selected;
					}
//					System.out.println( "DraggableTabbedPane$MyDragGestureListener.dragGestureRecognized:\n	   selected class = "+ selected.getClass());
					if ( selectedAppPane != null ) {

						paneLock.setMovingPane(selectedAppPane);
						StringSelection text = new StringSelection(selectedAppPane.getTitle());
//						AppPaneTransferrable apt = new AppPaneTransferrable(selectedAppPane);

						// as the name suggests, starts the dragging
						dragSource.startDrag (event, DragSource.DefaultMoveDrop, text, dragSourceListener);
//						dragSource.startDrag (event, DragSource.DefaultMoveDrop, apt, dragSourceListener);
					}
					else {
//						System.out.println( "DraggableTabbedPane$MyDragGestureListener.dragGestureRecognized:\n	   nothing selected");
						paneLock.setLocked(false);
					}
				}
				else {
//					System.out.println( "DraggableTabbedPane$MyDragGestureListener.dragGestureRecognized:\n	 attempting to drag with pane locked!");
					paneLock.setLocked(false);
				}
			}

		}
	}


	/*
	 * This class handles messages relating the drag operation to the source (ie. the thing
	 *	being dragged.
	 */
	private class MyDragSourceListener implements DragSourceListener
	{

		/**
		 * this message goes to DragSourceListener, informing it that the dragging
		 *	has entered a possible DropSite. To get here the drop source must okay
		 *	this drag operation as being allowed to drop on it.
		 */
		public void dragEnter (DragSourceDragEvent event)
		{
//			System.out.println( "DraggableTabbedPane$MyDragSourceListener.dragEnter: source " + DraggableTabbedPane.this.hashCode());

			// Set the cursor to show if the target we are over just now can accept our
			//	action.
			DragSourceContext context = event.getDragSourceContext();
			//intersection of the users selected action, and the source and target actions
			int myaction = event.getDropAction();
			if ((myaction & acceptableActions) != 0) {
//				System.out.println( "DraggableTabbedPane$MyDragSourceListener.dragEnter:	drop cursor");
				context.setCursor(DragSource.DefaultMoveDrop);
			}
			else {
//				System.out.println( "DraggableTabbedPane$MyDragSourceListener.dragEnter: NO drop cursor");
				context.setCursor(DragSource.DefaultMoveNoDrop);
			}
		}

		/**
		 * is invoked if the use modifies the current drop gesture
		 */
		public void dropActionChanged ( DragSourceDragEvent event )
		{
			System.err.println("DraggableTabbedPane$MyDragSourceListener.dropActionChanged: source " + DraggableTabbedPane.this.hashCode());
		}


		/**
		 * this message goes to DragSourceListener, informing it that the dragging
		 * has ended
		 */
		public void dragDropEnd (DragSourceDropEvent event)
		{
//	 		System.out.println("DraggableTabbedPane$MyDragSourceListener.dragDropEnd source " + DraggableTabbedPane.this.hashCode());

			synchronized (paneLock) {
				if ( event.getDropSuccess()) {
//					System.out.println("DraggableTabbedPane.dragDropEnd		drop success");
					// Note: We should remove the source (since this is a move) but
					//	we cant do that because a pane can't be in two places at once and
					//	we must remove it from where it is to add it to where it is ending up.
				}
				else {
					// even if drag is not a success, we should unlock
//		  			System.out.println("DraggableTabbedPane$MyDragSourceListener.dragDropEnd	   drop failure");
				}
				paneLock.setLocked(false);
			}
		}

		/**
		 * this message goes to DragSourceListener, informing it that the dragging
		 * has exited the DropSite
		 */
		public void dragExit (DragSourceEvent event)
		{
//	  		System.out.println( "DraggableTabbedPane$MyDragSourceListener.dragExit: source " + DraggableTabbedPane.this.hashCode());
		}

		/**
		 * this message goes to DragSourceListener, informing it that the dragging is currently
		 * ocurring over the DropSite
		 */
		public void dragOver (DragSourceDragEvent event)
		{
//	  		System.out.println( "DraggableTabbedPane$MyDragSourceListener.dragOver: source " + DraggableTabbedPane.this.hashCode());

		}

	}


	/*
	 * This class handles messages relating the drag operation to the thing it will be dropped
	 *	upon.
	 */
	private class MyDropTargetListener implements DropTargetListener
	{

		/**
		 * is invoked when you are dragging over the DropSite
		 */
		public void dragEnter (DropTargetDragEvent event)
		{

//	  		System.out.println( "DraggableTabbedPane$MyDropTargetListener.dragEnter target " + DraggableTabbedPane.this.hashCode());
			if (isDragOk(event) == false) {
				event.rejectDrag();
				return ;
			}

			// highlight the drop target pane unpon entry (when its ok to drop here)
			((JComponent)(DraggableTabbedPane.this.getParent())).setOpaque(true);
			((JComponent)(DraggableTabbedPane.this.getParent())).setBackground(java.awt.Color.green);

			event.acceptDrag (acceptableActions);
		}

		/**
		 * is invoked when a drag operation is going on
		 */
		public void dragOver (DropTargetDragEvent event)
		{
//			System.out.println( "DraggableTabbedPane$MyDropTargetListener.dragOver target " + DraggableTabbedPane.this.hashCode());
//			event.acceptDrag (acceptableActions);
		}

		/**
		 * is invoked if the user modifies the current drop gesture
		 */
		public void dropActionChanged ( DropTargetDragEvent event )
		{
//			System.out.println("DraggableTabbedPane$MyDropTargetListener.dropActionChanged: target " + DraggableTabbedPane.this.hashCode());
			if (isDragOk(event) == false) {
				event.rejectDrag();
				return ;
			}
			event.acceptDrag (acceptableActions);
		}

		/**
		 * is invoked when you are exit the DropSite without dropping
		 */
		public void dragExit (DropTargetEvent event)
		{
//			System.out.println( "DraggableTabbedPane$MyDropTargetListener.dragExit target " + DraggableTabbedPane.this.hashCode());

			// unHighlight the drop target pane (no drop done)
			((JComponent)(DraggableTabbedPane.this.getParent())).setOpaque(false);
			((JComponent)(DraggableTabbedPane.this.getParent())).setBackground(null);
		}

		/**
		 * a drop has occurred
		 */
		public void drop(DropTargetDropEvent event)
		{
//			System.out.println("DraggableTabbedPane$MyDropTargetListener.drop: Entry " + DraggableTabbedPane.this.hashCode());

			// unHighlight the drop target pane
			((JComponent)(DraggableTabbedPane.this.getParent())).setOpaque(false);
			((JComponent)(DraggableTabbedPane.this.getParent())).setBackground(null);

			try {
				Transferable transferable = event.getTransferable();
//				DataFlavor objectFlavor = AppPaneTransferrable.localApFlavor;
				DataFlavor objectFlavor = DataFlavor.stringFlavor;
				if (transferable.isDataFlavorSupported(objectFlavor)) {

					event.acceptDrop(acceptableActions);

					Object o = transferable.getTransferData (objectFlavor);
					if (o instanceof AppPane) {
						DraggableTabbedPane.this.addElement(o);
						event.getDropTargetContext().dropComplete(true);
					}
					else if (o instanceof String) {
//						System.out.println("DraggableTabbedPane$MyDropTargetListener.drop: recieve <" + (String)o + ">");

						synchronized (paneLock) {
							if (paneLock.isLocked()) {
								AppPane pane = paneLock.getMovingPane();
								if (pane.getTitle().equals(o)) {		// Make sure we move the right one
									DraggableTabbedPane.this.addElement(pane);
								}
							}
						}

						event.getDropTargetContext().dropComplete(true);
					}
				}
				else {
					event.rejectDrop();
				}
			}
			catch (IOException exception) {
				exception.printStackTrace();
				//System.err.println( "Exception" + exception.getMessage());
				event.rejectDrop();
			}
			catch (UnsupportedFlavorException ufException ) {
				ufException.printStackTrace();
				//System.err.println( "Exception" + ufException.getMessage());
				event.rejectDrop();
			}
		}

		private boolean isDragOk(DropTargetDragEvent e)
		{
//			System.out.println("DraggableTabbedPane$MyDropTargetListener.isDragOk: Entry");

//			DataFlavor objectFlavor = AppPaneTransferrable.localApFlavor;
//			if (!e.isDataFlavorSupported(objectFlavor)){
//				/*
//				 * the src does not support any of our flavors
//				 */
//				System.out.println("DraggableTabbedPane$MyDropTargetListener.isDragOk: Exit BAD 1");
//				return false;
//			}

			// the actions specified when the source
			// created the DragGestureRecognizer
			int sa = e.getSourceActions();

			// we're saying that these actions are necessary
			if ((sa & DraggableTabbedPane.acceptableActions) == 0) {
//				System.out.println("DraggableTabbedPane$MyDropTargetListener.isDragOk: Exit BAD 2");
				return false;
			}

//			System.out.println("DraggableTabbedPane$MyDropTargetListener.isDragOk: Exit OK");
			return true;

		}


	}

	/**
	 *	Class used to prevent panes from being moved while pane drag operation is
	 *	occuring. Also stores the pane that is being dragged so we can move it when dropped.
	 */
	private class PaneLock
	{

		boolean locked = false;

		AppPane movingPane;

		boolean isLocked ()
		{
			return locked;
		}

		void setLocked(boolean newLock)
		{
//			System.out.println("DraggableTabbedPane$PaneLock.setLocked: " + (newLock ? "Setting" : "Clearing") + " lock: for " + this);
			locked = newLock;
			if (!isLocked()) {
				movingPane = null;
			}
		}

		AppPane getMovingPane()
		{
			return movingPane;
		}

		void setMovingPane(AppPane newMovingPane)
		{
			movingPane = newMovingPane;
		}
	}
}
