package jsource.gui;


/**
 * @(#)TextAreaDefaults.java	12/17/02
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
import javax.swing.JPopupMenu;
import java.awt.Color;
import jsource.syntax.*;
import jsource.syntax.tokenmarker.*;
import jsource.util.JSConstants;


/**
 * <code>TextAreaDefaults</code> encapsulates default settings for a <code>JSEditor</code> object.
 * An object of this class can be passed to the <code>JSEditor</code> constructor once the necessary
 * fields have been filled out. The advantage of doing this over calling lots of set() methods after
 * creating the text area is that this method is faster.
 *
 * Based on <code>TextAreaDefaults</code> in jEdit syntax package.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class TextAreaDefaults {

    private static TextAreaDefaults DEFAULTS = null;

    public SyntaxDocument document = null;
    public boolean editable = false;

    public boolean caretVisible = false;
    public boolean caretBlinks = false;
    public boolean blockCaret = false;
    public int electricScroll = 0;

    public int cols = 0;
    public int rows = 0;
    public SyntaxStyle[] styles = null;
    public Color caretColor = null;
    public Color selectionColor = null;
    public Color lineHighlightColor = null;
    public boolean lineHighlight = false;
    public Color bracketHighlightColor = null;
    public boolean bracketHighlight = false;
    public Color eolMarkerColor = null;
    public boolean eolMarkers = false;
    public boolean paintInvalid = false;

    /**
     * Returns a new <code>TextAreaDefaults</code> object with the default values filled in.
     */
    public static TextAreaDefaults getDefaults() {
        if (DEFAULTS == null) {
            DEFAULTS = new TextAreaDefaults();
            DEFAULTS.document = new SyntaxDocument();
            DEFAULTS.editable = true;

            DEFAULTS.blockCaret = false;
            DEFAULTS.caretVisible = true;
            DEFAULTS.caretBlinks = true;
            DEFAULTS.electricScroll = 3;

            DEFAULTS.cols = 80;
            DEFAULTS.rows = 25;
            DEFAULTS.styles = SyntaxUtilities.getDefaultSyntaxStyles();
            DEFAULTS.caretColor = Color.yellow;
            DEFAULTS.selectionColor = new Color(133, 133, 133);
            DEFAULTS.lineHighlightColor = new Color(133, 133, 133);
            DEFAULTS.lineHighlight = true;
            DEFAULTS.bracketHighlightColor = Color.black;
            DEFAULTS.bracketHighlight = true;
            DEFAULTS.eolMarkerColor = new Color(0x009999);
            DEFAULTS.eolMarkers = false;
            DEFAULTS.paintInvalid = false;
        }

        return DEFAULTS;
    }
}
