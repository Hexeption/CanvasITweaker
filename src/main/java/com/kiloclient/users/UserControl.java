package com.kiloclient.users;

import com.kiloclient.render.utilities.TextureImage;
import com.kiloclient.resource.ResourceHelper;
import net.minecraft.client.Minecraft;

public class UserControl {
	
	public String clientID, kiloName, minecraftName, email, gameStatus, ircTag;
	public Player minecraftPlayer;
	public boolean isPremium, flareCheats;
	public int updateTime, colorSchemeID;
	public TextureImage kiloHead;
	
	public String currentVersion, currentVersionURL;
	
	public UserControl(String clientID, String kiloName, String minecraftName, String isPremium, String email, String gameStatus, String ircTag, String updateTime, String colorScheme) {
		this.clientID = clientID;
		this.kiloName = kiloName;
		this.minecraftName = minecraftName;
		this.minecraftPlayer = new Player(Minecraft.getMinecraft().getSession().getProfile(), true);
		this.isPremium = Boolean.parseBoolean(isPremium);
		this.email = email;
		this.gameStatus = gameStatus;
		this.ircTag = ircTag;
		this.updateTime = Integer.parseInt(updateTime);
		this.colorSchemeID = Integer.parseInt(colorScheme);

//		this.kiloHead = ResourceHelper.downloadTexture(String.format("https://mcapi.ca/avatar/2d/%s", Minecraft.getMinecraft().getSession().getUsername(), "128"));
		this.kiloHead = ResourceHelper.downloadTexture(String.format("https://mcapi.ca/avatar/2d/%s", "Hexeption", "128"));


		this.currentVersion = currentVersion;
		this.currentVersionURL = currentVersionURL;
		
		/*if (!currentVersion.equalsIgnoreCase(KiLO.getKiLO().getVersion())) {
			KiLO.getKiLO().setOutOfDate(true);

		}*/
		
		//UpdateManager.initUpdating(this.updateTime);
	}
}
