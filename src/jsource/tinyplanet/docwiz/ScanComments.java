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

import java.io.*;
import java.util.*;
import tinyplanet.javaparser.ParseException;

public class ScanComments{
	static CompilationUnit	currentUnit;
	static String		lastName="";


	static void scanFile(String fileName) throws tinyplanet.javaparser.ParseException {
		Vector		sumInfo;
		CommentableCode commentInfo;
		int		commentLevel, i;
		boolean		firstFlag=true;


		try {
			currentUnit = new CompilationUnit("", fileName);

			System.out.print("\rScanning "+fileName+"...");

			if (lastName.length() > fileName.length())
				for (i=fileName.length(); i<lastName.length(); i++)
					System.out.print(" ");

			lastName = fileName;

			sumInfo = currentUnit.getSummaryInfo();

			for (i=0; i<currentUnit.getSize(); i++){
				commentInfo = (CommentableCode) currentUnit.getElementAt(i);
				commentLevel = commentInfo.getCodeComment().getCommentLevel();

				if (commentLevel<4){
					if (firstFlag){
						System.out.println("\rMissing comments in "+fileName);

						firstFlag=false;
					}

					System.out.println("   "+commentInfo);
				}
			}

			if (!firstFlag)
				System.out.println();
		} catch (FileNotFoundException fnfe) {
      	System.err.println(" file  not found!");
  			return;
		} catch (IOException ioe) {
			System.err.println(ioe);
		} catch (ParseException pe) {
			System.err.println(pe);
		}
	}

        /** Main method
         *
         * @exception Exception
         * @param args Argument list
         */
	public static void main(String args[]) throws tinyplanet.javaparser.ParseException {
		for (int i=0; i<args.length; i++)
			scanFile(args[i]);

		System.exit(0);
	}
}
