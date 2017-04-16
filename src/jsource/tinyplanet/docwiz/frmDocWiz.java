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

package tinyplanet.docwiz;


import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.JTextComponent;
import javax.swing.JToolBar;
import javax.swing.text.Document;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import javax.swing.JOptionPane;
import java.awt.FileDialog;
import tinyplanet.javaparser.ParseException;
 /**
  * This class is the outer frame for DocWiz
  *
  * @author Simon Arthur
  */
public class frmDocWiz extends JFrame  {
  BorderLayout borderLayout1 =  new BorderLayout();
  //frmCommentDisplay frmPreview = new frmCommentDisplay ();
  JPanel jpnlContent = new JPanel();
  JToolBar buttonBar = new JToolBar();
  JPanel statusBar = new JPanel();

 /**
  */
  CompilationUnit currentUnit;
 /**  The entire open file, as a String
  */
  String wholeFile;

  GridLayout gridLayout2 = new GridLayout();
 /**   */
  JLabel jLblStatus = new JLabel();
  JScrollPane jScrlPnItemList = new JScrollPane();
  GridLayout gridLayout3 = new GridLayout();
  FieldPanel pnlField = new FieldPanel();
  JPanel pnlEditControls = new JPanel();
  JSplitPane jSplitPane1 = new JSplitPane();
  JSplitPane jSplitPane2 = new JSplitPane();
  CardLayout cardLayout1 = new CardLayout();
  ExecutableElementPanel pnlExecutableElement = new ExecutableElementPanel();
  ClassPanel pnlClass = new ClassPanel();
  JScrollPane jScrlPnCodeView = new JScrollPane();

  JTextArea jTxtArCodeViewer = new JTextArea();
  JList lstCodeElement = new JList();
  CodeCommentListCellRenderer cclcrrenderer = new  CodeCommentListCellRenderer();
  JPanel jPnlBlank = new JPanel();


  //Construct the frame
 /**
  * Create a new DocWiz frame
  *
  */
  public frmDocWiz() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
 /** Standard JBuilder method for initializing all Java Beans
  *
  * @exception Exception
  * @param e
  */

  private void jbInit() throws Exception {
    lstCodeElement.setCellRenderer(this.cclcrrenderer);
    jPnlBlank.setBackground(SystemColor.control);
    jSplitPane1.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    jSplitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);


    //this.getContentPane().setLayout(borderLayout1);
    this.setSize(new Dimension(709, 620));


    //jpnlContent.setBackground(SystemColor.control);


 /*   jpnlContent.setLayout(gridLayout3);
    jScrlPnItemList.setPreferredSize(new Dimension(90, 100));
    pnlEditControls.setPreferredSize(new Dimension(100, 350));
    jTxtArCodeViewer.setMinimumSize(new Dimension(77, 0));
    pnlClass.setPreferredSize(new Dimension(310, 419));
    pnlExecutableElement.setPreferredSize(new Dimension(310, 601));

    this.getContentPane().add(statusBar,  BorderLayout.SOUTH);
    statusBar.add(jLblStatus, null);
    this.getContentPane().add(jpnlContent, BorderLayout.CENTER);
    jpnlContent.add(jSplitPane1, null);
    jSplitPane1.add(jScrlPnItemList, JSplitPane.LEFT);
    jScrlPnItemList.getViewport().add(lstCodeElement, null);
    jSplitPane1.add(jSplitPane2, JSplitPane.RIGHT);
    jSplitPane2.add(pnlEditControls, JSplitPane.TOP);
    jSplitPane2.add(jScrlPnCodeView, JSplitPane.BOTTOM);
    jScrlPnCodeView.getViewport().add(jTxtArCodeViewer, null);




    jpnlContent.setMinimumSize(new Dimension(0, 0));

    jTxtArCodeViewer.setEditable(false);
    jTxtArCodeViewer.setAutoscrolls(false);
    jTxtArCodeViewer.setText("");
    jScrlPnCodeView.setMinimumSize(new Dimension(0, 0));
    jScrlPnCodeView.setPreferredSize(new Dimension(100, 20));
    jScrlPnCodeView.setBackground(SystemColor.control);
    jSplitPane2.setOneTouchExpandable(true);
    jSplitPane2.setMinimumSize(new Dimension(0, 0));
    jSplitPane2.setContinuousLayout(true);
    jSplitPane1.setBackground(SystemColor.control);

    jSplitPane1.setOneTouchExpandable(true);
    jSplitPane1.setContinuousLayout(true);
    jSplitPane1.setBackground(SystemColor.control);

    pnlEditControls.setBackground(SystemColor.control);
    pnlEditControls.setMinimumSize(new Dimension(0, 200));
    pnlEditControls.setLayout(cardLayout1);

    gridLayout3.setColumns(1);
    jScrlPnItemList.setMinimumSize(new Dimension(0, 0));
    jScrlPnItemList.setAutoscrolls(true);
    jLblStatus.setText("DocWiz");
    jLblStatus.setRequestFocusEnabled(false);
    gridLayout2.setColumns(1);
    statusBar.setLayout(gridLayout2);
   // jTxtArClassBody.setWrapStyleWord(true);
    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    jBtnOpen.setText("Open");
    jBtnOpen.setBackground(SystemColor.control);
    jBtnOpen.addActionListener(new frmDocWiz_jBtnOpen_actionAdapter(this));
    jBtnSave.setText("Save");
    jBtnSave.setBackground(SystemColor.control);
    jBtnSave.addActionListener(new frmDocWiz_jBtnSave_actionAdapter(this));

    jScrlPnItemList.getViewport();
    jScrlPnItemList.getViewport();
    jScrlPnItemList.getViewport();
    pnlClass.setMainForm(this);
    pnlField.setMainForm(this);
    pnlExecutableElement.setMainForm(this);
    pnlEditControls.add(pnlClass, "pnlClassControls");
    pnlEditControls.add(pnlField, "fieldControls");
    pnlEditControls.add(pnlExecutableElement, "methodControls");
    pnlEditControls.add(jPnlBlank, "jPnlBlank");
    jScrlPnCodeView.getViewport();
    cardLayout1.show(pnlEditControls,"jPnlBlank");*/
  }

  //File | Exit action performed
 /** Event handler for the user choosing File|Exit from the menu. Ask the user
  * if the file should be saved before exiting.
  *
  * @param e The event object from the menu item selection action
  */
  public void fileExit_actionPerformed(ActionEvent e) {
   // askExit();
  }

  //Help | About action performed
 /** Event handler for the user picking Help|About from the menu. Display the
  * "About" dialog box.
  *
  * @param e The event produced by the menu item
  */
  public void helpAbout_actionPerformed(ActionEvent e) {
    //frmDocWiz_AboutBox dlg = new frmDocWiz_AboutBox(this,true);
  }






  /**
  * Sets flag marking current file as having been modified.
  * If the comment preview window is open, updates it.
  */

  /*void checkAndUpdatePreview () {
    if ((frmPreview != null) && (frmPreview.isVisible())) {
      CodeComment currentComment = getCurrentComment();
      if (currentComment != null) {
        frmPreview.setCodeComment(currentComment);
      }
    }

  }*/

 /**   */
 /*
*/




 /**   */
 // void mnItmInputJavaSource_actionPerformed(ActionEvent e) {
    //dlgInputFile(Frame frame, String title, boolean modal) {
    // dlgInputFile d = new dlgInputFile(new Frame(), "Enter Java Source", true);
    // d.setVisible(true);
    // String theInput = d.getInputString();
    // d.dispose();
    //    StringBufferInputStream bis  = new StringBufferInputStream(theInput);
    //    doOpenFile(bis,"(Entered Java Source)");
 // }





 /**   */


  void touchFile() {
    currentUnit.touchFile();
  }

 /**   */
  void this_windowClosing(WindowEvent e) {
    //askExit();
  }
  /** Check to see if the file has been externally modified since it was
  * loaded
  */




}

/** Class to check if the file has changed since it was loaded */

//////////////////////
//////////////////////
//////////////////////         Adapters
//////////////////////
//////////////////////
//////////////////////


