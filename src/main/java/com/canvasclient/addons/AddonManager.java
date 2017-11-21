package com.canvasclient.addons;

import com.canvasclient.users.User;
import com.canvasclient.utilities.IMinecraft;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddonManager implements IMinecraft{

	public static Map<String, User> users = new LinkedHashMap<String, User>();
	
	public static void loadUsers() {
//		List<User> list = APIHelper.getUserExtras();
		List<User> list = new ArrayList<>();
		list.add(new User(mc.getSession().getUsername(), false, true, true, true, true, true, true, 1,  "l"));
		if (list != null) {
			for(User u : list) {
				users.put(u.minecraftName, u);
			}
		}
	}
}
