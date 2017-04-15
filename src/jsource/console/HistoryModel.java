package jsource.console;


/**
 * HistoryModel.java 07/08/03
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

import java.util.*;

/**
 * <code>HistoryModel</code> is a history model for the JSource system terminal.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class HistoryModel {

    private int max = 0;
    private Vector data = null;

    /**
     * Creates a new history model, sizing it according to the specified size.
     * @param max the maximum numbers of items this history can hold
     */
    public HistoryModel(int max) {
        this.max = max;
        data = new Vector(max);
    }

    /**
     * When the user validates a new entry, we add it to the history.
     * @param text the string to be added to the history
     */
    public void addItem(String text) {
        if (text == null || text.length() == 0) return;

        int index = data.indexOf(text);

        if (index != -1)
            data.removeElementAt(index);
        data.insertElementAt(text, 0);

        if (getSize() > max)
            data.removeElementAt(getSize() - 1);
    }

    /**
     * When user press UP or DOWN, we need to get
     * a previous typed string, stored in the <code>Vector</code>.
     * @param index the index of the string to get
     * @return a <code>String</code> corresponding to a previous entry
     */
    public String getItem(int index) {
        return (String) data.elementAt(index);
    }

    /**
     * As the user can use arrows to get up and down
     * in the list, we need to know its max capacity.
     * @return Maximum capacity of the history
     */
    public int getSize() {
        return data.size();
    }

    /**
     * We may need to add an item directly at the end of the list.
     * @param item the String to add at the end
     */
    private void addItemToEnd(String item) {
        data.addElement(item);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        data.clear();
        data = null;
    }
}
