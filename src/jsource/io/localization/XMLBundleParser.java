package jsource.io.localization;


/**
 * XMLBundleParser.java	12/28/02
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
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;


/**
 * <code>XMLBundleParser</code> is the object that handles parsing of the XML source documents.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class XMLBundleParser extends DefaultHandler {
    protected ResourceCache cache = new ResourceCache();
    protected String parserLanguage, parserCountry, parserVariant;
    protected String targetLanguage, targetCountry, targetVariant;
    protected StringBuffer buffer = new StringBuffer();
    protected ResourceKey key;

    public void parse(ResourceCache cache,String filename, Locale targetLocale) throws Exception {
        this.cache = cache;
        targetLanguage = targetLocale.getLanguage();
        targetCountry = targetLocale.getCountry();
        targetVariant = targetLocale.getVariant();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();

        reader.setContentHandler(this);
        reader.parse(filename);
    }

    protected boolean inContext() {
        if (parserLanguage == null || parserLanguage.equals(targetLanguage)) {
            if (
                    parserCountry == null || parserCountry.equals(targetCountry)) {
                if (parserVariant == null || parserVariant.equals(targetVariant)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void characters(char[] chars, int offset, int length) {
        buffer.append(chars, offset, length);
    }

    public void startElement(
            String uri, String lName, String qName,
            Attributes attrs) {
        if (qName.equals("LANGUAGE")) {
            parserLanguage = attrs.getValue("name");
        }
        if (qName.equals("COUNTRY")) {
            parserCountry = attrs.getValue("name");
        }
        if (qName.equals("VARIANT")) {
            parserVariant = attrs.getValue("name");
        }
        if (qName.equals("RES")) {
            key = new ResourceKey(
                    attrs.getValue("key"),
                    attrs.getValue("type"));
        }
    }

    public void endElement(
            String uri, String lName, String qName) {
        String content = buffer.toString().trim();

        buffer.setLength(0);
        if (qName.equals("LANGUAGE")) {
            parserLanguage = null;
        }
        if (qName.equals("COUNTRY")) {
            parserCountry = null;
        }
        if (qName.equals("VARIANT")) {
            parserVariant = null;
        }
        if (qName.equals("RES") && inContext()) {
            cache.put(key, content);
        }
    }
}

