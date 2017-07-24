package com.canvasclient.addons;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.canvasclient.api.APIHelper;
import com.canvasclient.users.User;

public class AddonManager {

	public static Map<String, User> users = new LinkedHashMap<String, User>();
	
	public static void loadUsers() {
		List<User> list = APIHelper.getUserExtras();
		if (list != null) {
			for(User u : list) {
				users.put(u.minecraftName, u);
			}
		}
	}
}