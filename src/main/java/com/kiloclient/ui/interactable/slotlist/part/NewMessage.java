package com.kiloclient.ui.interactable.slotlist.part;

import com.kiloclient.utilities.ActivityType;

public class NewMessage extends Activity{

	public NewMessage(String id, String iconLocation, String kiloName, String minecraftName, String ip, Message message, String partyID) {
		super(id, ActivityType.NEW_MESSAGE, iconLocation, kiloName, minecraftName, ip, message.message, partyID);
	}
}
