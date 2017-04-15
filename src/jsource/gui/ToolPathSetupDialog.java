package jsource.gui;


/**
 * ToolPathSetupDialog.java  04/25/03
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
import java.io.File;
import jsource.*;
import jsource.io.*;
import jsource.io.localization.*;
import jsource.util.*;



/**
 * <code>ToolPathSetupDialog</code> is a dialog box for setting the path to external tools.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class ToolPathSetupDialog extends BaseDialog {
    private MainFrame mainFrame = null;
    private JSEditor editor = null;
    private XMLResourceBundle bundle = null;
    private Toolkit toolkit = JSConstants.TOOLKIT;
    private jsource.io.FileReader reader = null;
    private JButton okButton = null;
    private JButton cancelButton = null;
    private JButton findSDKButton = null;
    private JButton findANTButton = null;
    private JButton findBrowserButton = null;
    private JButton findCoreAPIButton = null;
	private JTextField sdkPathValue = null;
    private JTextField antPathValue = null;
	private JTextField browserPathValue = null;
	private JTextField coreAPIPathValue = null;


    ToolPathSetupDialog(MainFrame mainFrame, JSEditor editor, XMLResourceBundle bundle) {
        super(null, "", true);
        this.mainFrame = mainFrame;
        this.editor = editor;
        this.bundle = bundle;
        reader = new jsource.io.FileReader(editor, mainFrame);
        setTitle(bundle.getValueOf("pathsetitle"));
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        content.add(BorderLayout.CENTER, new ToolPathPanel());
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
        setSize(670,210);
        setResizable(false);
        setLocationRelativeTo(null);
        show();
    }

    private String getToolPath() {
		String path = "Undefined";
		File filePath = reader.openFile();
		if (filePath != null) {
			path = filePath.getAbsolutePath();
	    }
		return path;
	}

    public void ok() {

		dispose();
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

    class FindSDKButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
			String sdk = getToolPath();
	    	sdkPathValue.setText(sdk);
	    	mainFrame.jsprops.setSDK(sdk);
        }
    }

    class FindANTButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
			String ant = getToolPath();
			antPathValue.setText(ant);
	    	mainFrame.jsprops.setANT(ant);
        }
    }

    class FindBrowserButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
			reader.setSearchForEXE(true);
			String browser = getToolPath();
			browserPathValue.setText(browser);
	    	mainFrame.jsprops.setBrowser(browser);
        }
    }

    class FindCoreAPIButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
			reader.setIsAPI(true);
			String coreAPI = getToolPath();
			coreAPIPathValue.setText(coreAPI);
	    	mainFrame.jsprops.setCoreAPI(coreAPI);
        }
    }

    class ToolPathPanel extends JPanel {
		private JPanel buttonPanel, fieldsPanel = null;

		ToolPathPanel() {
			Container pane = getContentPane();
			buttonPanel = new JPanel();
			fieldsPanel = new JPanel();
			sdkPathValue = new JTextField(60);
			antPathValue = new JTextField(60);
			browserPathValue = new JTextField(60);
			coreAPIPathValue = new JTextField(60);
			sdkPathValue.setText(mainFrame.sdkPath);
			antPathValue.setText(mainFrame.antPath);
			browserPathValue.setText(mainFrame.browserPath);
			coreAPIPathValue.setText(mainFrame.coreAPIPath);
			findSDKButton = new JButton(bundle.getValueOf("findsdk"));
			findANTButton = new JButton(bundle.getValueOf("findant"));
			findBrowserButton = new JButton(bundle.getValueOf("findbrowser"));
			findCoreAPIButton = new JButton(bundle.getValueOf("findcoreapi"));
			findSDKButton.addActionListener(new FindSDKButtonHandler());
			findANTButton.addActionListener(new FindANTButtonHandler());
			findBrowserButton.addActionListener(new FindBrowserButtonHandler());
			findCoreAPIButton.addActionListener(new FindCoreAPIButtonHandler());
			buttonPanel.setLayout(new GridLayout(4,0));
			fieldsPanel.setLayout(new GridLayout(4,0,0,7));
			buttonPanel.add(findSDKButton);
			buttonPanel.add(findANTButton);
			buttonPanel.add(findBrowserButton);
			buttonPanel.add(findCoreAPIButton);
			fieldsPanel.add(sdkPathValue);
			fieldsPanel.add(antPathValue);
			fieldsPanel.add(browserPathValue);
			fieldsPanel.add(coreAPIPathValue);
			pane.setLayout(new FlowLayout());
			pane.add(buttonPanel);
			pane.add(fieldsPanel);
			pack();
		}
	}
}