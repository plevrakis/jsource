package jsource.syntax.tokenmarker;


/**
 * @(#)JavaTokenMarker.java	03/29/03
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
import jsource.syntax.*;
import javax.swing.text.Segment;


/**
 * CSharp token marker.
 *
 * @author Panagiotis Plevrakis
 */
public class CSharpTokenMarker extends CTokenMarker {

    private static KeywordMap csharpKeywords;

    public CSharpTokenMarker() {
        super(false, getKeywords());
    }

    public static KeywordMap getKeywords() {
        if (csharpKeywords == null) {
            csharpKeywords = new KeywordMap(false);
            csharpKeywords.add("Object", Token.KEYWORD2);
            csharpKeywords.add("decimal", Token.KEYWORD2);
            csharpKeywords.add("sbyte", Token.KEYWORD3);
            csharpKeywords.add("byte", Token.KEYWORD3);
            csharpKeywords.add("char", Token.KEYWORD3);
            csharpKeywords.add("short", Token.KEYWORD3);
            csharpKeywords.add("ushort", Token.KEYWORD3);
            csharpKeywords.add("int", Token.KEYWORD3);
            csharpKeywords.add("uint", Token.KEYWORD3);
            csharpKeywords.add("long", Token.KEYWORD3);
            csharpKeywords.add("ulong", Token.KEYWORD3);
            csharpKeywords.add("float", Token.KEYWORD3);
            csharpKeywords.add("double", Token.KEYWORD3);
            csharpKeywords.add("bool", Token.KEYWORD3);
            csharpKeywords.add("String", Token.KEYWORD3);
            csharpKeywords.add("void", Token.KEYWORD3);
            csharpKeywords.add("class", Token.KEYWORD3);
            csharpKeywords.add("interface", Token.KEYWORD3);
            csharpKeywords.add("abstract", Token.KEYWORD1);
            csharpKeywords.add("final", Token.KEYWORD1);
            csharpKeywords.add("private", Token.KEYWORD1);
            csharpKeywords.add("protected", Token.KEYWORD1);
            csharpKeywords.add("public", Token.KEYWORD1);
            csharpKeywords.add("static", Token.KEYWORD1);
            csharpKeywords.add("synchronized", Token.KEYWORD1);
            csharpKeywords.add("native", Token.KEYWORD1);
            csharpKeywords.add("volatile", Token.KEYWORD1);
            csharpKeywords.add("transient", Token.KEYWORD1);
            csharpKeywords.add("break", Token.KEYWORD1);
            csharpKeywords.add("case", Token.KEYWORD1);
            csharpKeywords.add("continue", Token.KEYWORD1);
            csharpKeywords.add("default", Token.KEYWORD1);
            csharpKeywords.add("do", Token.KEYWORD1);
            csharpKeywords.add("else", Token.KEYWORD1);
            csharpKeywords.add("for", Token.KEYWORD1);
            csharpKeywords.add("if", Token.KEYWORD1);
            csharpKeywords.add("instanceof", Token.KEYWORD1);
            csharpKeywords.add("new", Token.KEYWORD1);
            csharpKeywords.add("return", Token.KEYWORD1);
            csharpKeywords.add("switch", Token.KEYWORD1);
            csharpKeywords.add("while", Token.KEYWORD1);
            csharpKeywords.add("throw", Token.KEYWORD1);
            csharpKeywords.add("try", Token.KEYWORD1);
            csharpKeywords.add("catch", Token.KEYWORD1);
            csharpKeywords.add("extends", Token.KEYWORD1);
            csharpKeywords.add("override", Token.KEYWORD1);
            csharpKeywords.add("finally", Token.KEYWORD1);
            csharpKeywords.add("implements", Token.KEYWORD1);
            csharpKeywords.add("throws", Token.KEYWORD1);
            csharpKeywords.add("this", Token.LITERAL2);
            csharpKeywords.add("null", Token.LITERAL2);
            csharpKeywords.add("super", Token.LITERAL2);
            csharpKeywords.add("true", Token.LITERAL2);
            csharpKeywords.add("false", Token.LITERAL2);
        }
        return csharpKeywords;
    }
}
