package com.kiloclient.logger;

public class Logger {
	
	public void log(String message, LogType type) {
		String defaultTag = "[KiLO] ";
		switch(type) {
		case INFO:
			System.out.println(defaultTag + "[INFO] " + message);
		case WARNING:
			System.out.println(defaultTag + "[WANING] " + message);
		case ERROR:
			System.err.println(defaultTag + "[ERROR] " + message);
		}
	}
	
	public void log(String message, LogType type, Throwable throwable) {
		log(message, type);
		throwable.printStackTrace();
	}
	
	public static enum LogType {
		INFO, WARNING, ERROR;
	}
	
}
