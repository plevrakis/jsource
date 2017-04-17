package jsource.console;


/**
 * ExitCommand.java 07/08/03
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

import jsource.gui.MainFrame;
import jsource.io.localization.*;

/**
 * <code>ExitCommand</code> represents a command that exits JSource.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 * Portions Copyright (C) 1998-2003 Romain Guy (www.jext.org)
 */
public class ExitCommand extends Command {
    private static final String COMMAND_NAME = "exit";
    private MainFrame main = null;
    private XMLResourceBundle bundle = null;

    ExitCommand(MainFrame main, XMLResourceBundle bundle) {
        this.main = main;
        this.bundle = bundle;
    }

    public String getCommandName() {
        return COMMAND_NAME;
    }

    public String getCommandSummary() {
        return bundle.getValueOf("console.exit.command.help");
    }

    public boolean handleCommand(Console2 console, String command) {
        if (command.equalsIgnoreCase(COMMAND_NAME)) {
            main.closeSequence();
            return true;
        }

        return false;
    }
}
