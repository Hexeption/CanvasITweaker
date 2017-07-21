package com.kiloclient.event.boot;

import com.kiloclient.event.base.EventCancellable;

public class EventStartup extends EventCancellable {
    private static final EventStartup EVENT = new EventStartup();

    public static EventStartup get() {
        return EventStartup.EVENT;
    }
}
