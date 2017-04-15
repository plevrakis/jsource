package jsource.syntax;


/**
 * Indent.java	07/08/03
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

import javax.swing.text.Element;
import javax.swing.text.BadLocationException;

import jsource.gui.*;
import jsource.util.*;
import jsource.io.localization.*;
import gnu.regexp.*;


public class Indent {
    public static boolean indent(JSEditor editor, XMLResourceBundle bundle, int lineIndex, boolean canIncreaseIndent, boolean canDecreaseIndent) {
        if (lineIndex == 0)
            return false;

        String openBrackets = "{";
        String closeBrackets = "}";
        String _indentPrevLine = bundle.getValueOf("indPrevLine");
        RE indentPrevLineRE = null;
        SyntaxDocument doc = editor.getDocument();

        if (openBrackets == null)
            openBrackets = "";
        if (closeBrackets == null)
            closeBrackets = "";
        if (_indentPrevLine != null) {
            try {
                indentPrevLineRE = new RE(_indentPrevLine, RE.REG_ICASE,
                        new RESyntax(RESyntax.RE_SYNTAX_PERL5).set(RESyntax.RE_CHAR_CLASSES).setLineSeparator(JSConstants.LINE_SEP));
            } catch (REException re) {}
        }

        int tabSize = JSConstants.INDENT;
        int indentSize = tabSize;
        boolean noTabs = true;

        Element map = doc.getDefaultRootElement();

        String prevLine = null;
        String line = null;

        Element lineElement = map.getElement(lineIndex);
        int start = lineElement.getStartOffset();

        // Get line text
        try {
            line = doc.getText(start, lineElement.getEndOffset() - start - 1);

            for (int i = lineIndex - 1; i >= 0; i--) {
                lineElement = map.getElement(i);
                int lineStart = lineElement.getStartOffset();
                int len = lineElement.getEndOffset() - lineStart - 1;

                if (len != 0) {
                    prevLine = doc.getText(lineStart, len);
                    break;
                }
            }

            if (prevLine == null)
                return false;
        } catch (BadLocationException e) {
            return false;
        }

        /*
         * If 'prevLineIndent' matches a line --> +1
         */
        boolean prevLineMatches = (indentPrevLineRE == null
                ? false
                : indentPrevLineRE.isMatch(prevLine));

        /*
         * On the previous line,
         * if(bob) { --> +1
         * if(bob) { } --> 0
         * } else if(bob) { --> +1
         */
        boolean prevLineStart = true; // False after initial indent
        int prevLineIndent = 0; // Indent width (tab expanded)
        int prevLineBrackets = 0; // Additional bracket indent

        for (int i = 0; i < prevLine.length(); i++) {
            char c = prevLine.charAt(i);

            switch (c) {
            case ' ':
                if (prevLineStart)
                    prevLineIndent++;
                break;

            case '\t':
                if (prevLineStart) {
                    prevLineIndent += (tabSize - (prevLineIndent % tabSize));
                }
                break;

            default:
                prevLineStart = false;
                if (closeBrackets.indexOf(c) != -1)
                    prevLineBrackets = Math.max(prevLineBrackets - 1, 0);
                else if (openBrackets.indexOf(c) != -1) {
                    prevLineMatches = false;
                    prevLineBrackets++;
                }
                break;
            }
        }

        /*
         * On the current line,
         * } --> -1
         * } else if(bob) { --> -1
         * if(bob) { } --> 0
         */
        boolean lineStart = true; // False after initial indent
        int lineIndent = 0; // Indent width (tab expanded)
        int lineWidth = 0; // White space count
        int lineBrackets = 0; // Additional bracket indent
        int closeBracketIndex = -1; // For lining up closing

        // and opening brackets
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            switch (c) {
            case ' ':
                if (lineStart) {
                    lineIndent++;
                    lineWidth++;
                }
                break;

            case '\t':
                if (lineStart) {
                    lineIndent += (tabSize - (lineIndent % tabSize));
                    lineWidth++;
                }
                break;

            default:
                lineStart = false;
                if (closeBrackets.indexOf(c) != -1) {
                    if (lineBrackets == 0)
                        closeBracketIndex = i;
                    else
                        lineBrackets--;
                } else if (openBrackets.indexOf(c) != -1) {
                    prevLineMatches = false;
                    lineBrackets++;
                }
                break;
            }
        }

        try {
            if (closeBracketIndex != -1) {
                int offset = TextUtilities.findMatchingBracket(doc,
                        map.getElement(lineIndex).getStartOffset() + closeBracketIndex);

                if (offset != -1) {
                    lineElement = map.getElement(map.getElementIndex(offset));
                    int startOffset = lineElement.getStartOffset();
                    String closeLine = doc.getText(startOffset, lineElement.getEndOffset() - startOffset - 1);

                    prevLineIndent = Utilities.getLeadingWhiteSpaceWidth(closeLine, tabSize);
                } else
                    return false;
            } else {
                prevLineIndent += (prevLineBrackets * indentSize);
            }

            if (prevLineMatches)
                prevLineIndent += indentSize;

            if (!canDecreaseIndent && prevLineIndent <= lineIndent)
                return false;

            if (!canIncreaseIndent && prevLineIndent >= lineIndent)
                return false;

            // Do it
            doc.remove(start, lineWidth);
            doc.insertString(start, Utilities.createWhiteSpace(prevLineIndent, (noTabs ? 0 : tabSize)), null);
            return true;
        } catch (BadLocationException bl) {}

        return false;
    }
}

