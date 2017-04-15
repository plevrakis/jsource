package jsource.util;


/**
 * @(#)GUIUtilities.java	12/18/02
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 */

import jsource.syntax.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;


/**
 * <code>GUIUtilities</code> holds useful GUI utilities.
 *
 * @author Panagiotis Plevrakis
 * <pre>
 * Email: pplevrakis@hotmail.com
 * URL:   http://jsource.sourceforge.net/
 * </pre>
 */
public class GUIUtilities {

    private static String imagePath = JSConstants.IMAGE_PATH;
    private static String templatePath = JSConstants.TEMPLATE_PATH;
    private static Dimension screenSize = JSConstants.SCREEN_SIZE;
    private static Toolkit toolkit = JSConstants.TOOLKIT;

    /**
     * Convenience method that creates and returns <code>ImageIcon</code> objects used by toolbar buttons.
     * @param imageName the name of the image
     * @return a new <code>ImageIcon</code> object
     */
    public static ImageIcon createIcon(String imageName) {
        String imageLocation = imagePath + imageName;

        return new ImageIcon(imageLocation);
    }

    /**
     * Convenience method that creates and returns a string that holds the template's location.
     * @param template the name of the template file
     * @return the path with the template location
     */
    public static String getTemplateLocation(String template) {
        String templateLocation = templatePath + template + ".txt";

        return templateLocation;
    }

    /**
     * Displays the exception message in a <code>JOptionPane</code>.
     * @param s the exception message
     */
    public static void showExceptionErrorMessage(String s) {
        JOptionPane.showMessageDialog(null, s, s, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a user message in a <code>JOptionPane</code>.
     * @param message the message
     */
    public static void showMessage(String message) {
    	JOptionPane.showMessageDialog(null, message, "Message",
                                  		JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays an error message in a <code>JOptionPane</code>.
     * @param message The message to display
     */
    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error",
                                  		JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Converts a hex color value prefixed with #, for example #ff0088.
     * @param name the color value
     */
    public static Color parseColor(String name) {
		if (name == null) {
			  return Color.black;
		  } else if (name.startsWith("#")) {
			  try {
				  return Color.decode(name);
			  } catch (NumberFormatException nfe) {
				  return Color.black;
			  }
		}
		return Color.black;
    }

    /**
     * Converts a color object to its hex value. The hex value prefixed is with #, for example #ff0088.
     * @param c the <code>Color</code> object
     */
    public static String getColorHexString(Color c) {
	    String colString = Integer.toHexString(c.getRGB() & 0xffffff);
	    return "#000000".substring(0, 7 - colString.length()).concat(colString);
    }

    /**
     * Converts a style string to a style object.
     * @param str the style string
     * @exception <code>IllegalArgumentException</code> if the style is invalid
     */
    public static SyntaxStyle parseStyle(String str) throws IllegalArgumentException {
	    Color color = Color.black;
	    boolean italic = false;
	    boolean bold = false;
	    StringTokenizer st = new StringTokenizer(str);
	    while (st.hasMoreTokens()) {
		    String s = st.nextToken();
		    if (s.startsWith("color:")) {
			    color = GUIUtilities.parseColor(s.substring(6));
		    } else if (s.startsWith("style:")) {
			    for (int i = 6; i < s.length(); i++) {
				    if (s.charAt(i) == 'i')
					    italic = true;
				    else if (s.charAt(i) == 'b')
					    bold = true;
				    else
					    throw new IllegalArgumentException("Invalid style: " + s);
			    }
		    } else {
			    throw new IllegalArgumentException("Invalid directive: " + s);
		    }
	    }
	    return new SyntaxStyle(color, italic, bold);
    }

    /**
     * Converts a style into it's string representation.
     * @param style the <code>SyntaxStyle</code>
     */
    public static String getStyleString(SyntaxStyle style) {
	    StringBuffer buf = new StringBuffer();

	    buf.append("color:" + getColorHexString(style.getColor()));
	    if (!style.isPlain()) {
		    buf.append(" style:" + (style.isItalic() ? "i" : "") + (style.isBold() ? "b" : ""));
	    }
	    return buf.toString();
    }

    /**
     * Centers a window on the screen.
     * @param cmp the <code>Component</code> to center
     */
    public static void centerComponent(Component cmp) {
        cmp.setLocation(new Point((screenSize.width - cmp.getSize().width) / 2,
                        (screenSize.height - cmp.getSize().height) / 2));
    }

    /**
     * Centers a dialog within its parent.
     * @param parent the parent <code>Component</code>
     * @param child the <code>Component</code> to center
     */
    public static void centerComponentChild(Component parent, Component child) {
        Rectangle par = parent.getBounds();
        Rectangle chi = child.getBounds();
        child.setLocation(new Point(par.x + (par.width - chi.width) / 2,
                                par.y + (par.height - chi.height) / 2));
    }

    /**
     * "Beeps" the user.
     */
    public static void beep() {
        toolkit.beep();
    }

    /**
     * Long operations need to display an hourglass.
     * @param comp the <code>Component</code> on which to apply the hour glass cursor
     * @param on if true, we set the cursor on the hourglass
     */
    public static void setCursorOnWait(Component comp, boolean on) {
        if (on) {
            comp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
