package jsource.util;


/**
 * FindResult.java	03/31/03
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
 * A <code>FindResult</code> object is used in a <code>FindReplaceMachine</code>
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 *
 * @see FindReplaceMachine
 */
public class FindResult {

    private int _foundoffset;
    private boolean _wrapped;

    public FindResult(int foundoffset, boolean wrapped) {
        _foundoffset = foundoffset;
        _wrapped = wrapped;
    }

    public int getFoundOffset() {
        return _foundoffset;
    }

    public boolean getWrapped() {
        return _wrapped;
    }
}
