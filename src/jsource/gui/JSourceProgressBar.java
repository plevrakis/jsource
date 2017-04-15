package jsource.gui;


/**
 * JSourceProgressBar.java 06/18/03
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
import javax.swing.JProgressBar;
import javax.swing.UIManager;


/**
 * <code>JSourceProgressBar</code> is a modified <code>JProgressBar</code>.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class JSourceProgressBar extends JProgressBar {
    private static final String uiClassID = "JSourceProgressBarUI";
    static {
        UIManager.getDefaults().put("JSourceProgressBarUI", "jsource.gui.JSourceProgressBarUI");
    }

    public JSourceProgressBar() {
        super();
    }

    public JSourceProgressBar(int orient) {
        super(orient);
    }

    public JSourceProgressBar(int min, int max) {
        super(min, max);
    }

    public JSourceProgressBar(int orient, int min, int max) {
        super(orient, min, max);
    }

    public String getUIClassID() {
        return uiClassID;
    }

    public void updateUI() {
        this.setUI((JSourceProgressBarUI) UIManager.getUI(this));
    }
}
