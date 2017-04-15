package jsource.codegenerator;


/**
 * ImportsTableModel.java 05/29/03
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

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 * <code>ImportsTableModel</code> is an implementation of <code>TableModel</code>
 * that uses an <code>ArrayList</code> to store the <code>String</code> values
 * of the imported classes in <code>CodeGenerator</code>.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class ImportsTableModel extends DefaultTableModel {

    LanguageManager manager = null;
    private ArrayList imports = null;

    public ImportsTableModel(LanguageManager manager) {
        imports = new ArrayList();
        this.manager = manager;
    }

    public void setManager(LanguageManager manager) {
        imports.clear();
        this.manager = manager;
        fireTableDataChanged();
    }

    public void add(String imp) {
        if (imports.contains(imp)) {
            return;
        } else {
            imports.add(imp);
            fireTableRowsInserted(imports.size() - 1, imports.size() - 1);
            return;
        }
    }

    public String remove(int index) {
        String p = (String) imports.remove(index);

        fireTableRowsDeleted(index, index);
        return p;
    }

    public void removeAll() {
        imports.clear();
        fireTableDataChanged();
    }

    public String getImport(int index) {
        if (index > imports.size() || index < 0)
            return null;
        else
            return (String) imports.get(index);
    }

    public String[] getImports() {
        String result[] = new String[imports.size()];

        imports.toArray(result);
        return result;
    }

    public int getRowCount() {
        if (imports != null)
            return imports.size();
        else
            return 10;
    }

    public int getColumnCount() {
        return 1;
    }

    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public String getColumnName(int column) {
        switch (column) {
        	case 0:
        	    return "Import";
        }
        return "Undefined col name";
    }

    public Object getValueAt(int row, int column) {
        String imp = (String) imports.get(row);
		if (imp != null) {
			return imp;
		} else {
        	return "Undefined value";
		}
    }

    public void setValueAt(Object newValue, int row, int column) {
        if (row > imports.size() - 1) {
            return;
		}
        String imp = (String) imports.get(row);
		imp = (String)newValue;
        fireTableDataChanged();
    }
}
