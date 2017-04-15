package jsource.syntax;


/**
 * SyntaxDocument.java	12/17/02
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
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.*;
import java.awt.Toolkit;
import jsource.syntax.tokenmarker.*;
import jsource.util.*;

/**
 * A document implementation that can be tokenized by the
 * syntax highlighting system.
 *
 * @author Slava Pestov
 * <br>Revised for JSource 2002 Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class SyntaxDocument extends PlainDocument {

    private static final int UNDO_IN_PROGRESS = 9;
    private static final int TEMPORARY = 10;

    private int flags = 0;
    private int indent = JSConstants.INDENT;
    private int currentLocation = 0;
    private MyUndoManager undo = null;
    private UndoableEdit saveUndo = null;
    private CompoundEdit compoundEdit = null;
    private boolean compoundEditNonEmpty = false;
    private int compoundEditCount = 0;
    private Toolkit mToolKit = null;

    public SyntaxDocument() {
        undo = new MyUndoManager();
        addUndoableEditListener(new UndoHandler());
        mToolKit = JSConstants.TOOLKIT;
    }

    /**
     * Returns the token marker that is to be used to split lines
     * of this document up into tokens. May return null if this
     * document is not to be colorized.
     */
    public TokenMarker getTokenMarker() {
        return tokenMarker;
    }

    /**
     * Sets the token marker that is to be used to split lines of
     * this document up into tokens. May throw an exception if
     * this is not supported for this type of document.
     * @param tm The new token marker
     */
    public void setTokenMarker(TokenMarker tm) {
        tokenMarker = tm;
        if (tm == null)
            return;
        tokenMarker.insertLines(0, getDefaultRootElement().getElementCount());
        tokenizeLines();
    }

    /**
     * Reparses the document, by passing all lines to the token
     * marker. This should be called after the document is first
     * loaded.
     */
    public void tokenizeLines() {
        tokenizeLines(0, getDefaultRootElement().getElementCount());
    }

    /**
     * Reparses the document, by passing the specified lines to the
     * token marker. This should be called after a large quantity of
     * text is first inserted.
     * @param start The first line to parse
     * @param len The number of lines, after the first one to parse
     */
    public void tokenizeLines(int start, int len) {
        if (tokenMarker == null || !tokenMarker.supportsMultilineTokens())
            return;

        Segment lineSegment = new Segment();
        Element map = getDefaultRootElement();

        len += start;

        try {
            for (int i = start; i < len; i++) {
                Element lineElement = map.getElement(i);
                int lineStart = lineElement.getStartOffset();

                getText(lineStart, lineElement.getEndOffset() - lineStart - 1, lineSegment);
                tokenMarker.markTokens(lineSegment, i);
            }
        } catch (BadLocationException bl) {
            bl.printStackTrace();
        }
    }

    /**
     * Undoes the most recent edit. Returns true if the undo was successful.
     */
    public boolean undo() {
        if (undo == null)
            return false;

        try {
            setFlag(UNDO_IN_PROGRESS, true);
            if (undo.canUndo()) undo.undo();
            else if (mToolKit != null) mToolKit.beep();
        } catch (CannotUndoException cu) {
            cu.printStackTrace();
            return false;
        } finally {
            setFlag(UNDO_IN_PROGRESS, false);
        }

        UndoableEdit toUndo = undo.editToBeUndone();

        return true;
    }

    /**
     * Redoes the most recently undone edit. Returns true if the redo was successful.
     */
    public boolean redo() {
        if (undo == null)
            return false;

        try {
            setFlag(UNDO_IN_PROGRESS, true);
            if (undo.canRedo()) undo.redo();
            else if (mToolKit != null) mToolKit.beep();
        } catch (CannotRedoException cr) {
            cr.printStackTrace();
            return false;
        } finally {
            setFlag(UNDO_IN_PROGRESS, false);
        }

        UndoableEdit toUndo = undo.editToBeUndone();

        return true;
    }

    /**
     * Adds an undoable edit to this document. This is non-trivial
     * mainly because the text area adds undoable edits every time
     * the caret is moved. First of all, undos are ignored while
     * an undo is already in progress. This is no problem with Swing
     * Document undos, but caret undos are fired all the time and
     * this needs to be done. Also, insignificant undos are ignored
     * if the redo queue is non-empty to stop something like a caret
     * move from flushing all redos.
     * @param edit The undoable edit
     */
    public void addUndoableEdit(UndoableEdit edit) {
        if (undo == null || getFlag(UNDO_IN_PROGRESS))
            return;

        // Ignore insificant edits if the redo queue is non-empty.
        // This stops caret movement from killing redos.
        if (undo.canRedo() && !edit.isSignificant())
            return;

        if (compoundEdit != null) {
            compoundEditNonEmpty = true;
            compoundEdit.addEdit(edit);
        } else
            undo.addEdit(edit);
    }

    /**
     * Starts a compound edit. All edits from now on until
     * <code>endCompoundEdit()</code> are called will be merged
     * into one. This can be used to make a complex operation
     * undoable in one step. Nested calls to
     * <code>beginCompoundEdit()</code> behave as expected,
     * requiring the same number of <code>endCompoundEdit()</code>
     * calls to end the edit.
     */
    public void beginCompoundEdit() {
        if (getFlag(TEMPORARY))
            return;

        compoundEditCount++;
        if (compoundEdit == null) {
            compoundEditNonEmpty = false;
            compoundEdit = new CompoundEdit();
        }
    }

    /**
     * Ends a compound edit. All edits performed since
     * <code>beginCompoundEdit()</code> was called can now
     * be undone in one step by calling <code>undo()</code>.
     */
    public void endCompoundEdit() {
        if (getFlag(TEMPORARY))
            return;

        if (compoundEditCount == 0)
            return;

        compoundEditCount--;
        if (compoundEditCount == 0) {
            compoundEdit.end();
            if (compoundEditNonEmpty && compoundEdit.canUndo())
                undo.addEdit(compoundEdit);
            compoundEdit = null;
        }
    }

    protected TokenMarker tokenMarker;

    /**
     * We overwrite this method to update the token marker
     * state immediately so that any event listeners get a
     * consistent token marker.
     */
    protected void fireInsertUpdate(DocumentEvent evt) {
        if (tokenMarker != null) {
            DocumentEvent.ElementChange ch = evt.getChange(
                    getDefaultRootElement());

            if (ch != null) {
                tokenMarker.insertLines(ch.getIndex() + 1,
                        ch.getChildrenAdded().length - ch.getChildrenRemoved().length);
            }
        }

        super.fireInsertUpdate(evt);
    }

    /**
     * We overwrite this method to update the token marker
     * state immediately so that any event listeners get a
     * consistent token marker.
     */
    protected void fireRemoveUpdate(DocumentEvent evt) {
        if (tokenMarker != null) {
            DocumentEvent.ElementChange ch = evt.getChange(
                    getDefaultRootElement());

            if (ch != null) {
                tokenMarker.deleteLines(ch.getIndex() + 1,
                        ch.getChildrenRemoved().length - ch.getChildrenAdded().length);
            }
        }

        super.fireRemoveUpdate(evt);
    }

    private void setFlag(int flag, boolean value) {
        if (value)
            flags |= (1 << flag);
        else
            flags &= ~(1 << flag);
    }

    private boolean getFlag(int flag) {
        int mask = (1 << flag);

        return (flags & mask) == mask;
    }

    /**
     * Sets a new indent length.
     * @param indent the int value for new indent length
     */
    public void setIndent(int indent) {
        this.indent = indent;
    }

    /**
     * Gets the indent length
     * @return indent the int value of current indent length
     */
    public int getIndent() {
        return indent;
    }

    public void indentLines(int selStart, int selEnd, int currentLocation) {

    }

    public void commentLines(int selStart, int selEnd, int currentLocation) {

    }

    public void uncommentLines(int selStart, int selEnd, int currentLocation) {

    }

    // we need to call some protected methods, so override this class to make them public
    class MyUndoManager extends UndoManager {
        public UndoableEdit editToBeUndone() {
            return super.editToBeUndone();
        }

        public UndoableEdit editToBeRedone() {
            return super.editToBeRedone();
        }
    }

    class UndoHandler implements UndoableEditListener {
        public void undoableEditHappened(UndoableEditEvent evt) {
            addUndoableEdit(evt.getEdit());
        }
    }
}
