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

// History:
//	7/13/2001 lmm	Added filter to only show .java files here.

package tinyplanet.docwiz;
import tinyplanet.gui.ConcreteRack;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.awt.Component;
import java.io.File;

/**
 * Manager for various modal dialogs.
 * @author Simon Arthur
 */
public class ModeManager implements ItemChangedListener
{
  private static ModeManager modeManager = new ModeManager();
  private ConcreteRack rack;
  CompilationUnit currentUnit;
  FileManager fileManager = FileManager.getFileManager();

  public void itemChanged(ItemChangedEvent ie) {
    currentUnit = ie.getCompilationUnit();
  }

  private ModeManager() {
  }

  public static ModeManager getModeManager() {
    return modeManager;
  }

  public int askReload() {
    //System.err.println("ModeMgr: about to ask for reload");
    return JOptionPane.showConfirmDialog
          (this.rack,
          "The file's time stamp has changed. \nDo you want to reload the file?",
          "Reload file?",
          JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
  }

  public void showSaveError() {
      JOptionPane.showMessageDialog(null,
        "There was a problem saving the file.    \nCheck your available disk space and permissions.    ",
        "Error while saving file",JOptionPane.ERROR_MESSAGE);
  }

  public void showOpenError() {
        JOptionPane.showMessageDialog(null,
        "There was a problem while opening the file.    \nCheck your file permissions.    ",
        "Error while opening file",JOptionPane.ERROR_MESSAGE);
  }

  public void showParseError() {
      JOptionPane.showMessageDialog(null,
        "There was a problem while parsing the file.    \nMake sure that the file compiles correctly.    ",
        "Error while opening file",JOptionPane.ERROR_MESSAGE);
  }

 public void askExit() {
   // System.err.println("ModeManager: asking");
   // System.err.println("ModeManager: currentUnit="+currentUnit);
   // System.err.println("ModeManager: currentUnit.getCurrentFileModified()="+currentUnit.getCurrentFileModified());
    if (currentUnit != null && currentUnit.getCurrentFileModified()) {
        // ask if user wants to save modified file.
      int result = JOptionPane.showConfirmDialog
        (null,
        "The current file has been modified,     \nsave before closing?",
        "Save current file?",
        JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);

      if (result == JOptionPane.YES_OPTION) {
         fileManager.doSave();
      }
      if (result ==  JOptionPane.NO_OPTION) {
       // just proceed.
      }
      if (result == JOptionPane.CANCEL_OPTION) {
       // skip over everything.
       return;
      }
    }
    //this.dispose();
    // save configuration
    ConfigurationService cs = ConfigurationService.getConfigurationService();
    cs.saveConfiguration();

    System.exit(0);
  }

  public void askOpen(Component parent) {
    if (currentUnit!= null && currentUnit.getCurrentFileModified()) {
        // ask if user wants to save modified file.
      int result = JOptionPane.showConfirmDialog
        (null,
        "The current file has been modified,     \nsave before opening the new file?",
        "Save current file?",
        JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);

      if (result == JOptionPane.YES_OPTION) {
         fileManager.doSave();
      }
      if (result ==  JOptionPane.NO_OPTION) {
       // just proceed.
      }
      if (result == JOptionPane.CANCEL_OPTION) {
        // skip over everything.
        return;
      }
    }
    ConfigurationService cs = ConfigurationService.getConfigurationService();
    String lastDir = cs.getLastDirectory();
    JFileChooser flrFileToDoc = new JFileChooser();
    flrFileToDoc.setCurrentDirectory(new File(lastDir));

    // Establish file filter for the chooser
    flrFileToDoc.setFileFilter(
    	new javax.swing.filechooser.FileFilter() {
        	public boolean accept(File f) {
            	String name = f.getName().toLowerCase();
                return name.endsWith(".java") || f.isDirectory();
            }
            public String getDescription() {
            	return "Java source (*.java)";
            }
        }
    );

    flrFileToDoc.setVisible(true);
    flrFileToDoc.showOpenDialog(parent);
    File f = flrFileToDoc.getSelectedFile();
    if (f != null) {
      // user hasn't pressed Cancel
      fileManager.doOpenFile( f.getParent(), f.getName());
    }
  }
}