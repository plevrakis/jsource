package jsource.codegenerator;


/**
 * LanguageManager.java	03/17/03
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


/**
 * <code>LanguageManager</code> is an abstract language manager
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public abstract class LanguageManager {

    private DefinitionGenerator m_Generator = null;
    private LanguageTypes m_Types = null;

    protected abstract DefinitionGenerator getDefGenerator();

    protected abstract LanguageTypes getLanguageTypes();

    public DefinitionGenerator getGenerator() {
        if (m_Generator == null)
            m_Generator = getDefGenerator();
        return m_Generator;
    }

    public LanguageTypes getTypes() {
        if (m_Types == null)
            m_Types = getLanguageTypes();
        return m_Types;
    }
}
