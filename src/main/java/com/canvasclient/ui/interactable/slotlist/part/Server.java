package com.canvasclient.ui.interactable.slotlist.part;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import net.minecraft.client.network.ServerPinger;
import org.apache.commons.codec.binary.Base64;
import org.newdawn.slick.opengl.TextureLoader;

import net.minecraft.client.multiplayer.ServerData;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.canvasclient.api.APIHelper;
import com.canvasclient.render.utilities.TextureImage;
import com.canvasclient.resource.ResourceHelper;

public class Server {

	private final ServerPinger osp = new ServerPinger();
    private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
	
	public String ip, name, description, website, webstore;
	public TextureImage icon, image;
	
	public ServerData serverData;
	
	public Server(String i, String p) {
		this(i, null, null, null, null);
	}

	public Server(String i, String sn, String sd, String sws, String swt) {
		ip = i;
		name = sn;
		description = sd;
		website = sws;
		webstore = swt;
		
		icon = new TextureImage();

		String imu = String.format(APIHelper.SERVER_IMAGE, ip);
		image = ResourceHelper.downloadTexture(imu);
		
		getServerIcon();
		
	    serverData = new ServerData("", ip, false);
		
		ping();
	}
	
	public void ping() {
		if (!serverData.pinged) {
			serverData.pinged = true;
			serverData.pingToServer = -2L;
			serverData.serverMOTD = "";
			serverData.populationInfo = "";
			field_148302_b.submit(new Runnable() {
				public void run() {
					try {
						osp.ping(serverData);
					} catch (UnknownHostException var2) {
						serverData.pingToServer = -1L;
						serverData.serverMOTD = "Can\'t resolve hostname";
					} catch (Exception var3) {
						serverData.pingToServer = -1L;
						serverData.serverMOTD = "Can\'t connect to server";
					}
				}
			});
		}
	}
	
	public TextureImage getServerIcon() {
		if (serverData != null) {
			if (serverData.getBase64EncodedIconData() != null && icon.getTexture() == null) {
				String s = serverData.getBase64EncodedIconData().substring(serverData.getBase64EncodedIconData().indexOf(",")+1);
				InputStream stream = new ByteArrayInputStream(Base64.decodeBase64(s.getBytes()));
				try {
					icon.texture = TextureLoader.getTexture("PNG", stream);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return null;
			}
			return icon;
		}
		return null;
	}
}
