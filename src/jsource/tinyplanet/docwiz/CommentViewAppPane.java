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
//	5/2/2001 Lee Meador - don't update the comment string unless it changes.
//				Note the bug related to the scroll position mentioned below.
//	6/15/2001 lmm	- fix small bug with previous fix
//	6/20/2001 lmm	- Set the tab size in the display pane to match the users configuration.
//					- Restore the prior scroll position after rewriting the text pane
//					contents so this pane does not jump to the top upon auto-update.
//					- Make attributes and methods private, public, etc. as appropriate.
//	6/26/2001 lmm	- change to courier and simplify building page


package tinyplanet.docwiz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import tinyplanet.gui.ConcreteAppPane;

public class CommentViewAppPane extends ConcreteAppPane
   implements ItemChangedListener
{
  private JTextArea textArea1 = new JTextArea();
  private GridLayout lm = new GridLayout(1,1);
  private JScrollPane jScrollPane1 = new JScrollPane();
  private CodeComment codeComment;
  private CommentUpdater updater;

  public CommentViewAppPane() {
    ConfigurationService configurationService = ConfigurationService.getConfigurationService();

    textArea1.setEditable(false);
    textArea1.setAutoscrolls(false);
    textArea1.setText("");
	textArea1.setFont(new Font("Courier", 0, 12));
	textArea1.setTabSize(configurationService.getTabSize());

    this.setLayout(lm);
    this.add(jScrollPane1);

    jScrollPane1.getViewport().add(textArea1);

    this.setTitle("Formatted Comment");
  }

  public void itemChanged(ItemChangedEvent ie)
  {
    tinyplanet.docwiz.CommentableCode c = ie.getCurrentCode();
    setCodeComment(c.getCodeComment());
  }

  protected void setDisplayText(String s) {
  	Point position = jScrollPane1.getViewport().getViewPosition();
    textArea1.setText(s);
  	jScrollPane1.getViewport().setViewPosition(position);
  }

  public void setCodeComment(CodeComment newc) {
    codeComment = newc;
    if (updater != null) {
      updater.stop();
    }
    updater = new CommentUpdater(this);
    updater.start();
  }

  /**
   * Standard getter method returns curent value.
   *
   * @return The current value of the codeComment attribute.
   */
  public CodeComment getCodeComment() {
  	return codeComment;
  }

  /*void this_windowClosing(WindowEvent e) {
    if (updater != null) {
      updater.stop();
    }
  }*/

}

class CommentUpdater extends Thread
{
  private CommentViewAppPane f;
  private String lastJavaDocString = null;

  public CommentUpdater(CommentViewAppPane newF)
  {
    f = newF;
  }

  public void run() {
    CodeComment codeComment;
    try {
      while (true) {
        codeComment = f.getCodeComment();
        if (codeComment != null) {
		  String javaDocString = codeComment.toJavaDocString();
		  if (!javaDocString.equals(lastJavaDocString)) {
	          lastJavaDocString = javaDocString;

			  // NOTE: There is a bug here in that this moves the scroll bar to the top
			  //  and it should stay in the same place if not below the end.

    	      f.setDisplayText(lastJavaDocString);
          	  // f.jScrollPane1.validate();
		  }
        }
        Thread.sleep(2000);
      }
    } catch ( InterruptedException ie) {
      return;
    }
  }

}

