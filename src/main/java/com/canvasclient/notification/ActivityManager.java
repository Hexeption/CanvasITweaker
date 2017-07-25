package com.canvasclient.notification;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.ui.interactable.slotlist.part.Activity;
import com.canvasclient.utilities.ActivityType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ActivityManager {

	private static List<Activity> latestActivities = new CopyOnWriteArrayList<Activity>();
	
	public static void addActivity(String id, ActivityType t, String i, String f, String ign, String ip, String msg, String partyID) {
		latestActivities.add(new Activity(id, t, i, f, ign, ip, msg, partyID));
	}

	public static void loadActivities() {
		new Thread(() -> latestActivities = APIHelper.getLatestActivity(Canvas.getCanvas().getUserControl().clientID)).start();
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
