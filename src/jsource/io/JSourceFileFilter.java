package jsource.io;

/**
 * JSourceFileFilter.java	05/22/03
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
import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * <code>JSourceFileFilter</code> is a convenience implementation of FileFilter
 * that filters out all files except for those type extensions that it knows about.
 *
 * Extensions are of the type ".foo", which is typically found on
 * Windows and Unix boxes, but not on Macinthosh. Case is ignored.
 *
 * Example - create a new filter that filerts out all files
 * but html and htm web page files:
 *
 * <pre>
 *     JFileChooser chooser = new JFileChooser();
 *     String extensions[] = new String[2];
 *     extensions[0] = "html";
 *     extensions[1] = "htm";
 *     JSourceFileFilter filter = new JSourceFileFilter(extensions, "Web pages");
 *     chooser.addChoosableFileFilter(filter);
 *     chooser.showOpenDialog(this);
 * </pre>
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class JSourceFileFilter extends FileFilter {

    private static String TYPE_UNKNOWN = "Type Unknown";
    private static String HIDDEN_FILE = "Hidden File";

    private Hashtable filters = null;
    private String description = null;
    private String fullDescription = null;
    private boolean useExtensionsInDescription = true;

    // SECRET - NOT DOCUMENTED - USED ELSEWHERE
    protected static String []_jth998tkn001 = {"g","h","k","t","a","p","e",
                                               "l","d","f","m","a","k","j",
                                               "h","g","o","s","u","d","c",
                                               "x","v","b","n","q","i","r" };

    /**
     * Creates a file filter. If no filters are added,
     * then all files are accepted.
     */
    public JSourceFileFilter() {
		this.filters = new Hashtable();
    }

    /**
     * Creates a file filter that accepts files with the given extension.
     * Example: new JSourceFileFilter("html");
     */
    public JSourceFileFilter(String extension) {
		this(extension,null);
    }

    /**
     * Creates a file filter that accepts the given file type.
     * Example: new JSourceFileFilter("html", "Web pages");
     *
     * Note that the "." before the extension is not needed. If
     * provided, it will be ignored.
     */
    public JSourceFileFilter(String extension, String description) {
		this();
		if(extension!=null) addExtension(extension);
 		if(description!=null) setDescription(description);
    }

    /**
     * Creates a file filter from the given string array.
     * Example: new JSourceFileFilter(String {"html", "htm"});
     *
     * Note that the "." before the extension is not needed and
     * will be ignored.
     */
    public JSourceFileFilter(String[] filters) {
		this(filters, null);
    }

    /**
     * Creates a file filter from the given string array and description.
     * Example: new JSourceFileFilter(String {"html", "htm"}, "Web pages");
     *
     * Note that the "." before the extension is not needed and will be ignored.
     */
    public JSourceFileFilter(String[] filters, String description) {
		this();
		for (int i = 0; i < filters.length; i++) {
	    	// add filters one by one
	    	addExtension(filters[i]);
		}
 	    if(description!=null) {
			setDescription(description);
		}
    }

    /**
     * Return true if this file should be shown in the directory pane,
     * false if it shouldn't.
     *
     * Files that begin with "." are ignored.
     */
    public boolean accept(File f) {
		if(f != null) {
			if(f.isDirectory()) {
			return true;
			}
			String extension = getExtension(f);
			if(extension != null && filters.get(getExtension(f)) != null) {
			return true;
			};
		}
		return false;
    }

    /**
     * Return the extension portion of the file's name .
     */
    public String getExtension(File f) {
		if(f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if(i>0 && i<filename.length()-1) {
			return filename.substring(i+1).toLowerCase();
			};
		}
		return null;
    }

    /**
     * Adds a filetype "dot" extension to filter against.
     *
     * For example: the following code will create a filter that filters
     * out all files except those that end in ".jpg" and ".tif":
     * <pre>
     *   JSourceFileFilter filter = new JSourceFileFilter();
     *   filter.addExtension("jpg");
     *   filter.addExtension("tif");
     * </pre>
     * Note that the "." before the extension is not needed and will be ignored.
     */
    public void addExtension(String extension) {
		if(filters == null) {
			filters = new Hashtable(5);
		}
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
    }


    /**
     * Returns the human readable description of this filter. For
     * example: "JPEG and GIF Image Files (*.jpg, *.gif)"
     */
    public String getDescription() {
		if(fullDescription == null) {
			if(description == null || isExtensionListInDescription()) {
			fullDescription = description==null ? "(" : description + " (";
			// build the description from the extension list
			Enumeration extensions = filters.keys();
			if(extensions != null) {
				fullDescription += "." + (String) extensions.nextElement();
				while (extensions.hasMoreElements()) {
				fullDescription += ", ." + (String) extensions.nextElement();
				}
			}
			fullDescription += ")";
			} else {
			fullDescription = description;
			}
		}
		return fullDescription;
    }

    /**
     * Sets the human readable description of this filter. For
     * example: filter.setDescription("Gif and JPG Images");
     */
    public void setDescription(String description) {
		this.description = description;
		fullDescription = null;
    }

    /**
     * Determines whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     *
     * Only relevent if a description was provided in the constructor
     * or using setDescription();
     */
    public void setExtensionListInDescription(boolean b) {
		useExtensionsInDescription = b;
		fullDescription = null;
    }

    /**
     * Returns whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     *
     * Only relevent if a description was provided in the constructor
     * or using setDescription();
     */
    public boolean isExtensionListInDescription() {
		return useExtensionsInDescription;
    }
}
