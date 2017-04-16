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

import java.awt.Container;

/** An AppPane represents a draggable container which resides
 * within a Shelf.
 *
 * @author Simon Arthur
 */
public interface AppPane {

  /** Return the AppPane's title, typically to be used displayed
   * with the AppPane.
   *
   * @return The AppPane's title.
   */
  public String getTitle();
  /** Called by the system when it is ready for the AppPane to start.
   *
   */
  public void start();
  /** Called by the system when it is ready for the AppPane to stop.
   *
   */
  public void stop();
  /** Move the current AppPane to the given Sshelf.
   *
   * @param s The Shelf to move to.
   */
  public void moveTo(Shelf s);

  /**
   * This is here so that we don't have to do all that funky casting.
   */
  public Container getParent();

}
