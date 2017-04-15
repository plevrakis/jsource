package jsource.io.localization;


/**
 * ResourceCache.java 12/28/02
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
import java.util.*;


/**
 * <code>ResourceCache</code> is a HashMap that requires a ResourceKey for each entry.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class ResourceCache extends HashMap {
    public String getResource(String name, String type) {
        ResourceKey key = new ResourceKey(name, type);

        if (!containsKey(key)) {
            throw new IllegalArgumentException("no such key: " + key);
        }
        return (String) get(key);
    }
}

