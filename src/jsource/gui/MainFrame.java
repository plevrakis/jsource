package jsource.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.MutableComboBoxModel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.DefaultMetalTheme;

import jsource.com.incors.plaf.kunststoff.KunststoffLookAndFeel;
import jsource.io.FileUnit;
import jsource.io.localization.XMLResourceBundle;
import jsource.syntax.Indent;
import jsource.syntax.SyntaxDocument;
import jsource.syntax.tokenmarker.JavaTokenMarker;
import jsource.tinyplanet.docwiz.appDocWiz;
import jsource.util.FileUtilities;
import jsource.util.GUIUtilities;
import jsource.util.JSConstants;
import jsource.util.Log;
import jsource.console.*;

/**
 * <code>MainFrame</code> is the root container for the JSource GUI.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class MainFrame extends JFrame implements JSConstants, PropertyChangeListener {

    private Actions actions = null;
    //private static MainFrame staticMain = null;
    private Container pane = null;
    private JComboBox filesBox = null;
    private DefaultComboBoxModel comboModel = null;
    protected JPopupMenu editorPopup = null;
    protected JPopupMenu notepadPopup = null;
    protected JToolBar toolBar = null;
    private JPanel northPanel1 = null;
    private JPanel northPanel2 = null;
    private JPanel groupNorthPanels = null;
    private JLabel lineCountLabel, lines = null;
    private JLabel lengthLabel, characters = null;
    private String mainTitle = "JSource IDE v 2.0 ";
    private Toolkit toolkit = TOOLKIT;
    private String version = JAVA_VERSION;
    public appDocWiz jdocWiz = null;
    protected jsource.util.Properties jsprops = null;
    private Dimension dim = SCREEN_SIZE;

    // input-output
    private static java.util.Properties props = null, defaultProps = null;
    private AutoSaver auto = null;
    private java.util.List openFilesList = null;
    private FileListComboBoxModel model = null;
    public boolean isJavaFile = false;
    public boolean hasMain = false;
    public boolean hasAppletTags = false;
    public boolean isWebPage = false;
    private jsource.io.FileReader reader = null;
    public jsource.io.FileWriter writer = null;
    public XMLResourceBundle bundle = null;
    public Console2 console = null;
    private static String fs = FILE_SEP;
    private static String nl = LINE_SEP;
    public FileUnit currFile = null;
    private File tmp[] = null;
    private String tempFile = null;
    private int openDoc = 0;
    private int tmpCounter = 0;
    public String currentDir = null;
    private FileUnit antFile = null;
    private static boolean reload = false;

    // editor area
    public JTabbedPane tabbedPane = null;
    public JSEditor editor = null;
    public JSEditor notepad = null;
    public javax.swing.Timer refreshTimer = null;
    private Font font = null;
    protected boolean overwrite = false;
    protected boolean isNotepad = false;

    protected String sdkPath = null;
    protected String antPath = null;
    protected String browserPath = null;
    protected String coreAPIPath = null;
    private String javaCommand = null;
    private String javacCommand = null;
    private String appletCommand = null;
    private String codeBase = null;
    private String packagePath = null;
    private String classpath = null;
    private boolean isWindows = false;

    /**
     * Creates a new <code>MainFrame</code> object.
     */
    public MainFrame() {
		ImageIcon icon = GUIUtilities.createIcon("jsource.gif");
		setIconImage(icon.getImage());
        // setup localization
        bundle = new XMLResourceBundle(BUNDLE_PATH);
        try {
            bundle.loadFile(LOCALES[0]); // Locale.English
        } catch (Exception ex) {
			Log.log(ex);
        }
    	if (OS_NAME.startsWith("Windows")) {
    	  isWindows = true;
    	}
    	
        //setKunststoffLookAndFeel(new KunststoffPresentationTheme());
        
        try {
			UIManager.setLookAndFeel(new KunststoffLookAndFeel());
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        setDefaultLookAndFeelDecorated(true);
        
        //staticMain = this;

        if (version.compareTo("1.1.2") < 0) {
			GUIUtilities.showMessage(bundle.getValueOf("versionwarn")); // Swing warning
        }

        font = EDITOR_FONT;

        defaultProps = props = new java.util.Properties();

		toolBar = new JToolBar();

		actions = new Actions(this, this, bundle);
        toolBar.setFloatable(false);
        toolBar.setBorderPainted(false);

        editor = new JSEditor(this);
        editor.setRightClickPopup(editorPopup);
        editor.setTokenMarker(new JavaTokenMarker());

        notepad = new JSEditor(this);
        notepad.setRightClickPopup(notepadPopup);
        notepad.setTokenMarker(null);
        notepad.setDocument(new SyntaxDocument());
        notepad.remove(notepad.getGutter());
		notepad.add("left", new JLabel(""));
		notepad.revalidate();
        notepad.repaint();

        // disable lightweight popup support
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

        reader = new jsource.io.FileReader(editor, this);
        writer = new jsource.io.FileWriter(editor, this);
		refreshTimer = new javax.swing.Timer(10, new Refresher());

		// setup of open files list
        openFilesList = new ArrayList();
        model = new FileListComboBoxModel(openFilesList);
        filesBox = new JComboBox(model);

        tmp = new File[49]; // 50 temporary documents can be open
		openDoc = 1;

        lineCountLabel = new JLabel("  " + bundle.getValueOf("lineslabel") + "  ");
        lengthLabel = new JLabel("  " + bundle.getValueOf("charslabel") + "  ");
        lineCountLabel.setFont(font);
        lengthLabel.setFont(font);
        lines = new JLabel("  " + editor.getLineCount());
        characters = new JLabel("  " + editor.getDocumentLength());
        lines.setFont(font);
        characters.setFont(font);

        northPanel1 = new JPanel();
        northPanel1.setLayout(new BoxLayout(northPanel1, BoxLayout.X_AXIS));
        northPanel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        northPanel1.add(toolBar);

		FlowLayout flow = new FlowLayout(FlowLayout.LEFT, 1, 1);
        northPanel2 = new JPanel(flow);
        northPanel2.add(filesBox);

        groupNorthPanels = new JPanel();
        groupNorthPanels.setLayout(new GridLayout(2,1));
        groupNorthPanels.add(northPanel1);
        groupNorthPanels.add(northPanel2);

        pane = getContentPane();

        console = new Console2(this, bundle);

        JPanel infoPanel = new JPanel(flow);
        infoPanel.add(lineCountLabel);
		infoPanel.add(lines);
		infoPanel.add(lengthLabel);
		infoPanel.add(characters);
        JPanel editorPanel = new JPanel(new BorderLayout());
        editorPanel.add(editor, BorderLayout.CENTER);
        editorPanel.add(infoPanel, BorderLayout.SOUTH);

		tabbedPane = new JTabbedPane();
        tabbedPane.addTab(bundle.getValueOf("codeditor"), null, editorPanel, bundle.getValueOf("codeditortip"));
        tabbedPane.addTab(bundle.getValueOf("systerm"), null, console, bundle.getValueOf("systermtip"));
        tabbedPane.addTab(bundle.getValueOf("notepad"), null, notepad, bundle.getValueOf("notepadtip"));
        tabbedPane.setSelectedIndex(0);

        pane.add(groupNorthPanels, BorderLayout.NORTH);
        pane.add(tabbedPane, BorderLayout.CENTER);
        pane.setBackground(Color.black);
        ActionListener filesBoxListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	currFile = (FileUnit)model.getSelectedItem();
            	if (currFile.isFileModified()) {
					if (askReload() == JOptionPane.YES_OPTION)
						currFile = new FileUnit(currFile.getFile());
		        }
            	writer.writeFileToEditor(currFile);
            	reader.detectFileType(currFile.getFileName());
            	lines.setText("" + editor.getLineCount());
            	characters.setText("" + editor.getDocumentLength());
            	setTitle(mainTitle + "- [" + currFile.getFileName() + "]");
            	editor.repaint();
			}
        };
        ChangeListener tabbedPaneListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	if (tabbedPane.getSelectedIndex() == 2) {
					isNotepad = true;
				} else {
					isNotepad = false;
				}
			}
        };
        filesBox.addActionListener(filesBoxListener);
        tabbedPane.addChangeListener(tabbedPaneListener);

        addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				closeSequence();
			}

			public void windowActivated(WindowEvent evt) {
				repaintEditor();
      		}

      		public void windowGainedFocus(WindowEvent e) {
				repaintEditor();
			}

      		public void windowDeiconified(WindowEvent e) {
				repaintEditor();
			}

      		private void repaintEditor() {
				// System.out.println("here34343");
				editor.requestFocus();
				editor.setFirstLine(0);
			    editor.repaint();
			}
		  }
        );

    	init();
    }

    public void closeSequence() {
		close();
		if (currFile != null) {
			jsprops.setLastDirectory(currFile.getFileDir());
			jsprops.setLastFile(currFile.getFileName());
		}
		jsprops.setMainFrameSize(getSize());
		jsprops.setMainFrameLocation(getLocation());

		XMLEncoder encoder = null;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(JSConstants.PROPERTIES)));
			encoder.writeObject(jsprops);
		} catch(FileNotFoundException ex) {
			Log.log(ex);
		} finally {
			encoder.close();
		}
		if (console != null) {
			console.stop();
			console.save();
		}
		stopAutoSave();
		System.exit(0);
	}

	/**
	 * Start the auto saving function. If the <code>Thread</code> used
	 * for the auto save is <code>null</code>, we need to create it.
	 */
	void startAutoSave() {
		if (auto == null) {
	  		auto = new AutoSaver();
		}
	}

	/**
	 * Stop the auto saving function. We just interrupt the
	 * <code>Thread</code> and then 'kill' it.
	 */
    void stopAutoSave() {
		if (auto != null) {
		  auto.interrupt();
		  auto = null;
		}
	}

    private void init() {
        readProperties();
        startAutoSave();

        //classpath = CLASS_PATH;
		appletCommand = "appletviewer";
		javacCommand = "javac";

        // use javaw on windows since it does not pop up an extra annoying box
        //if (isWindows) {
        //    javaCommand = "javaw";
        //} else {
            javaCommand = "java";
        //}
        //packagePath = "com" + pathSeparator + "dunncom" + pathSeparator + "judo";
    }

	/**
	 * Gets the paths to external tools used by JSource are set.
	 * If the value "Undefined" is returned a file chooser dialog box
	 * is displayed asking for the paths to be set.
	 */
    private void readProperties() {
		XMLDecoder decoder = null;
		try {
			decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(JSConstants.PROPERTIES)));
			jsprops = (jsource.util.Properties)decoder.readObject();
		} catch (FileNotFoundException ex) {
			jsprops = new jsource.util.Properties();
			Log.log(ex);
		} finally {
			if (decoder != null)
				decoder.close();
		}
		if (jsprops != null) {
			sdkPath = jsprops.getSDK();
			antPath = jsprops.getANT();
			browserPath = jsprops.getBrowser();
			coreAPIPath = jsprops.getCoreAPI();
			String lastDir = jsprops.getLastDirectory();
			String lastFile = jsprops.getLastFile();
			Dimension frameSize = jsprops.getMainFrameSize();
			Point frameLocation = jsprops.getMainFrameLocation();

			if (frameSize != null) {
				setSize(frameSize);
			} else {
				frameSize = dim;
				setSize((frameSize.width - 5), (frameSize.height - 50));
			}

			if (frameLocation != null) {
				setLocation(frameLocation);
			}

			if (!lastDir.equals("Undefined") && new File(lastDir).exists()) {
				currentDir = lastDir;
			} else {
				currentDir = USER_DIR;
			}

			if (lastFile != null && new File(lastDir + fs + lastFile).exists()) {
				FileUnit unit = new FileUnit(new File(lastDir + fs + lastFile));
				model.addElement(unit);
				model.setSelectedItem(model.getElementAt(0));
				filesBox.setSelectedIndex(0);
			} else {
				createNewTempDocument();
			}

			if (!sdkPath.equals("Undefined")) {
				String tmp = sdkPath.substring(0, sdkPath.length()-3);
				//classpath = FileUtilities.reverseSlashes(tmp + "jre" + fs + "lib" + fs) + "rt.jar;" + CLASS_PATH;
			}
			/*if (sdkPath.equals("Undefined") ||
				antPath.equals("Undefined") ||
				browserPath.equals("Undefined") ||
				coreAPIPath.equals("Undefined")) {
				DialogFactory.getToolPathSetupDialog();
			}*/
		} else {
			jsprops = new jsource.util.Properties();
			createNewTempDocument();
		}
		setVisible(true);
    }

	/**
	 * Cleanup temporary empty files and check if the current file has been saved.
	 */
    void close() {
		if (currFile != null) {
			String tf = currFile.getFileName();
			closeCurrentFile(tf);
			for (int i = 0;i < tmp.length;i++) {
				if (tmp[i] != null) {
					tf = tmp[i].getName();
					if ((tf.startsWith("Document") && tf.endsWith("txt")) && (tmp[i].length() == 0)) {
						tmp[i].delete();
					}
				} else {
					break;
				}
			}
		}
	}

    /*************************** Combobox Model inner class ************************************/

    /**
     * <code>FileListComboBoxModel</code> is an inner class that creates the
     * <code>MutableComboBoxModel</code> used by the open files <code>JCombobox</code>
     * (created and implemented 01/12/03)
     */
    private class FileListComboBoxModel extends AbstractListModel
            implements MutableComboBoxModel {

        private Object selectedItem = null;
        private java.util.List list = null;

        public FileListComboBoxModel(java.util.List list) {
			this.list = list;
        }

        public int getSize() {
			return list.size();
        }

        public Object getElementAt(int i) {
			return list.get(i);
        }

        // ComboBoxModel implementation
        public Object getSelectedItem() {
            return selectedItem;
        }

        public void setSelectedItem(Object newValue) {
            selectedItem = newValue;
        }

        // MutableComboBoxModel implementation
        public void addElement(Object element) {
			list.add(element);
            int length = getSize();
            fireIntervalAdded(this, length - 1, length - 1); // notify ListDataListener objects
        }

        public void insertElementAt(Object element, int index) {
			list.add(index, element);
            fireIntervalAdded(this, index, index); // notify ListDataListener objects
        }

        public void removeElement(Object element) {
            int index = list.indexOf(element);

            if (index != -1) {
				list.remove(index);
                fireIntervalRemoved(this, index, index); // notify ListDataListener objects
            }
        }

        public void removeElementAt(int index) {
            if (getSize() >= index && index >= 0) {
				list.remove(index);
                fireIntervalRemoved(this, index, index); // notify ListDataListener objects
            }
        }
    }

    /*************************** Repainting inner class ************************************/

    /**
     * <code>Refresher</code> is an inner class that repaints the <code>JSEditor</code>
     * component and the panel that holds open files' buttons. This happens only when a
     * keyTyped event happens. The main purpose of this operation is to display new line
     * numbers every time the user presses enter. Also the statistics for lines count
     * and characters count of the current document are displayed updated.<br>
     * Created and implemented 12/19/02<br>
     * Revised 06/03/03
     */
    private class Refresher implements ActionListener {
        public void actionPerformed(ActionEvent e) {
			editor.repaint();
            lines.setText("" + editor.getLineCount());
            characters.setText("" + editor.getDocumentLength());
        }
    }

    /**
     * Compiles a java file.
     * @param classDir the directory where the generated class file will be placed.
     * @param fileName the absolute path of the java file to be compiled.
     */
    private void compile(String classDir, String fileName) {
		String javac_EXE = JAVA_TOOL_PATH + "javac.exe";
		FileUtilities.copyFile(javac_EXE, currentDir + fs + "javac.exe");
		String command = "javac " + fileName;
        String title = bundle.getValueOf("compiling") + " " + fileName;
        try {
            new CommandOutputDialog(bundle, command, title);
        } catch (Exception err) {
            GUIUtilities.showExceptionErrorMessage(bundle.getValueOf("cannotrun") + " " + fileName + nl + err.getMessage());
        }

		/*
    	com.sun.tools.javac.Main javac = new com.sun.tools.javac.Main();
		String[] args = new String[] { "-d", classDir, fileName };
        int status = javac.compile(args);
        switch (status) {
        	case 0:  // Success
				//if (hasMain) {
				//	run(classname, false);
				//}
            break;
        	case 1:
        	    String msg1 = "    " + bundle.getValueOf("notjavafile") + "   1 ";
        		new MessageDialog(msg1, false, this, bundle);
        		break;
        	case 2:
        	    String msg2 = "    " + bundle.getValueOf("notjavafile") + "   2 ";
        		new MessageDialog(msg2, false, this, bundle);
        		break;
        	case 3:
        	    String msg3 = "    " + bundle.getValueOf("notjavafile") + "   3 ";
        		new MessageDialog(msg3, false, this, bundle);
        		break;
        	case 4:
        	    String msg4 = "    " + bundle.getValueOf("notjavafile") + "   4 ";
        		new MessageDialog(msg4, false, this, bundle);
        		break;
        	default:
        	    String msg5 = "    " + bundle.getValueOf("unknowncomprm") + "    ";
        		new MessageDialog(msg5, false, this, bundle);
        }*/
    }

    /**
     * Run Java application or Java applet
     * @param file the file that contains "main" or the web page that contains applet tags
     */
    private void run(String fileName, boolean isApplet) {
		String[] args = new String[] { "-cp", ".", fileName };
		System.out.println(fileName);
		String command = "";
		String title = "";
		if (isApplet && isWebPage) {
			String appletviewer_EXE = JAVA_TOOL_PATH + "appletviewer.exe";
			FileUtilities.copyFile(appletviewer_EXE, currentDir + fs + "appletviewer.exe");
			command = appletCommand + " " + fileName;
			title = "Applet Viewer" + " " + fileName;
		} else {
			String java_EXE = JAVA_TOOL_PATH + "java.exe";
			FileUtilities.copyFile(java_EXE, currentDir + fs + "java.exe");
			command = javaCommand + " " + fileName;

			title = bundle.getValueOf("running") + " " + fileName;
		}
        try {
            new CommandOutputDialog(bundle, command, title);
        } catch (Exception err) {
            GUIUtilities.showExceptionErrorMessage(bundle.getValueOf("cannotrun") + " " + fileName + nl + err.getMessage());
        }
    }

    /**
     * Not currently used. It simply sets the default Swing L&F. This method
     * exists solely for future modifications of L&F.
     */
    private void setLookAndFeel() {
        UIManager UIM = new UIManager();
        UIManager.LookAndFeelInfo[] installedLnFs = UIM.getInstalledLookAndFeels();

        for (int i = 0; i < installedLnFs.length; i++) {
            if (installedLnFs[i].getName().equals("Metal"))
                try {
                    UIM.setLookAndFeel(installedLnFs[i].getClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * Sets the Kunststoff's custom Look&Feel
     */
    private void setKunststoffLookAndFeel(DefaultMetalTheme theme) {
        KunststoffLookAndFeel kls = new KunststoffLookAndFeel();

        KunststoffLookAndFeel.setCurrentTheme(theme);
        try {
            UIManager.setLookAndFeel(kls);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Inserts the specified code template.
     * @param file the text <code>File</code> that contains the template.
     */
    protected void insertTemplate(File file) {
        StringBuffer buffer = new StringBuffer();

        try {
            java.io.FileReader filereader = new java.io.FileReader(file);
            BufferedReader bufferedreader = new BufferedReader(filereader);
            String line = "";

            while ((line = bufferedreader.readLine()) != null) {
                buffer.append(line + nl);
            }
            filereader.close();
        } catch (Exception e) {}

        String template = buffer.toString();

        editor.insert(template);
    }

    private void createNewTempDocument() {
		openDoc = tmpCounter;
		if (openDoc == 0) openDoc = 1;
		if (tmpCounter != 49) {
			while (new File(currentDir + fs + "Document" + openDoc + ".txt").exists()) {
				openDoc++;
			}
			tempFile = "Document" + openDoc + ".txt";
			try {
				tmp[tmpCounter] = new File(currentDir + fs + tempFile);
				boolean created = tmp[tmpCounter].createNewFile();
				if (created) {
					FileUnit newUnit = new FileUnit(tmp[tmpCounter]);
					model.addElement(newUnit);
					model.setSelectedItem(model.getElementAt(tmpCounter));
					filesBox.setSelectedIndex(tmpCounter);
					if (editor.getTokenMarker() == null) editor.setTokenMarker(new JavaTokenMarker());
					if (tmpCounter != 48) tmpCounter++;
					if (tmpCounter == 48) actions.newAction.setEnabled(false);
				}
			} catch(Exception e) {
				Log.log(e);
			}
		}
	}

	private void viewWebPageInBrowser() {
		if (isWebPage) {
			String command = browserPath + " " + currFile.getFileDir();
            new CommandOutputDialog(bundle, command, "browser");
		} else {
			String msg = bundle.getValueOf("nowebpage");
        	new MessageDialog(msg, false, this, bundle);
		}
	}

    private int askReload() {
        return JOptionPane.showConfirmDialog
                (null, bundle.getValueOf("askrload") + " " + currFile.getFileName() +"?",
                 bundle.getValueOf("askrloadtitle") + " " + currFile.getFileName() + "?",
                 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    private int askSave() {
        return JOptionPane.showConfirmDialog
                (null, currFile.getFileName() + " " + bundle.getValueOf("asksave"),
                 bundle.getValueOf("asksavetitle") + " " + currFile.getFileName() + "?",
                 JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    private int askOverwrite(String fileName) {
        return JOptionPane.showConfirmDialog
                (null, fileName + " " + bundle.getValueOf("askoverwrite") + "?",
                 bundle.getValueOf("overwrite") + " " + fileName + "?",
                 JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    private void closeCurrentFile(String name) {
		tabbedPane.setSelectedIndex(0);
		if ((name.startsWith("Document") &&
			 name.endsWith("txt")) &&
			 (currFile.getFile().length() == 0) &&
			 !currFile.getCurrentFileModified() &&
			 !currFile.isFileModified()) {
			currFile.getFile().delete();
		} else if (currFile.getCurrentFileModified() || currFile.isFileModified()) {
			int result = askSave();

			if (result == JOptionPane.YES_OPTION) {
				if (name.startsWith("Document") && name.endsWith("txt")) {
					File saveAsFile = null;
					boolean saveFlag = false;
					while(!saveFlag) {
						saveAsFile = reader.saveAsFile();
						if (saveAsFile.exists()) {
							int res = askOverwrite(saveAsFile.getName());
							if (res == JOptionPane.YES_OPTION) {
								currFile = new FileUnit(saveAsFile);
								writer.writeStringToFile(currFile, editor.getText());
								saveFlag = true;
							} else if (res == JOptionPane.NO_OPTION) {
								// loop again
							}
						} else {
							writer.writeStringToFile(currFile, editor.getText());
							currFile.getFile().renameTo(saveAsFile);
							saveFlag = true;
						}
				    }
				} else {
					writer.writeStringToFile(currFile, editor.getText());
				}
			}
			if (result == JOptionPane.NO_OPTION) {// just proceed.
			}
			if (result == JOptionPane.CANCEL_OPTION) {
				// skip over everything.
				return;
			}
		}
	}

	public void reloadCurrentFile() {
        writer.writeFileToEditor(currFile);
        setTitle(mainTitle + "- [" + currFile.getFileName() + "]");
	}

	private class AutoSaver extends Thread {
		AutoSaver() {
			setDaemon(true);
    		start();
		}

		public void run() {
			while (currFile != null) {
         		writer.writeStringToFile(currFile, editor.getText());
         		currFile.reloadModTime();
                try {
					sleep(30000);
				} catch (InterruptedException ie) {
					Log.log(ie);
	            }
			}
		}
	}

	public void openBuildFile() {
		String alreadyOpenedFile = null;
		boolean alreadyOpen = false;
		int index = 0;
		File openFile = antFile.getFile();
		if (openFile != null) {
			if (actions.closeAction.isEnabled() == false) actions.closeAction.setEnabled(true);
			if (actions.saveAction.isEnabled() == false) actions.saveAction.setEnabled(true);
			if (actions.saveAsAction.isEnabled() == false) actions.saveAsAction.setEnabled(true);
			if (editor.isCaretVisible() == false) editor.setCaretVisible(true);
			for (int i = 0;i < model.getSize();i++) {
				alreadyOpenedFile = ((FileUnit)model.getElementAt(i)).getFileName();
				// check if the file is already open
				if (alreadyOpenedFile.equals(openFile.getName())) {
					alreadyOpen = true;
					index = i;
					break;
				}
			}
			if (alreadyOpen) {
				filesBox.setSelectedIndex(index);
			} else {
				model.addElement(antFile);
				model.setSelectedItem(model.getElementAt(tmpCounter));
				filesBox.setSelectedIndex(tmpCounter + 1);
				tmpCounter++;
				tabbedPane.setSelectedIndex(0);
			}
		}
	}

    /**
     * Some external functions may need to manipulate the console.
     * The Scripting system, for instance, need it to launch OS
     * specific commands.
     * @return current <code>Console</code> used by JSource
     */
    public Console2 getConsole() {
        return console;
    }

	/**
	 * Set a property.
	 * @param name the property's name
	 * @param value the value to store as name
	 */
	public static void setProperty(String name, String value) {
		if (name == null || value == null)
		    return;
		props.put(name, value);
	}

	/**
	 * If we store properties, we need to read them, too!
	 * @param name the name of the property to read
	 * @return the value of the specified property
	 */
	public static String getProperty(String name) {
		return props.getProperty(name);
	}

	//public static MainFrame getMainFrame() {
	//	return staticMain;
	//}

    /************************************ Actions **********************************************/

	public void propertyChange(PropertyChangeEvent ev) {
		String name = ev.getPropertyName();
		if (name.equals("new")) {
			if (tabbedPane.getSelectedIndex() != 0) {
				tabbedPane.setSelectedIndex(0);
			}
			if (actions.closeAction.isEnabled() == false) actions.closeAction.setEnabled(true);
			if (actions.saveAction.isEnabled() == false) actions.saveAction.setEnabled(true);
			if (actions.saveAsAction.isEnabled() == false) actions.saveAsAction.setEnabled(true);
			if (editor.isCaretVisible() == false) editor.setCaretVisible(true);
			createNewTempDocument();
			editor.repaint();
		}
		if (name.equals("open")) {
			if (tabbedPane.getSelectedIndex() != 0) {
				tabbedPane.setSelectedIndex(0);
			}
			actions.openAction.setEnabled(false); // avoid repeated clicks before file chooser appears
			String alreadyOpenedFile = null;
			boolean alreadyOpen = false;
			int index = 0;
			File openFile = reader.openFile();
			actions.openAction.setEnabled(true); // enable action after file chooser is closed
			if (openFile != null) {
				if (actions.closeAction.isEnabled() == false) actions.closeAction.setEnabled(true);
				if (actions.saveAction.isEnabled() == false) actions.saveAction.setEnabled(true);
				if (actions.saveAsAction.isEnabled() == false) actions.saveAsAction.setEnabled(true);
				if (editor.isCaretVisible() == false) editor.setCaretVisible(true);
				for (int i = 0;i < model.getSize();i++) {
					alreadyOpenedFile = ((FileUnit)model.getElementAt(i)).getFileName();
					// check if the file is already open
					if (alreadyOpenedFile.equals(openFile.getName())) {
						alreadyOpen = true;
						index = i;
						break;
					}
				}
				if (alreadyOpen) {
					filesBox.setSelectedIndex(index);
				} else {
					FileUnit newUnit = new FileUnit(openFile);
					model.addElement(newUnit);
					model.setSelectedItem(model.getElementAt(tmpCounter));
					filesBox.setSelectedIndex(tmpCounter + 1);
					tmpCounter++;
				}
			}
		}
		if (name.equals("jarview")) {
			reader.setIsJAR(true);
			File jarFile = reader.openFile();
			if(jarFile.isFile() && jarFile.exists() && jarFile.getName().endsWith(".jar")) {
				new JarViewerDialog(bundle, jarFile);
			}
		}
		if (name.equals("close")) {
			if (tabbedPane.getSelectedIndex() != 0) {
				tabbedPane.setSelectedIndex(0);
			}
			int index = filesBox.getSelectedIndex();
			int size = model.getSize();
			String file = currFile.getFileName();
			model.removeElementAt(index);
			if (tmpCounter != 0) {
				tmpCounter--;
			}
			if (tmpCounter < 48 && actions.newAction.isEnabled() == false) {
				actions.newAction.setEnabled(true);
			}
            if (size > 1) {
				closeCurrentFile(file);
				if (index != 0) {
					currFile = (FileUnit)model.getElementAt(index - 1);
					reader.detectFileType(currFile.getFileName());
					filesBox.setSelectedIndex(index - 1);
				} else {
				    currFile = (FileUnit)model.getElementAt(size - 2);
				    reader.detectFileType(currFile.getFileName());
				    filesBox.setSelectedIndex(size - 2);
				}
			} else {
				closeCurrentFile(file);
				currFile = null;
				actions.closeAction.setEnabled(false);
				actions.saveAction.setEnabled(false);
				actions.saveAsAction.setEnabled(false);
				editor.setCaretVisible(false);
				setTitle(mainTitle);
				writer.writeStringToEditor("");
			}
			editor.repaint();
			filesBox.revalidate();
			filesBox.repaint();
		}
		if (name.equals("cut")) {
			if (isNotepad) {
				notepad.cut();
			} else {
				editor.cut();
			}
		}
		if (name.equals("copy")) {
			if (isNotepad) {
				notepad.copy();
			} else {
				editor.copy();
			}
	    }
		if (name.equals("paste")) {
			if (isNotepad) {
				notepad.paste();
			} else {
				editor.paste();
			}
		}
		if (name.equals("save")) {
			String fName = currFile.getFileName();
            if (fName.startsWith("Document") && fName.endsWith("txt")) {
				File saveAsFile = reader.saveAsFile();
				currFile.getFile().delete();
				model.removeElement(model.getSelectedItem());
				currFile = new FileUnit(saveAsFile);
				model.addElement(currFile);
				model.setSelectedItem(currFile);
                writer.writeStringToFile(currFile, editor.getText());
                currFile.setFileModified(false);
                currFile.setModTime(System.currentTimeMillis());
                setTitle(mainTitle + "- [" + currFile.getFileName()  + "]");
            } else {
                writer.writeStringToFile(currFile, editor.getText());
                currFile.setFileModified(false);
                currFile.setModTime(System.currentTimeMillis());
            }
		}
		if (name.equals("saveas")) {
			File newFile = reader.saveAsFile();
			File renamed = new File(newFile.getParent(), newFile.getName());
			boolean isRenamed = currFile.getFile().renameTo(renamed);
			if (isRenamed) {
				currFile = new FileUnit(renamed);
				reader.detectFileType(currFile.getFileName());
			}
            writer.writeStringToFile(currFile, editor.getText());
            currFile.setFileModified(false);
            currFile.setModTime(System.currentTimeMillis());
            setTitle(mainTitle + currFile.getFileName());
		}
		if (name.equals("undo")) {
			if (isNotepad) {
				notepad.undo();
			} else {
				editor.undo();
			}
		}
		if (name.equals("redo")) {
			if (isNotepad) {
				notepad.redo();
			} else {
				editor.redo();
			}
		}
		if (name.equals("selectall")) {
			if (isNotepad) {
				notepad.selectAll();
			} else {
				editor.selectAll();
			}
		}
		if (name.equals("findreplace")) {
			if (isNotepad) {
				new FindReplaceDialog(bundle, this, notepad);
			} else {
				new FindReplaceDialog(bundle, this, editor);
			}
		}
		if (name.equals("gencode")) {
			new CodeGeneratorDialog(bundle, this);
		}
		if (name.equals("ant")) {
			File buildFile = reader.getBuildFile();
			antFile = new FileUnit(buildFile);
			new AntRunDialog(bundle, this, buildFile);
		}
		if (name.equals("compile")) {
			if (currFile.getFileName().endsWith(".java")) {
				compile(currFile.getFileDir(), currFile.getFileDir() + fs + currFile.getFileName());
			} else {
        	    String msg = "    " + bundle.getValueOf("notjavafile") + "    ";
        		new MessageDialog(msg, false, this, bundle);
			}
		}
		if (name.equals("run")) {
			if (tabbedPane.getSelectedIndex() != 1) {
				tabbedPane.setSelectedIndex(1);
			}
			if (currFile.getFileName().endsWith(".java")) {
				String comm = "cd " + currFile.getFileDir();
				console.execute(comm);
				//String javac_EXE = JAVA_TOOL_PATH + "javac.exe";
				//FileUtilities.copyFile(javac_EXE, currentDir + fs + "javac.exe");
				comm = "javac " + currFile.getFileName();
				console.execute(comm);
				comm = "java " + FileUtilities.formatFileToRun(currFile.getFileName());
				if (new File(FileUtilities.formatFileToRun(currFile.getFileName()) + ".class").exists()) {
					console.execute(comm);
				} else {
        			new MessageDialog(FileUtilities.formatFileToRun(currFile.getFileName()) + ".class " +
        			                  bundle.getValueOf("filenoexist"), false, this, bundle);
				}
			} else {
        	    String msg = "    " + bundle.getValueOf("notjavafile") + "    ";
        		new MessageDialog(msg, false, this, bundle);
			}
		}
		if (name.equals("runapplet")) {
			if (tabbedPane.getSelectedIndex() != 0) {
				tabbedPane.setSelectedIndex(0);
			}
			if (!browserPath.equals("Undefined")) {
				viewWebPageInBrowser();
			} else if (!sdkPath.equals("Undefined")) {
				run(currFile.getFileName(), true);
			}
		}
		if (name.equals("webbrowser")) {
			if (tabbedPane.getSelectedIndex() != 0) {
				tabbedPane.setSelectedIndex(0);
			}
			if (browserPath.equals("Undefined")) {
				new ToolPathSetupDialog(this, editor, bundle);
			} else {
				viewWebPageInBrowser();
			}
		}
		if (name.equals("indent")) {
			if (tabbedPane.getSelectedIndex() != 0) {
				tabbedPane.setSelectedIndex(0);
			}
			editor.getDocument().beginCompoundEdit();
            editor.setSelectedText(JSConstants.LINE_SEP);
            Indent.indent(editor, bundle, editor.getCaretLine(), true, false);
            editor.getDocument().endCompoundEdit();
		}
		if (name.equals("comment")) {
			if (tabbedPane.getSelectedIndex() != 0) {
				tabbedPane.setSelectedIndex(0);
			}
			editor.comment();
		}
		if (name.equals("uncomment")) {
			if (tabbedPane.getSelectedIndex() != 0) {
				tabbedPane.setSelectedIndex(0);
			}
			editor.uncomment();
		}
		if (name.equals("viewapi")) {
			if (!coreAPIPath.equals("Undefined")) {
			    APIViewerDialog apiViewerDialog = new APIViewerDialog(bundle, this);
			    apiViewerDialog.getCalHTMLPane().repaint();
			} else {
				String msg = bundle.getValueOf("setcoreapi");
        	    new MessageDialog(msg, true, this, bundle);
			}
		}
		if (name.equals("docwiz")) {
			if (tabbedPane.getSelectedIndex() != 0) {
				tabbedPane.setSelectedIndex(0);
			}
			writer.writeStringToFile(currFile, editor.getText());
			if (currFile.getFileName().endsWith("java") && currFile.getFile().length() != 0) {
				jdocWiz = new appDocWiz(currFile.getFileDir(), currFile.getFileName());
			} else {
				String msg = "    " + bundle.getValueOf("notjavafile") + "    ";
        	    new MessageDialog(msg, false, this, bundle);
			}
		}
		if (name.equals("newproject")) {
			new NewProjectDialog(bundle, this);
		}
	}
}