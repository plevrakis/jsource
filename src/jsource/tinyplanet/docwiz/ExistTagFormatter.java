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

// History
//	3/22/2001 Lee Meador (LeeMeador@usa.net) 1 Changed so that the javadoc lines
//		for parameters don't have an indent on the first line (it is not added
//		by the caller.) 2) Use StringBuffer objects to build strings. 3) Fix some bugs
//		in choosing line endings for longer parameter lines. 4) Put tabs on the
//		front of javadoc comment lines using the new properties file entry.
// 7/17/2001 lmm	- use the constructor from the super class.

package tinyplanet.docwiz;
import java.util.StringTokenizer;

/** A TagFormatter for those tagged paragraphs which have meaning by their mere presence.
 * The deprecated tag should use this TagFormatter, since it should print even if it has
 * no description string associated with it.
 *
 * Tagged paragraphs which must have a value associated with them
 * to print: see, author, since, and the like should use ExistTagFormatter as their TagFormatter instead.
 */
public class ExistTagFormatter extends TagFormatter {

  public ExistTagFormatter(TagSet t, String tagName) {
  	super(t, tagName);
//    setTagSet(t);
//    setTagName(tagName);
  }


   /**
    * Format these tags into comment lines. The first line should not have the indent and
    *  leading star (asterisk) but all subsequent lines should.
    *
    * @return The tags formatted into comment lines.
    */
   public String getJavaDocString() {
    ConfigurationService config = ConfigurationService.getConfigurationService();
    StringBuffer returnValue = new StringBuffer();
    String LS = System.getProperty("line.separator");
    boolean somethingToPrint = false;
    int i;
    int totalElements = getTagSet().size();
    String tagType = this.getTagName();

    String indentString = "";
    int indentLevel = this.getTagSet().getCodeToTag().getIndentLevel();
	int tabSize = config.getTabSize();
    i = 0;
	while (i+tabSize-1 < indentLevel) {			// use tabs where possible
	 	indentString += "\t";
	 	i += tabSize;
	}
    for (;i<indentLevel;i++) {
     	indentString += " ";
    }

    StringTokenizer st;
    String currentToken;
    int lineLength =0 ;
    String currentValue;
    StringBuffer currentTagString = new StringBuffer();

	String frontOfLine = "";						// Leave off the indent on the first line
    for (i=0; i<totalElements; i++) {
      currentTagString.setLength(0);
      currentValue = this.getTagSet().tagAt(i);

      st = new StringTokenizer(currentValue," \t", true);
      currentTagString.append(frontOfLine + "@" + tagType + " ");
	  frontOfLine = indentString + "* ";			// additional lines have an indent
													// Account for indent to be added by caller
													//  to the front of the 1st line.
      lineLength = currentTagString.length() + frontOfLine.length();
      while (st.hasMoreTokens()) {
          currentToken = st.nextToken();
          lineLength += currentToken.length();
          if (lineLength > config.getMaxLineLength()) {
               currentTagString.append(LS + indentString + "*     " + currentToken);
               lineLength = currentToken.length() + indentString.length() + 6;
          } else {
               currentTagString.append(currentToken);
          }
      }
      returnValue.append(currentTagString);
	  returnValue.append(LS);
    }
    return returnValue.toString();
  }
}