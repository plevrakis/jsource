
/**
 * Title:        DocWiz Java Documentation Tool<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Simon Arthur<p>
 * Company:      <p>
 * @author Simon Arthur
 * @version 1.0
 */
package tinyplanet.docwiz;

import tinyplanet.gui.ConcreteAppPane;
import javax.swing.*;
import java.util.Enumeration;
import java.awt.*;
import java.awt.event.*;

/** AppPane for displaying those tags which aren't known about and hard coded
 *  somewhere else in the source.
 */
public class UnknownTagAppPane extends ConcreteAppPane
  implements ItemChangedListener
{

  GridLayout gridLayout1 = new GridLayout(1, 1);

  UnknownTagPanel panel = new UnknownTagPanel();

  CommentableCode currentCode;

  public UnknownTagAppPane() {
  	this.setTitle("Unknown Tags");
	this.setLayout(gridLayout1);
	this.add(panel);
  }

  /** When the user selected item changes, change the display.
   *
   * @param ie ItemChangedEvent with information about the change.
   */
  public void itemChanged(ItemChangedEvent ie) {
    CommentableCode currentCode = ie.getCurrentCode();
    this.panel.doneEditing();
    if (currentCode != null) {
        this.panel.setCurrentComment(currentCode.getCodeComment());
    }
  }

}