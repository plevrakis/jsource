package jsource.gui;


/**
 * CodeGenerator.java 03/27/03
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
import java.io.PrintStream;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;
import jsource.codegenerator.*;
import jsource.util.*;
import jsource.syntax.*;
import jsource.syntax.tokenmarker.*;
import jsource.io.localization.*;

/**
 * <code>CodeGeneratorPanel</code> is a panel that holds tools used
 * to generate frame code for Java and C# classes.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class CodeGeneratorPanel extends JPanel {

    private LanguageManager LanguageManager = null;
    private PropertiesTableModel model = null;
    private ImportsTableModel importModel = null;
    private PropertyTableCellEditor CelEditor = null;
    private JTextField txtPropName = null;
    private JTextField txtImportClass = null;
    private JComboBox cmbPropTypes = null;
    private JCheckBox chkReadOnly = null;
    private JButton btnAdd = null;
    private JButton btnRemove = null;
    private JButton btnRemoveAll = null;
    private JTable tblProps = null;
    private JTable tblImports = null;
    private JComboBox cmbLanguages = null;
    private JButton btnGenerate = null;
    private JTextField txtClassName = null;
    private JSEditor editor[] = null;;
    private JPopupMenu popup = null;
    private JTabbedPane tpOutputTabs = null;
    private SyntaxDocument doc[] = null;
    private XMLResourceBundle bundle = null;
    private MainFrame mainFrame = null;
    private Action cutAction = null;
    private Action copyAction = null;
    private Action pasteAction = null;
    private Action undoAction = null;
    private Action redoAction = null;
    private Action selectAllAction = null;
    private String lineSep = JSConstants.LINE_SEP;

    public CodeGeneratorPanel(XMLResourceBundle bundle, MainFrame mainFrame) {
		this.bundle = bundle;
		this.mainFrame = mainFrame;
        cutAction = new CutAction(bundle.getValueOf("cut"), GUIUtilities.createIcon("cut.jpg"),
                bundle.getValueOf("cutdesc"), new Integer(KeyEvent.VK_T));
        copyAction = new CopyAction(bundle.getValueOf("copy"), GUIUtilities.createIcon("copy.jpg"),
                bundle.getValueOf("copydesc"), new Integer(KeyEvent.VK_Y));
        pasteAction = new PasteAction(bundle.getValueOf("paste"), GUIUtilities.createIcon("paste.jpg"),
                bundle.getValueOf("pastedesc"), new Integer(KeyEvent.VK_P));
        undoAction = new UndoAction(bundle.getValueOf("undo"), GUIUtilities.createIcon("undo.jpg"),
                bundle.getValueOf("undodesc"), new Integer(KeyEvent.VK_U));
        redoAction = new RedoAction(bundle.getValueOf("redo"), GUIUtilities.createIcon("redo.jpg"),
                bundle.getValueOf("redodesc"), new Integer(KeyEvent.VK_R));
        selectAllAction = new SelectAllAction(bundle.getValueOf("selectall"), null,
                bundle.getValueOf("selectalldesc"), new Integer(KeyEvent.VK_A));
        popup = getPopupMenu(new JPopupMenu());
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        LanguageManager = new JavaLanguageManager();
        model = new PropertiesTableModel(LanguageManager);
        importModel = new ImportsTableModel(LanguageManager);
        txtPropName = new JTextField();
        txtImportClass = new JTextField();
        cmbPropTypes = new JComboBox();
        chkReadOnly = new JCheckBox(bundle.getValueOf("readonly"));
        btnAdd = new JButton(bundle.getValueOf("add"));
        btnRemove = new JButton(bundle.getValueOf("remove"));
        btnRemoveAll = new JButton(bundle.getValueOf("removeall"));
        tblProps = new JTable();
        tblImports = new JTable();
        cmbLanguages = new JComboBox();
        btnGenerate = new JButton(bundle.getValueOf("generate"));
        txtClassName = new JTextField(30);
        doc = new SyntaxDocument[3];
        editor = new JSEditor[3];
        for (int i = 0;i < 3;i++) {
			editor[i] = new JSEditor(mainFrame);
			doc[i] = new SyntaxDocument();
			editor[i].setDocument(doc[i]);
			editor[i].setRightClickPopup(popup);
		}
        tpOutputTabs = new JTabbedPane();
        setLayout(new BorderLayout(5, 5));
        JPanel p1 = new JPanel(new GridLayout(0, 1, 3, 3));

        JPanel first = new JPanel(new GridLayout(1,2));
		first.add(new JLabel(bundle.getValueOf("import")), JLabel.CENTER);
        first.add(txtImportClass);
		JPanel second = new JPanel(new GridLayout(1,2));
		second.add(new JLabel(bundle.getValueOf("propertyname") + ": "), JLabel.CENTER);
        second.add(txtPropName);
        JPanel third = new JPanel(new GridLayout(1,2));
		third.add(new JLabel(bundle.getValueOf("propertytype") + ": "), JLabel.CENTER);
        third.add(cmbPropTypes);
        JPanel fourth = new JPanel(new GridLayout(1,2));
		fourth.add(chkReadOnly);
        fourth.add(new JLabel(""));
        p1.add(first);
        p1.add(second);
        p1.add(third);
        p1.add(fourth);
        Insets insets = p1.getInsets();

        insets.left = 10;
        insets.top = 10;
        insets.right = 10;
        insets.bottom = 10;
        String types[] = LanguageManager.getTypes().getTypeStrings();

        for (int i = 0; i < types.length; i++)
            cmbPropTypes.addItem(types[i]);

        TitledBorder tb = new TitledBorder(new LineBorder(Color.black), bundle.getValueOf("deftitle"));

        p1.setBorder(tb);
        JPanel p2 = new JPanel();

        p2.add(btnAdd);
        p2.add(btnRemove);
        p2.add(btnRemoveAll);
        JPanel p3 = new JPanel(new BorderLayout());

        p3.add(p2, BorderLayout.NORTH);
        JPanel tablesPanel = new JPanel(new BorderLayout());
        JScrollPane propsPane = new JScrollPane(tblProps);
        propsPane.setSize(800,150);
        tablesPanel.add(propsPane, BorderLayout.CENTER);
        JScrollPane importsPane = new JScrollPane(tblImports);
        tablesPanel.add(importsPane, BorderLayout.EAST);
        p3.add(tablesPanel, BorderLayout.CENTER);

        tblProps.setModel(model);
        tblImports.setModel(importModel);
        JComboBox combo = new JComboBox();

        combo.addItem("true");
        combo.addItem("false");
        TableColumnModel columnmodel = tblProps.getColumnModel();

        columnmodel.getColumn(3).setCellEditor(new PropertyTableCellEditor(combo));
        CelEditor = new PropertyTableCellEditor(LanguageManager, new JComboBox());
        columnmodel.getColumn(1).setCellEditor(CelEditor);
        JPanel p4 = new JPanel(new BorderLayout(5, 5));

        p4.add(p1, BorderLayout.NORTH);
        p4.add(p3, BorderLayout.CENTER);
        JPanel p5 = new JPanel(new BorderLayout(5, 5));
        JPanel p6 = new JPanel(new GridLayout());
        JLabel lbl = new JLabel(bundle.getValueOf("language") + ":", JLabel.CENTER);

        p6.add(lbl);
        p6.add(cmbLanguages);
        p6.add(btnGenerate);
        lbl = new JLabel(bundle.getValueOf("classname") + ":", JLabel.CENTER);
        lbl.setAlignmentX(1.0F);
        p6.add(lbl);
        p6.add(txtClassName);
        cmbLanguages.addItem("Java");
        cmbLanguages.addItem("C#");
        p5.add(p6, BorderLayout.NORTH);
        tpOutputTabs.add(bundle.getValueOf("definition"), editor[0]);
        tpOutputTabs.add(bundle.getValueOf("members"), editor[1]);
        tpOutputTabs.add(bundle.getValueOf("constructors"), editor[2]);
        p5.add(tpOutputTabs, BorderLayout.CENTER);
        JSplitPane split_pane = new JSplitPane(0, p4, p5);

        split_pane.setDividerLocation(300);
        split_pane.setOneTouchExpandable(true);
        add(split_pane);

        ActionListener ah = new ActionHandler();
        btnAdd.addActionListener(ah);
        btnRemove.addActionListener(ah);
        btnRemoveAll.addActionListener(ah);
        btnGenerate.addActionListener(ah);

        KeyListener kh = new KeyHandler();
        txtPropName.addKeyListener(kh);

        ItemListener ih = new ItemHandler();
        cmbLanguages.addItemListener(ih);
        cmbPropTypes.addItemListener(ih);

        ToolTipManager.sharedInstance().registerComponent(cmbPropTypes);
    }

    private void changeLanguageManager(LanguageManager manager) {
        PropertiesTableModel model = (PropertiesTableModel) tblProps.getModel();

        model.setManager(manager);
        TypeDef types[] = manager.getTypes().getTypes();

        cmbPropTypes.removeAllItems();
        for (int i = 0; i < types.length; i++)
            cmbPropTypes.addItem(types[i]);

        CelEditor.setManager(manager);
    }

    private void setLanguageManager(String item) {
        if (item.toLowerCase().equals("java"))
            LanguageManager = new JavaLanguageManager();
        else
            LanguageManager = new CSharpLanguageManager();
    }

    private void addPropAndClass() {
        String name = txtPropName.getText().trim();
        String imp = txtImportClass.getText().trim();

        if (name.equals("") && imp.equals("")) {
            new MessageDialog(bundle.getValueOf("novaluesmsg"), bundle);
            return;
        } else {
            String first_letter = name.substring(0, 1).toUpperCase();
            String the_rest = name.substring(1);

            name = first_letter + the_rest;
            Property p = new Property(name, LanguageManager.getTypes().getTypeFromString(cmbPropTypes.getSelectedItem().toString()), chkReadOnly.isSelected());
            model.add(p);
            importModel.add(imp);
            return;
        }
    }

    private void removePropAndClass() {
		int index1 = -2;
		int index2 = -2;

		if (model.getRowCount() != 0)
        	index1 = tblProps.getSelectedRow();

        if (importModel.getRowCount() != 0)
        	index2 = tblImports.getSelectedRow();

        if (index1 == -1 && index2 == -1) {
            JOptionPane.showMessageDialog(this, bundle.getValueOf("selectrowmsg"), "Warning", 2);
        } else if (index1 == -2 && index2 == -1) {
            JOptionPane.showMessageDialog(this, bundle.getValueOf("selectrowmsg"), "Warning", 2);
        } else if (index1 == -1 && index2 == -2) {
            JOptionPane.showMessageDialog(this, bundle.getValueOf("selectrowmsg"), "Warning", 2);
        } else {
			if (index1 != -1 && index1 != -2)
            	model.remove(index1);
            if (index2 != -1 && index2 != -2)
            	importModel.remove(index2);
        }
    }

    private void removeAllPropsAndClasses() {
        if (model != null)
            model.removeAll();
            importModel.removeAll();
    }

    private void generateProperties() {
        generate();
    }

    private void generate() {
        DefinitionGenerator generator = LanguageManager.getGenerator();

        editor[1].setText("");
        editor[2].setText("");
        StringBuffer all = new StringBuffer();

        for (int i = 0; i < importModel.getRowCount(); i++) {
            String importValue = importModel.getImport(i);

            if (importValue != null && importValue != "") {
                if (!importValue.endsWith(";"))
                	all.append("import " + importValue + ";" + lineSep);
                else
                    all.append("import " + importValue + lineSep);
			}
        }
        all.append(lineSep + lineSep);
        all.append(generator.getClassDefinition(txtClassName.getText().trim()));
        StringBuffer sb = new StringBuffer();

        sb.append(generator.getMembersStartComment());
        for (int i = 0; i < model.getRowCount(); i++) {
            Property p = model.getProperty(i);

            if (p != null)
                sb.append(generator.getMemberVarDefinition(p));
        }

        sb.append(generator.getMembersEndComment());
        all.append(sb.toString());
        all.append(generator.getConstructors(txtClassName.getText().trim(), model.getProperties()));
        StringBuffer defs = new StringBuffer(generator.getAccessorsStartComment());

        for (int i = 0; i < model.getRowCount(); i++) {
            Property p = model.getProperty(i);

            if (p != null)
                defs.append(generator.getDefinition(p));
        }

        defs.append(generator.getAccessorsEndComment());
        sb.append(defs.toString());
        all.append(defs.toString());
        all.append(generator.toString(model.getProperties()));
        all.append(lineSep + "}" + lineSep);
        editor[0].setText(all.toString());
        editor[1].setText(sb.toString());
        editor[2].setText(generator.getConstructors(txtClassName.getText().trim(), model.getProperties()));
        if (mainFrame.editor.getText().trim().equals("")) {
			//mainFrame.editor.setCaretPosition(0);
			mainFrame.editor.setText(all.toString());
		}
    }

    /**
     * Sets up the popup menu
     * @return popup the JPopupMenu
     */
    private JPopupMenu getPopupMenu(JPopupMenu popup) {
        JMenuItem menuItem;

        // "Undo" menu item
        menuItem = new JMenuItem(undoAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "Redo" menu item
        menuItem = new JMenuItem(redoAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "Select All" menu item
        menuItem = new JMenuItem(selectAllAction);
        popup.add(menuItem);
        // "Cut" menu item
        menuItem = new JMenuItem(cutAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "Copy" menu item
        menuItem = new JMenuItem(copyAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "Paste" menu item
        menuItem = new JMenuItem(pasteAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);

        return popup;
    }

    /*************************** Inner Action classes ************************************/

    private class CutAction extends AbstractAction {

        public CutAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            editor[tpOutputTabs.getSelectedIndex()].cut();
        }
    }

    private class CopyAction extends AbstractAction {

        public CopyAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            editor[tpOutputTabs.getSelectedIndex()].copy();
        }
    }


    private class PasteAction extends AbstractAction {

        public PasteAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            editor[tpOutputTabs.getSelectedIndex()].paste();
        }
    }

    private class UndoAction extends AbstractAction {

        public UndoAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            editor[tpOutputTabs.getSelectedIndex()].undo();
        }
    }

    private class RedoAction extends AbstractAction {

        public RedoAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            editor[tpOutputTabs.getSelectedIndex()].redo();
        }
    }

    private class SelectAllAction extends AbstractAction {

        public SelectAllAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            editor[tpOutputTabs.getSelectedIndex()].selectAll();
        }
    }

    /*************************** Inner Listener classes ************************************/

    private class KeyHandler extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            if (e.getSource() == txtPropName && e.getKeyCode() == 10)
                addPropAndClass();
        }
    }

    private class ActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            if (source == btnAdd)
                addPropAndClass();
            else
            if (source == btnRemove)
                removePropAndClass();
            else
            if (source == btnRemoveAll)
                removeAllPropsAndClasses();
            else
            if (source == btnGenerate)
                generateProperties();
        }
    }

    private class ItemHandler implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            Object source = e.getSource();
            String lang = e.getItem().toString();

            if (source == cmbLanguages && e.getStateChange() == 1) {
                setLanguageManager(lang);
                changeLanguageManager(LanguageManager);
                if (lang.equals("C#")) {
			        for (int i = 0;i < 3;i++) {
			            editor[i].setTokenMarker(new CSharpTokenMarker());
					}
				} else {
			        for (int i = 0;i < 3;i++) {
			            editor[i].setTokenMarker(new JavaTokenMarker());
					}
				}
            }
        }
    }
}
