package jsource.util;


/**
 * Log.java	04/16/03
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
import java.io.*;


/**
 * <code>Log</code> is a debugging class that provides one utility method.
 * The method takes an object of type Throwable as an argument and sends
 * the contents of printStackTrace to a file for better viewing.
 * Usage:
 * <pre>
 *        try {
 *	         ...
 *		  }
 *		  catch(Exception ex) {
 *		   	  Log.log(ex);
 *		  {
 * </pre>
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class Log implements JSConstants {

    private static PrintStream stream = null;

    // No Log objects can be created
    private Log() {}

    /**
     * The static method that logs all exceptions to a file.
     * @param e the Throwable object
     */
    public static void log(Throwable e) {
        try {
            stream = new PrintStream(new FileOutputStream("stacktrace.txt", true));
        } catch (IOException ex) {
            System.err.println(e);
        }
        e.printStackTrace(stream);
    }
}
