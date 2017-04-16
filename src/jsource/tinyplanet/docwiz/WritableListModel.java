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

import javax.swing.AbstractListModel;
import java.util.*;

 /** A ListModel for use with Swing components, with easy accesssor methods for adding new elements
  * to the list
  *
  * @author Simon Arthur
  */
public class WritableListModel extends AbstractListModel {
 /** A Vector of objects for use in the list
  *
  *
  */
  Vector items = new Vector();

 /** Add a new item to the end of the list
  *
  * @param o The item to add to the list
  */
  public void addItem(Object o) {
    items.addElement(o);
    super.fireIntervalAdded(this,items.size()-1,items.size()-1);
  }
 /**
  * Returns the length of the list.
  *
  *
  * @return The number of items in the list
  */
  public int getSize() {
    return items.size();
  }

 /**
  * Returns the value at the specified index.
  *
  * @param index The index of the element to look up
  * @return The element at the specified location
  */
  public Object getElementAt(int index) {
    return items.elementAt(index);
  }
}