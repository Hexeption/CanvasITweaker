package com.kiloclient.ui.interactable.slotlist.part;

import com.kiloclient.render.utilities.TextureImage;
import com.kiloclient.utilities.ActivityType;
import com.kiloclient.resource.ResourceHelper;

public class Activity {

	public String id;
	public ActivityType type;
	public String kiloName, minecraftName, ip, message, partyID;
	public TextureImage icon;

	public Activity(String id, ActivityType type, String iconURL, String kiloName, String minecraftName, String ip, String message, String partyID) {
		this.id = id;
		this.type = type;
		this.icon = ResourceHelper.downloadTexture(iconURL);
		this.kiloName = kiloName;
		this.minecraftName = minecraftName;
		this.ip = ip;
		this.message = message;
		this.partyID = partyID;
		
	}
}