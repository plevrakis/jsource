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
//	7/12/2001 lmm	- add grey versions of the icons for use with private code items
//					when the preferences say they should be grey.
//					- add an intermediate icon that is half-way between done and not.
//	7/16/2001 lmm	- make the text a grey color to match icon (when appropriate)

package tinyplanet.docwiz;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.ImageIcon;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.awt.SystemColor;

/** A ListCellRenderer to show code elements in a list. Each cell will
 * be rendered as a piece of text with an associated icon
 */
public class CodeCommentListCellRenderer extends JLabel implements ListCellRenderer
{

  /** The icon to use when the element is in the "incomplete"
   * description state
   */
  ImageIcon incompleteIcon;

  /** The icon to use when the element is in the "half done"
   * description state
   */
  ImageIcon halfDoneIcon;

  /** The icon to use when the element is in the "incomplete"
   * description state
   */
  ImageIcon completeIcon;

  /** The icon to use when the element is in the "incomplete"
   * description state and the code is private and needs to be grey
   */
  ImageIcon incompleteGreyIcon;

  /** The icon to use when the element is in the "half done"
   * description state and the code is private and needs to be grey
   */
  ImageIcon halfDoneGreyIcon;

  /** The icon to use when the element is in the "incomplete"
   * description state and the code is private and needs to be grey
   */
  ImageIcon completeGreyIcon;

  /** Implementation from ListCellRenderer base class
   *
   * @param isSelected
   * @param cellHasFocus
   * @param index
   * @param value
   * @param list
   */
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    if (isSelected) {
      this.setForeground(list.getSelectionForeground()); // /* Color.red*/: list.getForeground());
      this.setBackground(list.getSelectionBackground()); ///*SystemColor.activeCaption */: Color.cyan /*list.getBackground()*/);
    } else  {
      this.setForeground(list.getForeground());
      this.setBackground(list.getBackground());
    }

    if (value instanceof CommentableCode) {
      CommentableCode c = (CommentableCode)value;
      int commentLevel = c.getCodeComment().getCommentLevel();
	  String scope = c.getScope();
      ConfigurationService config = ConfigurationService.getConfigurationService();
	  boolean isGrey = false;
	  if (scope.equals("private") && c.isPrivateGrey()) {
//	  	if ((c instanceof tinyplanet.docwiz.Field && config.getFieldAppearanceInList() == 1) ||
//		  	(c instanceof tinyplanet.docwiz.Class && config.getClassAppearanceInList() == 1) ||
//		  	(c instanceof tinyplanet.docwiz.Interface && config.getInterfaceAppearanceInList() == 1) ||
//		  	(c instanceof tinyplanet.docwiz.Method && config.getMethodAppearanceInList() == 1) ||
//			(c instanceof tinyplanet.docwiz.Constructor && config.getConstructorAppearanceInList() == 1)
//			) {
		  isGrey = true;
//	  	}
	  }

	  if (isGrey) {
		setForeground(SystemColor.controlShadow);
	  }

      switch (commentLevel) {
        case 0:
			if (isGrey) {
	            this.setIcon(incompleteGreyIcon);
			}
			else  {
	            this.setIcon(incompleteIcon);
			}
            break;
        case 1:
        case 2:
        case 3:
			if (isGrey) {
	            this.setIcon(halfDoneGreyIcon);
			}
			else  {
	            this.setIcon(halfDoneIcon);
			}
            break;
        case 4:
			if (isGrey) {
	            this.setIcon(completeGreyIcon);
			}
			else  {
	            this.setIcon(completeIcon);
			}
            break;
      }

      this.setText(value.toString());

    } else {
      this.setText(value.toString());
      this.setIcon(incompleteIcon);
    }
    return this;
  }

  public CodeCommentListCellRenderer() {
    incompleteIcon = _readIcon("incomplete.gif");
    halfDoneIcon = _readIcon("halfdone.gif");
    completeIcon = _readIcon("complete.gif");

//    incompleteGreyIcon = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon)incompleteIcon).getImage()));
//    halfDoneGreyIcon = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon)halfDoneIcon).getImage()));
//    completeGreyIcon = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon)completeIcon).getImage()));

    incompleteGreyIcon = _readIcon("incomplete_grey.gif");
    halfDoneGreyIcon = _readIcon("halfdone_grey.gif");
    completeGreyIcon = _readIcon("complete_grey.gif");

    this.setOpaque(true);
  }

  /** Load in the icon with the given name as a resource. If the data can't be read,
   * return an empty icon.
   *
   * Since we're reading the icon as a resource, it can be safely stored in the program's
   * JAR file.
   *
   * @param name The name of the icon to read in.
   * @return an ImageIcon for the given name
   */
  protected ImageIcon _readIcon (String name) {
    byte[] imageData, buffer;
    InputStream is = getClass().getResourceAsStream(name);
    if  ( is != null) {
      ByteArrayOutputStream bos = new  ByteArrayOutputStream();
      int totalBytes =0 , bytesRead =0 ;
      try {
        do {
          buffer = new byte[is.available()];
          bytesRead = is.read(buffer);
          if (bytesRead != -1) {
            bos.write(buffer);
            totalBytes += bytesRead;
          }
        } while (bytesRead != -1 && is.available() != 0);
        is.close();
        bos.close();
        imageData = bos.toByteArray();
      } catch (java.io.IOException ioe ) {
        imageData = new byte[1];
      }
    } else {
    // there's a problem reading the icon
      System.err.println("Couldn't read icon '"+name+"', using default");
      imageData = new byte[1];
    }

    return new ImageIcon(imageData);
  }
}