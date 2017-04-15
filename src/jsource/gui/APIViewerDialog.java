package jsource.gui;


/**
 * APIViewerDialog.java  05/14/03
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
import javax.swing.border.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import jsource.*;
import jsource.util.*;
import jsource.io.localization.*;
import jsource.codegenerator.*;
import calpa.html.*;
import java.net.*;


/**
 * <code>APIViewerDialog</code> is the API viewer dialog box for the core Java API.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class APIViewerDialog extends BaseDialog {

    private XMLResourceBundle bundle = null;
    private MainFrame mainFrame = null;
    private JButton close = null;
	private URL apiURL = null;
	private String url = null;
    private CalHTMLPane pane = null;
	private CalHTMLPreferences prefs = null;

    APIViewerDialog(XMLResourceBundle bundle, MainFrame mainFrame) {
        super(null, "JSource API Viewer", true);

        this.bundle = bundle;
        this.mainFrame = mainFrame;
        url = "file:///" + mainFrame.coreAPIPath;
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

		JPanel apiBrowser = new JPanel();
		apiBrowser.setLayout(new BorderLayout());

        try {
            apiURL = new URL(url);
	    } catch (MalformedURLException e) {
			GUIUtilities.showExceptionErrorMessage(bundle.getValueOf("invalidpiurl") + JSConstants.LINE_SEP +
			                                       url + JSConstants.LINE_SEP + e.getMessage());
			Log.log(e);
		}
        prefs = new CalHTMLPreferences();
        prefs.setFormRenderingStyle(CalCons.USE_LOOK_AND_FEEL);
        prefs.setUnderlineLinks(true);
        //prefs.setHomeURL(apiURL); REMOVE???
        pane = new CalHTMLPane(prefs, null, "");
        pane.showHTMLDocument(apiURL);
		apiBrowser.add(pane, BorderLayout.CENTER);

        content.add(BorderLayout.CENTER, apiBrowser);

        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        buttonPanel.add(Box.createGlue());
        close = new JButton(bundle.getValueOf("close"));
        close.addActionListener(new ActionHandler());
        getRootPane().setDefaultButton(close);
        buttonPanel.add(close);
        buttonPanel.add(Box.createGlue());
        content.add(BorderLayout.SOUTH, buttonPanel);
        setResizable(false);
        setLocation(10,30);
        Dimension dim = JSConstants.SCREEN_SIZE;
        setSize((dim.width - 15), (dim.height - 60));
        show();
    }

    public void ok() {
        dispose();
    }

    public void cancel() {
        dispose();
    }

    public CalHTMLPane getCalHTMLPane() {
		return pane;
	}

    class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            dispose();
        }
    }
}
