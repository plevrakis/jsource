package jsource.gui;


/**
 * MessageFrame.java  06/19/03
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class MessageFrame extends JComponent {

	private static JTextPane messageArea;
	private static DefaultStyledDocument doc;

	public MessageFrame() {
		super();
		setLayout(new BorderLayout());
		doc = new DefaultStyledDocument(createStyles());
		messageArea = new JTextPane(doc);
		messageArea.setEnabled(false);
		messageArea.setDoubleBuffered(false);
		JScrollPane scroller = new JScrollPane();
		JViewport viewer = scroller.getViewport();
		viewer.setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE );
		viewer.add(messageArea);
		add(scroller,BorderLayout.CENTER);
	}

	public JTextPane getMessagePane() {
		return messageArea;
	}

	private StyleContext createStyles() {
		StyleContext sc = new StyleContext();
		Style def = sc.addStyle("default",null);
		StyleConstants.setForeground(def,Color.black);
		StyleConstants.setFontFamily(def,"SansSerif");
		StyleConstants.setFontSize(def,10);
		Style s = sc.addStyle("date",def);
		StyleConstants.setForeground(s,Color.blue);
		s = sc.addStyle("error",def);
		StyleConstants.setForeground(s,Color.red);
		s = sc.addStyle("other",def);
		StyleConstants.setForeground(s,Color.green.darker());
		return sc;
	}

	private Style findStyle(String text) {
		return doc.getStyle(text);
	}

	public static void addText(String textToAdd, Style s) {
		try {
			doc.insertString(doc.getLength(),textToAdd,s);
			messageArea.repaint();
		}
		catch(BadLocationException ex) {
			setErrorText(ex.toString());
		}
	}

	public static void setText(String textToAdd) {
		addText(textToAdd,null);
	}

	public static void setDateText(String dateToShow) {
		Style s = doc.getStyle("date");
		addText(dateToShow,s);
	}

	public static void setErrorText(String textToAdd) {
		Style s = doc.getStyle("error");
		addText(textToAdd,s);
	}

	public static void setOtherText(String textToAdd) {
		Style s = doc.getStyle("other");
		addText(textToAdd,s);
	}

	public static void removeText() {
		try {
			doc.remove(0,doc.getLength());
		}
		catch(BadLocationException ex) {
			setErrorText(ex.toString());
		}
	}
}
