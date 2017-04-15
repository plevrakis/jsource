package jsource.gui;


/**
 * NewProjectDialog.java  06/19/03
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
 * <code>NewProjectDialog</code> is a new project dialog box.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class NewProjectDialog extends BaseDialog {
    private XMLResourceBundle bundle = null;
    private MainFrame mainFrame = null;
    private NewProjectPanel projPanel = null;
    private JButton okButton = null;
    private JButton cancelButton = null;

    NewProjectDialog(XMLResourceBundle bundle, MainFrame mainFrame) {
        super(null, "", true);
        this.bundle = bundle;
        this.mainFrame = mainFrame;
        setTitle(bundle.getValueOf("newproj"));

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(6, 6, 6, 12));
        setContentPane(content);

		projPanel = new NewProjectPanel(bundle, mainFrame);
        content.add(BorderLayout.CENTER, projPanel);
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        buttonPanel.add(Box.createGlue());
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        okButton.addActionListener(new OKButtonHandler());
        cancelButton.addActionListener(new CancelButtonHandler());
        getRootPane().setDefaultButton(cancelButton);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createGlue());
        content.add(BorderLayout.SOUTH, buttonPanel);
        setSize(710,480);
		setResizable(false);
		setLocationRelativeTo(null);
        show();
    }

    public void ok() {
        projPanel.createNewProject();
    }

    public void cancel() {
        dispose();
    }

    class OKButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            ok();
        }
    }

    class CancelButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            cancel();
        }
    }
}
