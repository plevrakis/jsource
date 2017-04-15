 package jsource.util;


/**
 * GCManager.java	07/12/03
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

import java.util.TimerTask;
import java.util.Timer;

/**
 * <code>GCManager</code> is a garbage collector scheduler.
 *
 * @author Panagiotis Plevrakis
 * <br>Email: pplevrakis@hotmail.com
 * <br>URL:   http://jsource.sourceforge.net
 */
public class GCManager {

    private static boolean done = false;

    public static void suggestGCNow() {
        System.gc();
    }

    private class GCStimulatorTask extends TimerTask {
        public void run() {
            suggestGCNow();
        }
    }

    private static GCStimulatorTask instance = null;

    private synchronized GCStimulatorTask getInstance() {
        if (instance == null) {
            instance = new GCStimulatorTask();
        }
        return instance;
    }

    public void scheduleRegularGC(long intervalMilliSecs) {
        if (!done) { // only schedule 1 garbage collector
            GCStimulatorTask stimulator = getInstance();
            Timer scheduler = new Timer();
			System.out.println("1");
            scheduler.scheduleAtFixedRate(stimulator, 10, intervalMilliSecs);
            done = true;
            System.out.println("2");
        } else {
            System.err.println("GC Task already scheduled.");
        }
    }

    public GCManager() {}
}
