package jsource.gui;


/**
 * JarViewerDialog.java  06/30/03
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

import javax.swing.border.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import jsource.*;
import jsource.util.*;
import jsource.io.localization.*;

/**
 * <code>JarViewerDialog</code> provides a viewer of compressed .jar files.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class JarViewerDialog extends BaseDialog {
    private XMLResourceBundle bundle = null;
    private JButton close = null;

    JarViewerDialog(XMLResourceBundle bundle, File file) {
        super(null, "", true);
        this.bundle = bundle;
        setTitle(bundle.getValueOf("jarview"));

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        content.add(BorderLayout.CENTER, new JarViewerPanel(file));

        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        buttonPanel.add(Box.createGlue());
        close = new JButton(bundle.getValueOf("close"));
        close.addActionListener(new ActionHandler());
        getRootPane().setDefaultButton(close);
        buttonPanel.add(close);
        buttonPanel.add(Box.createGlue());
        content.add(BorderLayout.SOUTH, buttonPanel);
        setSize(1000, 650);
        setResizable(false);
        setLocationRelativeTo(null);
        show();
    }

    public void ok() {
        dispose();
    }

    public void cancel() {
        dispose();
    }

    class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            ok();
        }
    }
}
