package jsource.util;


/**
 * UnexpectedException.java	03/31/03
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
 * An <code>UnexpectedException</code> may be thrown in <code>FindReplaceMachine</code> methods
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 *
 * @see FindReplaceMachine
 */
public class UnexpectedException extends RuntimeException {

    private Throwable value = null;

    public UnexpectedException(Throwable value) {
        super(value.toString());
        this.value = value;
    }

    public UnexpectedException(Throwable value, String msg) {
        super(msg + ": " + value.toString());
        this.value = value;
    }

    public Throwable getContainedThrowable() {
        return value;
    }
}
