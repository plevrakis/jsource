package jsource.gui;


/**
 * FindReplacePanel.java  03/31/03
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
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.text.*;
import jsource.*;
import jsource.syntax.*;
import jsource.syntax.tokenmarker.*;
import jsource.util.*;
import jsource.io.*;
import jsource.io.localization.*;

/**
 * <code>FindReplacePanel</code> is the main panel of the find/replace dialog.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class FindReplacePanel extends JPanel {

    private JOptionPane optionPane = null;
    private JButton findNextButton = null;
    private JButton replaceButton = null;
    private JButton replaceFindButton = null;
    private JButton replaceAllButton = null;
    private JTextField findField = null;
    private JTextField replaceField = null;
    private JLabel message = null;
    private JPanel labelPanel = null;
    private JCheckBox matchCase = null;
    private JPanel matchCaseAndClosePanel = null;
    private JPanel rightPanel = null;
    private FindReplaceMachine machine = null;
    private boolean caretChanged = false;
    private CaretListener caretListener = null;
    private Action findNextAction = null;
    private Action replaceAction = null;
    private Action replaceFindAction = null;
    private Action replaceAllAction = null;
    private MainFrame frame = null;
    private XMLResourceBundle bundle = null;
    private JSEditor editor = null;

    class MatchCaseListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == 2)
                machine.setMatchCase(false);
            if(e.getStateChange() == 1)
                machine.setMatchCase(true);
        }
    }

    public void requestFocus() {
        super.requestFocus();
        findField.requestFocus();
        findField.selectAll();
    }

    public void findNext() {
        if(findField.getText().length() > 0)
            doFind();
    }

    public void beginListeningTo(JSEditor editor) {
        this.editor = editor;
        editor.addCaretListener(caretListener);
        caretChanged = true;
        updateMachine();
        machine.setFindWord(findField.getText());
        machine.setReplaceWord(replaceField.getText());
        message.setText("");
        if(!machine.isOnMatch() || findField.getText().equals("")) {
            replaceAction.setEnabled(false);
            replaceFindAction.setEnabled(false);
        } else {
            replaceAction.setEnabled(true);
            replaceFindAction.setEnabled(true);
        }
        if(findField.getText().equals(""))
            replaceAllAction.setEnabled(false);
        else
            replaceAllAction.setEnabled(true);

        message.setText("");
    }

    public void stopListening() {
        if(editor != null) {
            editor.removeCaretListener(caretListener);
            editor = null;
        }
    }

    private void doFind() {
        updateMachine();
        machine.setFindWord(findField.getText());
        machine.setReplaceWord(replaceField.getText());
        message.setText("");
        FindResult fr = machine.findNext();
        if(fr.getWrapped()) {
            Toolkit.getDefaultToolkit().beep();
            message.setText(bundle.getValueOf("wrappedmsg"));
        }
        int pos = fr.getFoundOffset();
        if(pos >= 0) {
            selectFoundItem(pos - machine.getFindWord().length(), pos);
            replaceAction.setEnabled(true);
            replaceFindAction.setEnabled(true);
        } else
        if(pos == -1) {
            Toolkit.getDefaultToolkit().beep();
            message.setText(bundle.getValueOf("searchtext") + " " + machine.getFindWord() + " " + bundle.getValueOf("notfound"));
        }
    }

    public FindReplacePanel(final XMLResourceBundle bundle, MainFrame frame) {
        this.bundle = bundle;
        this.frame = frame;
        findField = new JTextField(20);
        replaceField = new JTextField(20);
        caretListener = new CaretListener() {

            public void caretUpdate(CaretEvent e) {
                replaceAction.setEnabled(false);
                replaceFindAction.setEnabled(false);
                caretChanged = true;
            }

        };
        findNextAction = new AbstractAction(bundle.getValueOf("findnext")) {

            public void actionPerformed(ActionEvent e) {
                doFind();
                findField.requestFocus();
            }

        };
        replaceAction = new AbstractAction(bundle.getValueOf("replace")) {

            public void actionPerformed(ActionEvent e) {
                updateMachine();
                machine.setFindWord(findField.getText());
                String replaceWord = replaceField.getText();
                machine.setReplaceWord(replaceWord);
                message.setText("");
                boolean replaced = machine.replaceCurrent();
                if(replaced) {
                    int pos = machine.getCurrentOffset();
                    selectFoundItem(pos - replaceWord.length(), pos);
                }
                replaceAction.setEnabled(false);
                replaceFindAction.setEnabled(false);
                replaceButton.requestFocus();
            }

        };
        replaceFindAction = new AbstractAction(bundle.getValueOf("repfindnext")) {

            public void actionPerformed(ActionEvent e) {
                machine.setFindWord(findField.getText());
                String replaceWord = replaceField.getText();
                machine.setReplaceWord(replaceWord);
                message.setText("");
                boolean replaced = machine.replaceCurrent();
                if(replaced) {
                    int pos = machine.getCurrentOffset();
                    selectFoundItem(pos - replaceWord.length(), pos);
                    doFind();
                    replaceFindButton.requestFocus();
                } else {
                    replaceAction.setEnabled(false);
                    replaceFindAction.setEnabled(false);
                    Toolkit.getDefaultToolkit().beep();
                    message.setText(bundle.getValueOf("replacefailed"));
                }
            }

        };
        replaceAllAction = new AbstractAction(bundle.getValueOf("replaceall")) {

            public void actionPerformed(ActionEvent e) {
                updateMachine();
                machine.setFindWord(findField.getText());
                machine.setReplaceWord(replaceField.getText());
                message.setText("");
                int count = machine.replaceAll();
                Toolkit.getDefaultToolkit().beep();
                message.setText(bundle.getValueOf("replaced") + count + " " + bundle.getValueOf("occurrence") + (count != 1 ? "s" : "") + ".");
                replaceAction.setEnabled(false);
                replaceFindAction.setEnabled(false);
            }

        };
        int i = 1;
        InputMap im = getInputMap(i);
        im.put(KeyStroke.getKeyStroke(10, 0), bundle.getValueOf("findnext"));
        ActionMap am = getActionMap();
        am.put(bundle.getValueOf("findnext"), findNextAction);
        findNextButton = new JButton(findNextAction);
        replaceButton = new JButton(replaceAction);
        replaceFindButton = new JButton(replaceFindAction);
        replaceAllButton = new JButton(replaceAllAction);
        message = new JLabel("");
        replaceAction.setEnabled(false);
        replaceFindAction.setEnabled(false);
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 0, 5, 0));
        buttons.add(findNextButton);
        buttons.add(replaceButton);
        buttons.add(replaceFindButton);
        buttons.add(replaceAllButton);
        JLabel findLabel = new JLabel(bundle.getValueOf("find"), 2);
        findLabel.setHorizontalAlignment(2);
        JLabel replaceLabel = new JLabel(bundle.getValueOf("replace"), 2);
        replaceLabel.setHorizontalAlignment(2);
        labelPanel = new JPanel(new GridLayout(0, 1));
        labelPanel.add(findLabel);
        labelPanel.add(replaceLabel);
        labelPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
        MatchCaseListener mcl = new MatchCaseListener();
        matchCase = new JCheckBox(bundle.getValueOf("matchcase"), true);
        matchCase.addItemListener(mcl);
        removeAll();
        matchCaseAndClosePanel = new JPanel(new BorderLayout());
        matchCaseAndClosePanel.add(matchCase, "West");
        rightPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        JPanel midPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        JPanel farRightPanel = new JPanel(new GridLayout(2, 1));
        midPanel.add(wrap(findField));
        midPanel.add(wrap(replaceField));
        farRightPanel.add(matchCaseAndClosePanel);
        farRightPanel.add(message);
        rightPanel.add(midPanel);
        rightPanel.add(farRightPanel);
        hookComponents(this, rightPanel, labelPanel, buttons);
        machine = new FindReplaceMachine();
        findField.addActionListener(findNextAction);
        findField.getDocument().addDocumentListener(new DocumentListener() {

	             public void changedUpdate(DocumentEvent e) {
	                 machine.makeCurrentOffsetStart();
	                 replaceAction.setEnabled(false);
	                 replaceFindAction.setEnabled(false);
	                 if(findField.getText().equals(""))
	                     replaceAllAction.setEnabled(false);
	                 else
	                     replaceAllAction.setEnabled(true);
	             }

	             public void insertUpdate(DocumentEvent e) {
	                 machine.makeCurrentOffsetStart();
	                 replaceAction.setEnabled(false);
	                 replaceFindAction.setEnabled(false);
	                 if(findField.getText().equals(""))
	                     replaceAllAction.setEnabled(false);
	                 else
	                     replaceAllAction.setEnabled(true);
	             }

	             public void removeUpdate(DocumentEvent e) {
	                 machine.makeCurrentOffsetStart();
	                 replaceAction.setEnabled(false);
	                 replaceFindAction.setEnabled(false);
	                 if(findField.getText().equals(""))
	                     replaceAllAction.setEnabled(false);
	                 else
	                     replaceAllAction.setEnabled(true);
	             }

	         });
    }

    public void setFieldFont(Font f) {
        findField.setFont(f);
        replaceField.setFont(f);
    }

    private static Container wrap(JComponent comp) {
        Container stretcher = Box.createHorizontalBox();
        stretcher.add(comp);
        stretcher.add(Box.createHorizontalGlue());
        return stretcher;
    }

    private static void hookComponents(Container parent, JComponent q1, JComponent q2, JComponent q4) {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        parent.setLayout(gbl);
        c.fill = 1;
        addComp(parent, q2, c, gbl, 0, 0, 0.0F, 0.0F, 1, 0);
        addComp(parent, q1, c, gbl, 0, 1, 1.0F, 0.0F, 1, 0);
        addComp(parent, new JPanel(), c, gbl, 1, 0, 1.0F, 1.0F, 2, 0);
        addComp(parent, new JPanel(), c, gbl, 2, 0, 0.0F, 0.0F, 1, 0);
        addComp(parent, q4, c, gbl, 2, 1, 1.0F, 0.0F, 1, 0);
    }

    private static void addComp(Container p, JComponent child, GridBagConstraints c, GridBagLayout gbl, int row, int col, float weightx, float weighty,
            int gridw, int ipady) {
        c.gridx = col;
        c.gridy = row;
        c.weightx = weightx;
        c.weighty = weighty;
        c.gridwidth = gridw;
        c.ipady = ipady;
        gbl.setConstraints(child, c);
        p.add(child);
    }

    private void updateMachine() {
        if(caretChanged) {
            machine.setDocument(editor.getDocument());
            machine.setStart(editor.getCaretPosition());
            machine.setPosition(editor.getCaretPosition());
            caretChanged = false;
        }
    }

    private void selectFoundItem(int from, int to) {
        editor.select(from, to);
    }
}