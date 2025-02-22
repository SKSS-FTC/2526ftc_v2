package org.nknsd.teamcode.helperClasses;

import java.util.concurrent.Callable;

public class EventPair {
    public final Callable<Boolean> listener;
    public final Runnable event;
    public final String name;

    public EventPair(Callable<Boolean> listener, Runnable event, String name) {
        this.listener = listener;
        this.event = event;
        this.name = name;
    }

    public EventPair(Callable<Boolean> listener, String name) {
        this.listener = listener;
        this.event = new Runnable() {@Override public void run() {}};
        this.name = name;
    }

    public boolean isEqualTo(String name) {
        return this.name.equals(name);
    }
}
