package jsource.codegenerator;


/**
 * Property.java	03/17/03
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

public class Property {

    private String m_sName = null;
    private int m_iType = 0;
    private boolean m_bReadOnly = false;

    public Property(String name, int type) {
        this(name, type, false);
    }

    public Property(String name, int type, boolean read_only) {
        m_bReadOnly = false;
        m_sName = name;
        m_iType = type;
        m_bReadOnly = read_only;
    }

    public String getName() {
        return m_sName;
    }

    public void setName(String name) {
        m_sName = name;
    }

    public int getType() {
        return m_iType;
    }

    public void setType(int i_type) {
        m_iType = i_type;
    }

    public boolean isReadOnly() {
        return m_bReadOnly;
    }

    public void setReadOnly(boolean value) {
        m_bReadOnly = value;
    }

    public boolean equals(Object o) {
        try {
            Property p = (Property) o;

            return m_sName.equals(p.m_sName);
        } catch (ClassCastException ex) {
            return false;
        }
    }

    public int hashCode() {
        return m_sName.hashCode();
    }
}
