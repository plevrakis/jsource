/*
DocWiz: Easy Javadoc documentation
Copyright (C) 2000 Simon Arthur

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

import java.io.File;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import tinyplanet.javaparser.ParseException;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class FileManager {
  private static FileManager fileManager = new FileManager();
  private ModifiedChecker modifiedChecker;
  CompilationUnit currentUnit;
  ChangeDispatcher changeDispatcher = ChangeDispatcher.getChangeDispatcher();
  ModeManager modeManager = ModeManager.getModeManager();

  public static FileManager getFileManager() {
    return fileManager;
  }

  /** Default Constructor is private */
  private FileManager() {
  }

/**
  * Open the file with the given file name and directory
  * @param  directoryName directory name, with trailing dir separator
  * @param  fileName name of the file to open
  */

  void doOpenFile( String directoryName, String fileName) {
    if (directoryName == null)
      directoryName = "";
    String directorySeparator = System.getProperty("file.separator");
    if (!directoryName.endsWith(directorySeparator)) {
      directoryName += directorySeparator;
    }
    // Shut down the thread which watches the file on the disk to see if it's changed
    if (this.modifiedChecker != null) {
      this.modifiedChecker.setActiveCheck(false);
    }

    long modTime;
    File f;
    CompilationUnit newUnit = currentUnit;
    ModeManager modeManager = ModeManager.getModeManager();
    try {
       newUnit = new CompilationUnit(directoryName, fileName);
       this.currentUnit = newUnit;
    } catch (FileNotFoundException fnfe) {
      System.err.println(" file " + directoryName + " " + fileName + " not found!");
      //this.jLblStatus.setText("Problem opening file.");
      modeManager.showOpenError();
      return;
    } catch (IOException ioe) {
      System.err.println(ioe);
      //this.jLblStatus.setText("Problem opening file.");
      modeManager.showOpenError();
    } catch (ParseException pe) {
      System.err.println(pe);
      //this.jLblStatus.setText("Problem opening file.");
      modeManager.showParseError();
    }

    // Watch to see if the file's been changed
    if (this.modifiedChecker == null ) {
      this.modifiedChecker = new ModifiedChecker(this);
      this.modifiedChecker.start();
    }
    this.modifiedChecker.setActiveCheck(true);

    changeDispatcher.fireItemChanged(newUnit, (CommentableCode)newUnit.getElementAt(0));


    //this.jLblStatus.setText("File opened.");
    //this.jLblStatus.repaint();

    //setCurrentElementControls();
    newUnit.setFileModified(false);
  }

 void doSave()
 {
  // Shut down the thread which watches the file on the disk to see if it's changed
    if (this.modifiedChecker != null) {
      this.modifiedChecker.setActiveCheck(false);
    }

  //stopEditing();
  //this.jLblStatus.setText("Saving...");
  try {
      currentUnit.doSave();
    } catch (IOException IOE) {
      System.err.println(IOE);
      //this.jLblStatus.setText("Problem saving file.");
      modeManager.showSaveError();
    }

    // Watch to see if the file's been changed
    if (this.modifiedChecker == null ) {
      this.modifiedChecker = new ModifiedChecker(this);
      this.modifiedChecker.start();
    }
    this.modifiedChecker.setActiveCheck(true);
    currentUnit.setFileModified(false);
    //this.jLblStatus.setText("File saved.");
  }

	/**
	 * Fire a change event to tell the list pane that the model is changed for its list
	 *		of code items.
	 *
	 * @since 07/12/2001 3:02:17 PM
	 */
	public void updateListPane()
	{
		if (currentUnit instanceof CompilationUnit) {	// also handles it being null
		  	currentUnit.fireContentsChanged();
		}
		return;
	}

 /** Check to see if the file has been externally modified since it was
  * loaded
  */
  void checkFileModified()
  {
    //System.out.println("FileMgr.checkFileModified: 1");
    if (this.modifiedChecker != null) {
      this.modifiedChecker.setActiveCheck(false);
    }

    boolean isModified = currentUnit.isFileModified();
    if (isModified) {
      //System.err.println("FileMgr : Is modified!");
      modeManager = ModeManager.getModeManager();
      int result = modeManager.askReload();
      //System.err.println("FileMgr : result="+result);
      if (result == JOptionPane.YES_OPTION) {
        this.doOpenFile(currentUnit.getFileDir() , currentUnit.getFileName());
      }
      if (result ==  JOptionPane.NO_OPTION) {
        // reset the file info, so it doesn't keep asking
        currentUnit.resetModTime();
      }

    }
    //System.out.println("filemgr.checkFileModified: 3");
    // Watch to see if the file's been changed
    if (this.modifiedChecker == null ) {
      this.modifiedChecker = new ModifiedChecker(this);
      this.modifiedChecker.start();
    }
    this.modifiedChecker.setActiveCheck(true);
    //System.out.println("filemgr.checkFileModified: 4");
  }


/** Class to check if the file has changed since it was loaded */
class ModifiedChecker extends Thread {
  FileManager f;
  boolean activeCheck;
  public ModifiedChecker(FileManager newF) {
    f = newF;
  }

  public void setActiveCheck(boolean b) {
    this.activeCheck = b;
  }

  public boolean isActiveCheck() {
    return this.activeCheck;
  }

  public void run() {
    CodeComment codeComment;
    try {
      while (true) {
        if (isActiveCheck()) {
           Runnable doWorkRunnable = new Runnable() {
              public void run() {
                f.checkFileModified();
              }
           };
           try {
             SwingUtilities.invokeAndWait(doWorkRunnable);
           } catch (InterruptedException ire) {
             System.err.println("ModifiedChecker: interrupted");
           }  catch (InvocationTargetException ite) {
             System.err.println("ModifiedChecker: InvocationTargetException");
           }
        }
        Thread.sleep(3000);
      }
    } catch (InterruptedException ie) {
      return;
    }
  }
   }
}