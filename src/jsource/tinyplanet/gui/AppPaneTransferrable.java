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
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/** Transferrable class for AppPanes in order to facilitate Drag and Drop of Panes
 *
 * @author Simon Arthur
 */
public class AppPaneTransferrable implements Transferable {
  /** MIME type of transferrable, indicating that it's a JVM object
   */
  static final String MIME_TYPE = DataFlavor.javaJVMLocalObjectMimeType;
  /** Human readable description of the dragged object's class
   */
  static final String HUMAN_NAME = "AppPane";
  /** Class name for appending to the MIME type.
   */
  static final String CLASS_NAME = ";class=tinyplanet.gui.AppPane";
  /** The AppPane which is being transferred
   */
  AppPane appPane;

  /**    */
  public static DataFlavor apFlavor;
  /**    */
  public static DataFlavor localApFlavor;

  public static DataFlavor[] flavors;

  static {
    try {
      apFlavor = new DataFlavor(AppPane.class, HUMAN_NAME);
      localApFlavor = new DataFlavor(MIME_TYPE + CLASS_NAME, HUMAN_NAME);
    } catch(Exception e) {
       System.err.println(e);
    }
    flavors = new DataFlavor[] { apFlavor, localApFlavor};
  }

  /** Supported data flavors
   *
   * @return DataFlavors supported
   */
  public DataFlavor[] getTransferDataFlavors() {
    return flavors;
  }

  /** Checks to see if the data flavor provided is supported. This class compares the MIME type and
   * the human readable name of the DataFlavor provided and matches them agains the internal MIME
   * type and human readable name.
   *
   * @param flavor The DataFlavor to check
   * @return true if the DataFlavor is supported, false otherwise.
   */
  public boolean isDataFlavorSupported(DataFlavor flavor) {
//  	System.out.println("AppPaneTransferrable.isDataFlavorSupported: (" + flavor.getMimeType() + ", " + flavor.getHumanPresentableName() + ")");
    if (MIME_TYPE.equals(flavor.getMimeType()) ||
        HUMAN_NAME.equals(flavor.getHumanPresentableName())) {
      return true;
    } else {
      return false;
    }
  }


  /** Create a new Transferrable for AppPanes
   *
   * @param theAppPane AppPane to provide transfer services for
   */
  public AppPaneTransferrable(AppPane theAppPane)  {
    this.appPane = theAppPane;
  }

    /** Request the transferrable data, in this case, an AppPane.
     *
     * @param flavor The requested flavor. Hopefully the calling code will ask
     *      for an AppPane.
     * @exception UnsupportedFlavorException if the requested data flavor is
     *     not supported.
     * @exception IOException if the data is no longer available in the
     *     requested flavor.
     * @return The AppPane to be transferred
     */
    public synchronized Object getTransferData(DataFlavor flavor) throws
      UnsupportedFlavorException, IOException {
      if (isDataFlavorSupported(flavor)) {
        return appPane;
      } else {
        throw new UnsupportedFlavorException(flavor);
      }
    }

}
