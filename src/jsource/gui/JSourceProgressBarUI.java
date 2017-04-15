package jsource.gui;


/**
 * JSourceProgressBarUI.java 06/18/03
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
import java.awt.Color;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;


/**
 * <code>JSourceProgressBarUI</code> is new UI for progress bar.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class JSourceProgressBarUI extends BasicProgressBarUI {
    private static final Color START = new Color(38, 92, 147);
    private static final Color END = new Color(146, 173, 201);

    public static ComponentUI createUI(JComponent c) {
        return new JSourceProgressBarUI();
    }

    public void paint(Graphics g, JComponent c) {
        Insets b = progressBar.getInsets();
        int barRectX = b.left;
        int barRectY = b.top;
        int barRectWidth = progressBar.getWidth() - (b.right + barRectX);
        int barRectHeight = progressBar.getHeight() - (b.bottom + barRectY);
        int amountFull = getAmountFull(b, barRectWidth, barRectHeight);

        if (amountFull > 0) {
            GradientPaint painter = new GradientPaint(barRectX, barRectY,
                    START,
                    barRectX + barRectWidth, barRectY + barRectHeight,
                    END);
            Graphics2D g2 = (Graphics2D) g;

            g2.setPaint(painter);
            g2.fill(new Rectangle(barRectX, barRectY, amountFull, barRectHeight));
        }

        if (progressBar.isStringPainted())
            paintString(g, barRectX, barRectY, barRectWidth, barRectHeight, amountFull, b);
    }
}
