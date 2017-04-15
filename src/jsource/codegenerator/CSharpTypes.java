package jsource.codegenerator;


/**
 * CSharpTypes.java	03/17/03
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

public class CSharpTypes extends LanguageTypes {

    public CSharpTypes() {}

    public TypeDef[] getTypes() {
        TypeDef result[] = new TypeDef[15];

        result[0] = new TypeDef(1, "sbyte", "Signed 8-bit value");
        result[1] = new TypeDef(2, "byte", "Unsigned 8-bit value");
        result[2] = new TypeDef(3, "short", "Signed 16-bit value");
        result[3] = new TypeDef(4, "ushort", "Unsigned 16-bit value");
        result[4] = new TypeDef(5, "int", "Signed 32-bit value");
        result[5] = new TypeDef(6, "uint", "Unsigned 32-bit value");
        result[6] = new TypeDef(7, "long", "Signed 64-bit value");
        result[7] = new TypeDef(8, "ulong", "Unsigned 64-bit value");
        result[8] = new TypeDef(9, "char", "16-bit Unicode character");
        result[9] = new TypeDef(10, "float", "IEEE 32-bit float");
        result[10] = new TypeDef(11, "double", "IEEE 64-bit float");
        result[11] = new TypeDef(12, "bool", "A True/False value");
        result[12] = new TypeDef(13, "decimal", "A 128-bit high precision floating point value");
        result[13] = new TypeDef(14, "Object", "Base of all types");
        result[14] = new TypeDef(15, "String", "String type");
        return result;
    }

    public static final int SBYTE = 1;
    public static final int BYTE = 2;
    public static final int SHORT = 3;
    public static final int USHORT = 4;
    public static final int INT = 5;
    public static final int UINT = 6;
    public static final int LONG = 7;
    public static final int ULONG = 8;
    public static final int CHAR = 9;
    public static final int FLOAT = 10;
    public static final int DOUBLE = 11;
    public static final int BOOL = 12;
    public static final int DECIMAL = 13;
    public static final int OBJECT = 14;
    public static final int STRING = 15;
}
