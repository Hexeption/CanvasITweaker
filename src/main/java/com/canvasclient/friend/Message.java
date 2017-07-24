package com.canvasclient.friend;

public class Message {
	
	private String message;
	
	private int type;
	
	public Message (String message, int type) {
		this.message = message;
		this.type = type;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getType() {
		return this.type;
	}
	
}
