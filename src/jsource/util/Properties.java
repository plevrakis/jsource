package jsource.util;


/**
 * Properties.java  06/19/03
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
import java.awt.*;
import java.util.*;
import jsource.gui.MessageFrame;
import jsource.io.*;


public class Properties {

	private String lastFile = null;
	private String lastDir = "";
	private String classPath = ".";
	private String rootPath = "";
	private String commandLine = "";
	private String sdkpath = null;
	private String antpath = null;
	private String browser = null;
	private String coreapi = null;
	private String projectName = null;
	private String driverClass = "";
	private String openFiles = "";
	private boolean isApplet = false;
	private Dimension size = null;
	private Point location = null;

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String[] getClassPathArray() {
        String cp = getClassPath();
        StringTokenizer tokenizer = new StringTokenizer(cp, File.pathSeparator);
        String[] lines = new String[tokenizer.countTokens()];

        for (int i = 0; i < lines.length; i++)
            lines[i] = tokenizer.nextToken();
        return lines;
    }

    public String[] getEnvironmentArray(String fileName) {
        Vector vector = new Vector();

        if (getVerbose())
            vector.addElement("-verbose");
        if (getDeprecation())
            vector.addElement("-deprecation");
        if (getNoWarn())
            vector.addElement("-nowarn");
        vector.addElement("-classpath");
        vector.addElement(getRootPath() + File.pathSeparator + getClassPath());
        vector.addElement(fileName);
        //String dir = getOutputDirectory();

        //if (dir != null && dir.length() > 0) {
        //    vector.addElement("-d");
        //    vector.addElement(dir);
        //}
        String[] lines = new String[vector.size()];

        for (int i = 0; i < lines.length; i++)
            lines[i] = (String) vector.elementAt(i);
        return lines;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getDriver() {
		return driverClass;
    }

    public void setDriver(String driverClass) {
		this.driverClass = driverClass;
    }

    public String getLastFile() {
        return lastFile;
	}

    public void setLastFile(String lastFile) {
        this.lastFile = lastFile;
    }

    public String getLastDirectory() {
        return lastDir;
    }

    public void setLastDirectory(String lastDir) {
        this.lastDir = lastDir;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    public boolean getVerbose() {
		return false;
        //return new Boolean(properties.getProperty("Environment.verbose", "false")).booleanValue();
    }

    public void setVerbose(boolean b) {
        //properties.setProperty("Environment.verbose", new Boolean(b).toString());
    }

    public boolean getDeprecation() {
		return false;
        //return new Boolean(properties.getProperty("Environment.deprecation", "false")).booleanValue();
    }

    public void setDeprecation(boolean b) {
       // properties.setProperty("Environment.deprecation", new Boolean(b).toString());
    }

    public boolean getNoWarn() {
		return false;
       // return new Boolean(properties.getProperty("Environment.warn", "false")).booleanValue();
    }

    public void setNoWarn(boolean b) {
        //properties.setProperty("Environment.warn", new Boolean(b).toString());
    }

    public boolean getShowCommandLine() {
		return false;
       // return new Boolean(properties.getProperty("Environment.showCommandLine", "false")).booleanValue();
    }

    public void setShowCommandLine(boolean b) {
       // properties.setProperty("Environment.showCommandLine", new Boolean(b).toString());
    }

    public boolean getInternalCompile() {
		return false;
       // return new Boolean(properties.getProperty("Environment.internalCompile", "false")).booleanValue();
    }

    public void setInternalCompile(boolean b) {
       // properties.setProperty("Environment.internalCompile", new Boolean(b).toString());
    }

    public boolean getApplet() {
		return isApplet;
    }

    public void setApplet(boolean isApplet) {
    	this.isApplet = isApplet;
    }

    public String[] getOpenFiles() {
        String str = openFiles;
        StringTokenizer tok = new StringTokenizer(str, ",");
        int size = tok.countTokens();
        int i = 0;
        String[] array = new String[size];

        while (tok.hasMoreTokens()) {
            array[i] = tok.nextToken();
            i++;
        }
        return array;
    }

    public void setOpenFiles(String[] array) {
        StringBuffer buff = new StringBuffer();

        for (int i = 0; i < array.length; i++) {
            buff.append(array[i] + ",");
        }
        if (buff.length() > 0)
            buff.setLength(buff.length() - 1);
        openFiles = buff.toString();
    }

    public File getAppletFile() {
		return null;
    }

    public void setAppletFile(File f) {

    }

    public void load(String fileName) throws IOException {

    }

	public String getProjectName() {
		if (projectName == null)
			return "default";
		else
			return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSDK() {
		if (sdkpath == null)
			return "Undefined";
		else
			return sdkpath;
	}

	public void setSDK(String sdkpath) {
		this.sdkpath = sdkpath;
	}

 	public String getANT() {
		if (antpath == null)
			return "Undefined";
		else
			return antpath;
	}

	public void setANT(String antpath) {
		this.antpath = antpath;
	}

 	public String getBrowser() {
		if (browser == null)
			return "Undefined";
		else
			return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

 	public String getCoreAPI() {
		if (coreapi == null)
			return "Undefined";
		else
			return coreapi;
	}

	public void setCoreAPI(String coreapi) {
		this.coreapi = coreapi;
	}

	public void setMainFrameSize(Dimension size) {
		this.size = size;
	}

	public Dimension getMainFrameSize() {
		return size;
	}

	public void setMainFrameLocation(Point location) {
		this.location = location;
	}

	public Point getMainFrameLocation() {
		return location;
	}
}

