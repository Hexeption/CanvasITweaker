package com.canvasclient.event.input;

import com.canvasclient.event.base.EventCancellable;

public class EventClick extends EventCancellable {
    private static final EventClick EVENT = new EventClick();
    public int k;

    public static EventClick get(int k) {
    	EVENT.setCancelled(false);
        EVENT.k = k;
        return EventClick.EVENT;
    }
}
