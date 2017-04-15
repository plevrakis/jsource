package jsource.io.localization;


/**
 * XMLResourceBundle.java 12/28/02
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
import java.util.*;


/**
 * <code>XMLResourceBundle</code> loads a HashMap with values associated with
 * localized keys. More specific locale information is prefered, and overwritten
 * as it's processed. Deeper XML values are more specific.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class XMLResourceBundle {
    protected XMLBundleParser parser;
    protected ResourceCache cache;
    protected String filename;

	/**
	 * Creates a <code>XMLResourceBundle</code> object using as resource the given file name
	 */
    public XMLResourceBundle(String filename) {
        parser = new XMLBundleParser();
        this.filename = filename;
    }

    public void loadFile(Locale locale) throws Exception {
        cache = new ResourceCache();
        String[] fileList = LocaleUtil.getFileNameList(locale, filename, ".xml");

        for (int i = 0; i < fileList.length; i++) {
            parseFile(cache, fileList[i], locale);
        }
    }

    protected void parseFile(ResourceCache cache, String filename, Locale locale) throws Exception {
        File file = new File(filename);

        if (file.exists()) {
            parser.parse(cache, filename, locale);
        }
    }

    public String getValueOf(String key) {
        return cache.getResource(key, "String");
    }

    public File getFile(String key) {
        String file = cache.getResource(key, "File");

        return new File(file);
    }
}

