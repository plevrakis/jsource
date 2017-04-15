package jsource;


/**
 * JSourceLoader.java	12/17/02
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 */

import jsource.gui.SplashScreen;
import jsource.util.GCManager;

/**
 * <code>JSourceLoader</code> is the driver class that launches
 * JSource IDE and schedules the garbage collector.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class JSourceLoader {
    public static void main(String[]args) {
		new SplashScreen();
    }
}
