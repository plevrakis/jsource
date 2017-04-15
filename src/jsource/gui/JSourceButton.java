package jsource.gui;


/**
 * JSourceButton.java 06/18/03
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <code>JSourceButton</code> is a modified <code>JButton</code>
 * that provides rollover and highlight capabilities.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class JSourceButton extends JButton {
    private MouseHandler mouseListener = null;
    private ImageIcon grayedIcon, coloredIcon = null;
    private Color nColor = null;
    private static Color commonHighlightColor = new Color(192, 192, 210);
    private static boolean rollover = false;
    private static boolean blockHighlightChange = false;

    public static void setRollover(boolean enabled) {
        rollover = enabled;
    }

    public static void setHighlightColor(Color color) {
        if (!blockHighlightChange)
            commonHighlightColor = color;
    }

    public static Color getHighlightColor() {
        return commonHighlightColor;
    }

    public static void blockHighlightChange() {
        blockHighlightChange = true;
    }

    public static void unBlockHighlightChange() {
        blockHighlightChange = false;
    }

    private void init() {
        mouseListener = new MouseHandler();
        if (rollover) {
            setBorderPainted(false);
            addMouseListener(mouseListener);
        } else {
            nColor = getBackground();
            addMouseListener(mouseListener);
        }
    }

    public JSourceButton(Action action) {
        super(action);
        init();
    }

    public JSourceButton(String text) {
        super(text);
        init();
    }

    class MouseHandler extends MouseAdapter {
        public void mouseEntered(MouseEvent me) {
            if (isEnabled()) {
                if (rollover) {
                    setBorderPainted(true);
                } else {
                    setBackground(commonHighlightColor);
                    setForeground(Color.black);
				}
            }
        }

        public void mouseExited(MouseEvent me) {
            if (isEnabled()) {
                if (rollover)
                    setBorderPainted(false);
                else {
                    setBackground(nColor);
                    setForeground(Color.white);
				}
            }
        }
    }

    protected void finalize() throws Throwable {
        removeMouseListener(mouseListener);
        super.finalize();
        mouseListener = null;
        nColor = null;
    }
}
