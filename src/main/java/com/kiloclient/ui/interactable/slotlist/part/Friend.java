package com.kiloclient.ui.interactable.slotlist.part;

import java.util.ArrayList;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.friend.Message;
import com.kiloclient.render.utilities.TextureImage;
import com.kiloclient.resource.ResourceHelper;

public class Friend {

	public String kiloname, mcname, status, ip, location, skype, steam, website, description;
	public TextureImage head;
	
	public ArrayList<Message> messages;
	
	public Friend(String kn, String n) {
		this(kn, n, "Offline", "");
	}
	
	public Friend(String kn, String n, String s, String i) {
		kiloname = kn;
		mcname = n;
		status = s;
		ip = i;

		head = ResourceHelper.downloadTexture(String.format(APIHelper.PLAYER_HEAD, n, "128"));
		this.messages = APIHelper.getMessagesWithFriend(KiLO.getKiLO().getUserControl().clientID, this.mcname);
	}
}
