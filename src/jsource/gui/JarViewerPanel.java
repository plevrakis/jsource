package jsource.gui;


/**
 * JarViewerPanel.java 06/18/03
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
import java.io.File;
import javax.swing.*;

/**
 * <code>JarViewerPanel</code> holds components that display the contents of a compressed .jar file.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class JarViewerPanel extends JPanel {

	public JarViewerPanel(File file) {
		super(new BorderLayout());
		JarFileTableModel tableModel = new JarFileTableModel(file);
		JTable table = new JTable(tableModel);
		table.setAutoCreateColumnsFromModel(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane,BorderLayout.CENTER);
	}
}