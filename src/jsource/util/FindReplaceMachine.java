package jsource.util;


/**
 * FindReplaceMachine.java  03/31/03
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
import javax.swing.text.*;
import jsource.*;
import jsource.syntax.*;
import jsource.syntax.tokenmarker.*;
import jsource.util.*;
import jsource.io.*;


/**
 * <code>FindReplaceMachine</code> holds text find/replace algorithms
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 *
 * @see FindResult
 * @see jsource.gui.FindReplacePanel
 */
public class FindReplaceMachine {

    private SyntaxDocument doc = null;
    private Position start = null;
    private Position current = null;
    private String findWord = null;
    private String replaceWord = null;
    private boolean found = false;
    private boolean wrapped = false;
    private boolean matchCase = false;

    public FindReplaceMachine() {
        findWord = "";
        replaceWord = "";
        matchCase = true;
    }

    public void setMatchCase(boolean matchCase) {
        matchCase = matchCase;
    }

    public void setDocument(SyntaxDocument doc) {
        this.doc = doc;
    }

    public void setPosition(int pos) {
        try {
            current = doc.createPosition(pos);
        } catch (BadLocationException ble) {
            throw new UnexpectedException(ble);
        }
    }

    public void setStart(int pos) {
        try {
            start = doc.createPosition(pos);
            found = false;
            wrapped = false;
        } catch (BadLocationException ble) {
            throw new UnexpectedException(ble);
        }
    }

    public int getStartOffset() {
        return start.getOffset();
    }

    public int getCurrentOffset() {
        return current.getOffset();
    }

    public void makeCurrentOffsetStart() {
        try {
            start = doc.createPosition(getCurrentOffset());
        } catch (BadLocationException e) {
            throw new UnexpectedException(e);
        }
    }

    public String getFindWord() {
        return findWord;
    }

    public String getReplaceWord() {
        return replaceWord;
    }

    public void setFindWord(String word) {
        findWord = word;
    }

    public void setReplaceWord(String word) {
        replaceWord = word;
    }

    public boolean isOnMatch() {
        int len = findWord.length();
        int off = current.getOffset();

        if (off < len)
            return false;
        try {
            String matchSpace = doc.getText(off - len, len);

            if (matchCase)
                return matchSpace.equals(findWord);
            else
                return matchSpace.toLowerCase().equals(findWord.toLowerCase());
        } catch (BadLocationException e) {
            throw new UnexpectedException(e);
        }
    }

    public FindResult findNext() {
        try {
            String findSpace = doc.getText(current.getOffset(), doc.getLength() - current.getOffset());
            int foundOffset;

            if (matchCase) {
                foundOffset = findSpace.indexOf(findWord);
            } else
                foundOffset = findSpace.toLowerCase().indexOf(findWord.toLowerCase());
            if (foundOffset >= 0) {
                found = true;
                foundOffset += current.getOffset() + findWord.length();
                current = doc.createPosition(foundOffset);
            } else {
                wrapped = true;
                findSpace = doc.getText(0, start.getOffset());
                if (matchCase) {
                    foundOffset = findSpace.indexOf(findWord);
                } else
                    foundOffset = findSpace.toLowerCase().indexOf(findWord.toLowerCase());
                if (foundOffset >= 0) {
                    foundOffset += findWord.length();
                    current = doc.createPosition(foundOffset);
                }
            }
            if (foundOffset == -1 && found) {
                current = start;
                found = false;
                return findNext();
            } else {
                FindResult fr = new FindResult(foundOffset, wrapped);

                wrapped = false;
                return fr;
            }
        } catch (BadLocationException e) {
            throw new UnexpectedException(e);
        }
    }

    public boolean replaceCurrent() {
        try {
            if (isOnMatch()) {
                doc.remove(getCurrentOffset() - findWord.length(), findWord.length());
                doc.insertString(getCurrentOffset(), replaceWord, null);
                return true;
            } else {
                return false;
            }
        } catch (BadLocationException e) {
            throw new UnexpectedException(e);
        }
    }

    public int replaceAll() {
        int count = 0;
        FindResult fr = findNext();
        int found = fr.getFoundOffset();
        int wrapped = 0;

        if (fr.getWrapped())
            wrapped++;
        while (found >= 0 && (wrapped == 0 || found <= start.getOffset())
                && wrapped < 2) {
            replaceCurrent();
            count++;
            fr = findNext();
            found = fr.getFoundOffset();
            if (fr.getWrapped())
                wrapped++;
        }
        return count;
    }
}
