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
package tinyplanet.gui;

/** A Rack is a container for several Shelfs, which, in turn, contain AppPanes.
 *
 */
public interface Rack {

  /** Split the specified shelf, creating a new shelf to the left of it.
   *
   * @return The newly created shelf
   */
  public Shelf splitLeft(Shelf s);
  /**    */
  public Shelf splitRight(Shelf s);
  /**    */
  public Shelf splitUp(Shelf s);
  /**    */
  public Shelf splitDown(Shelf s);

  /**    */
  public void removeShelf(Shelf s) throws ShelfNotEmptyException;

  /**    */
  public Shelf[] getShelfs();
}
