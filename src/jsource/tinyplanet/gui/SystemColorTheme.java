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

import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.*;
import javax.swing.*;
import java.awt.*;
/** Color theme for pulling the user's default colors into a MetalLookAndFeel application.
 */
public class SystemColorTheme extends DefaultMetalTheme {
    /**      */
    private FontUIResource controlFont;
    /**      */
    private FontUIResource systemFont;
    /**      */
    private FontUIResource userFont;
    /**      */
    private FontUIResource smallFont;

    /**      */
    private final ColorUIResource primary1 = new ColorUIResource(102, 102, 153);
    /**      */
    private final ColorUIResource primary2 = new ColorUIResource(153, 153, 204);
    /**      */
    private final ColorUIResource primary3 = new ColorUIResource(204, 204, 255);

    /**      */
    private ColorUIResource secondary1;
    /**      */
    private ColorUIResource secondary2;
    /**      */
    private ColorUIResource secondary3;
    private ColorUIResource controlFontColor;

  /**    */
  public String getName() { return "System Colors"; }
  /**    */
  public SystemColorTheme() {
      try {
        controlFont = new FontUIResource(Font.getFont("swing.plaf.metal.controlFont", new Font("Dialog", Font.BOLD, 12)));
        systemFont = new FontUIResource(Font.getFont("swing.plaf.metal.systemFont", new Font("Dialog", Font.PLAIN, 12)));
        userFont = new FontUIResource(Font.getFont("swing.plaf.metal.userFont", new Font("Dialog", Font.PLAIN, 12)));
        smallFont = new FontUIResource(Font.getFont("swing.plaf.metal.smallFont", new Font("Dialog", Font.PLAIN, 10)));
      } catch (Exception e) {
        controlFont = new FontUIResource("Dialog", Font.BOLD, 12);
        systemFont =  new FontUIResource("Dialog", Font.PLAIN, 12);
        userFont =  new FontUIResource("Dialog", Font.PLAIN, 12);
        smallFont = new FontUIResource("Dialog", Font.PLAIN, 10);
      }
    }

    /**      */
    protected ColorUIResource getPrimary1() { return primary1; }
    /**      */
    protected ColorUIResource getPrimary2() { return primary2; }
    /**      */
    protected ColorUIResource getPrimary3() { return primary3; }
    /**      */
    protected ColorUIResource getSecondary1() {
      if (secondary1 == null)
              secondary1 =new ColorUIResource(SystemColor.controlShadow);//new ColorUIResource(102, 102, 102);
      return secondary1;
    }
    /**      */
    protected ColorUIResource getSecondary2() {
      if (secondary2 == null)
        secondary2 = new ColorUIResource(SystemColor.controlHighlight);//new ColorUIResource(153, 153, 153);

      return secondary2;
    }

    /**      */
    protected ColorUIResource getSecondary3() {
      if (secondary3 == null)
         secondary3 = new ColorUIResource(SystemColor.control);//new ColorUIResource(204, 204, 204);
      return secondary3;
    }
  /* public ColorUIResource getControlDarkShadow() {
     return ;
   }*/
  public ColorUIResource getControlTextColor() {
     if (controlFontColor == null)
        controlFontColor = new ColorUIResource(SystemColor.controlText);//new ColorUIResource(204, 204, 204);
     return controlFontColor;
   }
  public ColorUIResource getMenuForeground() {
    return new ColorUIResource(SystemColor.menuText);
  }
}
