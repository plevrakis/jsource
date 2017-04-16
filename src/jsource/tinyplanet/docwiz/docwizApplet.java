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
import java.applet.*;

 /** An applet which can invoke DocWiz
  *
  * @author Simon Arthur
  */
public class docwizApplet extends Applet {
 /** The applet's layout manager
  *
  */
   /**   */
  boolean isStandalone = false;
 /**   */
  frmDocWiz frame;

  //Get a parameter value
 /**   *
  * @param def
  * @param key
  */
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
 /**   */
  public docwizApplet() {
    //netscape.security.PrivilegeManager.enablePrivilege("UniversalFileAccess");
  }

  //Initialize the applet
 /**   */
  public void init() {
    try { jbInit(); } catch (Exception e) { e.printStackTrace(); }
        frame = new frmDocWiz();
    frame.pack();
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getPreferredSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);

  }

  //Component initialization
 /**   */
  private void jbInit() throws Exception{

  }

  //Start the applet
 /**   */
  public void start() {
   frame.setVisible(true);
  }

  //Stop the applet
 /**   */
  public void stop() {
    frame.setVisible(false);
  }

  //Destroy the applet
 /**   */
  public void destroy() {
    frame.dispose();
  }

  //Get Applet information
 /**   */
  public String getAppletInfo() {
    return "Applet to launch Mail Program";
  }

  //Get parameter info
 /**   */
  public String[][] getParameterInfo() {
    return null;
  }

  //Main method
 /**   */
  public static void main(String[] args) {
    docwizApplet applet = new docwizApplet();
    applet.isStandalone = true;
    Frame frame = new Frame();
    frame.setTitle("Applet Frame");
    frame.add(applet, BorderLayout.CENTER);
    applet.init();
    applet.start();
    frame.pack();
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
    frame.setVisible(true);
  }
}
