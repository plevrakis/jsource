package jsource.codegenerator;


/**
 * PropertiesTableModel.java  03/30/03
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
 * <code>PropertiesTableModel</code> is an implementation of <code>TableModel</code>
 * that uses an <code>ArrayList</code> to store the <code>String</code> values
 * of the properties in <code>CodeGenerator</code>.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class PropertiesTableModel extends DefaultTableModel {

    LanguageManager manager = null;
    private ArrayList props = null;

    public PropertiesTableModel(LanguageManager manager) {
        props = new ArrayList();
        this.manager = manager;
    }

    public void setManager(LanguageManager manager) {
        props.clear();
        this.manager = manager;
        fireTableDataChanged();
    }

    public void add(Property p) {
        if (props.contains(p)) {
            return;
        } else {
            props.add(p);
            fireTableRowsInserted(props.size() - 1, props.size() - 1);
            return;
        }
    }

    public Property remove(int index) {
        Property p = (Property) props.remove(index);

        fireTableRowsDeleted(index, index);
        return p;
    }

    public void removeAll() {
        props.clear();
        fireTableDataChanged();
    }

    public Property getProperty(int index) {
        if (index > props.size() || index < 0)
            return null;
        else
            return (Property) props.get(index);
    }

    public Property[] getProperties() {
        Property result[] = new Property[props.size()];

        props.toArray(result);
        return result;
    }

    public int getRowCount() {
        if (props != null)
            return props.size();
        else
            return 10;
    }

    public int getColumnCount() {
        return 5;
    }

    public boolean isCellEditable(int row, int column) {
        return column != 2 && column != 4;
    }

    public String getColumnName(int column) {
        switch (column) {
        	case 0:
        	    return "Name";

        	case 1:
        	    return "Type";

        	case 2:
        	    return "Member var.";

        	case 3:
        	    return "Read only";

        	case 4:
        	    return "Description";
        }
        return "Undefined col name";
    }

    public Object getValueAt(int row, int column) {
        Property p = (Property) props.get(row);

        switch (column) {
        	case 0:
        	    return p.getName();

        	case 1:
        	    return manager.getTypes().getTypeString(p.getType());

        	case 2:
        	    return manager.getGenerator().getMemberVar(p);

        	case 3:
        	    return String.valueOf(p.isReadOnly());

        	case 4:
        	    return manager.getTypes().getDescription(p);
        }
        return "Undefined value";
    }

    public void setValueAt(Object aValue, int row, int column) {
        if (row > props.size() - 1)
            return;
        Property p = (Property) props.get(row);

        if (column == 0)
            p.setName(aValue.toString());
        else
        if (column == 1)
            p.setType(manager.getTypes().getTypeFromString(aValue.toString()));
        else
        if (column == 3) {
            boolean value = (new Boolean(aValue.toString())).booleanValue();

            p.setReadOnly(value);
        }
        fireTableDataChanged();
    }
}
