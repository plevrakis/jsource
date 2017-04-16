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
package tinyplanet.docwiz;

import java.awt.*;
import javax.swing.UIManager;
import gnu.getopt.LongOpt;
import gnu.getopt.Getopt;
import java.io.IOException;
import java.io.File;
import javax.swing.plaf.metal.MetalLookAndFeel;
import tinyplanet.gui.SystemColorTheme;
//import javax.swing.plaf.mac.*;

/** This class starts the DocWiz application. Use the command
 * <p>
 * <pre>java tinyplanet.docwiz.appDocWiz</pre>
 *
 * @since
 * @author Simon Arthur
 * @author docwiz@hyper-g.com
 * @version
 */
public class  appDocWiz {


  /** Whether or not pack() will be called on the frame.
   *
   */
  boolean packFrame = false;

  //Construct the application
  /** Create the main DocWiz window.
   *
   * @param filename the name of the file to load, null if no file is to be loaded
   * @see main()
   */
  public appDocWiz(String directory, String filename)
  {
	System.out.println("appDocWiz.<init>: directory=" + directory + ", file=" + filename);

    try  {
      //UIManager.setLookAndFeel(new javax.swing.plaf.windows.WindowsLookAndFeel());
      // UIManager.setLookAndFeel(new javax.swing.plaf.motif.MotifLookAndFeel());
      //UIManager.setLookAndFeel(new javax.swing.plaf.mac.MacLookAndFeel());

      MetalLookAndFeel.setCurrentTheme(new SystemColorTheme());
      UIManager.setLookAndFeel(new MetalLookAndFeel());
    }
    catch (Exception e) {
      System.err.println(e);
    }
	System.out.println("appDocWiz.<init>: instanciating 'g'");
    g frame = new g();
    if (filename != null) {
      FileManager fileManager = FileManager.getFileManager();
      fileManager.doOpenFile(directory, filename);
    }
    frame.setVisible(true);
	System.out.println("appDocWiz.<init>: 'g' now visible");
  }

  //Main method
  /** The first method invoked when this class is invoked via the command line.
   * Arguments:
   * filename
   * --help Print help message with arguments
   * --version Print version information
   * @see
   * @param args The command-line arguments
   */
  public static void main(String[] args) {
	System.out.println("appDocWiz.main");
    LongOpt[] longopts = new LongOpt[5];
    //
    StringBuffer sb =  new StringBuffer();
    longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
    longopts[1] = new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v');
    longopts[2] = new LongOpt("scan-comments", LongOpt.NO_ARGUMENT, null, 's');
    longopts[3] = new LongOpt("fill-in", LongOpt.NO_ARGUMENT, null, 'f');
    longopts[4] = new LongOpt("line-length", LongOpt.REQUIRED_ARGUMENT, null, 'l');
    //longopts[4] = new LongOpt("no-title-format", LongOpt.NO_ARGUMENT, null, 'n');
    //longopts[5] = new LongOpt("grab-end", LongOpt.NO_ARGUMENT, null, 'g');
    //
    Getopt g = new Getopt("DocWiz", args, "hvsfgl:n", longopts);
    g.setOpterr(false); // We'll do our own error handling
    int c;
    String commandLineFileName = null; // the name given on the command line

    String currentDir = System.getProperty("user.dir")+ System.getProperty("file.separator");
    ConfigurationService config = ConfigurationService.getConfigurationService();
    config.loadConfiguration();
    while ((c = g.getopt()) != -1) {
	  System.out.println("appDocWiz.main: option " + c);
      switch (c)  {
        case 0:
          System.out.println("option 0");
          break;
        case 'h':
          System.out.println(config.HELP_NOTICE);
          config.setStartGUI(false);
          break;
        case 'v':
          System.out.println(config.COPYRIGHT_NOTICE);
          config.setStartGUI(false);
          break;
        case 's':
          config.setDoScan(true);
          config.setStartGUI(false);
          break;
        case 'f':
          config.setDoFill(true);
          config.setStartGUI(false);
          break;
        case 'n':
          config.setFormatTagTitle(false);
          break;
        case 'g':
          config.setGrabFieldComments(true);
          break;
        case 'l':
          String sArg = g.getOptarg();
          try
          {
            config.setMaxLineLength(Integer.valueOf(sArg).intValue());
          }
          catch (NumberFormatException e)
          {
            System.out.println("Invalid value for flag l ("+sArg+") -- ignored.");
            config.setMaxLineLength(78);
          }
          break;
        case '?':
          System.out.println("Unknown option given on command line, continuing");
          break;
      }
    }

    // Split out the filenames
    int firstFilePos = g.getOptind();
    int numberOfFiles = args.length - firstFilePos;
    String[] filenameArray = new String[numberOfFiles];

	System.out.println("appDocWiz.main: options done");

    for (int i = 0 ; i < numberOfFiles ; i++)
      filenameArray[i] =  args[i+firstFilePos];
    if (config.getStartGUI()) {
	System.out.println("appDocWiz.main: start gui");
      // user did not specify an option which would prohibit starting the GUI
      if (numberOfFiles>0) {
        for (int i = 0 ; i < numberOfFiles ; i++) {
		File f = new File(filenameArray[i]);
		if (f.isAbsolute()) {
          new appDocWiz(f.getParent(),f.getName());
		}
		else  {
          new appDocWiz(currentDir,filenameArray[i]);
		}
        }
      } else {
        new appDocWiz(currentDir, null);
      }
    }
	System.out.println("appDocWiz.main: after start gui");

    if (config.getDoFill()) {
      for (int i = 0 ; i < numberOfFiles ; i++) {
        try {
          FillIn.fillin(currentDir, filenameArray[i]);
        } catch (IOException ioe) {
          System.err.println(ioe);
        }
       }
     }

    if (config.getDoScan()) {
      for (int i = 0 ; i < numberOfFiles ; i++) {
        try {
          ScanComments.scanFile( filenameArray[i]);
        } catch (tinyplanet.javaparser.ParseException pe) {
          System.err.println(pe);
          break;
        }
      }
    }


  }
}
