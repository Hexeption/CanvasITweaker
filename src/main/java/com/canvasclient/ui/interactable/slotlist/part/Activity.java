package com.canvasclient.ui.interactable.slotlist.part;

import com.canvasclient.render.utilities.TextureImage;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.utilities.ActivityType;

public class Activity {

	public String id;
	public ActivityType type;
	public String canvasName, minecraftName, ip, message, partyID;
	public TextureImage icon;

	public Activity(String id, ActivityType type, String iconURL, String canvasName, String minecraftName, String ip, String message, String partyID) {
		this.id = id;
		this.type = type;
		this.icon = ResourceHelper.downloadTexture(iconURL);
		this.canvasName = canvasName;
		this.minecraftName = minecraftName;
		this.ip = ip;
		this.message = message;
		this.partyID = partyID;
		
	}
}