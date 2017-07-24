package com.canvasclient.users;

import com.canvasclient.api.APIHelper;
import com.canvasclient.render.utilities.TextureImage;
import com.canvasclient.resource.ResourceHelper;
import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class Player {

	public UUID uuid;
	public GameProfile gameProfile;
	public TextureImage head;
	
	public Player(GameProfile gameProfile, boolean hasHead) {
		this.gameProfile = gameProfile;
		this.uuid = gameProfile.getId();

		if (hasHead) {
			this.head = ResourceHelper.downloadTexture(String.format(APIHelper.PLAYER_HEAD, gameProfile.getName(), "128"));
		}
	}
}
