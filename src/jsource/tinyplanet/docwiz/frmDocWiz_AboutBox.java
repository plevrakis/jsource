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

// History:
//	7/9/2001 lmm	- reorder the things in the init function to make more sense to me.
//					- change the text color for the labels.
//					- center the popup box.

package tinyplanet.docwiz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.text.JTextComponent;


public class frmDocWiz_AboutBox extends JFrame implements ActionListener {
  JPanel panel1 = new JPanel();
  JPanel bevelPanel1 = new JPanel();
  frmDocWiz_InsetsPanel insetsPanel1 = new frmDocWiz_InsetsPanel();
  frmDocWiz_InsetsPanel insetsPanel3 = new frmDocWiz_InsetsPanel();
  JButton button1 = new JButton();
  JLabel label1 = new JLabel();
  JLabel label2 = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  GridLayout gridLayout1 = new GridLayout();
  String product = "DocWiz";
  String version = "";

  JTextArea textArea1 = new JTextArea();

  public frmDocWiz_AboutBox(java.awt.Dimension parentSize)
  {
    super();
    try {
      jbInit();
    }
    catch (Exception e) {
       System.err.println(e);
      e.printStackTrace();
    }
	this.setLocation(((int)parentSize.getWidth() - this.getWidth())/2,
		((int)parentSize.getHeight() - this.getHeight())/2);
  }

/*
  public frmDocWiz_AboutBox(Frame parent, boolean modal) {

    super(parent,modal);
    Frame f=(Frame)parent;
    //this.setLocationRelativeTo(f.jBtnOpen);

    try {
      jbInit();
    }
    catch (Exception e) {
      System.err.println(e);
      e.printStackTrace();
    }
  }
*/

  private void jbInit() throws Exception{

    button1.setText("OK");
    button1.addActionListener(new frmDocWiz_AboutBox_button1_actionAdapter(this));
    button1.setBackground(SystemColor.control);
    button1.addActionListener(this);

    insetsPanel1.setBackground(SystemColor.control);
    insetsPanel1.add(button1, null);

    textArea1.setText(ConfigurationService.COPYRIGHT_NOTICE);
    textArea1.setFont(new Font("Helvetica", 0, 12));
    textArea1.setColumns(50);

    label1.setText(product);
    label1.setFont(new Font("Dialog", 3, 18));
	label1.setBackground(SystemColor.control);
	label1.setForeground(SystemColor.controlText);

    label2.setText(version);
	label2.setBackground(SystemColor.control);
	label2.setForeground(SystemColor.controlText);

    gridLayout1.setRows(5);
    gridLayout1.setColumns(1);
    insetsPanel3.setLayout(gridLayout1);
    insetsPanel3.setBackground(SystemColor.control);
    insetsPanel3.setInsets(new Insets(10, 60, 10, 10));
    insetsPanel3.add(label1, null);
    insetsPanel3.add(label2, null);

    bevelPanel1.setLayout(borderLayout2);
    bevelPanel1.setBackground(SystemColor.control);
    bevelPanel1.add(insetsPanel3, BorderLayout.CENTER);

    panel1.setLayout(borderLayout1);
    panel1.add(bevelPanel1, BorderLayout.NORTH);
    panel1.add(textArea1, BorderLayout.CENTER);
    panel1.add(insetsPanel1, BorderLayout.SOUTH);

    //this.setModal(true);
    this.setBackground(SystemColor.control);
    this.setTitle("About");
    this.getContentPane().add(panel1, null);

    pack();
    //this.setSize(100,100);
    this.setVisible(true);
    //setResizable(false);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button1) {
    setVisible(false);
    dispose();
    }
  }

  void button1_actionPerformed(ActionEvent e) {

  }
}

class frmDocWiz_InsetsPanel extends JPanel {
  protected Insets insets;

  public Insets getInsets() {
    return insets == null ? super.getInsets() : insets;
  }

  public void setInsets(Insets insets) {
    this.insets = insets;
  }
}

class frmDocWiz_AboutBox_button1_actionAdapter implements java.awt.event.ActionListener {
  frmDocWiz_AboutBox adaptee;

  frmDocWiz_AboutBox_button1_actionAdapter(frmDocWiz_AboutBox adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.button1_actionPerformed(e);
  }
}