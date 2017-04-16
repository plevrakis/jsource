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
package com.tinyplanet.gui;
import javax.swing.*;
import java.awt.*;

/** An implementation of the AppPane interface using JPanel.
 *
 * @author Simon Arthur
 * @version $Id: ConcreteAppPane.java,v 1.3 2000/07/12 22:27:36 chroma Exp $
 */
public class ConcreteAppPane extends javax.swing.JPanel implements AppPane {
  /**    *
   */
  Shelf s;
  /**    */
  Component textLabel;
  /**    */
  String title;
  /**    */
  public ConcreteAppPane() {
  }
  /**    */
  public ConcreteAppPane(Shelf s, String title) {
    this.s = s;
    this.title = title;
    textLabel = makeTextPanel(this.title);
    setBackground(SystemColor.control);
    //setForeground(SystemColor.control);
  }

  /**    */
  public String getTitle()  {
    return title;
  }

  /**    */
  public void setTitle(String newTitle)  {
    this.title = newTitle;
  }

  /** This method should be overridden by any classes which want to implement special behaviors
   * when the AppPane starts.
   *
   */
  public void start() {
  }

  /**    */
  public void stop() {
  }

  /**    */
  public void moveTo(Shelf s) {
  }

  /**    */
  protected Component makeTextPanel(String text) {
    JPanel panel = new JPanel(false);
    JLabel filler = new JLabel(text);
    filler.setHorizontalAlignment(JLabel.CENTER);
    filler.setBackground(SystemColor.control);
    panel.setLayout(new GridLayout(1, 1));
    panel.setBackground(SystemColor.control);
    panel.add(filler);
    return panel;
  }
}
