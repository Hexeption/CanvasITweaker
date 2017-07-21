package com.kiloclient.manager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.ui.interactable.slotlist.part.Server;

public class ServerManager {
	
	private static List<Server> servers = new CopyOnWriteArrayList<Server>();
	
	public static void addServer(String i, String p) {
		for(Server s : servers) {
			if (s.ip.equalsIgnoreCase(i+":"+p)) {
				return;
			}
		}
		servers.add(new Server(i, p));
	}

	public static void loadServers() {
		new Thread() {
			@Override
			public void run() {
				servers.clear();
				servers = APIHelper.getServers(KiLO.getKiLO().getUserControl().clientID);
			}
		}.start();
	}
	
	public static List<Server> getServerList() {
		return servers;
	}
	
	public static void addServer(Server s) {
		servers.add(s);
	}
	
	public static void addServer(int index, Server s) {
		servers.add(index, s);
	}
	
	public static void removeServer(Server s) {
		servers.remove(s);
	}
	
	public static void removeServer(int index) {
		servers.remove(servers.get(index));
	}
	
	public static Server getServer(int index) {
		try {
			return servers.get(index);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Server getServer(String ip) {
		for(Server s : servers) {
			if (s.ip.equalsIgnoreCase(ip)) {
				return s;
			}
		}
		return null;
	}
	
	public static int getIndex(Server s) {
		return servers.indexOf(s);
	}
	
	public static int getSize() {
		return getServerList().size();
	}
}
