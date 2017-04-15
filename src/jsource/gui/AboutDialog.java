package jsource.gui;


/**
 * AboutDialog.java  01/02/03
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
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import jsource.*;
import jsource.util.*;
import jsource.io.localization.*;


/**
 * <code>AboutDialog</code> is the JSource About dialog box.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class AboutDialog extends BaseDialog {

    private XMLResourceBundle bundle = null;
    private JButton close = null;

    AboutDialog(XMLResourceBundle bundle) {
        super(null, "", true);
        this.bundle = bundle;
        setTitle(bundle.getValueOf("aboutjsource"));
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        JTabbedPane mainTabbedPane = new JTabbedPane();

        mainTabbedPane.add(bundle.getValueOf("about"), getAboutJSourcePanel());
        mainTabbedPane.add(bundle.getValueOf("info"), getClientInfoPanel());

        content.add(BorderLayout.CENTER, mainTabbedPane);

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

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        show();
    }

    public void ok() {
        dispose();
    }

    public void cancel() {
        dispose();
    }

    class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            dispose();
        }
    }

    private JComponent getAboutJSourcePanel() {
		return new AboutPanel();
	}

    private JPanel getClientInfoPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());

        JLabel titleLabel = new JLabel(bundle.getValueOf("sys"));
        infoPanel.add(titleLabel, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 6, 0, 0), 0, 0));

        JTable clientTable = new JTable();
        clientTable.setAutoCreateColumnsFromModel(false);
        clientTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumn col = new TableColumn(0);
        col.setHeaderValue(bundle.getValueOf("property"));
        col.setPreferredWidth(100);
        clientTable.getColumnModel().addColumn(col);

        col = new TableColumn(1);
        col.setHeaderValue(bundle.getValueOf("value"));
        col.setPreferredWidth(250);
        clientTable.getColumnModel().addColumn(col);

        String [] dummy = {"",""};
        String[][] data = new String[7][2];
        data[0][0] = bundle.getValueOf("osname") + " : ";
        data[0][1] = JSConstants.OS_NAME + " " +
                     bundle.getValueOf("oversion") + " " +
                     JSConstants.OS_VERSION + " " +
                     bundle.getValueOf("runon") + " " +
                     JSConstants.OS_ARCH;
        data[1][0] = bundle.getValueOf("jversion") + " : ";
        data[1][1] = JSConstants.JAVA_VERSION;
        data[2][0] = "VM : ";
        data[2][1] = JSConstants.JAVA_VM + " " + JSConstants.JAVA_VM_VERSION;
        data[3][0] = bundle.getValueOf("javahome") + " : ";
        data[3][1] = JSConstants.JAVA_HOME;
        data[4][0] = bundle.getValueOf("syslocale") + " : ";
        data[4][1] = JSConstants.SYS_LANG;
        data[5][0] = bundle.getValueOf("freememory") + " : ";
        data[5][1] = (JSConstants.RUNTIME.freeMemory() / 1024) + " kByte";
        data[6][0] = bundle.getValueOf("totalmemory") + " : ";
        data[6][1] = (JSConstants.RUNTIME.totalMemory() / 1024) + " kByte";

        clientTable.setModel(new DefaultTableModel(data, dummy) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });

        clientTable.setRowSelectionAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(clientTable);
        tableScrollPane.setPreferredSize(new Dimension(250, 100));
        infoPanel.add(tableScrollPane, new GridBagConstraints(1, 4, 2, 1, 0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 6, 2, 6), 0, 0));

        JLabel classpathLabel = new JLabel(bundle.getValueOf("cplabel") + ": ");
        infoPanel.add(classpathLabel, new GridBagConstraints(1, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 6, 0, 0), 0, 0));

        String classFileString = JSConstants.CLASS_PATH;
        java.util.Vector pathElements = new java.util.Vector();
        for (StringTokenizer strTokenizer = new StringTokenizer(classFileString, JSConstants.PATH_SEP); strTokenizer.hasMoreElements(); pathElements.add(strTokenizer.nextElement()));
        JList list = new JList(pathElements);
        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(new java.awt.Dimension(100, 80));

        infoPanel.add(listScrollPane, new GridBagConstraints(1, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 6, 2, 6), 0, 0));

        infoPanel.setBorder(BorderFactory.createEmptyBorder(7,7,5,7));

        return infoPanel;
    }

    private class AboutPanel extends JComponent {
        ImageIcon image = null;

        AboutPanel() {
            setFont(UIManager.getFont("Label.font"));
            setForeground(new Color(206, 206, 229));
            image = GUIUtilities.createIcon("about.gif");
            setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
        }

		public void paintComponent(Graphics _g) {
			Graphics2D g = (Graphics2D)_g;
			image.paintIcon(this,g,1,1);
		}

        public Dimension getPreferredSize() {
            return new Dimension(1 + image.getIconWidth(), 1 + image.getIconHeight());
        }
    }
}
