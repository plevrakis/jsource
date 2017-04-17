package jsource.console;


/**
 * ListCommand.java 07/08/03
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
 * <code>ListCommand</code> represents a command that lists the contents
 * of the current directory Both "ls" and "dir" produce the same result.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class ListCommand extends Command {
    private static final String COMMAND_NAME = "ls";
    private static final String COMMAND_NAME_ALTERNATE = "dir";
    private XMLResourceBundle bundle = null;

    ListCommand(XMLResourceBundle bundle) {
        this.bundle = bundle;
        ConsoleListDir.setXMLResourceBundle(bundle);
    }

    public String getCommandName() {
        return COMMAND_NAME;
    }

    public String getCommandSummary() {
        return bundle.getValueOf("console.ls.command.help");
    }

    public boolean handleCommand(Console2 console, String command) {
        if (command.equalsIgnoreCase(COMMAND_NAME)
                || command.equalsIgnoreCase(COMMAND_NAME_ALTERNATE)) {
            ConsoleListDir.list(console, null);
            return true;
        } else if (command.startsWith(COMMAND_NAME)
                || command.startsWith(COMMAND_NAME_ALTERNATE)) {
            ConsoleListDir.list(console, command.substring(2));
            return true;
        }

        return false;
    }
}
