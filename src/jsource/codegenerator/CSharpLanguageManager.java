package jsource.codegenerator;


/**
 * CSharpLanguageManager.java 03/17/03
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
 * <code>CSharpLanguageManager</code> is the C# language manager
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class CSharpLanguageManager extends LanguageManager {

    public LanguageTypes getLanguageTypes() {
        return new CSharpTypes();
    }

    protected DefinitionGenerator getDefGenerator() {
        return new CSharpPropertyDefinitionGenerator(this);
    }
}
