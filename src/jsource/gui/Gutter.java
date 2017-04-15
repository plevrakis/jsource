package jsource.gui;


/**
 * @(#)Gutter.java	12/16/02
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
import java.awt.event.*;
import javax.swing.*;


/**
 * <code>Gutter</code> is the component that displays line numbers to the left of the text area.
 * Based on jEdit's Gutter component by Mike Dillon and Slava Pestov.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class Gutter extends JLabel implements SwingConstants {

    /** Internal <code>Gutter</code> color constants */
    final static Color DEFAULT_BG_COLOUR = new Color(133, 133, 133);
    final static Color DEFAULT_FG_COLOUR = new Color(209, 209, 209);
    final static Color DEFAULT_DIVIDER_COLOUR = Color.red;
    final static Color DEFAULT_CURRENT_LINE_COLOUR = Color.blue;
    final static Color DEFAULT_INTERVAL_COLOUR = Color.yellow;

    /** A reference to the <code>JSEditor</code> object where the <code>Gutter</code> is attached */
    private JSEditor editor;

    private JPopupMenu context;

    private int baseline = 0;
    private int ileft = 0;

    private int mMinWidth;

    private Dimension gutterSize;
    private Dimension collapsedSize;

    private Color intervalHighlight;
    private Color currentLineHighlight;
    private Color dividerColour;

    private FontMetrics fm;

    private int alignment;

    private int interval = 5;
    private boolean lineNumberingEnabled = true;
    private boolean currentLineHighlightEnabled = false;
    private boolean collapsed = false;

    public Gutter(JSEditor editor) {
        this.editor = editor;

        // minimum width of gutter - enough to display 5 digits
        mMinWidth = editor.getPainter().getFontMetrics().charWidth('8') * 5;
        // minimum size of gutter when expanded
        gutterSize = new Dimension(mMinWidth, 100);
        // minimum size of gutter when collapsed
        collapsedSize = new Dimension(10, 100);

        setDoubleBuffered(true);

        MouseHandler ml = new MouseHandler();

        addMouseListener(ml);

        setBackground(DEFAULT_BG_COLOUR);
        setForeground(DEFAULT_FG_COLOUR);
        setDividerColour(DEFAULT_DIVIDER_COLOUR);
        setHighlightedForeground(DEFAULT_INTERVAL_COLOUR);
        setCurrentLineForeground(DEFAULT_CURRENT_LINE_COLOUR);
    }

    public void paintComponent(Graphics gfx) {
        if (!collapsed) {
            // fill the background
            Rectangle r = gfx.getClipBounds();

            gfx.setColor(getBackground());
            gfx.fillRect(r.x, r.y, r.width, r.height);

            // draw the divider on RHS of gutter
            Color save = gfx.getColor();

            gfx.setColor(dividerColour);
            gfx.drawLine(gutterSize.width - 3, r.y, gutterSize.width - 3, r.y + r.height);
            gfx.setColor(save);

            // paint line numbers, if they are enabled
            if (lineNumberingEnabled) paintLineNumbers(gfx);
        }
    }

    public void paint(Graphics gfx) {
        paintComponent(gfx);
    }

    private void paintLineNumbers(Graphics gfx) {
        FontMetrics pfm = editor.getPainter().getFontMetrics();
        int lineHeight = pfm.getHeight();

        Rectangle clip = gfx.getClipBounds();

        int baseline = editor.lineToY(editor.getFirstLine());

        int firstLine = editor.getFirstLine();
        int lastLine = firstLine + 1 + clip.height / lineHeight;
        int caretLine = editor.getCaretLine() + 1;

        int firstValidLine = firstLine > 1 ? firstLine : 1;
        int lastValidLine = (lastLine > editor.getLineCount())
                ? editor.getLineCount()
                : lastLine;

        boolean highlightCurrentLine = currentLineHighlightEnabled
                && (editor.getSelectionStart() == editor.getSelectionEnd());

        gfx.setFont(editor.getPainter().getFont());

        Color fg = getForeground();
        Color hfg = getHighlightedForeground();
        Color clfg = getCurrentLineForeground();

        String number;
        int offset;

        for (int line = firstLine; line <= lastLine;
                line++, baseline += lineHeight) {
            // only print numbers for valid lines
            if (line < firstValidLine || line > lastValidLine)
                continue;

            number = Integer.toString(line);

            // if breakpoint at line, highlight in red
            if (editor.isBreakpoint(line)) {
                Color save = gfx.getColor();

                gfx.setColor(Color.red);
                gfx.fillRect(0, baseline - lineHeight, gutterSize.width - 3, lineHeight);
                gfx.setColor(save);
            }

            switch (alignment) {
            case RIGHT:
                offset = gutterSize.width - collapsedSize.width
                        - (fm.stringWidth(number) + 1);
                break;

            case CENTER:
                offset = ((gutterSize.width - collapsedSize.width)
                        - fm.stringWidth(number))
                        / 2;
                break;

            case LEFT:
            default:
                offset = 1;
            }

            if (line == caretLine && highlightCurrentLine) {
                gfx.setColor(clfg);
            } else if (interval > 1 && line % interval == 0) {
                gfx.setColor(hfg);
            } else {
                gfx.setColor(fg);
            }

            gfx.drawString(number, ileft + offset, baseline);
        }
    }

    /**
     * Marks a line as needing a repaint.
     * @param line The line to invalidate
     */
    public final void invalidateLine(int line) {
        FontMetrics pfm = editor.getPainter().getFontMetrics();

        repaint(0, editor.lineToY(line) + pfm.getDescent() + pfm.getLeading(),
                getWidth(), pfm.getHeight());
    }

    /**
     * Marks a range of lines as needing a repaint.
     * @param firstLine The first line to invalidate
     * @param lastLine The last line to invalidate
     */
    public final void invalidateLineRange(int firstLine, int lastLine) {
        FontMetrics pfm = editor.getPainter().getFontMetrics();

        repaint(0, editor.lineToY(firstLine) + pfm.getDescent() + pfm.getLeading(),
                getWidth(), (lastLine - firstLine + 1) * pfm.getHeight());
    }

    /*
     * <code>JComponent.setFont(Font)</code> is overridden here to cache the baseline for
     * the font. This avoids having to get the font metrics during every repaint.
     */
    public void setFont(Font font) {
        super.setFont(font);

        fm = getFontMetrics(font);
        baseline = fm.getAscent();
    }

    /**
     * Get the foreground color for highlighted line numbers
     * @return The highlight color
     */
    public Color getHighlightedForeground() {
        return intervalHighlight;
    }

    public void setHighlightedForeground(Color highlight) {
        intervalHighlight = highlight;
    }

    public Color getCurrentLineForeground() {
        return currentLineHighlight;
    }

    public void setCurrentLineForeground(Color highlight) {
        currentLineHighlight = highlight;
    }

    /**
     * Set the width of the expanded gutter
     * @param width The gutter width
     */
    public void setGutterWidth(int width) {
        if (width < collapsedSize.width) width = collapsedSize.width;

        gutterSize.width = width;

        // if the gutter is expanded, ask the text area to revalidate
        // the layout to resize the gutter
        if (!collapsed) editor.revalidate();
    }

    /**
     * Get the width of the expanded gutter
     * @return The gutter width
     */
    public int getGutterWidth() {
        return gutterSize.width;
    }

    /*
     * Component.getPreferredSize() is overridden here to support the
     * collapsing behavior.
     */
    public Dimension getPreferredSize() {
        if (collapsed) {
            return collapsedSize;
        } else {
            return gutterSize;
        }
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * Identifies whether or not the line numbers are drawn in the gutter
     * @return true if the line numbers are drawn, false otherwise
     */
    public boolean isLineNumberingEnabled() {
        return lineNumberingEnabled;
    }

    /**
     * Turns the line numbering on or off and causes the gutter to be
     * repainted.
     * @param enabled true if line numbers are drawn, false otherwise
     */
    public void setLineNumberingEnabled(boolean enabled) {
        if (lineNumberingEnabled == enabled) return;

        lineNumberingEnabled = enabled;

        repaint();
    }

    /**
     * Identifies whether the horizontal alignment of the line numbers.
     * @return Gutter.RIGHT, Gutter.CENTER, Gutter.LEFT
     */
    public int getLineNumberAlignment() {
        return alignment;
    }

    /**
     * Sets the horizontal alignment of the line numbers.
     * @param alignment Gutter.RIGHT, Gutter.CENTER, Gutter.LEFT
     */
    public void setLineNumberAlignment(int alignment) {
        if (this.alignment == alignment) return;

        this.alignment = alignment;

        repaint();
    }

    /**
     * Identifies whether the gutter is collapsed or expanded.
     * @return true if the gutter is collapsed, false if it is expanded
     */
    public boolean isCollapsed() {
        return collapsed;
    }

    /**
     * Sets whether the gutter is collapsed or expanded and force the text
     * area to update its layout if there is a change.
     * @param collapsed true if the gutter is collapsed,
     *                   false if it is expanded
     */
    public void setCollapsed(boolean collapsed) {
        if (this.collapsed == collapsed) return;

        this.collapsed = collapsed;

        editor.revalidate();
    }

    /**
     * Toggles whether the gutter is collapsed or expanded.
     */
    public void toggleCollapsed() {
        setCollapsed(!collapsed);
    }

    /**
     * Sets the number of lines between highlighted line numbers.
     * @return The number of lines between highlighted line numbers or
     *          zero if highlighting is disabled
     */
    public int getHighlightInterval() {
        return interval;
    }

    /**
     * Sets the number of lines between highlighted line numbers. Any value
     * less than or equal to one will result in highlighting being disabled.
     * @param interval The number of lines between highlighted line numbers
     */
    public void setHighlightInterval(int interval) {
        if (interval <= 1) interval = 0;
        this.interval = interval;
        repaint();
    }

    public void setDividerColour(Color c) {
        dividerColour = c;
        repaint();
    }

    public Color getDividerColour() {
        return dividerColour;
    }

    public boolean isCurrentLineHighlightEnabled() {
        return currentLineHighlightEnabled;
    }

    public void setCurrentLineHighlightEnabled(boolean enabled) {
        if (currentLineHighlightEnabled == enabled) return;

        currentLineHighlightEnabled = enabled;

        repaint();
    }

    public JPopupMenu getContextMenu() {
        return context;
    }

    public void setContextMenu(JPopupMenu context) {
        this.context = context;
    }

    class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) toggleCollapsed();
        }
    }
}
