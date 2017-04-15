package jsource.codegenerator;


/**
 * JavaTypes.java 03/17/03
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

public class JavaTypes extends LanguageTypes {

    public TypeDef[] getTypes() {
        TypeDef result[] = new TypeDef[10];

        result[0] = new TypeDef(4, "byte", "Sighed 8-bit value");
        result[1] = new TypeDef(1, "boolean", "A true/false value");
        result[2] = new TypeDef(2, "char", "A 16-bit Unicode character");
        result[3] = new TypeDef(3, "short", "Signed 16-bit value");
        result[4] = new TypeDef(5, "int", "Signed 32-bit value");
        result[5] = new TypeDef(6, "long", "Signed 64-bit value");
        result[6] = new TypeDef(7, "float", "IEEE 32-bit float");
        result[7] = new TypeDef(8, "double", "IEEE 64-bit float");
        result[8] = new TypeDef(9, "Object", "Object type");
        result[9] = new TypeDef(10, "String", "String type");
        return result;
    }

    public static final int BOOLEAN = 1;
    public static final int CHAR = 2;
    public static final int SHORT = 3;
    public static final int BYTE = 4;
    public static final int INT = 5;
    public static final int LONG = 6;
    public static final int FLOAT = 7;
    public static final int DOUBLE = 8;
    public static final int OBJECT = 9;
    public static final int STRING = 10;
}
