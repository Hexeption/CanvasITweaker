package com.kiloclient.notification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.ui.interactable.slotlist.part.Activity;
import com.kiloclient.ui.interactable.slotlist.part.Friend;
import com.kiloclient.utilities.ActivityType;

public class ActivityManager {

	private static List<Activity> latestActivities = new CopyOnWriteArrayList<Activity>();
	
	public static void addActivity(String id, ActivityType t, String i, String f, String ign, String ip, String msg, String partyID) {
		latestActivities.add(new Activity(id, t, i, f, ign, ip, msg, partyID));
	}

	public static void loadActivities() {
		new Thread() {
			@Override
			public void run() {
				latestActivities = APIHelper.getLatestActivity(KiLO.getKiLO().getUserControl().clientID);
			}
		}.start();
	}
	
	public static List<Activity> getList() {
		return latestActivities;
	}
	
	public static void addActivity(Activity a) {
		latestActivities.add(a);
	}
	
	public static void addActivity(int index, Activity a) {
		latestActivities.add(index, a);
	}
	
	public static void removeActivity(Activity a) {
		latestActivities.remove(a);
	}
	
	public static void removeActivity(int index) {
		latestActivities.remove(latestActivities.get(index));
	}
	
	public static Activity getActivity(int index) {
		if (latestActivities.size() == 0 || index >= latestActivities.size()) {
			return null;
		}
		return latestActivities.get(index);
	}
	
	public static int getIndex(Activity a) {
		return latestActivities.indexOf(a);
	}
	
	public static int getSize() {
		return getList().size();
	}
}
