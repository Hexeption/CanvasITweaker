package com.canvasclient.ui.interactable.slotlist.part;

import java.util.ArrayList;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.friend.Message;
import com.canvasclient.render.utilities.TextureImage;
import com.canvasclient.resource.ResourceHelper;

public class Friend {

	public String canvasName, mcname, status, ip, location, skype, steam, website, description;
	public TextureImage head;
	
	public ArrayList<Message> messages;
	
	public Friend(String kn, String n) {
		this(kn, n, "Offline", "");
	}
	
	public Friend(String kn, String n, String s, String i) {
		canvasName = kn;
		mcname = n;
		status = s;
		ip = i;

		head = ResourceHelper.downloadTexture(String.format(APIHelper.PLAYER_HEAD, n, "128"));
		this.messages = APIHelper.getMessagesWithFriend(Canvas.getCanvas().getUserControl().clientID, this.mcname);
	}
}
