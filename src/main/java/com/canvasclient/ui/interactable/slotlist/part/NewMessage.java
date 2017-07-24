package com.canvasclient.ui.interactable.slotlist.part;

import com.canvasclient.utilities.ActivityType;

public class NewMessage extends Activity{

	public NewMessage(String id, String iconLocation, String canvasName, String minecraftName, String ip, Message message, String partyID) {
		super(id, ActivityType.NEW_MESSAGE, iconLocation, canvasName, minecraftName, ip, message.message, partyID);
	}
}
