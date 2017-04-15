package jsource.gui;


/**
 * Actions.java 06/18/03
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
import java.beans.*;
import java.io.*;
import javax.swing.*;

import jsource.actions.*;
import jsource.util.*;
import jsource.io.localization.*;

/**
 * <code>Actions</code> is the class that creates and holds all the <code>Action</code> objects.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class Actions {

	private PropertyChangeListener listener;
	private XMLResourceBundle bundle = null;
	private MainFrame main = null;
	private JMenuBar menuBar = null;
	private JToolBar toolBar = null;

    protected Action newAction = null;
    protected Action openAction = null;
    protected Action jarViewerAction = null;
    protected Action closeAction = null;
    protected Action cutAction = null;
    protected Action copyAction = null;
    protected Action pasteAction = null;
    protected Action saveAction = null;
    protected Action saveAsAction = null;
    protected Action undoAction = null;
    protected Action redoAction = null;
    protected Action selectAllAction = null;
    protected Action findReplaceAction = null;
    protected Action generateCodeAction = null;
    protected Action compileAction = null;
    protected Action antRunAction = null;
    protected Action runAction = null;
    protected Action runAppletAction = null;
    protected Action openBrowserAction = null;
    protected Action indentLinesAction = null;
    protected Action commentLinesAction = null;
    protected Action uncommentLinesAction = null;
    protected Action viewAPIAction = null;
    protected Action javadocWizAction = null;
	protected Action newProjectAction = null;

	public Actions(PropertyChangeListener listener, MainFrame main, XMLResourceBundle bundle) {
		this.listener = listener;
		this.main = main;
		this.bundle = bundle;
		setupActions();
		setToolBar(main.toolBar);
		main.setJMenuBar(setMenuBar());
		main.editorPopup = getEditorPopupMenu(new JPopupMenu());
		main.notepadPopup = getNotepadPopupMenu(new JPopupMenu());
	}

	private void setupActions() {
        newAction = new NewAction(listener, bundle.getValueOf("new"), GUIUtilities.createIcon("new.jpg"),
                bundle.getValueOf("newdesc"), new Integer(KeyEvent.VK_N));
        openAction = new OpenAction(listener, bundle.getValueOf("open"), GUIUtilities.createIcon("open.jpg"),
                bundle.getValueOf("opendesc"), new Integer(KeyEvent.VK_O));
        closeAction = new CloseAction(listener, bundle.getValueOf("close"), GUIUtilities.createIcon("close.jpg"),
                bundle.getValueOf("closedesc"), new Integer(KeyEvent.VK_C));
        cutAction = new CutAction(listener, bundle.getValueOf("cut"), GUIUtilities.createIcon("cut.jpg"),
                bundle.getValueOf("cutdesc"), new Integer(KeyEvent.VK_T));
        copyAction = new CopyAction(listener, bundle.getValueOf("copy"), GUIUtilities.createIcon("copy.jpg"),
                bundle.getValueOf("copydesc"), new Integer(KeyEvent.VK_Y));
        pasteAction = new PasteAction(listener, bundle.getValueOf("paste"), GUIUtilities.createIcon("paste.jpg"),
                bundle.getValueOf("pastedesc"), new Integer(KeyEvent.VK_P));
        saveAction = new SaveAction(listener, bundle.getValueOf("save"), GUIUtilities.createIcon("save.jpg"),
                bundle.getValueOf("savedesc"), new Integer(KeyEvent.VK_S));
        saveAsAction = new SaveAsAction(listener, bundle.getValueOf("saveas"), GUIUtilities.createIcon("saveas.jpg"),
                bundle.getValueOf("saveasdesc"), new Integer(KeyEvent.VK_V));
        undoAction = new UndoAction(listener, bundle.getValueOf("undo"), GUIUtilities.createIcon("undo.jpg"),
                bundle.getValueOf("undodesc"), new Integer(KeyEvent.VK_U));
        redoAction = new RedoAction(listener, bundle.getValueOf("redo"), GUIUtilities.createIcon("redo.jpg"),
                bundle.getValueOf("redodesc"), new Integer(KeyEvent.VK_R));
        selectAllAction = new SelectAllAction(listener, bundle.getValueOf("selectall"), null,
                bundle.getValueOf("selectalldesc"), new Integer(KeyEvent.VK_A));
        findReplaceAction = new FindReplaceAction(listener, bundle.getValueOf("findreplace"), GUIUtilities.createIcon("find.jpg"),
                bundle.getValueOf("findrepdesc"), new Integer(KeyEvent.VK_F));
        generateCodeAction = new GenerateCodeAction(listener, bundle.getValueOf("gencode"), GUIUtilities.createIcon("gencode.jpg"),
                bundle.getValueOf("gencodedesc"), new Integer(KeyEvent.VK_F));
        jarViewerAction = new JarViewerAction(listener, bundle.getValueOf("jarview"), null,
                bundle.getValueOf("jarviewdesc"), new Integer(KeyEvent.VK_J));
        compileAction = new CompileAction(listener, bundle.getValueOf("compile"), GUIUtilities.createIcon("compile.jpg"),
                bundle.getValueOf("compiledesc"), new Integer(KeyEvent.VK_B));
        antRunAction = new AntRunAction(listener, bundle.getValueOf("ant"), GUIUtilities.createIcon("ant.jpg"),
                bundle.getValueOf("antdesc"), new Integer(KeyEvent.VK_A));
        runAction = new RunAction(listener, bundle.getValueOf("run"), GUIUtilities.createIcon("run.jpg"),
                bundle.getValueOf("rundesc"), new Integer(KeyEvent.VK_J));
        runAppletAction = new RunAppletAction(listener, bundle.getValueOf("applet"), GUIUtilities.createIcon("applet.jpg"),
                bundle.getValueOf("appletdesc"), new Integer(KeyEvent.VK_L));
        openBrowserAction = new OpenBrowserAction(listener, bundle.getValueOf("browser"), GUIUtilities.createIcon("browser.jpg"),
                bundle.getValueOf("browserdesc"), new Integer(KeyEvent.VK_W));
        indentLinesAction = new IndentLinesAction(listener, bundle.getValueOf("indent"), GUIUtilities.createIcon("indent.jpg"),
                bundle.getValueOf("indentdesc"), new Integer(KeyEvent.VK_I));
        commentLinesAction = new CommentLinesAction(listener, bundle.getValueOf("comment"), GUIUtilities.createIcon("comment.jpg"),
                bundle.getValueOf("commentdesc"), new Integer(KeyEvent.VK_M));
        uncommentLinesAction = new UncommentLinesAction(listener, bundle.getValueOf("uncomment"), GUIUtilities.createIcon("uncomment.jpg"),
                bundle.getValueOf("uncommentdesc"), new Integer(KeyEvent.VK_U));
        viewAPIAction = new ViewAPIAction(listener, bundle.getValueOf("apiviewer"), GUIUtilities.createIcon("api.jpg"),
                bundle.getValueOf("apiviewerdesc"), new Integer(KeyEvent.VK_A));
        javadocWizAction = new JavadocWizAction(listener, bundle.getValueOf("jdocwiz"), null,
                bundle.getValueOf("jdocwizdesc"), new Integer(KeyEvent.VK_J));
        newProjectAction = new NewProjectAction(listener, bundle.getValueOf("newproj"), GUIUtilities.createIcon("newproj.jpg"),
                bundle.getValueOf("newprojdesc"), new Integer(KeyEvent.VK_F5));
	}

    /**
     * Sets up the toolbar
     * @param toolBar the JToolBar to set up
     */
    private void setToolBar(JToolBar toolBar) {
        JSourceButton button = null;


        // New Document Button
        button = new JSourceButton (newAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // New Project Button
        button = new JSourceButton (newProjectAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Open File Button
        button = new JSourceButton (openAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Close File Button
        button = new JSourceButton (closeAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Save File Button
        button = new JSourceButton (saveAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Save As File Button
        button = new JSourceButton (saveAsAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Undo Button
        button = new JSourceButton (undoAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Redo Button
        button = new JSourceButton (redoAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Cut Button
        button = new JSourceButton (cutAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Copy Button
        button = new JSourceButton (copyAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Paste Button
        button = new JSourceButton (pasteAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Indent Button
        button = new JSourceButton (indentLinesAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Comment Button
        button = new JSourceButton (commentLinesAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Uncomment Button
        button = new JSourceButton (uncommentLinesAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Find/Replace Button
        button = new JSourceButton (findReplaceAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Code Generator Button
        button = new JSourceButton (generateCodeAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Compile Button
        button = new JSourceButton (compileAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Ant Button
        button = new JSourceButton (antRunAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Run Button
        button = new JSourceButton (runAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Applet Button
        button = new JSourceButton (runAppletAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // Browser Button
        button = new JSourceButton (openBrowserAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);

        // API Viewer Button
        button = new JSourceButton (viewAPIAction);
        button.setBorderPainted(false);
        if (button.getIcon() != null) {
            button.setText(""); // show only the icon
        }
        toolBar.add(button);
    }

    /**
     * Setup the menu bar
     * @return the JMenuBar object
     */
    private JMenuBar setMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItem;
        final JCheckBoxMenuItem checkBox;

        // File Menu
        menu = new JMenu(bundle.getValueOf("file"));
        menu.setMnemonic(KeyEvent.VK_F);
        // New File
        menuItem = new JMenuItem(newAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // New Project
        menuItem = new JMenuItem(newProjectAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Open File
        menuItem = new JMenuItem(openAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Close File
        menuItem = new JMenuItem(closeAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // add menu separator
        menu.addSeparator();
        // Save changes
        menuItem = new JMenuItem(saveAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Save as new file
        menuItem = new JMenuItem(saveAsAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // separator
        menu.addSeparator();
        // exit
        menuItem = new JMenuItem(bundle.getValueOf("exit"));
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
						main.closeSequence();
                    }
                }
                );
        menu.add(menuItem);
        menuBar.add(menu);

        // Edit menu
        menu = new JMenu(bundle.getValueOf("edit"));
        menu.setMnemonic(KeyEvent.VK_E);
        // Undo
        menuItem = new JMenuItem(undoAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Redo
        menuItem = new JMenuItem(redoAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Cut
        menuItem = new JMenuItem(cutAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Copy
        menuItem = new JMenuItem(copyAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Paste
        menuItem = new JMenuItem(pasteAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Select All
        menuItem = new JMenuItem(selectAllAction);
        menu.add(menuItem);
        // Find/Replace
        menuItem = new JMenuItem(findReplaceAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Enable overwrite
        menuItem = new JMenuItem(bundle.getValueOf("overwrite"));
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
						if (!main.overwrite) {
                        	main.editor.setOverwriteEnabled(true);
						    main.overwrite = true;
						} else {
							main.editor.setOverwriteEnabled(false);
							main.overwrite = false;
						}
                    }
                }
                );
        menu.add(menuItem);
        // Indent lines
        menuItem = new JMenuItem(indentLinesAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Comment lines
        menuItem = new JMenuItem(commentLinesAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Uncomment lines
        menuItem = new JMenuItem(uncommentLinesAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);

        // View Menu
        menu = new JMenu(bundle.getValueOf("view"));
        menu.setMnemonic(KeyEvent.VK_V);
        // Open Web Browser to preview web page
        menuItem = new JMenuItem(openBrowserAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Open API viewer
        menuItem = new JMenuItem(viewAPIAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // go to line
        menuItem = new JMenuItem(bundle.getValueOf("gotoline"));
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        new GoToLineDialog(bundle, main);
                    }
                }
                );
        menu.add(menuItem);
        // line numbers
        checkBox = new JCheckBoxMenuItem(bundle.getValueOf("linenumbers"));
        checkBox.setMnemonic(KeyEvent.VK_L);
        checkBox.setSelected(true);
        checkBox.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
						JLabel dummyLabel = new JLabel("");
                        if (checkBox.isSelected() == false) {
                            if (main.editor.getGutter() != null) {
                                main.editor.remove(main.editor.getGutter());
                                main.editor.add("left", dummyLabel);
                                main.editor.revalidate();
                                main.editor.repaint();
                            }
                        } else {
							main.editor.remove(dummyLabel);
                            main.editor.add("left", main.editor.getGutter());
                            main.editor.revalidate();
                            main.editor.repaint();
                        }
                    }
                }
                );
        menu.add(checkBox);
        menuBar.add(menu);

        // Tools Menu
        menu = new JMenu(bundle.getValueOf("tools"));
        menu.setMnemonic(KeyEvent.VK_L);
        // Setup external tools
        menuItem = new JMenuItem(bundle.getValueOf("toolsetup"));
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        new ToolPathSetupDialog(main, main.editor, bundle);
                    }
                }
                );
        menu.add(menuItem);
        // Jar Viewer
        menuItem = new JMenuItem(jarViewerAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Compile
        menuItem = new JMenuItem(compileAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Ant
        menuItem = new JMenuItem(antRunAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Run
        menuItem = new JMenuItem(runAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        // Run Applet
        menuItem = new JMenuItem(runAppletAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);
        menuBar.add(menu);

        // Code Menu
        menu = new JMenu(bundle.getValueOf("code"));
        menu.setMnemonic(KeyEvent.VK_T);
        JMenu subMenu = null;

        // Code Generator
        menuItem = new JMenuItem(generateCodeAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);

        // Javadoc Wizard
        menuItem = new JMenuItem(javadocWizAction);
        menuItem.setIcon(null); // show only the text
        menu.add(menuItem);

        // GUI templates
        subMenu = new JMenu(bundle.getValueOf("guitemp"));
        // GridBagConstraints
        menuItem = new JMenuItem("GridBagConstraints");
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("GridBagConstraints")));
                    }
                }
                );
        subMenu.add(menuItem);
        menu.add(subMenu);

        // Bean templates
        subMenu = new JMenu(bundle.getValueOf("beantemp"));
        // setter method
        menuItem = new JMenuItem(bundle.getValueOf("setter"));
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("setterMethod")));
                    }
                }
                );
        subMenu.add(menuItem);
        // getter method
        menuItem = new JMenuItem(bundle.getValueOf("getter"));
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("getterMethod")));
                    }
                }
                );
        subMenu.add(menuItem);
        menu.add(subMenu);

        // Documentation templates
        subMenu = new JMenu(bundle.getValueOf("javadoctemp"));
        // class description
        menuItem = new JMenuItem(bundle.getValueOf("classdesc"));
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("classDescription")));
                    }
                }
                );
        subMenu.add(menuItem);
        menu.add(subMenu);

        // Debug templates
        subMenu = new JMenu(bundle.getValueOf("debugtemp"));
        // System.err
        menuItem = new JMenuItem("System.err");
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("SystemErr")));
                    }
                }
                );
        subMenu.add(menuItem);
        // System.out
        menuItem = new JMenuItem("System.out");
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("SystemOut")));
                    }
                }
                );
        subMenu.add(menuItem);
        menu.add(subMenu);

        // Loop templates
        subMenu = new JMenu(bundle.getValueOf("looptemp"));
        // switch block
        menuItem = new JMenuItem("Switch");
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("switch")));
                    }
                }
                );
        subMenu.add(menuItem);
        // if else block
        menuItem = new JMenuItem("If-else");
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("if")));
                    }
                }
                );
        subMenu.add(menuItem);
        // for block
        menuItem = new JMenuItem("For");
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("for")));
                    }
                }
                );
        subMenu.add(menuItem);
        // while block
        menuItem = new JMenuItem("While");
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("while")));
                    }
                }
                );
        subMenu.add(menuItem);
        menu.add(subMenu);

        // General templates
        subMenu = new JMenu(bundle.getValueOf("moretemp"));
        // try block
        menuItem = new JMenuItem("Try");
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("try")));
                    }
                }
                );
        subMenu.add(menuItem);
        // enumeration
        menuItem = new JMenuItem("Enumeration");
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        main.insertTemplate(new File(GUIUtilities.getTemplateLocation("enum")));
                    }
                }
                );
        subMenu.add(menuItem);
        menu.add(subMenu);
        menuBar.add(menu);

        // Help Menu
        menu = new JMenu(bundle.getValueOf("help"));

        // About JSource
        menuItem = new JMenuItem(bundle.getValueOf("aboutjsource"));
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        new AboutDialog(bundle);
                    }
                }
                );
        menu.add(menuItem);
        // Help Dialog
        menuItem = new JMenuItem(bundle.getValueOf("helpindex"));
        menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        new HelpDialog(bundle);
                    }
                }
                );
        menu.add(menuItem);

        menuBar.add(menu);

        menuBar.setVisible(true);
        return menuBar;
    }

    /**
     * Sets up the popup menu for the code editor.
     * @return popup the JPopupMenu
     */
    private JPopupMenu getEditorPopupMenu(JPopupMenu popup) {
        JMenuItem menuItem;

        // "New" menu item
        menuItem = new JMenuItem(newAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "Open" menu item
        menuItem = new JMenuItem(openAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "Close" menu item
        menuItem = new JMenuItem(closeAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "Save" menu item
        menuItem = new JMenuItem(saveAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
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
        // "Indent" menu item
        menuItem = new JMenuItem(indentLinesAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "Comment" menu item
        menuItem = new JMenuItem(commentLinesAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "Uncomment" menu item
        menuItem = new JMenuItem(uncommentLinesAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "Find/Replace" menu item
        menuItem = new JMenuItem(findReplaceAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "Code generator" menu item
        menuItem = new JMenuItem(generateCodeAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);
        // "API viewer" menu item
        menuItem = new JMenuItem(viewAPIAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);

        return popup;
    }

    /**
     * Sets up the popup menu for the notepad.
     * @return popup the JPopupMenu
     */
    private JPopupMenu getNotepadPopupMenu(JPopupMenu popup) {
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
        // "Find/Replace" menu item
        menuItem = new JMenuItem(findReplaceAction);
        menuItem.setIcon(null); // show only the text
        popup.add(menuItem);

        return popup;
    }
}