package com.kiloclient.notification;

import com.kiloclient.KiLO;
import com.kiloclient.addons.AddonManager;
import com.kiloclient.api.APIHelper;
import com.kiloclient.infrastructure.ServerManager;
import com.kiloclient.friend.*;
import com.kiloclient.utilities.IMinecraft;
import com.kiloclient.utilities.Timer;

public class UpdateManager implements IMinecraft {

	public static void initUpdating(final float time) {
		new Thread() {
			@Override
			public void run() {
				
				if (KiLO.getKiLO().getMusicHandler() != null)
					KiLO.getKiLO().getMusicHandler().setPlaylists(APIHelper.getPlaylists(KiLO.getKiLO().getUserControl().clientID));
				
				updateFriendsList();
				updateLatestActivityList();
				updateMessageList();
				updateParties();
				updateFriendsList();
				
				Timer premiumTimer = new Timer();
				premiumTimer.reset();			
				Timer timer = new Timer();
				timer.reset();
				while (true) {
					if (timer.isTime(time)) {
						checkUpdate();
						updateFriendsList();
						updateLatestActivityList();
						updateMessageList();
						updateExtrasList();
						updateParties();
						timer.reset();
					}				
				}
			}
		}.start();
	}
	
	public static void updateMultiplayerServerList() {
		if (KiLO.getKiLO().getUserControl() == null) {
			return;
		}
		ServerManager.loadServers();
	}
	
	public static void updateFriendsList() {
		if (KiLO.getKiLO().getUserControl() == null) {
			return;
		}
		FriendManager.loadFriends();
	}
	
	public static void updateLatestActivityList() {
		if (KiLO.getKiLO().getUserControl() == null) {
			return;
		}
		ActivityManager.loadActivities();
	}
	
	public static void updateMessageList() {
		if (KiLO.getKiLO().getUserControl() == null) {
			return;
		}
		MessageManager.loadMessages();
	}
	
	public static void updateParties() {
		KiLO.getKiLO().getPartyManager().updateParties();
	}
	
	public static void checkUpdate() {
		if (KiLO.getKiLO().getUserControl() == null) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				boolean singleplayer = mc.isSingleplayer();
				boolean ingame = mc.world != null;
				boolean onServer = mc.getCurrentServerData() != null;
				String ip = onServer?mc.getCurrentServerData().serverIP:"";
				
				NotificationManager.list = APIHelper.getUpdates(KiLO.getKiLO().getUserControl().clientID, singleplayer?"singleplayer":ingame && onServer?"multiplayer":(ip.length() > 0?"online":"online"), ip);
			}
		}.start();
	}
	
	public static void updateExtrasList() {
		if (KiLO.getKiLO().getUserControl() == null) {
			return;
		}
		AddonManager.loadUsers();
	}
}
