package jsource.gui;


/**
 * OptionPane.java 07/18/03
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

import java.awt.Component;

/**
 * <code>OptionPane</code> is an interface that defines the basic behavior of an option pane.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public interface OptionPane {

    /**
     * Returns the name of the option pane.
     * This name can be required by componens holder
     * such as tabbed panes.
     */
    public String getName();

    /**
     * Returns the component which stands for the option
     * pane itself. In fact, an option pane can be a label,
     * a checkbox, etc...
     */
    public Component getComponent();

    /**
     * When user closes an option dialog by clicking ok,
     * the settings have to be changed. So, all the settings
     * relative to an option pane have to be saved in this method.
     */
    public void save();

}
