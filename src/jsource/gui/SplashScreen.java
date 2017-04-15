package jsource.gui;


/**
 * SplashScreen.java 07/12/03
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
import java.util.*;

import java.net.URL;

import javax.swing.*;
import javax.swing.border.*;

import jsource.gui.*;
import jsource.util.*;


/**
 * <code>SplashScreen</code> displays the JSource splash screen.
 * This class can also be used to dynamically load other classes.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class SplashScreen extends JWindow implements Runnable {

    private Thread thread = null;
    private boolean finished = false;
    private String[] classes = null;
    private JSourceProgressBar progress = null;

    /**
     * Creates a new <code>SplashScreen</code> which displays a picture, a copyright
     * and a progress bar used to indicate the loading progress of the application.
     */
    public SplashScreen() {
        setBackground(Color.lightGray);

        JPanel pane = new JPanel(new BorderLayout());

        pane.setFont(new Font("Monospaced", 0, 14));
        pane.add(BorderLayout.NORTH, new JLabel(GUIUtilities.createIcon("load" + (Math.abs(new Random().nextInt()) % 2) + ".jpg")));

        progress = new JSourceProgressBar(0, 100);
        progress.setStringPainted(true);
        progress.setFont(new Font("Monospaced", Font.BOLD, 9));
        progress.setString("");
        progress.setBorder(new MatteBorder(0, 1, 1, 1, Color.black));
        pane.add(BorderLayout.CENTER, progress);
        pane.add(BorderLayout.SOUTH, new JLabel("v " + JSConstants.RELEASE + " - (C) 2003 Panagiotis Plevrakis",
                SwingConstants.CENTER));

        pane.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        getContentPane().add(pane);

        pack();
		setVisible(true);

        boolean load = true;

        if (load) {
            classes = new String[4];
            classes[0] = "jsource.gui.MainFrame";
            classes[1] = "jsource.gui.JSEditor";
            classes[2] = "jsource.console.Console";
            classes[3] = "jsource.util.JSConstants";

            thread = new Thread(this);
            thread.setDaemon(true);
            thread.setPriority(Thread.NORM_PRIORITY);
        }

        setLocationRelativeTo(null); // used after 1.4 for centering
        GUIUtilities.setCursorOnWait(this, true);

        if (load) {
            thread.start();
        } else {
            finished = true;
            setProgress(0);
            setText("Loading GUI...");
        }
    }

    /**
     * Loads the classes dynamically from the list.
     */
    public void run() {
        String packs = getClass().getName();
        int i = packs.lastIndexOf('.');

        if (i >= 0)
            packs = packs.substring(0, i + 1);
        else
            packs = "";

        for (i = 0; i < classes.length; i++) {
            String n = classes[i];
            int j = n.lastIndexOf('.');

            if (j < 0) n = packs + n;
            progress.setString(n);

            try {
                Class c = Class.forName(n);
                Thread.sleep(500);
            } catch (Exception e) {}
            progress.setValue(100 * (i + 1) / classes.length);
        }
        finished = true;
        setText("Loading GUI...");
        stop();
    }

    /**
     * Sets the current text of the progress bar but
     * only in the case the loading of classes is ended.
     */
    public void setText(String text) {
        if (finished)
            progress.setString(text);
    }

    /**
     * Sets the current progress value of the progress bar but
     * only in the case the loading of classes is ended.
     */
    public void setProgress(int percent) {
        if (finished)
            progress.setValue(percent);
    }

    /**
     * Stop the loading process and launch <code>MainFrame</code>.
     */
    public void stop() {
		new MainFrame();
        thread = null;
        setVisible(false);
        dispose();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        thread = null;
        classes = null;
        progress = null;
    }
}