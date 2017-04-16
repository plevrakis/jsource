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

import  java.io.* ;
import  java.util.* ;
import tinyplanet.javaparser.ParseException;

public class FillIn {
  String currentFileDirectory, currentFileName;
  static CompilationUnit currentUnit;
  String wholeFile;
  public static boolean bGrabFieldComments = false;

  /**
   * Create a new skeleton DocWiz.  Unusable without setting
   * currentFileDirectory and currentFileName
   */
  public FillIn ()
  {
    init ();
  }

  /**
   * Create a new skeleton DocWiz.
   */
  public FillIn (String sDirectory,
                     String sFileName)
  {
    currentFileDirectory = sDirectory;
    currentFileName = sFileName;
    init ();
  }


  /**
   * initialization
   */
  private void init ()
  {
  }

  /**
   * Open the file with the given file name and directory
   *
   * @param  directoryName directory name, with trailing dir separator
   * @param  fileName name of the file to open
   */
  void doOpenFile (String directoryName, String fileName)
  {
    try
    {
      currentUnit = new CompilationUnit(directoryName, fileName);
      this.currentFileName = fileName;
      this.currentFileDirectory = directoryName;

    }
    catch (FileNotFoundException fnfe)
    {
      this.currentFileName = null;
      System.err.println("File  not found: "+ directoryName + fileName);
      return;
    }
    catch (IOException ioe)
    {
      System.err.println(ioe);
    }
    catch (ParseException pe)
    {
      System.err.println(pe);
    }
  }



  public static void fillin(String currentDir, String commandLineFileName)  throws IOException {
    FillIn pDocWiz = new FillIn  (currentDir, commandLineFileName);
    if (commandLineFileName  !=  null)
    {
      // load the file
      pDocWiz.doOpenFile(currentDir, commandLineFileName);
      // int j = pDocWiz.currentUnit.getSize();
      // System.out.println ("pDocWiz.currentUnit.getSize() == "+j);
      for (int i = 0; i < pDocWiz.currentUnit.getSize(); i++) {
        // System.out.println("block "+i);
        CommentableCode pCode =
          (CommentableCode)(pDocWiz.currentUnit.getElementAt(i));
        pCode.setupCodeComment(pCode.baseNode);
        CodeComment pComment = pCode.getCodeComment();
        if (pCode instanceof Method)
        {
          pComment.getTagEntry("param");
          pComment.getTagEntry("exception");
          Method pMethod = (Method)pCode;
          String sRtnType = pMethod.getType();
          if (!sRtnType.equals("void")) {
            pComment.getTagEntry("return");
            // TagSet pTagSet = pComment.getTagEntry("return");
            // pTagSet.addValue(sRtnType);
          }

        }
        else if (pCode instanceof Constructor)
        {
          pComment.getTagEntry("param");
          pComment.getTagEntry("exception");
        }
        else if (pCode instanceof Field)
        {
          Field pField = (Field)pCode;
          if (bGrabFieldComments)
          {
            // pField.findComment();
            if (pField.comment != null)
            {
              String sOldTitle = pComment.getTitle();
              if (sOldTitle != null)
              {
                sOldTitle += System.getProperty("line.separator")
                + pField.comment;
              }
              else
              {
                sOldTitle = pField.comment.toString();
              }
              pComment.setTitle(sOldTitle);
            }
          }
        }
        else if (pCode instanceof ElementContainer)
        {
          pComment.getTagEntry("author");
          pComment.getTagEntry("version");
          pComment.getTagEntry("design");
          pComment.getTagEntry("example");
        }
        else
        {
        }
      }
      // System.out.println("saving...");
      currentUnit.doSave();
      // System.out.println("done");
      }
  }

  public static void main(String[] args) {
    FillIn fillIn = new FillIn();
    fillIn.invokedStandalone = true;
  }
  private boolean invokedStandalone = false;
}