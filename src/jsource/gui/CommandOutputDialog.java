package jsource.gui;


/**
 * CommandOutputDialog.java  04/15/03
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
import java.io.*;
import jsource.*;
import jsource.util.*;
import jsource.io.localization.*;
import jsource.codegenerator.*;


/**
 * <code>CommandOutputDialog</code> displays the output of various external commands
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class CommandOutputDialog extends BaseDialog {
    private XMLResourceBundle bundle = null;
    private JButton close = null;
    private StatusOutput output = null;
    private Process ps = null;

    CommandOutputDialog(XMLResourceBundle bundle, String command, String title) {
        super(null, title, true);
        this.bundle = bundle;
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);
        output = new StatusOutput();
        content.add(BorderLayout.CENTER, output);
		new Command(command, title).start();
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
        setResizable(true);
        pack();
        setLocationRelativeTo(null);
        if (!title.equals("browser")) { // if web browser is launched do not show the dialog
        	show();
		}
    }

    public void ok() {
        dispose();
        if (ps != null) {
			ps.destroy();
			ps = null;
        }
    }

    public void cancel() {
        dispose();
        if (ps != null) {
			ps.destroy();
			ps = null;
        }
    }

    class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            dispose();
        }
    }

	/**
	 * <code>Command</code> is a thread that runs external .exe commands
	 * like java, javac, ant, etc.
	 */
	private class Command extends Thread {

	  private String command = null;
	  private String message = null;
	  private boolean successfulCompletion = false;

	  private Command(String command, String message) {
		this.command = command;
		this.message = message;
	  }

	  public void run() {
          output.showOutput(message + LINE_SEP);
          try {
              ps = Runtime.getRuntime().exec(command);
              InputStream iserr = ps.getErrorStream();
              InputStream isinp = ps.getInputStream();

              /*---------------------------------------------------------------------
                             Reason for using CheckStream below
                 Because some native platforms only provide limited buffer size
			     for standard input and output streams, failure to promptly write
			     the input stream or read the output stream of the subprocess
                 may cause the subprocess to block, and even deadlock.
              ---------------------------------------------------------------------*/

              CheckStream cserr = new CheckStream(iserr);
              CheckStream csinp = new CheckStream(isinp);
              cserr.start();
              csinp.start();
              if (ps.waitFor() == 0) {
              	  System.out.println("WAITFOR == 0");
              	  ps.destroy();
              	  ps = null;
			  }
          } catch (Throwable ex) {
              output.showOutput(bundle.getValueOf("procfailure") + LINE_SEP + ex.getMessage());
          }
	  }

	  public boolean completedSuccessfully() {
	      return successfulCompletion;
	  }
	}

    /**
     * <code>CheckStream</code> is used for checking the results if any of an InputStream
     *  @author: Panagiotis Plevrakis <br> Based on a class written by Charles Bell
     */
    private class CheckStream extends Thread {

        private BufferedReader br = null;
        private java.util.Vector outputData = null;
        private int errorLineCount = 0;

        /**
         * Constructor needs an InputStream to form an anonymous
         * InputStreamReader which is used to create a BufferedReader
         * for reading the stream.
         */
        private CheckStream(InputStream is) {
            this.br = new BufferedReader(new InputStreamReader(is));
            outputData = new java.util.Vector();
        }

        /**
         * Reads the input stream and displays anything returned.
         */
        public void run() {
			String lineread = "";
            try {
                while((lineread = br.readLine()) != null) {
                    outputData.add(lineread);
                }
                while (errorLineCount < outputData.size()) {
                    output.showOutput((String)outputData.elementAt(errorLineCount) + LINE_SEP);
                    errorLineCount++;
                }
            } catch (IOException ioe) {
                Log.log(ioe);
            } finally {
				try {
					br.close();
				} catch (Exception e) {
					Log.log(e);
				}
			}
        }
    }
}
