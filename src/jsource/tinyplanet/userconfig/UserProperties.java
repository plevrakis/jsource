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
//	7/21/2001 lmm	- add some convenience methods for making setting and getting properties
//					easier when they are ints and booleans.

package com.tinyplanet.userconfig;

import java.util.Properties;
import java.io.IOException;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;

/** Class to facilitate saving and restoring a Java property file for program configuration.
 * This class can locate the appropriate directory for the property file for the user's
 * OS.
 *
 * @author Simon Arthur
 * @version $Id: UserProperties.java,v 1.2 2001/07/17 16:37:01 lmeador Exp $
 */
public class UserProperties extends Properties {

  /**    */
  String directoryName;
  /**    */
  static String fs = System.getProperty("file.separator");

  /**    */
  public UserProperties(String directoryName) {
    this.directoryName = directoryName;
  }

  /**    *
   * @exception IOException
   * @param fileName
   */
  public void storeProperties(String fileName) throws IOException {
    String fullFile = makeConfigDir(directoryName) + fs + fileName;
    OutputStream out = new FileOutputStream(fullFile);
    this.store(out,"");
  }

  /**    *
   * @exception IOException
   * @param fileName
   */
  public void loadProperties(String fileName) throws IOException {
    String fullFile = makeConfigDir(directoryName) + fs + fileName;
    InputStream in = new FileInputStream(fullFile);
    this.load(in);
  }

  /**    *
   * @exception IOException
   * @param directoryName
   */
  public static String makeConfigDir (String directoryName) throws IOException  {
    String homeDirectory = System.getProperty("user.home");
    String makeDirectoryName = directoryName;
    if (fs.equals("/")) {
      // a cheesy way to find out if the OS is Unix-like
      makeDirectoryName = "." + makeDirectoryName;
    }

    String homeDir = System.getProperty("user.home");
    String fullDir = homeDir + fs + makeDirectoryName;
    File f = new File (fullDir);
    if (!f.exists()) {
      f.mkdirs();
    }
    return fullDir;
  }

	/**
	 * Alternative method that does not take a String as the 2nd parm.
	 *
	 * @since 07/12/2001 3:34:57 PM
	 * @param key The key that tells which property to change.
	 * @param value The new value for the property.
	 * @return The old value of the property (as a String).
	 */
	public Object setProperty(String key, int value)
	{
		return this.setProperty(key, String.valueOf(value));
	}
  
	/**
	 * Alternative method that does not take a String as the 2nd parm.
	 *
	 * @since 07/12/2001 3:34:57 PM
	 * @param key The key that tells which property to change.
	 * @param value The new value for the property.
	 * @return The old value of the property (as a String).
	 */
	public Object setProperty(String key, boolean value)
	{
		return this.setProperty(key, (value) ? "1" : "0");
	}

	/**
	 * Alternative to getProperty method that returns a non-String.
	 *
	 * @since 07/12/2001 3:42:22 PM
	 * @param key The key that tells which property to change.
	 * @param defaultValue The value to give the property if not found.
	 * @return The value of the property converted to an int.
	 */
	public int getIntProperty(String key, int defaultValue)
	{
		String intVal = getProperty(key, defaultValue + "");
		try {
			if (intVal != null) {
				return Integer.parseInt(intVal.trim());
			}
			else {
				return defaultValue;
			}
		}
		catch (NumberFormatException nfe) {
			System.err.println("Cannot convert String '" + intVal + "' to integer in " +
			                   "properties file");
			return defaultValue;
		}
	}
  
	/**
	 * Alternative to getProperty method that returns a non-String.
	 *
	 * @since 07/12/2001 3:42:22 PM
	 * @param key The key that tells which property to change.
	 * @param defaultValue The value to give the property if not found.
	 * @return The value of the property converted to an boolean.
	 */
	public boolean getBooleanProperty(String key, boolean defaultValue)
	{
		String booleanVal = getProperty(key, (defaultValue) ? "1" : "0");
		try {
			if (booleanVal != null) {
				return (Integer.parseInt(booleanVal.trim()) != 0);
			}
			else {
				return defaultValue;
			}
		}
		catch (NumberFormatException nfe) {
			System.err.println("Cannot convert String '" + booleanVal + "' to integer in " +
			                   "properties file");
			return defaultValue;
		}
	}
  

}
