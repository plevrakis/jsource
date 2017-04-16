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
package com.tinyplanet.gui.demo;

import com.tinyplanet.gui.*;

public class GoDemo {



  public static void main(String[] args) {
    ConcreteRack r = new ConcreteRack();
    r.setVisible(true);
    Shelf[] s = r.getShelfs();
    for (int i = 0; i< s.length; i++) {
      System.out.println("GD: about to split down");
      r.splitLeft(s[i]);
    }
    s = r.getShelfs();
    for (int i = 0; i< s.length; i++) {
      System.out.println("demo: about to splitLeft");
      r.splitDown(s[i]);
    }
    s = r.getShelfs();
    for (int i = 0; i< s.length-1; i++) {
      s[i].addPane(new com.tinyplanet.gui.demo.HelloWorld());
    }
    //r.getRootPane().revalidate();
  }

}