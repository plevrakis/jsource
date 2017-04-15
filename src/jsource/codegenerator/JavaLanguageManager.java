package jsource.codegenerator;


/**
 * JavaLanguageManager.java	03/17/03
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

public class JavaLanguageManager extends LanguageManager {

    public JavaLanguageManager() {}

    public LanguageTypes getLanguageTypes() {
        return new JavaTypes();
    }

    protected DefinitionGenerator getDefGenerator() {
        return new JavaPropertyDefinitionGenerator(this);
    }
}
