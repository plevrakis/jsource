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
import java.awt.GridBagConstraints;
/**
 * A new implementation of GridBagConstraints with an additional convenience
 * constructor. The main purpose of this class is to provide a GPL'd implementation
 * of Borland's class by the same name.
 */
public class GridBagConstraints2 extends GridBagConstraints {

  /**    */
  public GridBagConstraints2(int newGridx, int newGridy, int newGridWidth,
      int newGridHeight, double newWeightx, double newWeighty,
      int newAnchor, int newFill, java.awt.Insets newInsets, int newIPadx,
      int newIPady) {
    super();
    this.gridx = newGridx;
    this.gridy = newGridy;
    this.gridwidth = newGridWidth;
    this.gridheight = newGridHeight;
    this.weightx = newWeightx;
    this.weighty = newWeighty;
    this.anchor = newAnchor;
    this.fill = newFill;
    this.insets = newInsets;
    this.ipadx = newIPadx;
    this.ipady = newIPady;
  }
}

