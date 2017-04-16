// Source file: com/tinyplanet/PreferencesBox.java
// $Id: PreferencesBox.java,v 1.1 2001/07/17 17:13:51 lmeador Exp $
//
// $Log: PreferencesBox.java,v $
// Revision 1.1  2001/07/17 17:13:51  lmeador
// - new class displays a popup box to view and edit user preferences.
//
//

/*
DocWiz: Easy Javadoc documentation
Copyright (C) 1998  Simon Arthur

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

// History
//	7/10/2001 lmm	- new class partially copied from the about box to
//					allow view and modify the preferences.
//	7/12/2001 lmm	- add more entries regarding the types of code units
//					- edit max line length parameter for tags

package tinyplanet.docwiz;

import java.util.*;
//import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.text.JTextComponent;


/** A class to show a popup box and allow the user to change the
 *		preferences
 *
 * @author Arthur Simon
 * @author Lee Meador
 * @since  07/09/2001 3:33:18 PM
 */
public class PreferencesBox extends JFrame implements ActionListener
{

// Attributes:
	private static final int componentGap = 10;

	private static final String BUTTON_BLANK = "blank";
	private static final String BUTTON_NEVER_BLANK = "neverblank";
	private static final String BUTTON_POSSIBLY_BLANK = "maybeblank";

	protected JLabel titleLabel = new JLabel("Preferences", SwingConstants.CENTER);
	protected JSeparator titleSeparator = new JSeparator();

	protected JLabel tabSizeLabel = new JLabel("Tab size");
	protected JTextField tabSizeField = new JTextField();
//	protected JSeparator tabSizeSeparator = new JSeparator();

	protected JLabel maxLineLengthLabel = new JLabel("Tag line length");
	protected JTextField maxLineLengthField = new JTextField();
//	protected JSeparator maxLineLengthSeparator = new JSeparator();

	protected JCheckBox blankLineBeforeParmsField = new JCheckBox("Insert blank line");
	protected JRadioButton firstLineBlank = new JRadioButton("Blank top line");
	protected JRadioButton startOnFirstLine = new JRadioButton("Comment on top line");
	protected JRadioButton maybeStartOnFirstLine = new JRadioButton("Top line may be blank");
	protected JSeparator formatSeperator = new JSeparator();
	protected ButtonGroup firstLineButtons = new ButtonGroup();
	protected GridLayout formatLayout = new GridLayout(5, 1);
	protected JPanel formatPanel = new JPanel();
	protected JLabel sampleLabel = new JLabel("Sample Javadoc", SwingConstants.CENTER);
	protected JTextArea sampleJavaDoc = new JTextArea();
	protected JScrollPane sampleScrollPane = new JScrollPane();
	protected BorderLayout formatSampleLayout = new BorderLayout();
	protected JPanel formatSamplePanel = new JPanel();
	protected BorderLayout combinedSampleLayout = new BorderLayout();
	protected JPanel combinedSamplePanel = new JPanel();

	protected JPanel commentableInfoPanel = new JPanel();
	protected GridLayout commentableInfoLayout = new GridLayout(6, 5);
	protected JLabel blankLabel = new JLabel("");
	protected JLabel showAllLabel = new JLabel("Show all");
	protected JLabel showGreyLabel = new JLabel("Private are grey");
	protected JLabel showNoneLabel = new JLabel("Don't show private");
	protected JLabel singleLineLabel = new JLabel("Single line comment");
	protected JLabel classLabel = new JLabel("Classes");
	protected JRadioButton classShowAllButton = new JRadioButton("");
	protected JRadioButton classShowGreyButton = new JRadioButton("");
	protected JRadioButton classShowNoneButton = new JRadioButton("");
	protected ButtonGroup classButtons = new ButtonGroup();
	protected JCheckBox classSingleLineButton = new JCheckBox("");
	protected JLabel interfaceLabel = new JLabel("Interfaces");
	protected JRadioButton interfaceShowAllButton = new JRadioButton("");
	protected JRadioButton interfaceShowGreyButton = new JRadioButton("");
	protected JRadioButton interfaceShowNoneButton = new JRadioButton("");
	protected ButtonGroup interfaceButtons = new ButtonGroup();
	protected JCheckBox interfaceSingleLineButton = new JCheckBox("");
	protected JLabel fieldLabel = new JLabel("Attributes");
	protected JRadioButton fieldShowAllButton = new JRadioButton("");
	protected JRadioButton fieldShowGreyButton = new JRadioButton("");
	protected JRadioButton fieldShowNoneButton = new JRadioButton("");
	protected ButtonGroup fieldButtons = new ButtonGroup();
	protected JCheckBox fieldSingleLineButton = new JCheckBox("");
	protected JLabel constructorLabel = new JLabel("Constructors");
	protected JRadioButton constructorShowAllButton = new JRadioButton("");
	protected JRadioButton constructorShowGreyButton = new JRadioButton("");
	protected JRadioButton constructorShowNoneButton = new JRadioButton("");
	protected ButtonGroup constructorButtons = new ButtonGroup();
	protected JCheckBox constructorSingleLineButton = new JCheckBox("");
	protected JLabel methodLabel = new JLabel("Methods");
	protected JRadioButton methodShowAllButton = new JRadioButton("");
	protected JRadioButton methodShowGreyButton = new JRadioButton("");
	protected JRadioButton methodShowNoneButton = new JRadioButton("");
	protected ButtonGroup methodButtons = new ButtonGroup();
	protected JCheckBox methodSingleLineButton = new JCheckBox("");

	protected JCheckBox showScopeCheckBox = new JCheckBox("Show scope (public, private, etc.) in list pane");
	protected JPanel showScopePanel = new JPanel();
	protected BorderLayout showScopeLayout = new BorderLayout();

//	protected JSeparator saveCancelSeparator = new JSeparator();
	protected JButton buttonSave = new JButton("Save");
	protected JButton buttonCancel = new JButton("Cancel");

// Constructors:

	/**
	 * Default Constructor
	 */
	public PreferencesBox(java.awt.Dimension parentSize)
	{
    	super();
	    try {
    	  jbInit();
	    }
    	catch (Exception e) {
	       System.err.println(e);
    	  e.printStackTrace();
	    }
		this.setLocation(((int)parentSize.getWidth() - this.getWidth())/2,
			((int)parentSize.getHeight() - this.getHeight())/2);
	}

// Methods:

  	private void jbInit() throws Exception
	{
	    ConfigurationService configurationService = ConfigurationService.getConfigurationService();

		titleLabel.setBackground(SystemColor.control);
		titleLabel.setForeground(SystemColor.controlText);
	    titleLabel.setFont(new Font("Dialog", 0, 18));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		titleSeparator.setMaximumSize(new Dimension(9999, componentGap));

		// -----------------------------

		tabSizeLabel.setBackground(SystemColor.control);
		tabSizeLabel.setForeground(SystemColor.controlText);

		tabSizeField.setText("" + configurationService.getTabSize());
		tabSizeField.setMaximumSize(new Dimension(9999, tabSizeLabel.getPreferredSize().height));

		Box tabSizeBox = Box.createHorizontalBox();
		tabSizeBox.add(tabSizeLabel);
		tabSizeBox.add(Box.createHorizontalStrut(componentGap));
		tabSizeBox.add(tabSizeField);

		// -----------------------------

		maxLineLengthLabel.setBackground(SystemColor.control);
		maxLineLengthLabel.setForeground(SystemColor.controlText);

		maxLineLengthField.setText("" + configurationService.getMaxLineLength());
		maxLineLengthField.setMaximumSize(new Dimension(9999, maxLineLengthLabel.getPreferredSize().height));

		Box maxLineLengthBox = Box.createHorizontalBox();
		maxLineLengthBox.add(maxLineLengthLabel);
		maxLineLengthBox.add(Box.createHorizontalStrut(componentGap));
		maxLineLengthBox.add(maxLineLengthField);

		// -----------------------------

  		blankLineBeforeParmsField.setSelected(configurationService.getAddBlankLineBeforeParms());

		firstLineBlank.setActionCommand(BUTTON_BLANK);
		startOnFirstLine.setActionCommand(BUTTON_NEVER_BLANK);
		maybeStartOnFirstLine.setActionCommand(BUTTON_POSSIBLY_BLANK);

		if (configurationService.getExtraLineAtJavaDocTop() &&
				configurationService.getIgnoreBlankLinesAtJavaDocTop()) {
			firstLineBlank.setSelected(true);
		}
		else if (configurationService.getExtraLineAtJavaDocTop() &&
				!configurationService.getIgnoreBlankLinesAtJavaDocTop()) {
			firstLineBlank.setSelected(true);
		}
		else if (!configurationService.getExtraLineAtJavaDocTop() &&
				configurationService.getIgnoreBlankLinesAtJavaDocTop()) {
			startOnFirstLine.setSelected(true);
		}
		else  {
			maybeStartOnFirstLine.setSelected(true);
		}

		firstLineButtons.add(firstLineBlank);
		firstLineButtons.add(startOnFirstLine);
		firstLineButtons.add(maybeStartOnFirstLine);

		ActionListener showSampleText = new ActionListener()  {
			public void actionPerformed(ActionEvent e) {
		    	setSampleText();
			}
		};

		firstLineBlank.addActionListener(showSampleText);
		startOnFirstLine.addActionListener(showSampleText);
		maybeStartOnFirstLine.addActionListener(showSampleText);
  		blankLineBeforeParmsField.addActionListener(showSampleText);

		formatSeperator.setMaximumSize(new Dimension(9999, componentGap));

  		formatPanel.setLayout(formatLayout);
		formatPanel.setBorder(BorderFactory.createEtchedBorder());
  		formatPanel.add(startOnFirstLine);
  		formatPanel.add(maybeStartOnFirstLine);
  		formatPanel.add(firstLineBlank);
  		formatPanel.add(formatSeperator);
  		formatPanel.add(blankLineBeforeParmsField);

	    sampleJavaDoc.setEditable(false);
		sampleJavaDoc.setFont(new Font("Courier", 0, 12));

		sampleScrollPane.getViewport().add(sampleJavaDoc);
		sampleScrollPane.setAutoscrolls(true);
		sampleScrollPane.setMinimumSize(new Dimension(100, 50));
		sampleScrollPane.setPreferredSize(new Dimension(450, 100));
		sampleScrollPane.setMaximumSize(new Dimension(3000, 100*30));

		sampleLabel.setBackground(SystemColor.control);
		sampleLabel.setForeground(SystemColor.controlText);
		sampleLabel.setLabelFor(sampleJavaDoc);

		formatSamplePanel.setLayout(formatSampleLayout);
		formatSamplePanel.add(sampleLabel, BorderLayout.NORTH);
		formatSamplePanel.add(sampleScrollPane, BorderLayout.CENTER);

		combinedSamplePanel.setLayout(combinedSampleLayout);
		combinedSamplePanel.add(formatPanel, BorderLayout.CENTER);
		combinedSamplePanel.add(formatSamplePanel, BorderLayout.EAST);
		combinedSamplePanel.setBorder(BorderFactory.createEtchedBorder());

		// -----------------------------

  		showScopeCheckBox.setSelected(configurationService.getShowScopeInList());
		showScopeCheckBox.setHorizontalAlignment(SwingConstants.LEFT);

		showScopePanel.setLayout(showScopeLayout);
		showScopePanel.add(showScopeCheckBox, BorderLayout.CENTER);
		showScopePanel.setMaximumSize(new Dimension(9999, showScopeCheckBox.getPreferredSize().height));

		// -----------------------------

		classButtons.add(classShowAllButton);
		classButtons.add(classShowGreyButton);
		classButtons.add(classShowNoneButton);
		switch (configurationService.getClassAppearanceInList()) {
			case 0:
				classShowAllButton.setSelected(true);
				break;
			case 1:
				classShowGreyButton.setSelected(true);
				break;
			case 2:
				classShowNoneButton.setSelected(true);
				break;
		}
  		classSingleLineButton.setSelected(configurationService.getClassUsesSingleLineComment());
		classSingleLineButton.setHorizontalAlignment(SwingConstants.CENTER);
		classShowAllButton.setHorizontalAlignment(SwingConstants.CENTER);
		classShowGreyButton.setHorizontalAlignment(SwingConstants.CENTER);
		classShowNoneButton.setHorizontalAlignment(SwingConstants.CENTER);

		interfaceButtons.add(interfaceShowAllButton);
		interfaceButtons.add(interfaceShowGreyButton);
		interfaceButtons.add(interfaceShowNoneButton);
		switch (configurationService.getInterfaceAppearanceInList()) {
			case 0:
				interfaceShowAllButton.setSelected(true);
				break;
			case 1:
				interfaceShowGreyButton.setSelected(true);
				break;
			case 2:
				interfaceShowNoneButton.setSelected(true);
				break;
		}
  		interfaceSingleLineButton.setSelected(configurationService.getInterfaceUsesSingleLineComment());
		interfaceSingleLineButton.setHorizontalAlignment(SwingConstants.CENTER);
		interfaceShowAllButton.setHorizontalAlignment(SwingConstants.CENTER);
		interfaceShowGreyButton.setHorizontalAlignment(SwingConstants.CENTER);
		interfaceShowNoneButton.setHorizontalAlignment(SwingConstants.CENTER);

		fieldButtons.add(fieldShowAllButton);
		fieldButtons.add(fieldShowGreyButton);
		fieldButtons.add(fieldShowNoneButton);
		switch (configurationService.getFieldAppearanceInList()) {
			case 0:
				fieldShowAllButton.setSelected(true);
				break;
			case 1:
				fieldShowGreyButton.setSelected(true);
				break;
			case 2:
				fieldShowNoneButton.setSelected(true);
				break;
		}
  		fieldSingleLineButton.setSelected(configurationService.getFieldUsesSingleLineComment());
		fieldSingleLineButton.setHorizontalAlignment(SwingConstants.CENTER);
		fieldShowAllButton.setHorizontalAlignment(SwingConstants.CENTER);
		fieldShowGreyButton.setHorizontalAlignment(SwingConstants.CENTER);
		fieldShowNoneButton.setHorizontalAlignment(SwingConstants.CENTER);

		constructorButtons.add(constructorShowAllButton);
		constructorButtons.add(constructorShowGreyButton);
		constructorButtons.add(constructorShowNoneButton);
		switch (configurationService.getConstructorAppearanceInList()) {
			case 0:
				constructorShowAllButton.setSelected(true);
				break;
			case 1:
				constructorShowGreyButton.setSelected(true);
				break;
			case 2:
				constructorShowNoneButton.setSelected(true);
				break;
		}
  		constructorSingleLineButton.setSelected(configurationService.getConstructorUsesSingleLineComment());
		constructorSingleLineButton.setHorizontalAlignment(SwingConstants.CENTER);
		constructorShowAllButton.setHorizontalAlignment(SwingConstants.CENTER);
		constructorShowGreyButton.setHorizontalAlignment(SwingConstants.CENTER);
		constructorShowNoneButton.setHorizontalAlignment(SwingConstants.CENTER);

		methodButtons.add(methodShowAllButton);
		methodButtons.add(methodShowGreyButton);
		methodButtons.add(methodShowNoneButton);
		switch (configurationService.getMethodAppearanceInList()) {
			case 0:
				methodShowAllButton.setSelected(true);
				break;
			case 1:
				methodShowGreyButton.setSelected(true);
				break;
			case 2:
				methodShowNoneButton.setSelected(true);
				break;
		}
  		methodSingleLineButton.setSelected(configurationService.getMethodUsesSingleLineComment());
		methodSingleLineButton.setHorizontalAlignment(SwingConstants.CENTER);
		methodShowAllButton.setHorizontalAlignment(SwingConstants.CENTER);
		methodShowGreyButton.setHorizontalAlignment(SwingConstants.CENTER);
		methodShowNoneButton.setHorizontalAlignment(SwingConstants.CENTER);

		blankLabel.setBackground(SystemColor.control);
		blankLabel.setForeground(SystemColor.controlText);
		blankLabel.setHorizontalAlignment(SwingConstants.CENTER);

		showAllLabel.setBackground(SystemColor.control);
		showAllLabel.setForeground(SystemColor.controlText);
		showAllLabel.setHorizontalAlignment(SwingConstants.CENTER);

		showGreyLabel.setBackground(SystemColor.control);
		showGreyLabel.setForeground(SystemColor.controlText);
		showGreyLabel.setHorizontalAlignment(SwingConstants.CENTER);

		showNoneLabel.setBackground(SystemColor.control);
		showNoneLabel.setForeground(SystemColor.controlText);
		showNoneLabel.setHorizontalAlignment(SwingConstants.CENTER);

		singleLineLabel.setBackground(SystemColor.control);
		singleLineLabel.setForeground(SystemColor.controlText);
		singleLineLabel.setHorizontalAlignment(SwingConstants.CENTER);

		classLabel.setBackground(SystemColor.control);
		classLabel.setForeground(SystemColor.controlText);
		classLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		interfaceLabel.setBackground(SystemColor.control);
		interfaceLabel.setForeground(SystemColor.controlText);
		interfaceLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		fieldLabel.setBackground(SystemColor.control);
		fieldLabel.setForeground(SystemColor.controlText);
		fieldLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		constructorLabel.setBackground(SystemColor.control);
		constructorLabel.setForeground(SystemColor.controlText);
		constructorLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		methodLabel.setBackground(SystemColor.control);
		methodLabel.setForeground(SystemColor.controlText);
		methodLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		commentableInfoPanel.setLayout(commentableInfoLayout);
		commentableInfoPanel.setBorder(BorderFactory.createEtchedBorder());
		commentableInfoPanel.add(blankLabel);
		commentableInfoPanel.add(showAllLabel);
		commentableInfoPanel.add(showGreyLabel);
		commentableInfoPanel.add(showNoneLabel);
		commentableInfoPanel.add(singleLineLabel);
		commentableInfoPanel.add(classLabel);
		commentableInfoPanel.add(classShowAllButton);
		commentableInfoPanel.add(classShowGreyButton);
		commentableInfoPanel.add(classShowNoneButton);
		commentableInfoPanel.add(classSingleLineButton);
		commentableInfoPanel.add(interfaceLabel);
		commentableInfoPanel.add(interfaceShowAllButton);
		commentableInfoPanel.add(interfaceShowGreyButton);
		commentableInfoPanel.add(interfaceShowNoneButton);
		commentableInfoPanel.add(interfaceSingleLineButton);
		commentableInfoPanel.add(fieldLabel);
		commentableInfoPanel.add(fieldShowAllButton);
		commentableInfoPanel.add(fieldShowGreyButton);
		commentableInfoPanel.add(fieldShowNoneButton);
		commentableInfoPanel.add(fieldSingleLineButton);
		commentableInfoPanel.add(constructorLabel);
		commentableInfoPanel.add(constructorShowAllButton);
		commentableInfoPanel.add(constructorShowGreyButton);
		commentableInfoPanel.add(constructorShowNoneButton);
		commentableInfoPanel.add(constructorSingleLineButton);
		commentableInfoPanel.add(methodLabel);
		commentableInfoPanel.add(methodShowAllButton);
		commentableInfoPanel.add(methodShowGreyButton);
		commentableInfoPanel.add(methodShowNoneButton);
		commentableInfoPanel.add(methodSingleLineButton);

		// -----------------------------

    	buttonSave.addActionListener(new ActionListener()  {
			public void actionPerformed(ActionEvent e) {
			    ConfigurationService configurationService = ConfigurationService.getConfigurationService();

				configurationService.setAddBlankLineBeforeParms(blankLineBeforeParmsField.isSelected());

				if (firstLineBlank.isSelected()) {
					configurationService.setExtraLineAtJavaDocTop(true);
					configurationService.setIgnoreBlankLinesAtJavaDocTop(true);
				}
				else if (startOnFirstLine.isSelected()) {
					configurationService.setExtraLineAtJavaDocTop(false);
					configurationService.setIgnoreBlankLinesAtJavaDocTop(true);
				}
				else if (maybeStartOnFirstLine.isSelected()) {
					configurationService.setExtraLineAtJavaDocTop(false);
					configurationService.setIgnoreBlankLinesAtJavaDocTop(false);
				}

				try  {
					configurationService.setTabSize(Integer.parseInt(tabSizeField.getText()));
				}
				catch (NumberFormatException ex)  {
					/* ignore it, but make no change */
				}

				try  {
					configurationService.setMaxLineLength(Integer.parseInt(maxLineLengthField.getText()));
				}
				catch (NumberFormatException ex)  {
					/* ignore it, but make no change */
				}

		  		configurationService.setShowScopeInList(showScopeCheckBox.isSelected());

				if (classShowAllButton.isSelected()) {
					configurationService.setClassAppearanceInList(0);
				}
				else if (classShowGreyButton.isSelected()) {
					configurationService.setClassAppearanceInList(1);
				}
				else if (classShowNoneButton.isSelected()) {
					configurationService.setClassAppearanceInList(2);
				}

				if (interfaceShowAllButton.isSelected()) {
					configurationService.setInterfaceAppearanceInList(0);
				}
				else if (interfaceShowGreyButton.isSelected()) {
					configurationService.setInterfaceAppearanceInList(1);
				}
				else if (interfaceShowNoneButton.isSelected()) {
					configurationService.setInterfaceAppearanceInList(2);
				}

				if (constructorShowAllButton.isSelected()) {
					configurationService.setConstructorAppearanceInList(0);
				}
				else if (constructorShowGreyButton.isSelected()) {
					configurationService.setConstructorAppearanceInList(1);
				}
				else if (constructorShowNoneButton.isSelected()) {
					configurationService.setConstructorAppearanceInList(2);
				}

				if (fieldShowAllButton.isSelected()) {
					configurationService.setFieldAppearanceInList(0);
				}
				else if (fieldShowGreyButton.isSelected()) {
					configurationService.setFieldAppearanceInList(1);
				}
				else if (fieldShowNoneButton.isSelected()) {
					configurationService.setFieldAppearanceInList(2);
				}

				if (methodShowAllButton.isSelected()) {
					configurationService.setMethodAppearanceInList(0);
				}
				else if (methodShowGreyButton.isSelected()) {
					configurationService.setMethodAppearanceInList(1);
				}
				else if (methodShowNoneButton.isSelected()) {
					configurationService.setMethodAppearanceInList(2);
				}

  				configurationService.setClassUsesSingleLineComment(classSingleLineButton.isSelected());
  				configurationService.setInterfaceUsesSingleLineComment(interfaceSingleLineButton.isSelected());
  				configurationService.setConstructorUsesSingleLineComment(constructorSingleLineButton.isSelected());
  				configurationService.setFieldUsesSingleLineComment(fieldSingleLineButton.isSelected());
  				configurationService.setMethodUsesSingleLineComment(methodSingleLineButton.isSelected());

				FileManager.getFileManager().updateListPane();	/* Redraw the item list */

				PreferencesBox.this.actionPerformed(e);
			}
    	});

    	buttonCancel.addActionListener(this);

//		tabSizeSeparator.setMaximumSize(new Dimension(9999, componentGap));
//		maxLineLengthSeparator.setMaximumSize(new Dimension(9999, componentGap));

//		saveCancelSeparator.setMaximumSize(new Dimension(9999, componentGap));

		Box saveCancelBox = Box.createHorizontalBox();
		saveCancelBox.add(Box.createGlue());
		saveCancelBox.add(buttonSave);
		saveCancelBox.add(buttonCancel);
		saveCancelBox.add(Box.createHorizontalStrut(componentGap));

		// -----------------------------

		Box wholeBox = Box.createVerticalBox();
		wholeBox.add(titleLabel);
		wholeBox.add(titleSeparator);
		wholeBox.add(tabSizeBox);
//		wholeBox.add(tabSizeSeparator);
		wholeBox.add(maxLineLengthBox);
//		wholeBox.add(maxLineLengthSeparator);
		wholeBox.add(combinedSamplePanel);
//		wholeBox.add(saveCancelSeparator);
		wholeBox.add(showScopePanel);
		wholeBox.add(commentableInfoPanel);
		wholeBox.add(saveCancelBox);

    	setSampleText();

    this.getContentPane().add(wholeBox, null);
    //this.setModal(true);
    this.setBackground(SystemColor.control);
    this.setTitle("Preferences");

    pack();
    //this.setSize(100,100);
    this.setVisible(true);
    //setResizable(false);
  }

  private void setSampleText()
  {
	    ConfigurationService configurationService = ConfigurationService.getConfigurationService();

		sampleJavaDoc.setTabSize(configurationService.getTabSize());

		StringBuffer sample = new StringBuffer("/** ");
		if (firstLineBlank.isSelected()) {
			sample.append("\n * ");
		}
		else if (maybeStartOnFirstLine.isSelected()) {
			sample.append("(may be empty)");
			sample.append("\n * ");
		}
		sample.append("Code comment here\n");

		if (blankLineBeforeParmsField.isSelected()) {
			sample.append(" *\n");
		}

		sample.append(" * @see (would be here)\n");
		sample.append(" */");

		sampleJavaDoc.setText(sample.toString());
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == buttonSave ||
			e.getSource() == buttonCancel) {
	    setVisible(false);
    	dispose();
    }
  }

	/**
	 * Come up with a printable string for this object.
	 *
	 *return A printable string.
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer(this.getClass().getName());
		buf.append("@");
		buf.append(this.hashCode());

		return buf.toString();
	}

// Get/Set Methods:

}