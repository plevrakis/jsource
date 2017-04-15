package jsource.gui;


/**
 * NewProjectPanel.java  06/19/03
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.text.*;
import java.io.*;
import jsource.*;
import jsource.syntax.*;
import jsource.syntax.tokenmarker.*;
import jsource.util.*;
import jsource.io.*;
import jsource.io.localization.*;

/**
 * <code>NewProjectPanel</code> creates a panel used to setup a new project.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class NewProjectPanel extends JPanel {

	private File rootFile = null;
	private File execFile = null;
	private File appletFile = null;
	private File directoryFile = null;
	private JTextField nameField = null;
	private JTextField rootField = null;
	private JTextField execField = null;
	private JTextField appletField = null;
	private JTextField buildField = null;
	private JTextField commandField = null;
	private JTextField outputField = null;
	private ClassPathPanel classPathPanel = null;
    private XMLResourceBundle bundle = null;
    private MainFrame frame = null;
    private JPanel labelPanel = null;
    private JPanel fieldsPanel = null;
    private JPanel buttonPanel = null;
    private JButton setRootButton = null;
    private JButton setExecButton = null;
    private JCheckBox appletBox = null;

    public NewProjectPanel(XMLResourceBundle bundle, MainFrame frame) {
		super(new GridLayout(2,0));
		this.bundle = bundle;
		this.frame = frame;

		labelPanel = new JPanel();
		fieldsPanel = new JPanel();
		buttonPanel = new JPanel();

		labelPanel.setLayout(new GridLayout(6,0,0,10));
		fieldsPanel.setLayout(new GridLayout(6,0,0,6));
		buttonPanel.setLayout(new GridLayout(6,0,0,4));

		labelPanel.add(new JLabel(bundle.getValueOf("projname"), SwingConstants.LEFT));
		nameField = new JTextField(60);
		fieldsPanel.add(nameField);
		buttonPanel.add(new JLabel("")); // dummy

		labelPanel.add(new JLabel(bundle.getValueOf("rootpath"), SwingConstants.LEFT));
		rootField = new JTextField(60);
		rootField.setText(frame.currentDir);
		fieldsPanel.add(rootField);
		setRootButton = new JButton(bundle.getValueOf("setroot"));
		setRootButton.addActionListener(new RootButtonListener());
		buttonPanel.add(setRootButton);

		labelPanel.add(new JLabel(bundle.getValueOf("driver"), SwingConstants.LEFT));
		execField = new JTextField(60);
		execField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {}
    		public void focusLost(FocusEvent e) {
				String text = execField.getText();
				char[] arr = text.toCharArray();
				for (int i = arr.length; i >= 0; i--) {
					if (arr[i] == '.') {
						arr[i] = ' ';
					}
				}
				execField.setText(new String(arr));
			}
		});
		fieldsPanel.add(execField);
		appletBox = new JCheckBox("Applet");
		buttonPanel.add(appletBox);

		labelPanel.add(new JLabel(bundle.getValueOf("appletfile"), SwingConstants.LEFT));
		appletField = new JTextField(60);
		appletField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {}
    		public void focusLost(FocusEvent e) {
				String text = buildField.getText();
				if (!text.endsWith(".html") || !text.endsWith(".htm"))
				    buildField.setText(text + ".html");
			}
		});
		fieldsPanel.add(appletField);
		buttonPanel.add(new JLabel("")); // dummy

		labelPanel.add(new JLabel(bundle.getValueOf("buildfile"), SwingConstants.LEFT));
		buildField = new JTextField(60);
		buildField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {}
    		public void focusLost(FocusEvent e) {
				String text = buildField.getText();
				if (!text.endsWith(".xml"))
				    buildField.setText(text + ".xml");
			}
		});
		fieldsPanel.add(buildField);
		buttonPanel.add(new JLabel("")); // dummy

		labelPanel.add(new JLabel(bundle.getValueOf("cmdline"), SwingConstants.LEFT));
		commandField = new JTextField(60);
		fieldsPanel.add(commandField);
		buttonPanel.add(new JLabel("")); // dummy

		JPanel center = new JPanel();
		center.setLayout(new FlowLayout());
		center.add(labelPanel);
		center.add(fieldsPanel);
		center.add(buttonPanel);

		JPanel cpanel = new JPanel();
		cpanel.setLayout(new BorderLayout());
		classPathPanel = new ClassPathPanel(frame.jsprops, bundle);
		cpanel.add(new JLabel(bundle.getValueOf("cplabel"), SwingConstants.CENTER), BorderLayout.NORTH);
		cpanel.add(classPathPanel, BorderLayout.CENTER);

		add(center);
		add(cpanel);
    }

	public void createNewProject() {
		rootFile = new File(rootField.getText());
		if(!rootFile.exists()) {
			rootFile.mkdirs();
			frame.currentDir = rootFile.getAbsolutePath();
		}
		execFile = new File(execField.getText().trim() + ".class");
		frame.jsprops.setProjectName(nameField.getText());
		frame.jsprops.setRootPath(rootFile.getPath());
		frame.jsprops.setDriver(execField.getText());
		frame.jsprops.setCommandLine(commandField.getText());
		frame.jsprops.setClassPath(classPathPanel.getClassPathString());
		frame.jsprops.setApplet(appletBox.isSelected());
		frame.jsprops.setAppletFile(new File(appletField.getText()));
	}

	class RootButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			JFileChooser chooser = new JFileChooser(new File(frame.currentDir));
			chooser.setDialogTitle(bundle.getValueOf("setrootpath"));
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				rootFile = chooser.getSelectedFile();
				rootField.setText(rootFile.getPath());
			}
		}
	}
}