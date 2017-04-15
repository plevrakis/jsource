package jsource.syntax;


/**
 * @(#)SyntaxStyle.java	12/17/02
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
import java.awt.*;
import java.util.StringTokenizer;


/**
 * A simple text style class. It can specify the color, italic flag,
 * and bold flag of a run of text.
 * @author Slava Pestov
 * Revised for JSource 2002 Panagiotis Plevrakis
 */
public class SyntaxStyle {
    private Color color = null;
    private boolean italic = false;
    private boolean bold = false;
    private Font lastFont = null;
    private Font lastStyledFont = null;
    private FontMetrics fontMetrics = null;

    /**
     * Creates a new <code>SyntaxStyle</code>.
     * @param color the text color
     * @param italic true if the text should be italics
     * @param bold true if the text should be bold
     */
    public SyntaxStyle(Color color, boolean italic, boolean bold) {
        this.color = color;
        this.italic = italic;
        this.bold = bold;
    }

    /**
     * Returns the color specified in this style.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns true if no font styles are enabled.
     */
    public boolean isPlain() {
        return !(bold || italic);
    }

    /**
     * Returns true if italics is enabled for this style.
     */
    public boolean isItalic() {
        return italic;
    }

    /**
     * Returns true if boldface is enabled for this style.
     */
    public boolean isBold() {
        return bold;
    }

    /**
     * Returns the specified font, but with the style's bold and
     * italic flags applied.
     */
    public Font getStyledFont(Font font) {
        if (font == null)
            throw new NullPointerException("font param must not" + " be null");
        if (font.equals(lastFont))
            return lastStyledFont;
        lastFont = font;
        lastStyledFont = new Font(font.getFamily(),
                (bold ? Font.BOLD : 0) | (italic ? Font.ITALIC : 0),
                font.getSize());
        return lastStyledFont;
    }

    /**
     * Returns the font metrics for the styled font.
     */
    public FontMetrics getFontMetrics(Font font) {
        if (font == null)
            throw new NullPointerException("font param must not" + " be null");
        if (font.equals(lastFont) && fontMetrics != null)
            return fontMetrics;
        lastFont = font;
        lastStyledFont = new Font(font.getFamily(),
                (bold ? Font.BOLD : 0) | (italic ? Font.ITALIC : 0),
                font.getSize());
        fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(lastStyledFont);
        return fontMetrics;
    }

    /**
     * Sets the foreground color and font of the specified graphics
     * context to that specified in this style.
     * @param gfx The graphics context
     * @param font The font to add the styles to
     */
    public void setGraphicsFlags(Graphics gfx, Font font) {
        Font _font = getStyledFont(font);

        gfx.setFont(_font);
        gfx.setColor(color);
    }

    /**
     * Returns a string representation of this object.
     */
    public String toString() {
        return getClass().getName() + "[color=" + color
                + (italic ? ",italic" : "") + (bold ? ",bold" : "") + "]";
    }
}
