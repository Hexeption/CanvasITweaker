package com.kiloclient.event.boot;


import com.kiloclient.event.base.Event;

public class EventShutdown implements Event {
    private static final EventShutdown EVENT = new EventShutdown();

    public static EventShutdown get() {
        return EventShutdown.EVENT;
    }
}
