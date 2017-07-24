package com.canvasclient.friend.party;

import com.canvasclient.render.utilities.TextureImage;

import java.util.ArrayList;

public class Party {
	
	public ArrayList<PartyMessage> messages = new ArrayList();
	
	public String partyName;
	public int partyID;
	public String description;
	
	public TextureImage partyImage;
	
	public Party(int partyID, String partyName, ArrayList<PartyMessage> messages, String description) {
		this.partyName = partyName;
		this.partyID = partyID;
		this.messages = messages;
		this.description = description;
	}
	
	public static class PartyMessage {
		public String message;
		public String user;
		public String timeStamp;
		
		public PartyMessage(String user, String message, String timeStamp) {
			this.message = message;
			this.user = user;
			this.timeStamp = timeStamp;
		}
	}
}