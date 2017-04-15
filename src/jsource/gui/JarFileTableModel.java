package jsource.gui;


/**
 * JarFileTableModel.java 06/18/03
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
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class JarFileTableModel implements TableModel {

	protected EventListenerList listenerList;
	private Vector data;
	private int COLUMN_COUNT = 7;

	public JarFileTableModel(File file) {
		data = new Vector();
		DateFormat dayFormat = DateFormat.getDateInstance(DateFormat.FULL);
		DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.FULL);
		listenerList = new EventListenerList();
		try {
			JarFile jarFile = new JarFile(file);
			Enumeration enum = jarFile.entries();
			while(enum.hasMoreElements()) {
				Object[] rowData = new Object[COLUMN_COUNT];
				JarEntry entry = (JarEntry) enum.nextElement();
				if(!entry.isDirectory()) {
					File entryFile = new File(entry.getName());
					String name = entryFile.getName();
					String path = entryFile.getParent();
					Date date = new Date(entry.getTime());
					float size = entry.getSize();
					float compressed = entry.getCompressedSize();
					float ratio ;
					if(size == 0 || compressed == 0)
						ratio = 0;
					else
						ratio = 100 - ((compressed/size) * 100);
					rowData[0] = name;
					rowData[1] = path;
					rowData[2] = dayFormat.format(date);
					rowData[3] = timeFormat.format(date);
					rowData[4] = new Float(size);
					rowData[5] = new Float(compressed);
					rowData[6] = new Float(ratio);
					data.addElement(rowData);
				}
			}
			jarFile.close();
		}
		catch(JarException ex) {
			MessageFrame.setErrorText(ex.toString());
		}
		catch(IOException ex) {
			MessageFrame.setErrorText(ex.toString());
		}
	}
	public Object getValueAt(int row, int column) {
		Object[] rowData = (Object[]) data.elementAt(row);
		return rowData[column];
	}
	public void setValueAt(Object obj, int row, int column) {
	}
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	public int getColumnCount() {
		return COLUMN_COUNT;
	}
	public int getRowCount() {
		return data.size();
	}
	public String getColumnName(int column) {
		String[] headerData = {"Name","Path","Date","Time","Size","Compressed Size","Compression Ratio"};
		return headerData[column];
	}
	public Class getColumnClass(int column) {
		Class[] classData = {String.class,String.class,String.class,String.class,Float.class,Float.class,Float.class};
		return classData[column];
	}
	public void addTableModelListener(TableModelListener l) {
		listenerList.add(TableModelListener.class, l);
	}
	public void removeTableModelListener(TableModelListener l) {
		listenerList.remove(TableModelListener.class, l);
	}
}




