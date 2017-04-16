
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
//	4/6/2001 Lee Meador (LeeMeador@usa.net)
//				Change to set the preferred size on the splits as saved from last time.
//	5/3/2001 Lee Meador Add a pane (in the bottom right section) to show debugging events
//				but only display it if debug is turned on in the config.
//	7/9/2001	lmm	- pass the window size to the about box
//					- add a preferences box
//					- rearrange the building to make more sense to me
//	7/16/2001	lmm	- for each shelf, set the first tab to be the selected one after setup.

package tinyplanet.docwiz;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.util.List;

import tinyplanet.gui.AppPane;
import tinyplanet.gui.ConcreteRack;
import tinyplanet.gui.Shelf;

public class g extends ConcreteRack implements ItemChangedListener {

  private JMenuBar menuBar1 = new JMenuBar();

  private JMenu menuFile = new JMenu();
  private JMenuItem mnItmOpen = new JMenuItem();
  private JMenuItem mnItmInputJavaSource = new JMenuItem();
  private JMenuItem mnItmSave = new JMenuItem();
  private JMenuItem menuFileExit = new JMenuItem();

  private JMenu menuHelp = new JMenu();
  private JMenuItem menuHelpAbout = new JMenuItem();

  private JMenu menuView = new JMenu();
  private JMenuItem menuViewPreferences = new JMenuItem();
  private JMenuItem mnItmFormattedComment = new JMenuItem();

  private JButton jBtnSave = new JButton();
  private JButton jBtnOpen = new JButton();

  private CompilationUnit currentUnit;
  private ModeManager modeManager = ModeManager.getModeManager();
  private FileManager fileManager = FileManager.getFileManager();

  public void itemChanged(ItemChangedEvent ie) {
    CompilationUnit newUnit = ie.getCompilationUnit();
    if  (newUnit != this.currentUnit) {
      /* new file*/
      this.setTitle("DocWiz - " + newUnit.getFileDir()+newUnit.getFileName());
      this.currentUnit = newUnit;
    }
  }

  void init() {

    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });

	menuBar1.setForeground(SystemColor.menuText);
    menuBar1.setBackground(SystemColor.menu);

	this.setTitle("DocWiz");

 	// menus on menu bar

	menuFile.setText("File");
    menuFile.setForeground(SystemColor.menuText);
    menuFile.setBackground(SystemColor.menu);

    menuView.setText("View");
    menuView.setForeground(SystemColor.menuText);
    menuView.setBackground(SystemColor.menu);

	menuHelp.setText("Help");
    menuHelp.setForeground(SystemColor.menuText);
    menuHelp.setBackground(SystemColor.menu);

	// Options on help menu

    menuHelpAbout.setText("About");
    menuHelpAbout.setForeground(SystemColor.menuText);
    menuHelpAbout.setBackground(SystemColor.menu);
    menuHelpAbout.addActionListener(new g_menuHelpAbout_ActionAdapter(this));

    menuHelp.add(menuHelpAbout);

    //statusBar.setBackground(SystemColor.control);

	// Options on View menu

//    mnItmFormattedComment.setText("Formatted Comment");
//    mnItmFormattedComment.setForeground(SystemColor.menuText);
//    mnItmFormattedComment.setBackground(SystemColor.menu);
//    mnItmFormattedComment.addActionListener(new g_mnItmFormattedComment_actionAdapter(this));

    menuViewPreferences.setText("Preferences...");
    menuViewPreferences.setForeground(SystemColor.menuText);
    menuViewPreferences.setBackground(SystemColor.menu);
    menuViewPreferences.addActionListener(new g_menuViewPreferences_actionAdapter(this));

    menuView.add(menuViewPreferences);

	// Options on File menu

    mnItmOpen.setText("Open...");
    mnItmOpen.setForeground(SystemColor.menuText);
    mnItmOpen.setBackground(SystemColor.menu);
    mnItmOpen.addActionListener(new g_mnItmOpen_actionAdapter(this));

//    mnItmInputJavaSource.setText("Input Java Source...");
//    mnItmInputJavaSource.setForeground(SystemColor.menuText);
//    mnItmInputJavaSource.setBackground(SystemColor.menu);

    mnItmSave.setText("Save");
    mnItmSave.setForeground(SystemColor.menuText);
    mnItmSave.setBackground(SystemColor.menu);
    mnItmSave.addActionListener(new g_mnItmSave_actionAdapter(this));

	menuFileExit.setText("Exit");
    menuFileExit.setForeground(SystemColor.menuText);
    menuFileExit.setBackground(SystemColor.menu);
    menuFileExit.addActionListener(new g_menuFileExit_ActionAdapter(this));

    menuFile.add(mnItmOpen);
    menuFile.add(mnItmInputJavaSource);
    menuFile.add(mnItmSave);
    menuFile.addSeparator();
    menuFile.add(menuFileExit);

   // buttonBar.setBackground(SystemColor.control);

	// Load up the menu bar with the menus

    menuBar1.add(menuFile);
    menuBar1.add(menuView);
    menuBar1.add(menuHelp);

    this.setJMenuBar(menuBar1);

   // this.getContentPane().add(buttonBar, BorderLayout.NORTH);
   // buttonBar.add(jBtnOpen, null);
   // buttonBar.add(jBtnSave, null);

  }

  public void configInit()
  {
	System.out.println("g.configInit: entry");

    ConfigurationService configurationService = ConfigurationService.getConfigurationService();

    this.setSize(configurationService.getSizeX(), configurationService.getSizeY());

    Shelf[] s = this.getShelfs();
    this.splitLeft(s[0]);
    s = this.getShelfs();
    this.splitDown(s[1]);
    s = this.getShelfs();

	System.out.println("g.configInit: Shelf's created");

    EditControlsAppPane edittingPane = new EditControlsAppPane();	// pane to fill in comments
    CommentViewAppPane commentViewPane = new CommentViewAppPane();	// pane shows formatted comment
    ItemListAppPane listPane = new ItemListAppPane();				// pane shows list of methods, etc.
    CodeViewAppPane codeViewPane = new CodeViewAppPane();			// current method or attribute pane
    UnknownTagAppPane unknownTagPane = new UnknownTagAppPane();		// unknown tags

	EventAppPane eventDebugPane = new EventAppPane();				// debug event pane shows events

	System.out.println("g.configInit: panes created");

    s[configurationService.getItemPaneLocation()].addPane(listPane);
    s[configurationService.getEditPaneLocation()].addPane(edittingPane);
    s[configurationService.getCodePaneLocation()].addPane(codeViewPane);
    s[configurationService.getCommentPaneLocation()].addPane(commentViewPane);
    s[configurationService.getUnknownTagPaneLocation()].addPane(unknownTagPane);

    if (configurationService.getDebug()) {
	    s[configurationService.getCommentPaneLocation()].addPane(eventDebugPane);
    }

	System.out.println("g.configInit: panes added");

	for (int i=0 ; i < s.length; ++i) {		// select the correct tab in each pane
		s[i].setSelectedIndex(0);
	}

	System.out.println("g.configInit: tabs selected");

    java.awt.Dimension shelfDim = s[0].getSize();
    s[0].setPreferredSize(new Dimension(configurationService.getShelf0Size(),(int)shelfDim.getHeight()));
	((JSplitPane)s[0].getParent()).setDividerLocation(configurationService.getShelf0Size());

    shelfDim = s[1].getSize();
    s[1].setPreferredSize(new Dimension((int)shelfDim.getWidth(),configurationService.getShelf1Size()));
	((JSplitPane)s[1].getParent()).setDividerLocation(configurationService.getShelf1Size());

	System.out.println("g.configInit: Shelf sizes set");

//	System.out.println(" Width = " + configurationService.getShelf0Size());
//	System.out.println("Height = " + configurationService.getShelf1Size());

    ChangeDispatcher cd = ChangeDispatcher.getChangeDispatcher();
    cd.addItemChangedListener(edittingPane);
    cd.addItemChangedListener(listPane);
    cd.addItemChangedListener(codeViewPane);
    cd.addItemChangedListener(commentViewPane);
    cd.addItemChangedListener(unknownTagPane);

    cd.addItemChangedListener(this.modeManager);
//    this.setSize(configurationService.getSizeX(),configurationService.getSizeY());

    if (configurationService.getDebug()) {
	    cd.addItemChangedListener(eventDebugPane);			// The event page gets notified about changes
		cd.addDebugMessageListener(eventDebugPane);		// The event pane will get these debug messages
    }

	System.out.println("g.configInit: exit");

  }

  public g() {
    this.init();
    this. configInit();

  }

  public static void main(String[] args) {
    g r = new g();
    r.setVisible(true);
  }

  /**
   * Print out debugging info, if necessary.
   */
  public static void debug(String debugMsg) {
    ConfigurationService configurationService = ConfigurationService.getConfigurationService();
    if (configurationService.getDebug()) {
      System.err.println(debugMsg);
    }
  }

  /**
   * Special version of toString with a single int parameter to return a string for debugging
   *
   * @param shelfNo The shelf number 0, 1, or 2 to get a string showing the sizes.
   */
  public String toString(int shelfNo) {
    Shelf[] s = this.getShelfs();
	return "divider " + shelfNo + " location (min, curr, max) = (" + ((JSplitPane)s[shelfNo].getParent()).getMinimumDividerLocation() + ", " + ((JSplitPane)s[shelfNo].getParent()).getDividerLocation() + ", " + ((JSplitPane)s[shelfNo].getParent()).getMaximumDividerLocation() + ")";
  }


  void updateConfigInfo() {
    ConfigurationService configurationService = ConfigurationService.getConfigurationService();
    java.awt.Dimension d = this.getSize();
    configurationService.setSizeX((int)d.getWidth());
    configurationService.setSizeY((int)d.getHeight());

    // Locate app panes
    Shelf[] theShelfs = this.getShelfs();
    for(int i =0; i < theShelfs.length; i++) {
      Shelf currentShelf = theShelfs[i];
      java.awt.Dimension shelfDim = currentShelf.getSize();
      if (i==0) {
        configurationService.setShelf0Size((int)shelfDim.getWidth());
      }
      if (i==1) {
        configurationService.setShelf1Size((int)shelfDim.getHeight());
      }

      List paneList = currentShelf.getPaneList();
      for (int j = 0; j<paneList.size(); j++) {
        AppPane currentAppPane = (AppPane)paneList.get(j);
        if (currentAppPane instanceof CommentViewAppPane) {
           configurationService.setCommentPaneLocation(i);
        }
        if (currentAppPane instanceof CodeViewAppPane) {
           configurationService.setCodePaneLocation(i);
        }
        if (currentAppPane instanceof EditControlsAppPane) {
           configurationService.setEditPaneLocation(i);
        }
        if (currentAppPane instanceof UnknownTagAppPane) {
           configurationService.setUnknownTagPaneLocation(i);
        }
        if (currentAppPane instanceof ItemListAppPane) {
           configurationService.setItemPaneLocation(i);
        }
      }
    }
  }

  void this_windowClosing(WindowEvent e) {
    updateConfigInfo();
    modeManager = ModeManager.getModeManager();
    modeManager.askExit();
  }

 /** Event handler for when the user picks File|Open from the menu. Ask the user if the
  * file should be saved, display the open dialog box, and open the file.
  *
  * @param e The event produced by the user clicking on the menu item
  */
  void mnItmOpen_actionPerformed(ActionEvent e) {
    modeManager.askOpen(this);
  }

  void mnItmSave_actionPerformed(ActionEvent e) {
    fileManager.doSave();
  }

  void jBtnOpen_actionPerformed(ActionEvent e) {
    mnItmOpen_actionPerformed(e);
  }


  void jBtnSave_actionPerformed(ActionEvent e) {
    mnItmSave_actionPerformed(e);
  }

 void fileExit_actionPerformed(ActionEvent e) {
   modeManager = ModeManager.getModeManager();
   updateConfigInfo();
   //System.err.println("g: about to askExit");
   //System.err.println("g: modeManager: " + modeManager);
   modeManager.askExit();
 }

 void helpAbout_actionPerformed(ActionEvent e) {
    java.awt.Dimension d = this.getSize();
    frmDocWiz_AboutBox f = new frmDocWiz_AboutBox(d);
    //f.setVisible(true);
    f.show();
 }

 void menuViewPreferences_actionPerformed(ActionEvent e) {
    java.awt.Dimension d = this.getSize();
    PreferencesBox f = new PreferencesBox(d);
    f.show();
 }

// void mnItmFormattedComment_actionPerformed(ActionEvent e) {
// }
}

class g_menuFileExit_ActionAdapter implements ActionListener{

 g adaptee;

  g_menuFileExit_ActionAdapter(g adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.fileExit_actionPerformed(e);
  }
}

class g_menuHelpAbout_ActionAdapter implements ActionListener{

  g adaptee;

  g_menuHelpAbout_ActionAdapter(g adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.helpAbout_actionPerformed(e);
  }
}



class g_mnItmOpen_actionAdapter implements java.awt.event.ActionListener{

 g adaptee;

  g_mnItmOpen_actionAdapter(g adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.mnItmOpen_actionPerformed(e);
  }
}


//class g_mnItmFormattedComment_actionAdapter implements java.awt.event.ActionListener{
// g adaptee;
//
//  g_mnItmFormattedComment_actionAdapter(g adaptee) {
//    this.adaptee = adaptee;
//  }
//
//  public void actionPerformed(ActionEvent e) {
//    adaptee.mnItmFormattedComment_actionPerformed(e);
//  }
//}

class g_menuViewPreferences_actionAdapter implements java.awt.event.ActionListener
{
 g adaptee;

  g_menuViewPreferences_actionAdapter(g adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.menuViewPreferences_actionPerformed(e);
  }
}

class g_mnItmSave_actionAdapter implements java.awt.event.ActionListener{
 g adaptee;

  g_mnItmSave_actionAdapter(g adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.mnItmSave_actionPerformed(e);
  }
}


class g_jBtnOpen_actionAdapter implements java.awt.event.ActionListener{
 g adaptee;

  g_jBtnOpen_actionAdapter(g adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jBtnOpen_actionPerformed(e);
  }
}

class g_jBtnSave_actionAdapter implements java.awt.event.ActionListener{
 g adaptee;


 /**
  * @param adaptee
  */
  g_jBtnSave_actionAdapter(g adaptee) {
    this.adaptee = adaptee;
  }

 /**
  * @param e
  */
  public void actionPerformed(ActionEvent e) {
    adaptee.jBtnSave_actionPerformed(e);
  }
}