package jsource.gui;

/**
 * StatusOutput.java	03/29/02
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
 * <code>StatusOutput</code> is a <code>JPanel</code> that holds a <code>JTextArea</code>
 * used to display the output of external commands.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class StatusOutput extends JPanel {
    private JTextArea area = null;
    private JPanel bottom = null;

    public StatusOutput() {
        area = new JTextArea(15, 150);
        area.setEditable(false);
        area.setBackground(Color.black);
        area.setForeground(Color.white);
        area.setFont(new Font("Courier", Font.PLAIN, 14));
        add(area, BorderLayout.CENTER);
    }

    public void showOutput(String get) {
        area.append(get);
    }
}
