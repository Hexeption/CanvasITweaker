package com.canvasclient.utilities;

public class Timer {

	private long previousTime;
	
	public Timer() {
		previousTime = 0;
	}
	
	public boolean isTime(float time) {
		return currentTime() >= time * 1000L;
	}
	
	public float currentTime() {
		return systemTime() - previousTime;
	}
	
	public void reset() {
		previousTime = systemTime();
	}
	
	public long systemTime() {
		return System.currentTimeMillis();
	}
}
