package jsource.codegenerator;


/**
 * TypeDef.java	03/17/03
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

public class TypeDef {

    private int m_iId = 0;
    private String m_sName = null;
    private String m_sDescription = null;

    public TypeDef(int _id, String _name, String _description) {
        m_iId = _id;
        m_sName = _name;
        m_sDescription = _description;
    }

    public TypeDef(TypeDef _from) {
        m_iId = _from.m_iId;
        m_sName = _from.m_sName;
        m_sDescription = _from.m_sDescription;
    }

    public int getId() {
        return m_iId;

    }

    public void setId(int _value) {
        m_iId = _value;
    }

    public String getName() {
        return m_sName;
    }

    public void setName(String _value) {
        m_sName = _value;
    }

    public String getDescription() {
        return m_sDescription;
    }

    public void setDescription(String _value) {
        m_sDescription = _value;
    }

    public String toString() {
        return getName();
    }
}
