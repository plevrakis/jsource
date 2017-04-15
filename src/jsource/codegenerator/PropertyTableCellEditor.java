package jsource.codegenerator;


/**
 * @(#)PropertyTableCellEditor.java	03/17/03
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

import java.awt.Component;
import javax.swing.*;


public class PropertyTableCellEditor extends DefaultCellEditor {

    public PropertyTableCellEditor(JComboBox box) {
        super(box);
    }

    public PropertyTableCellEditor(LanguageManager manager, JComboBox box) {
        super(box);
        m_LanguageManager = manager;
        setItems();
    }

    public void setManager(LanguageManager manager) {
        m_LanguageManager = manager;
        setItems();
    }

    private void setItems() {
        if (m_LanguageManager == null)
            return;
        JComboBox box = (JComboBox) super.getComponent();

        box.removeAllItems();
        String types[] = m_LanguageManager.getTypes().getTypeStrings();

        for (int i = 0; i < types.length; i++)
            box.addItem(types[i]);

    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (super.getComponent() instanceof JComboBox) {
            JComboBox box = (JComboBox) super.getComponent();

            box.setSelectedItem(value);
            return box;
        } else {
            return null;
        }
    }

    LanguageManager m_LanguageManager;
}
