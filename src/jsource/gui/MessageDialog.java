package jsource.gui;


/**
 * MessageDialog.java  05/22/03
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
import jsource.*;
import jsource.util.*;
import jsource.io.localization.*;


/**
 * <code>MessageDialog</code> is a dialog for displaying messages to the user.
 * This dialog is used as an alternative to some JOptionPane calls.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class MessageDialog extends BaseDialog {

    private MainFrame mainFrame = null;
    private XMLResourceBundle bundle = null;
    private JButton close = null;
	private JButton setButton = null;
	private JLabel msgLabel = null;

    MessageDialog(String message, XMLResourceBundle bundle) {
		this(message, false, null, bundle);
	}

    MessageDialog(String message, boolean showSetButton, MainFrame frame, XMLResourceBundle bdl) {
        super(null, "JSource Message", true);
        mainFrame = frame;
        bundle = bdl;
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new BorderLayout());
        msgLabel = new JLabel(message);
        if (mainFrame != null) {
        	setButton = new JButton(bundle.getValueOf("setpath"));
        	setButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					dispose();
					new ToolPathSetupDialog(mainFrame, mainFrame.editor, bundle);
				}
			});
		}
		msgPanel.add(msgLabel, BorderLayout.CENTER);

        content.add(BorderLayout.CENTER, msgPanel);

        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        buttonPanel.add(Box.createGlue());
        close = new JButton(bundle.getValueOf("close"));
        close.addActionListener(new ActionHandler());
        getRootPane().setDefaultButton(close);
		if (showSetButton) {
			buttonPanel.add(setButton);
		}
        buttonPanel.add(close);
        buttonPanel.add(Box.createGlue());
        content.add(BorderLayout.SOUTH, buttonPanel);
        setResizable(false);
        Dimension screenSize = JSConstants.TOOLKIT.getScreenSize();
        Dimension messageSize = msgPanel.getPreferredSize();
        setLocation(screenSize.width / 2 - (messageSize.width / 2), screenSize.height / 2 - (messageSize.height / 2));
        pack();
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
