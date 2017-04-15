package jsource.console;


/**
 * ChangeDirCommand.java 07/08/03
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
import jsource.util.Utilities;
import jsource.io.localization.*;

/**
 * <code>ChangeDirCommand</code> represents a command that changes the current directory.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class ChangeDirCommand extends Command {
    private static final String COMMAND_NAME = "cd";
    private XMLResourceBundle bundle = null;

    ChangeDirCommand(XMLResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getCommandName() {
        return COMMAND_NAME + " <path>";
    }

    public String getCommandSummary() {
        return bundle.getValueOf("console.cd.command.help");
    }

    public boolean handleCommand(Console console, String command) {
        if (command.equalsIgnoreCase(COMMAND_NAME) || command.equalsIgnoreCase(COMMAND_NAME + " -help")) {
            console.help(bundle.getValueOf("console.cd.help"));
            return true;
        } else if (command.startsWith(COMMAND_NAME + " ")) {
            String newPath = Utilities.constructPath(command.substring(3));

            if ((new File(newPath)).exists()) {
                System.getProperties().put("user.dir", newPath);
			}
            else {
                console.error(bundle.getValueOf("console.cd.error"));
		    }
            return true;
        } else if (command.startsWith(COMMAND_NAME + "..")) {
            String newPath = Utilities.constructPath(COMMAND_NAME + "..");

            if ((new File(newPath)).exists()) {
                System.getProperties().put("user.dir", newPath);
			}
            else {
                console.error(bundle.getValueOf("console.cd.error"));
		    }
            return true;
        } else if (command.startsWith(COMMAND_NAME + '~')) {
            String newPath = Utilities.constructPath(COMMAND_NAME + '~');

            if ((new File(newPath)).exists()) {
                System.getProperties().put("user.dir", newPath);
			}
            else {
                console.error(bundle.getValueOf("console.cd.error"));
		    }
            return true;
        }

        return false;
    }
}
