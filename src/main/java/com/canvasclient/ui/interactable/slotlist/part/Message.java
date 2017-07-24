package com.canvasclient.ui.interactable.slotlist.part;

import com.canvasclient.render.utilities.TextureImage;
import com.canvasclient.resource.ResourceHelper;

public class Message {

	public String minecraftName, iconURL, message;
	public TextureImage icon;
	
	public Message(String minecraftName, String message, String iconURL) {
		this.minecraftName = minecraftName;
		this.message = message;
		this.iconURL = iconURL;
		
		this.icon = ResourceHelper.downloadTexture(iconURL);
	}
}
