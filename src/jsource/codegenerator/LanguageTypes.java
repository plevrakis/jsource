package jsource.codegenerator;


/**
 * LanguageTypes.java 03/17/03
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

public abstract class LanguageTypes {

    TypeDef types[] = null;

    public LanguageTypes() {
        types = null;
        types = getTypes();
    }

    public String getTypeString(int type) {
        for (int i = 0; i < types.length; i++)
            if (types[i].getId() == type)
                return types[i].getName();

        return "Unsuported type: " + type;
    }

    public String[] getTypeStrings() {
        String result[] = new String[types.length];

        for (int i = 0; i < types.length; i++)
            result[i] = types[i].getName();

        return result;
    }

    public int getTypeFromString(String type_str) {
        if (type_str == null)
            return -1;
        for (int i = 0; i < types.length; i++)
            if (types[i].getName().equals(type_str))
                return types[i].getId();

        return -1;
    }

    public String getDescription(Property p) {
        if (p == null)
            throw new RuntimeException("Property may not be null");
        for (int i = 0; i < types.length; i++)
            if (types[i].getId() == p.getType())
                return types[i].getDescription();

        return "Undefined type";
    }

    public abstract TypeDef[] getTypes();

}
