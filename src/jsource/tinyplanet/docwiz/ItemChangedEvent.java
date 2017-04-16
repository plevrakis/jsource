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

public class ItemChangedEvent {
  private CompilationUnit compilationUnit;
  private CommentableCode commentableCode;

  public ItemChangedEvent(CompilationUnit  newCompilationUnit,
    CommentableCode newCommentableCode) {
    this.compilationUnit = newCompilationUnit;
    this.commentableCode = newCommentableCode;
  }

  public CommentableCode getCurrentCode() {
    return commentableCode;
  }
  public CompilationUnit getCompilationUnit() {
    return compilationUnit;
  }

}