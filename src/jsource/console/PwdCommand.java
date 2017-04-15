package jsource.console;


/**
 * PwdCommand.java 07/08/03
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

import jsource.io.localization.*;

/**
 * <code>PwdCommand</code> represents a command that
 * displays the current working directory.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class PwdCommand extends Command {
    private static final String COMMAND_NAME = "pwd";
    private XMLResourceBundle bundle = null;

    PwdCommand(XMLResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getCommandName() {
        return COMMAND_NAME;
    }

    public String getCommandSummary() {
        return bundle.getValueOf("console.pwd.command.help");
    }

    public boolean handleCommand(Console console, String command) {
        if (command.equalsIgnoreCase(COMMAND_NAME)) {
            console.output(System.getProperty("user.dir"));
            return true;
        }

        return false;
    }
}
