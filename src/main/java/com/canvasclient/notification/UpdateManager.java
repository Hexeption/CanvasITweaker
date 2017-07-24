package com.canvasclient.notification;

import com.canvasclient.Canvas;
import com.canvasclient.addons.AddonManager;
import com.canvasclient.api.APIHelper;
import com.canvasclient.friend.FriendManager;
import com.canvasclient.friend.MessageManager;
import com.canvasclient.infrastructure.ServerManager;
import com.canvasclient.utilities.IMinecraft;
import com.canvasclient.utilities.Timer;

public class UpdateManager implements IMinecraft {

	public static void initUpdating(final float time) {
		new Thread() {
			@Override
			public void run() {
				
				if (Canvas.getCanvas().getMusicHandler() != null)
					Canvas.getCanvas().getMusicHandler().setPlaylists(APIHelper.getPlaylists(Canvas.getCanvas().getUserControl().clientID));
				
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
		if (Canvas.getCanvas().getUserControl() == null) {
			return;
		}
		ServerManager.loadServers();
	}
	
	public static void updateFriendsList() {
		if (Canvas.getCanvas().getUserControl() == null) {
			return;
		}
		FriendManager.loadFriends();
	}
	
	public static void updateLatestActivityList() {
		if (Canvas.getCanvas().getUserControl() == null) {
			return;
		}
		ActivityManager.loadActivities();
	}
	
	public static void updateMessageList() {
		if (Canvas.getCanvas().getUserControl() == null) {
			return;
		}
		MessageManager.loadMessages();
	}
	
	public static void updateParties() {
		Canvas.getCanvas().getPartyManager().updateParties();
	}
	
	public static void checkUpdate() {
		if (Canvas.getCanvas().getUserControl() == null) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				boolean singleplayer = mc.isSingleplayer();
				boolean ingame = mc.world != null;
				boolean onServer = mc.getCurrentServerData() != null;
				String ip = onServer?mc.getCurrentServerData().serverIP:"";
				
				NotificationManager.list = APIHelper.getUpdates(Canvas.getCanvas().getUserControl().clientID, singleplayer?"singleplayer":ingame && onServer?"multiplayer":(ip.length() > 0?"online":"online"), ip);
			}
		}.start();
	}
	
	public static void updateExtrasList() {
		if (Canvas.getCanvas().getUserControl() == null) {
			return;
		}
		AddonManager.loadUsers();
	}
}
