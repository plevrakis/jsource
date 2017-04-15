package jsource.util;


/**
 * JSConstants.java	12/18/02
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
import java.awt.*;
import javax.swing.*;
import java.util.*;


/**
 * <code>JSConstants</code> holds constants used by all JSource packages
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public interface JSConstants {
    public final static int INDENT = 4;
    public final static String OS_NAME = System.getProperty("os.name");
    public final static String OS_ARCH = System.getProperty("os.arch");
    public final static String OS_VERSION = System.getProperty("os.version");
    public final static String CLASS_PATH = System.getProperty("java.class.path");
    public final static String USER_DIR = System.getProperty("user.dir");
    public final static String USER_NAME = System.getProperty("user.name");
    public final static String USER_HOME = System.getProperty("user.home");
    public final static String FILE_SEP = System.getProperty("file.separator");
    public final static String LINE_SEP = System.getProperty("line.separator");
    public final static String PATH_SEP = System.getProperty("path.separator");
    public final static Toolkit TOOLKIT = Toolkit.getDefaultToolkit();
    public final static Runtime RUNTIME = Runtime.getRuntime();
    public final static Dimension SCREEN_SIZE = TOOLKIT.getScreenSize();
    public final static String JAVA_TOOL_PATH = "jsource" + FILE_SEP + "tools" + FILE_SEP;
    public final static String IMAGE_PATH = "jsource" + FILE_SEP + "images" + FILE_SEP;
    public final static String TEMPLATE_PATH = "jsource" + FILE_SEP + "templates" + FILE_SEP;
    public final static String API_PATH = "jsource" + FILE_SEP + "api" + FILE_SEP;
    public final static String BUNDLE_PATH = "jsource" + FILE_SEP + "io"
            + FILE_SEP + "localization" + FILE_SEP + "bundles" + FILE_SEP
            + "ResourceBundle";
    public final static String JAVA_HOME = System.getProperty("java.home");
    public final static String JAVA_VERSION = System.getProperty("java.version");
    public final static String JAVA_VM = System.getProperty("java.vm.name");
    public final static String JAVA_VM_VERSION = System.getProperty("java.vm.version");
    public final static String SYS_LANG = System.getProperty("user.language");
    public final static String PROPERTIES = "jsource" + FILE_SEP + "io" + FILE_SEP + "properties.xml";
    public final static Locale[] LOCALES = { Locale.ENGLISH, Locale.FRENCH, Locale.ITALIAN };
    public final static Font EDITOR_FONT = new Font("Courier", Font.PLAIN, 14);
    public final static Font TERMINAL_FONT = new Font("Courier", Font.PLAIN, 12);
    public final static String RELEASE = "2.0";
}
