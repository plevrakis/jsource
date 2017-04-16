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

 /** This interface represents a piece of Java code which can be modified.
  *
  * @author Simon Arthur
  */
public interface WritableCode {
 /** Get the JavaDoc CodeComment associated with the Java code entity
  *
  * @return The JavaDoc CodeComment associated with the WritableCode
  */
  public CodeComment getCodeComment();
 /** Get the line number on which the declaration of the code entity starts
  *
  * @return The line number for the declaration
  */
  public int getLineNumber();
 /** The column on which the code starts
  *
  * @return The number of the column on which the code is declared
  */
  public int getColumnNumber();
 /** Change the JavaDoc code comment for the WritableCode Java code entity
  *
  * @param c The new CodeComment for this WritableCode
  */
  public void setCodeComment(CodeComment c);
}