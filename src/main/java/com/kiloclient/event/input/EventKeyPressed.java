package com.kiloclient.event.input;

import com.kiloclient.event.base.EventCancellable;

public class EventKeyPressed extends EventCancellable {
    private static final EventKeyPressed EVENT = new EventKeyPressed();
    public int key;

    public static EventKeyPressed get(int key) {
        EVENT.key = key;
        return EventKeyPressed.EVENT;
    }
}
