/*
DocWiz: Easy Javadoc documentation
Copyright (C) 2000 Simon Arthur

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

// History
//	5/2/2001 Lee Meador - Add the stuff to fire debug messages through here.
//  5/21/2001 Lee Meador - Add some javadocs. Pretty up the code format.

package tinyplanet.docwiz;
import java.util.*;

/**
 * Tracks all listeners for changes to current selection and current compilation unit.
 * Any class which wants to receive notification of a change in CompilationUnits or
 * selected commentable code should register with this class using addItemChangedListener.
 * Also tracks any listeners that want to get debug messages and provides a global way to
 * send those events.
 *
 * An additional function allows code to send debug messages to appear in an alternate
 * pane as they occur.
 *
 * @boo far
 * @foo bar
 */
public class ChangeDispatcher
{
	/**
	 * Object for singleton usage of ChangeDispatcher
	 */
	private static ChangeDispatcher theChangeDispatcher;
	/**
	 * List of all current listeners for changes
	 */
	private List ItemChangedListeners = new LinkedList();


	private List debugMessageListeners = new LinkedList();

	/**
	 * The CompilationUnit which is being tracked for changes.
	 */
	CompilationUnit currentUnit;

	/**
	 * Private to prevent direct instantiation. Please use getChangeDispatcher().
	 */
	private ChangeDispatcher()
	{
	}

	/**
	 * Get an instance of the ChangeDispatcher.
	 *
	 * @return The current ChangeDispatcher.
	 */
	public static synchronized ChangeDispatcher getChangeDispatcher()
	{
		if (theChangeDispatcher == null) {
			theChangeDispatcher = new ChangeDispatcher();
		}
		return theChangeDispatcher;
	}

	/**
	 * Register the provided ItemChangedListener as a listener for ItemChanged
	 * events.
	 *
	 * @param is The ItemChangedListener to register.
	 */
	public void addItemChangedListener(ItemChangedListener is)
	{
		ItemChangedListeners.add(is);
	}

	/**
	 * true when there is a change in process. Used to avoid nested firings.
	 */
	private boolean nowFiringItemChanged = false;

	/**
	 * Dispatch ItemChangedEvents to all registered listeners.
	 *
	 * Any code which changes the selected item (method, constructor, field or class) or the CompilationUnit
	 * should call this method.
	 *
	 * @param cu The CompilationUnit object for the entire java file with all
	 *     methods, etc.
	 * @param cc The CommentableCode (method, constructor, field or class)
	 *     that is becoming current.
	 */
	public void fireItemChanged(CompilationUnit cu, CommentableCode cc)
	{
		if (nowFiringItemChanged) {			/* Don't let these nest */
			return ;
		}

		nowFiringItemChanged = true;
		try {
			for (int i = 0 ; i < ItemChangedListeners.size() ; i++) {
				Object o = ItemChangedListeners.get(i);
				if (o != null) {
					ItemChangedListener is = (ItemChangedListener) o;
					is.itemChanged(new ItemChangedEvent(cu, cc));
					g.debug("ChangeDispatcher: notifying " + i);
				}
			}
			nowFiringItemChanged = false;
		}
		catch (RuntimeException e) {
			nowFiringItemChanged = false;
			throw e;
		}
	}


	/**
	 * Register the provided DebugMessageListener as a listener for DebugMessage
	 * events.
	 *
	 * @param is The DebugMessageListener to register.
	 */
	public void addDebugMessageListener(DebugMessageListener is)
	{
		debugMessageListeners.add(is);
	}

	/**
	 * Dispatch DebugMessageEvents to all registered listeners.
	 *
	 * @param message The debug message to send.
	 */
	public void fireDebugMessage(String message)
	{

		// When debugging is turned off, show no messages
		ConfigurationService configurationService = ConfigurationService.getConfigurationService();
		if (!configurationService.getDebug()) {
			return ;
		}

		for (int i = 0 ; i < debugMessageListeners.size() ; i++) {
			Object o = debugMessageListeners.get(i);
			if (o != null) {
				DebugMessageListener is = (DebugMessageListener) o;
				is.debugMessage(new DebugMessageEvent(message));
			}
		}
	}


}


