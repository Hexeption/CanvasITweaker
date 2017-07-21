package com.kiloclient.event.input;

import com.kiloclient.event.base.EventCancellable;

public class EventClick extends EventCancellable {
    private static final EventClick EVENT = new EventClick();
    public int k;

    public static EventClick get(int k) {
    	EVENT.setCancelled(false);
        EVENT.k = k;
        return EventClick.EVENT;
    }
}
