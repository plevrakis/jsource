package jsource.io.localization;


/**
 * ResourceKey.java	12/28/02
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
 * <code>ResourceKey</code> is the object that keeps information
 * about both the resource name and the data type.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class ResourceKey implements Comparable {

    protected String key, type;

    public ResourceKey(String key, String type) {
        this.key = key;
        this.type = type;
    }

    public String toString() {
        return key + '/' + type;
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }

    public int compareTo(Object obj) {
        if (obj instanceof ResourceKey) {
            ResourceKey other = (ResourceKey) obj;
            int response = key.compareTo(other.key);

            if (response == 0) {
                response = type.compareTo(other.type);
            }
            return response;
        }
        return 1;
    }
}

