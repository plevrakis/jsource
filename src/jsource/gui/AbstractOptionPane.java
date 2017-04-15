package jsource.gui;


/**
 * AbstractOptionPane.java 07/18/03
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
import javax.swing.*;

/**
 * <code>AbstractOptionPane</code> is an abstract implementation
 * of the <code>OptionPane</code> interface.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class AbstractOptionPane extends JPanel implements OptionPane {
    protected int y = 0;
    protected GridBagLayout gridBag = null;
    private String name = null;

    /**
     * Adds a labeled component in the pane. All the components
     * are placed on bottom of each other (vertically sorted).
     * @param label the label to be displayed next to the component
     * @param comp the component to be added
     */
    protected void addComponent(String label, Component comp) {
        GridBagConstraints cons = new GridBagConstraints();

        cons.gridy = y++;
        cons.gridheight = 1;
        cons.gridwidth = 3;
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1.0f;

        cons.gridx = 0;
        cons.anchor = GridBagConstraints.WEST;
        JLabel l = new JLabel(label, SwingConstants.LEFT);

        gridBag.setConstraints(l, cons);
        add(l);

        cons.gridx = 3;
        cons.gridwidth = 1;
        cons.anchor = GridBagConstraints.EAST;

        gridBag.setConstraints(comp, cons);
        add(comp);
    }

    /**
     * It does the same as <code>addComponent(String, Component)</code>
     * but it does not add a label next to the component.
     * @param comp the component to be added
     */
    protected void addComponent(Component comp) {
        GridBagConstraints cons = new GridBagConstraints();

        cons.gridy = y++;
        cons.gridheight = 1;
        cons.gridwidth = cons.REMAINDER;
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.WEST;
        cons.weightx = 1.0f;

        gridBag.setConstraints(comp, cons);
        add(comp);
    }

    /**
     * Creates a new option pane.
     * @param name the name used by OptionDialog to display title
     */
    public AbstractOptionPane(String name) {
        this.name = name;
        setLayout(gridBag = new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }

    /**
     * Returns itself.
     */
    public Component getComponent() {
        return this;
    }

    /**
     * Overrides default getName() method. Needed by
     * tabbed panes to display a title on the parent tab.
     */
    public String getName() {
        return name;
    }

    /**
     * Empty implementation of save() method, inherited
     * from the OptionPane interface.
     */
    public void save() {}

    protected void finalize() throws Throwable {
        super.finalize();
        gridBag = null;
        name = null;
    }
}