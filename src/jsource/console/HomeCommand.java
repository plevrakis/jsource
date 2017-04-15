package jsource.console;


/**
 * HomeCommand.java 07/08/03
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

import jsource.util.JSConstants;
import jsource.io.localization.*;

/**
 * <code>HomeCommand</code> represents a command that
 * places the prompt back to the JSource home directory.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class HomeCommand extends Command {
    private static final String COMMAND_NAME = "home";
    private XMLResourceBundle bundle = null;

    HomeCommand(XMLResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getCommandName() {
        return COMMAND_NAME;
    }

    public String getCommandSummary() {
        return bundle.getValueOf("console.home.command.help");
    }

    public boolean handleCommand(Console console, String command) {
        if (command.equalsIgnoreCase(COMMAND_NAME)) {
            System.getProperties().put("user.dir", JSConstants.USER_HOME);
            return true;
        }

        return false;
    }
}
