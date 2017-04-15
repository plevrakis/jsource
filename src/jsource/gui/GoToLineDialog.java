package jsource.gui;


/**
 * GoToLineDialog.java  04/05/03
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
 * <code>GoToLineDialog</code> is a "Go to line..." dialog box.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class GoToLineDialog extends BaseDialog {
    private XMLResourceBundle bundle = null;
    private MainFrame mainFrame = null;
    private Toolkit toolkit = JSConstants.TOOLKIT;
    private JButton okButton = null;
    private JButton cancelButton = null;
    private JLabel message = null;
    private JTextField line = null;
    private int lineNum = 0;
    private String input = null;

    GoToLineDialog(XMLResourceBundle bundle, MainFrame mainFrame) {
        super(null, bundle.getValueOf("gotoline"), true);

        this.bundle = bundle;
        this.mainFrame = mainFrame;
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        content.add(BorderLayout.CENTER, new GoToPanel());
		JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        buttonPanel.add(Box.createVerticalGlue());
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        okButton.addActionListener(new OKButtonHandler());
        cancelButton.addActionListener(new CancelButtonHandler());
        getRootPane().setDefaultButton(cancelButton);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createGlue());
        content.add(BorderLayout.SOUTH, buttonPanel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        show();
    }

    public void ok() {
		input = line.getText();
		try {
			if(input != null || input.trim() != "") {
				lineNum = Integer.parseInt(input);
				if (lineNum <= mainFrame.editor.getLineCount() && lineNum >= 1) {
					mainFrame.editor.setFirstLine(lineNum);
					mainFrame.editor.setCaretPosition(mainFrame.editor.getLineStartOffset(lineNum) - 1);
					mainFrame.editor.blinkCaret();
					dispose();
				} else {
					message.setText(bundle.getValueOf("lineoffrange"));
					toolkit.beep();
				}
			} else {
				message.setText(bundle.getValueOf("noline"));
				toolkit.beep();
			}
		} catch(NumberFormatException nfe) {
			message.setText(bundle.getValueOf("lineoffrange"));
			toolkit.beep();
		}
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

    class GoToPanel extends JPanel {
		GoToPanel() {
			Container pane = getContentPane();
			message = new JLabel(bundle.getValueOf("whatline"));
			line = new JTextField(5);
			pane.setLayout(new BorderLayout());
			pane.add(BorderLayout.NORTH, message);
			pane.add(BorderLayout.CENTER, line);
			pack();
		}
	}
}