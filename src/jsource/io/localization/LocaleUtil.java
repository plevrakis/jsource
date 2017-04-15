package jsource.io.localization;


/**
 * LocaleUtilities.java	12/28/02
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
 * <code>LocaleUtilities</code> is a collection of static methods
 * that help construct filenames from Locale objects.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class LocaleUtil {
    protected static final char DELIMITER = '_';

    public static String getFileName(Locale locale,
            String prefix, String suffix, int depth) {
        StringBuffer buffer = new StringBuffer(prefix);

        if (depth > 0) {
            String language = locale.getLanguage();

            if (!language.equals("")) {
                buffer.append(DELIMITER);
                buffer.append(language);
            }
        }
        if (depth > 1) {
            String country = locale.getCountry();

            if (!country.equals("")) {
                buffer.append(DELIMITER);
                buffer.append(country);
            }
        }
        if (depth > 2) {
            String variant = locale.getVariant();

            if (!variant.equals("")) {
                buffer.append(DELIMITER);
                buffer.append(variant);
            }
        }
        return buffer.toString() + suffix;
    }

    public static String[] getFileNameList(
            Locale locale, String prefix, String suffix) {
        List list = new ArrayList();

        for (int i = 0; i < 3; i++) {
            String filename = getFileName(
                    locale, prefix, suffix, i);

            if (!list.contains(filename)) {
                list.add(filename);
            }
        }
        String[] array = new String[list.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = (String) list.get(i);
        }
        return array;
    }

    public static String[] splitName(String name) {
        StringTokenizer tokenizer = new StringTokenizer(
                name, "" + DELIMITER, false);
        int count = tokenizer.countTokens();
        String[] array = new String[count];

        for (int i = 0; i < count; i++) {
            array[i] = tokenizer.nextToken();
        }
        return array;
    }
}

