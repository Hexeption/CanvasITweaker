package com.kiloclient.ui.interactable.slotlist.part;

import com.kiloclient.render.utilities.TextureImage;
import com.kiloclient.resource.ResourceHelper;

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
