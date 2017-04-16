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

import tinyplanet.gui.ConcreteAppPane;
import java.awt.*;
import javax.swing.*;

public class EditControlsAppPane extends ConcreteAppPane implements ItemChangedListener {
  CardLayout cardLayout = new CardLayout();

  FieldPanel pnlField = new FieldPanel();
  ExecutableElementPanel pnlExecutableElement = new ExecutableElementPanel();
  ClassPanel pnlClass = new ClassPanel();
  JPanel jPnlBlank = new JPanel();

  public EditControlsAppPane() {
    setTitle("JavaDoc Fields");
    this.setLayout(cardLayout);

    this.add(pnlClass, "pnlClassControls");
    this.add(pnlField, "fieldControls");
    this.add(pnlExecutableElement, "methodControls");
    this.add(jPnlBlank, "jPnlBlank");
    cardLayout.show(this,"jPnlBlank");
  }

  public void itemChanged(ItemChangedEvent ie) {
    CommentableCode currentCode = ie.getCurrentCode();
    this.pnlExecutableElement.doneEditing();
    this.pnlField.doneEditing();
    this.pnlClass.doneEditing();
    if (currentCode != null) {
      if (currentCode instanceof Method) {
        cardLayout.show(this,"methodControls");
        this.pnlExecutableElement.setCurrentComment(currentCode.getCodeComment());
      } // if Method
      if (currentCode instanceof Constructor) {
        cardLayout.show(this,"methodControls");
        this.pnlExecutableElement.setCurrentComment(currentCode.getCodeComment());
      } // if Method
      if (currentCode instanceof Field ) {
        cardLayout.show(this,"fieldControls");
        this.pnlField.setCurrentComment(currentCode.getCodeComment());
      } // if field
      if (currentCode  instanceof ElementContainer ) {
        cardLayout.show(this,"pnlClassControls");
        this.pnlClass.setCurrentComment(currentCode.getCodeComment());
      } // if class
    }
  }
}