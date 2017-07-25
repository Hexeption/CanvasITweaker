package com.canvasclient.friend;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.ui.interactable.slotlist.part.Friend;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FriendManager {

	private static List<Friend> friends = new CopyOnWriteArrayList<Friend>();
	
	public static void addFriend(String kn, String n, String s, String i) {
		for(Friend f : friends) {
			if (f.mcname.equalsIgnoreCase(n)) {
				return;
			}
		}
		friends.add(new Friend(kn, n, s, i));
	}

	public static void loadFriends() {
		new Thread(() -> friends = APIHelper.getFriends(Canvas.getCanvas().getUserControl().clientID)).start();
	}
	
	public static List<Friend> getList() {
		return friends;
	}
	
	public static void addFriend(Friend f) {
		friends.add(f);
	}
	
	public static void addFriend(int index, Friend f) {
		friends.add(index, f);
	}
	
	public static void removeFriend(Friend f) {
		friends.remove(f);
	}
	
	public static void removeFriend(int index) {
		friends.remove(friends.get(index));
	}
	
	public static Friend getFriend(int index) {
		if (friends.size() == 0) {
			return null;
		}
		return friends.get(index);
	}
	
	public static Friend getFriend(String n) {
		for(Friend f : friends) {
			if (f.mcname.equalsIgnoreCase(n)) {
				return f;
			}
		}
		return null;
	}
	
	public static int getIndex(Friend f) {
		return friends.indexOf(f);
	}
	
	public static int getSize() {
		return getList().size();
	}
}
