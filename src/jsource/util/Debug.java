package jsource.util;


/**
 * Debug.java  05/10/03
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

/**
 * <code>Debug</code> is just used for debugging purposes during JSource development.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class Debug {

    public static final boolean DEBUG = true;

    /**
     * Returns the total amount of memory used.
     */
    public static long reportMemoryUse() {
        Runtime run = JSConstants.RUNTIME;

        return run.totalMemory() - run.freeMemory();
    }
}
