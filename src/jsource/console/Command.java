package jsource.console;


/**
 * Console.java	07/08/03
 * Copyright (C) 1998-2003 Romain Guy (www.jext.org)
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


/**
 * The <code>Command</code> class is an empty implementation of a console
 * command. The commands list is a linked list.
 * @author Romain Guy
 * @author Panagiotis Plevrakis - JSource integration (7/2003)
 */
public abstract class Command {
    public Command next = null;

    /**
     * Return the command name. Displayed in console help summary.
     */
    public abstract String getCommandName();

    /**
     * Return the command summary. Displayed in console help summary.
     */
    public abstract String getCommandSummary();

    /**
     * Handles a command given by the console. If the command can be
     * handled, return true, false otherwise.
     */
    public abstract boolean handleCommand(Console console, String command);
}