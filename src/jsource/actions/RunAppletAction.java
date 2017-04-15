package jsource.actions;


/**
 * RunAppletAction.java 06/18/03
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
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class RunAppletAction extends AbstractAction {

	public RunAppletAction(PropertyChangeListener listener, String text, ImageIcon icon, String desc, Integer mnemonic) {
		super(text,icon);
		putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
		addPropertyChangeListener(listener);
	}

	public void actionPerformed(ActionEvent evt) {
		firePropertyChange("runapplet", null, null);
	}
}