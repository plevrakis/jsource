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

import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComponent;

import jsource.util.*;


/**
 * AntWorkPanel shows all javac messages in the Console.
 */
public class AntWorkPanel extends JScrollPane {
    private JList errorList = null;
    private DefaultListModel errorModel = null;
    private MainFrame mainFrame = null;
    private JTabbedPane tabbedPane = null;
    boolean flag = false;
    private int index = 0;

    public AntWorkPanel(MainFrame mainFrame) {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.mainFrame = mainFrame;
        tabbedPane = mainFrame.tabbedPane;
        flag = true;
        errorModel = new DefaultListModel();
        errorList = new JList(errorModel);
        errorList.addMouseListener(new MouseHandler());
        this.setViewportView(this.errorList);
    }

    /**
     * Appends a new message to the error list
     * Opens this AntWorkPanel in the Console-TabbedPane if the first message
     * is appended.
     */
    public void append(AntWorkMessage mess) {
        errorModel.addElement(mess);
        if (flag) {
            flag = false;
            tabbedPane.setSelectedIndex(index);
        }
    }

    /**
     * Opens the error at the specified index of the error List
     */
    private void openErrorAt(int index) {
        final AntWorkMessage error = (AntWorkMessage) errorModel.getElementAt(index);
        String file = error.getAbsoluteFilename();

        if (file == null)
            return;

        if (!(new File(file)).exists()) {
            GUIUtilities.showError(file + " " + mainFrame.bundle.getValueOf("filenoexist"));
            return;
        }

        // String _file;
        // JextTextArea textArea;
        // JextTextArea[] areas = jextFrame.getTextAreas();

        // out:   for (int i = 0; i < areas.length; i++)
        // {
        // textArea = areas[i];
        // if (textArea.isNew())
        // continue;

        // _file = textArea.getCurrentFile();
        // if (_file != null && _file.equals(file)) {
        // jextFrame.getTabbedPane().setSelectedComponent(textArea);
        // gotoErrorPos(textArea, error.getLine(), error.getColumn() );
        // return;
        // }
        // }
        //
        // textArea = jextFrame.open(file);
        // gotoErrorPos(textArea, error.getLine(), error.getColumn() );
    }

    public void gotoErrorPos(JSEditor textArea, int line, int column) {// textArea.setCaretPosition(textArea.getLineStartOffset(line - 1)+column);
        // textArea.grabFocus();
    }

    /**
     * A left click on the error opens the file and jumps to the error location
     */
    class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent evt) {
            int index = errorList.locationToIndex(evt.getPoint());

            if (index == -1)
                return;

            openErrorAt(index);
        }
    }

}
