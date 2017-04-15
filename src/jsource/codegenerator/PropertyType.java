package jsource.codegenerator;


/**
 * PropertyType.java	03/17/03
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
 * <code>PropertyType</code> is used to get property types
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class PropertyType {

    public static String getTypeString(int type) {
        switch (type) {
        case 1: // '\001'
            return "boolean";

        case 0: // '\0'
            return "bool";

        case 2: // '\002'
            return "char";

        case 5: // '\005'
            return "short";

        case 10: // '\n'
            return "int";

        case 20: // '\024'
            return "long";

        case 30: // '\036'
            return "decimal";

        case 35: // '#'
            return "float";

        case 40: // '('
            return "double";

        case 50: // '2'
            return "String";
        }
        throw new RuntimeException("Unsuported type");
    }

    public static String[] getTypeStrings() {
        String types[] = new String[10];

        types[0] = "String";
        types[1] = "int";
        types[2] = "long";
        types[3] = "decimal";
        types[4] = "double";
        types[5] = "char";
        types[6] = "short";
        types[7] = "float";
        types[8] = "bool";
        types[9] = "boolean";
        return types;
    }

    public static int getTypeFromString(String type_str) {
        if (type_str == null)
            return -1;
        if (type_str.equals("int"))
            return 10;
        if (type_str.equals("long"))
            return 20;
        if (type_str.equals("decimal"))
            return 30;
        if (type_str.equals("char"))
            return 2;
        if (type_str.equals("float"))
            return 35;
        if (type_str.equals("short"))
            return 5;
        if (type_str.equals("double"))
            return 40;
        if (type_str.equals("String"))
            return 50;
        if (type_str.equals("boolean"))
            return 1;
        return !type_str.equals("bool") ? -1 : 0;
    }

    public static final int BOOL = 0;
    public static final int BOOLEAN = 1;
    public static final int CHAR = 2;
    public static final int SHORT = 5;
    public static final int INT = 10;
    public static final int LONG = 20;
    public static final int DECIMAL = 30;
    public static final int FLOAT = 35;
    public static final int DOUBLE = 40;
    public static final int STRING = 50;
}
