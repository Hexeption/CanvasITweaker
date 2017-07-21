package com.kiloclient.users;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import com.google.common.base.Charsets;
import com.kiloclient.api.APIHelper;
import com.mojang.authlib.GameProfile;

public class PlayerHandler {

	public Map<UUID, Player> playerMap = new LinkedHashMap<UUID, Player>();
	
	public void addUserEntry(GameProfile gameProfile, boolean hasHead) {
		playerMap.put(gameProfile.getId(), new Player(gameProfile, hasHead));
	}
	
	public void removeUserEntry(GameProfile gameProfile) {
		if (playerMap.containsKey(gameProfile.getId())) {
			playerMap.remove(gameProfile.getId());
		}
	}
	
	public Player getPlayerFromUsername(String playerUsername) {
		for(Player player : playerMap.values()) {
			if (player.gameProfile.getName().equalsIgnoreCase(playerUsername)) {
				return player;
			}
		}
		return null;
	}
	
	public Map<UUID, Player> getPlayerMap() {
		return this.playerMap;
	}
	
}
