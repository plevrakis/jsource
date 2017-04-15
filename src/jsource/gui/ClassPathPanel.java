package jsource.gui;


/**
 * ClassPathPanel.java 06/18/03
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
import java.io.File;
import javax.swing.*;
import jsource.util.*;
import jsource.io.*;
import jsource.io.localization.*;

/**
 * <code>ClassPathPanel</code> holds components that accomodate classpath editing.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class ClassPathPanel extends JPanel {
	private String lines[] = null;
	private DefaultListModel listModel = new DefaultListModel();
	private Properties props = null;
	private XMLResourceBundle bundle = null;

	public ClassPathPanel(Properties props, XMLResourceBundle bundle) {
		super(new BorderLayout());
		this.props = props;
		this.bundle = bundle;
		lines = props.getClassPathArray();
		for(int i=0;i<lines.length;i++)
			listModel.addElement(lines[i]);
		final JList list = new JList(listModel);
		JScrollPane scrollPane = new JScrollPane(list);
		add(scrollPane,BorderLayout.CENTER);
		JButton addDirectory = new JButton(bundle.getValueOf("adddir"));
		addDirectory.addActionListener(new AddDirListener());
		JButton addArchive = new JButton(bundle.getValueOf("addarchive"));
		addArchive.addActionListener(new AddArchiveListener());
		JButton remove = new JButton(bundle.getValueOf("remove"));
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Object[] selected = list.getSelectedValues();
				for(int i=0;i<selected.length;i++)
					listModel.removeElement(selected[i]);
			}
		});
		JPanel southPanel = new JPanel();
		southPanel.add(addDirectory);
		southPanel.add(addArchive);
		southPanel.add(remove);
		add(southPanel,BorderLayout.SOUTH);
	}

	public String getClassPathString() {
		String classPath = "";
		for(int i=0;i<listModel.size();i++)
			classPath = classPath + (String) listModel.elementAt(i) + File.pathSeparator;
		if(classPath.length() > 0)
			classPath = classPath.substring(0,classPath.length() - 1);
		return classPath;
	}

	private class AddDirListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JFileChooser chooser = null;
			if (props != null) {
				chooser = new JFileChooser(props.getLastDirectory());
			} else {
				chooser = new JFileChooser(JSConstants.USER_DIR);
			}
			chooser.setDialogTitle(bundle.getValueOf("setrootpath"));
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				if((selectedFile.isDirectory()) && (selectedFile.exists())) {
					listModel.addElement(selectedFile.getPath());
				}
			}
		}
	}

	private class AddArchiveListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JFileChooser chooser = null;
			if (props != null) {
				chooser = new JFileChooser(props.getLastDirectory());
			} else {
				chooser = new JFileChooser(JSConstants.USER_DIR);
			}
			String extensions[] = new String[2];
			extensions[0] = "jar";
			extensions[1] = "zip";
			JSourceFileFilter filter = new JSourceFileFilter(extensions, bundle.getValueOf("archfiles") + " (*.jar, *.zip)");
			chooser.addChoosableFileFilter(filter);
			chooser.setMultiSelectionEnabled(true);
			if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				File[] selFiles = chooser.getSelectedFiles();
				for(int i=0;i<selFiles.length;i++) {
					listModel.addElement(selFiles[i].getAbsolutePath());
				}
			}
		}
	}
}