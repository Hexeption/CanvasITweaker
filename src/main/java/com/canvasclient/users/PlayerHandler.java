package com.canvasclient.users;

import com.mojang.authlib.GameProfile;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

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
