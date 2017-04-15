package jsource.gui;


/**
 * AntRunDialog.java 06/18/03
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

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.util.ArrayList;

import gnu.regexp.*;

import org.apache.tools.ant.*;

import jsource.console.*;
import jsource.io.localization.*;
import jsource.util.*;


/**
 * <code>AntRunDialog</code>.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class AntRunDialog extends BaseDialog {
	private XMLResourceBundle bundle = null;
    private MainFrame parent = null;
    private File buildFile = null;

    public AntRunDialog(XMLResourceBundle bundle, MainFrame parent, File aBuildFile) {
        super(null, "", true);
        setModal(true);
        this.bundle = bundle;
        this.parent = parent;
        buildFile = aBuildFile;
        setTitle(bundle.getValueOf("ant"));

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);
        content.add(BorderLayout.CENTER, new AntRunPanel(buildFile, bundle, parent, this));
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        show();
        toFront();
    }

    public void ok() {
		dispose();
		parent.repaint();
	}

    public void cancel() {
		dispose();
		parent.repaint();
	}
}