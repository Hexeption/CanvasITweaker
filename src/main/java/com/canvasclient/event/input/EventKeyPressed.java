package com.canvasclient.event.input;

import com.canvasclient.event.base.EventCancellable;

public class EventKeyPressed extends EventCancellable {
    private static final EventKeyPressed EVENT = new EventKeyPressed();
    public int key;

    public static EventKeyPressed get(int key) {
        EVENT.key = key;
        return EventKeyPressed.EVENT;
    }
}
