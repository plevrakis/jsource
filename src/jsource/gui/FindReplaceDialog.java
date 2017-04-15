package jsource.gui;


/**
 * FindReplaceDialog.java  03/28/03
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
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import java.util.*;
import jsource.*;
import jsource.util.*;
import jsource.io.localization.*;


/**
 * <code>FindReplaceDialog</code> is a find/replace dialog box.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class FindReplaceDialog extends BaseDialog {
    private XMLResourceBundle bundle = null;
    private MainFrame mainFrame = null;
    private FindReplacePanel findReplacePanel = null;
    private JButton close = null;

    FindReplaceDialog(XMLResourceBundle bundle, MainFrame mainFrame, JSEditor editor) {
        super(null, "", true);
        this.bundle = bundle;
        setTitle(bundle.getValueOf("findreplace"));
        this.mainFrame = mainFrame;
        findReplacePanel = new FindReplacePanel(bundle, mainFrame);
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        content.add(BorderLayout.CENTER, findReplacePanel);
		findReplacePanel.beginListeningTo(editor);
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
        pack();
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
            dispose();
        }
    }
}
