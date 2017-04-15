package jsource.gui;


/**
 * AntWorkPanel.java 07/28/03
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


public class AntRunPanel extends JPanel implements ActionListener {
    private XMLResourceBundle bundle = null;
    private JList results = null;
    private MainFrame parent = null;
    private DefaultListModel resultModel = null;
    private Console console = null;
    private File buildFile = null;
    private JSourceButton execAntBtn = null;
    private JSourceButton cancel = null;
    private JSourceButton refreshTargets = null;
    private JSourceButton openBuildFile = null;
    private JSourceButton close = null;
    private JTextField targetField = null;
    private JTextField basePathField = null;
    private JTextField buildFileField = null;
    private AntRunDialog ard = null;

    public AntRunPanel(File buildFile, XMLResourceBundle bundle, MainFrame parent, AntRunDialog ard) {
        super(new BorderLayout());
        this.buildFile = buildFile;
        this.bundle = bundle;
        this.parent = parent;
        this.ard = ard;
        console = parent.getConsole();
        targetField = new JTextField(bundle.getValueOf("anttarget"), 30);
        targetField.setEditable(true);

        basePathField = new JTextField(parent.currentDir, 30);
        basePathField.setEditable(true);

        buildFileField = new JTextField(buildFile.getName(), 30);
        buildFileField.setEditable(true);

        JPanel northPane = new JPanel();

        northPane.setLayout(new BorderLayout());

        JPanel pane = new JPanel();

        pane.add(new JLabel(bundle.getValueOf("anttargetlbl")));
        pane.add(targetField);

        northPane.add(pane, BorderLayout.NORTH);

        JPanel pane2 = new JPanel();
		execAntBtn = new JSourceButton(bundle.getValueOf("antexecbtn"));
    	execAntBtn.setMnemonic(bundle.getValueOf("antexecmnc").charAt(0));
    	execAntBtn.setToolTipText(bundle.getValueOf("antexectip"));
    	pane2.add(execAntBtn);

		cancel = new JSourceButton(bundle.getValueOf("cancel"));
		cancel.setMnemonic(bundle.getValueOf("cancelmnc").charAt(0));
        pane2.add(cancel);

        execAntBtn.addActionListener(this);
        cancel.addActionListener(this);

        northPane.add(pane2, BorderLayout.CENTER);

        resultModel = new DefaultListModel();
        results = new JList();
        results.setVisibleRowCount(10);
        FontMetrics fm = getFontMetrics(results.getFont());
        results.setModel(resultModel);
        results.addMouseListener(new MouseHandler());
        getTargets(resultModel);

        JScrollPane scroller = new JScrollPane(results,
                                               JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                               JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        northPane.add(scroller, BorderLayout.SOUTH);

        add(northPane, BorderLayout.NORTH);

        JPanel southPane = new JPanel();

        southPane.setLayout(new BorderLayout());

        JPanel pane3 = new JPanel();

        pane3.add(new JLabel(bundle.getValueOf("antpathlbl")));
        pane3.add(basePathField);
        southPane.add(pane3, BorderLayout.NORTH);

        JPanel pane4 = new JPanel();

        pane4.add(new JLabel(bundle.getValueOf("antbuildlbl")));
        pane4.add(buildFileField);
        southPane.add(pane4, BorderLayout.CENTER);

        JPanel button2Pane = new JPanel();

        refreshTargets = new JSourceButton(bundle.getValueOf("antrefresh"));
        openBuildFile = new JSourceButton(bundle.getValueOf("antopenbuild"));
        close = new JSourceButton(bundle.getValueOf("close"));
		close.setMnemonic(bundle.getValueOf("cancelmnc").charAt(0));
        button2Pane.add(refreshTargets);
        button2Pane.add(openBuildFile);
        button2Pane.add(close);
        openBuildFile.addActionListener(this);
        refreshTargets.addActionListener(this);
        close.addActionListener(this);

        southPane.add(button2Pane, BorderLayout.SOUTH);
        add(southPane, BorderLayout.SOUTH);
    }

    private void getTargets(DefaultListModel resultModel) {
        Project project = new Project();

        project.init();
        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
        // first use the ProjectHelper to create the project object
        // from the given build file.
        try {
            Class.forName("javax.xml.parsers.SAXParserFactory");
            ProjectHelper.configureProject(project, buildFile);
        } catch (NoClassDefFoundError ncdfe) {
            throw new BuildException("No JAXP compliant XML parser found. See http://java.sun.com/xml for the\nreference implementation.", ncdfe);
        } catch (ClassNotFoundException cnfe) {
            throw new BuildException("No JAXP compliant XML parser found. See http://java.sun.com/xml for the\nreference implementation.", cnfe);
        } catch (NullPointerException npe) {
            throw new BuildException("No JAXP compliant XML parser found. See http://java.sun.com/xml for the\nreference implementation.", npe);
        }
        AntWorkListener.printTargets(project, resultModel);
    }

    private void refreshTargets() {
        String basePath = basePathField.getText();
        String fileName = buildFileField.getText();
        buildFile = new File(basePath + JSConstants.FILE_SEP + fileName);

        if (!(buildFile.exists())) {
            GUIUtilities.showError(buildFile.getName() + " " + bundle.getValueOf("filenoexist"));
            return;
        }

        resultModel.removeAllElements();
        getTargets(resultModel);
    }

    private void executeAnt() {
        resultModel.removeAllElements();
        antRun(parent, buildFile, targetField.getText(), console);
        parent.tabbedPane.setSelectedIndex(1);
        ard.dispose();
    }

    protected void antRun(MainFrame main, File aBuildFile, String aTarget, Console cons) {
        try {
            String buildFilePath = aBuildFile.getAbsolutePath();
            String cmdLine = "";

            String args = "-buildfile " + "dummypath"
                    + ((aTarget != null) ? " " + aTarget : "")
                    + ((cmdLine != null) ? " " + cmdLine : "");

            String splitArgs[] = split(args);
            splitArgs[1] = buildFilePath;
			cons.append("-buildfile " + buildFilePath, Color.white);

            Thread thread = new AntWorkListener(main, splitArgs, cons);

            thread.start();
        } catch (org.apache.tools.ant.BuildException exp) {
            cons.append("BuildException: " + exp.toString(), Color.red);
        } catch (Throwable exp) {
            cons.append("Exception: " + exp.toString(), Color.red);
        }
    }

    protected String[] split(String aParams) {
        LinkedList l = new LinkedList();
        StringTokenizer tokenizer = new StringTokenizer(aParams);

        while (tokenizer.hasMoreTokens())
            l.add(tokenizer.nextToken());

        return (String[]) l.toArray(new String[] {} );
    }

    /**
     * A left click on the task copies it as the selected task
     */
    class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent evt) {
            int index = results.locationToIndex(evt.getPoint());

            if (index == -1)
                return;

            String msg = (String) resultModel.getElementAt(index);
            int p1 = msg.indexOf(" - [");
            String target = msg.substring(0, p1);

            targetField.setText(target);
        }
    }

    public void actionPerformed(ActionEvent evt) {
        Object o = evt.getSource();

        if (o == cancel || o == close) {
            ard.ok();
		} else if (o == execAntBtn) {
            executeAnt();
		} else if (o == openBuildFile) {
            parent.openBuildFile();
		} else if (o == refreshTargets) {
            refreshTargets();
		}
    }
}
