package com.kiloclient.event.ui;

import com.kiloclient.event.base.Event;

public class EventTick implements Event {
	private static final EventTick INSTANCE = new EventTick();
	
	public static EventTick get() {
		return INSTANCE;
	}
}