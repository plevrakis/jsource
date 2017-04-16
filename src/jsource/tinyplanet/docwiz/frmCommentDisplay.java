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
import javax.swing.*;
//import borland.jbcl.layout.*;
//import borland.jbcl.layout.GridBagConstraints2;

public class frmCommentDisplay extends JFrame {
  JTextArea textArea1 = new JTextArea();
  JButton btnOK = new JButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  CodeComment codeComment;
  CommentUpdater updater;

  public frmCommentDisplay() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception{
    this.setSize(new Dimension(508, 287));
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    this.setTitle("Comment");
    textArea1.setEditable(false);
    btnOK.setText("OK");
    btnOK.setBackground(SystemColor.control);
    btnOK.addActionListener(new frmCommentDisplay_btnOK_actionAdapter(this));
    btnOK.addActionListener(new frmCommentDisplay_btnOK_actionAdapter(this));
    this.getContentPane().setLayout(gridBagLayout1);

    this.getContentPane().add(jScrollPane1, new GridBagConstraints2(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 3), 55, 47));
    this.getContentPane().add(btnOK, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(9, 233, 14, 0), 0, 0));
    jScrollPane1.getViewport().add(textArea1);
  }



  void btnOK_actionPerformed(ActionEvent e) {
    this.setVisible(false);
    updater.stop();
    this.dispose();
  }

  void setDisplayText(String s) {
    textArea1.setText(s);
  }

  void setCodeComment(CodeComment newc) {
    codeComment = newc;
    if (updater != null) {
      updater.stop();
    }
    //updater = new CommentUpdater(this);
    updater.start();
  }

  void this_windowClosing(WindowEvent e) {
    if (updater != null) {
      updater.stop();
    }
  }

}

/*class CommentUpdater extends Thread {
  frmCommentDisplay f;
  public CommentUpdater(frmCommentDisplay newF) {
    f = newF;
  }

  public void run() {
    CodeComment codeComment;
    try {
      while (true) {
        codeComment = f.codeComment;
        if (codeComment != null) {
          f.setDisplayText(codeComment.toJavaDocString());
          // f.jScrollPane1.validate();
        }
        Thread.sleep(2000);
      }
    } catch ( InterruptedException ie) {
      return;
    }
  }

} */

class frmCommentDisplay_btnOK_actionAdapter implements java.awt.event.ActionListener {
  frmCommentDisplay adaptee;


  frmCommentDisplay_btnOK_actionAdapter(frmCommentDisplay adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.btnOK_actionPerformed(e);
  }
}