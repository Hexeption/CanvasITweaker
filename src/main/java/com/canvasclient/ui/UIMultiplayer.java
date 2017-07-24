package com.canvasclient.ui;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.infrastructure.ServerManager;
import com.canvasclient.notification.UpdateManager;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.interactable.Button;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.Link;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.ui.interactable.slotlist.part.Server;
import com.canvasclient.ui.interactable.slotlist.slot.ServerSlot;
import com.canvasclient.ui.interactable.slotlist.slot.Slot;
import com.canvasclient.utilities.Timer;
import com.canvasclient.utilities.Utilities;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

public class UIMultiplayer extends UI {

	private float formOffset;
	private boolean invalid, deleting;
	
	private String invalidMessage = "";
	
	private Timer invalidTimer = new Timer();

	private int selectedIndex;
	private float fX, fY, fW, fH;
	
	public SlotList ssl;
	
	public UIMultiplayer(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = "Multiplayer";

		formOffset = 0;
		invalid = false;

		UpdateManager.updateMultiplayerServerList();
		ssl = new SlotList(7f);
		
		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2)+44;
		fW = Display.getWidth()-40;
		fH = Display.getHeight()-124;

		interactables.clear();
		int i = 0;
		interactables.add(new IconButton(this, 32+(64*(i++)), 32, 32, 32, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconReturn[3]));
		interactables.add(new IconButton(this, 32+(64*(i++)), 32, 32, 32, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconRefresh[3]));
		interactables.add(new Button(this, "Direct", fX+(fW/2)-270-8-128-87, fY+(fH/2)-48+8, 80, 32, FontHandler.ROUNDED.get(12), 0xFF1e1e1e, null, 0));
		interactables.add(new Button(this, "Add Server", fX+(fW/2)-270-8-128, fY+(fH/2)-48+8, 128, 32, FontHandler.ROUNDED_BOLD.get(12), Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), ResourceHelper.iconAdd[1], 16));
		interactables.add(new Button(this, "Join Server", fX+(fW/2)-270+8, fY+(fH/2)-48+8-50, 270-16, 42, FontHandler.ROUNDED_BOLD.get(12), Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), null, 0));
		interactables.add(new Button(this, "Move to Top", fX+(fW/2)-270+8, fY+(fH/2)-48+8, ((270-16)/2)-4, 32, FontHandler.ROUNDED.get(12), 0xFF161616, null, 0));
		interactables.add(new Button(this, "Delete", fX+(fW/2)-270+12+((270-16)/2), fY+(fH/2)-48+8, ((270-16)/2)-4, 32, FontHandler.ROUNDED.get(12), 0xFF161616, null, 0));
		
		interactables.add(new Link(this, "", fX+(fW/2)-10, fY-(fH/2)+173+(FontHandler.ROUNDED_BOLD.get(12).getHeight()*3f)+(FontHandler.ROUNDED_BOLD.get(14).getHeight()*4), FontHandler.ROUNDED_BOLD.get(12), Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), Align.RIGHT, Align.TOP));
		interactables.add(new Link(this, "", fX+(fW/2)-10, fY-(fH/2)+173+(FontHandler.ROUNDED_BOLD.get(12).getHeight()*3.5f)+(FontHandler.ROUNDED_BOLD.get(14).getHeight()*5), FontHandler.ROUNDED_BOLD.get(12), Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), Align.RIGHT, Align.TOP));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		if (invalid) {
			formOffset+= ((-FontHandler.STANDARD.get(14).getHeight()*2)-formOffset)/5f;
			if (invalidTimer.isTime(2f)) {
				invalid = false;
			}
		} else {
			invalidTimer.reset();
		}
		if (!invalid) {
			formOffset+= (0-formOffset)/5f;
		}

		Server activeServer = null;
		for(Slot s : ssl.slots) {
			if (s.active) {
				activeServer = ServerManager.getServer(((ServerSlot)s).index);
				break;
			}
		}
		((Button)interactables.get(4)).enabled = activeServer != null;
		((Button)interactables.get(5)).enabled = activeServer != null;
		((Button)interactables.get(6)).enabled = activeServer != null;
		((Button)interactables.get(6)).text = deleting?(String)null:"Delete";

		ssl.x = fX-(fW/2)+32;
		ssl.y = fY-(fH/2)+32-ssl.oy;
		ssl.w = fW-334;
		ssl.h = fH-112;
		
		if (ssl.slots.size() != ServerManager.getSize()) {
			ssl.slots.clear();
			int i = 0;
			for(Server s : ServerManager.getServerList()) {
				ssl.slots.add(new ServerSlot(ssl, ServerManager.getIndex(s), ssl.x, ssl.y, ssl.w, 80, 0, i*80));
				i++;
			}
		}
		
		ssl.update(mx, my);

		((Link)interactables.get(7)).text = (activeServer != null && activeServer.website != null)?activeServer.website:"";
		((Link)interactables.get(8)).text = (activeServer != null && activeServer.webstore != null)?activeServer.webstore:"";
		interactables.get(7).x = fX+(fW/2)-10-(interactables.get(7).fontAlignH==Align.CENTER?interactables.get(7).font.getWidth(((Link)interactables.get(7)).text)/2:(interactables.get(7).fontAlignH==Align.RIGHT?interactables.get(7).font.getWidth(((Link)interactables.get(7)).text):0));
		interactables.get(8).x = fX+(fW/2)-10-(interactables.get(8).fontAlignH==Align.CENTER?interactables.get(8).font.getWidth(((Link)interactables.get(8)).text)/2:(interactables.get(8).fontAlignH==Align.RIGHT?interactables.get(8).font.getWidth(((Link)interactables.get(8)).text):0));
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		Server activeServer = null;
		int index = -1;
		
		int a = 0;
		for(Slot s : ssl.slots) {
			if (s.active) {
				index = a;
				activeServer = ServerManager.getServer(((ServerSlot)s).index);
				break;
			}
			a++;
		}
		
		switch(interactables.indexOf(i)) {
		case 0:
			mc.displayGuiScreen(new GuiMainMenu());
			//Canvas.getCanvas().getUIHandler().changeUI(new UIMainMenu(null));
			break;
		case 1:
			ServerManager.loadServers();
			break;
		case 2:
			Canvas.getCanvas().getUIHandler().changeUI(new UIDirectConnect(this));
			break;
		case 3:
			Canvas.getCanvas().getUIHandler().changeUI(new UIAddServer(this));
			break;
		case 4:
			if (activeServer != null) {
				mc.displayGuiScreen(new GuiConnecting(mc.currentScreen, mc, activeServer.serverData));
			}
			break;
		case 5:
			if (ServerManager.getIndex(activeServer) != 0) {
				Server temp = activeServer;
				ServerManager.removeServer(activeServer);
				ServerManager.addServer(0, temp);
				moveToTop(temp);
			}
			break;
		case 6:
			if (activeServer != null) {
				removeServer(activeServer);
			}
			break;
		case 7:
			Utilities.openWeb("http://"+((Link)i).text);
			break;
		case 8:
			Utilities.openWeb("http://"+((Link)i).text);
			break;
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		super.mouseClick(mx, my, b);
		ssl.mouseClick(mx, my, b);
	}
	
	public void mouseRelease(int mx, int my, int b) {
		super.mouseRelease(mx, my, b);
		ssl.mouseRelease(mx, my, b);
	}
	
	public void mouseScroll(int s) {
		super.mouseScroll(s);
		ssl.mouseScroll(s);
	}
	
	public void keyboardPress(int key) {
		Server activeServer = null;
		int index = -1;
		
		int a = 0;
		for(Slot s : ssl.slots) {
			if (s.active) {
				index = a;
				activeServer = ServerManager.getServer(((ServerSlot)s).index);
				break;
			}
			a++;
		}
		super.keyboardPress(key);
		switch (key) {
		case Keyboard.KEY_RETURN:
			if (activeServer != null) {
				mc.displayGuiScreen(new GuiConnecting(mc.currentScreen, mc, activeServer.serverData));
			}
			break;
		case Keyboard.KEY_DELETE:
			if (activeServer != null) {
				removeServer(activeServer);
			}
			break;
		case Keyboard.KEY_UP:
			if (ServerManager.getSize() > 0) {
				if (index > 0) {
					moveServer(activeServer, "up");
				}
			}
			break;
		case Keyboard.KEY_DOWN:
			if (ServerManager.getSize() > 0) {
				if (index < ServerManager.getSize()-1) {
					moveServer(activeServer, "down");
				}
			}
			break;
		}
	}
	
	public void moveServer(final Server s, final String method) {
		new Thread() {
			@Override
			public void run() {
				int index = ServerManager.getIndex(s);
				try {
					Server temp = s;
					ServerManager.removeServer(s);
					ServerManager.addServer(index+(method.equalsIgnoreCase("up")?-1:1), temp);
					for(Slot ser : ssl.slots) {
						ser.active = false;
						if (ServerManager.getServer(((ServerSlot)ser).index).ip == temp.ip) {
							ser.active = true;
						}
					}
					APIHelper.moveServer(s.ip, method);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void moveToTop(final Server s) {
		new Thread() {
			@Override
			public void run() {
				APIHelper.moveServer(s.ip, "top");
			}
		}.start();
	}
	
	public void removeServer(final Server s) {
		if (!deleting) {
			deleting = true;
			new Thread() {
				@Override
				public void run() {
					String connect = APIHelper.deleteServer(s.ip);
					if (connect != null) {
						ServerManager.removeServer(s);
					} else {
						invalidMessage = "Failed to remove server from database";
						invalid = true;
					}
					deleting = false;
				}
			}.start();
		}
		if (formOffset < (-FontHandler.STANDARD.get(14).getHeight()*1.5f)) {
			invalid = false;
		}
	}
	
	public void render(float opacity) {
		drawDarkerBackground(false, opacity);
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2, fY-(fH/2)-(FontHandler.ROUNDED_BOLD.get(40).getHeight(title)/2)-32, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2), fX+(fW/2)-270, fY+(fH/2), Utilities.reAlpha(0xFF161616, 0.94f*opacity));
		GuiHelper.drawRectangle(fX+(fW/2)-270, fY-(fH/2), fX+(fW/2), fY+(fH/2), Utilities.reAlpha(0xFF0e0e0e, 0.82f*opacity));
		
		GuiHelper.drawRectangle(fX-(fW/2), fY+(fH/2)-48, fX+(fW/2)-270, fY+(fH/2), Utilities.reAlpha(0xFF2d2d2d, 1f*opacity));

		if (Canvas.getCanvas().getUserControl().minecraftPlayer.head.getTexture() != null) {
			Texture tex = Canvas.getCanvas().getUserControl().minecraftPlayer.head.getTexture();
			GuiHelper.drawTexturedRectangle(Display.getWidth()-20-64, 20, 64, 64, tex, 1f*opacity);
			
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), Display.getWidth()-20-64-16, 20+39-(FontHandler.ROUNDED_BOLD.get(20).getHeight()/2), "Logged in as", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.RIGHT, Align.BOTTOM);
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()-20-64-16, 20+40, Canvas.getCanvas().getUserControl().minecraftPlayer.gameProfile.getName(), Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.RIGHT, Align.CENTER);
		}

		GuiHelper.startClip(fX-(fW/2)+32, fY-(fH/2)+32, fX+(fW/2)-302+ssl.sbw, fY+(fH/2)-80);
		ssl.render(opacity);
		GuiHelper.endClip();
		
		Server ss = null;
		
		for(Slot s : ssl.slots) {
			if (s.active) {
				ss = ServerManager.getServer(((ServerSlot)s).index);
				break;
			}
		}
		
		if (ss != null) {
			Texture t = ss.image.getTexture();
			if (t != null) {
				float w = t.getImageWidth()/(t.getImageWidth()/250f);
				float h = t.getImageHeight()/(t.getImageWidth()/(w));
				GuiHelper.drawTexturedRectangle(fX+(fW/2)-270+10, fY-(fH/2)+10, w, h, t, 1f*opacity);
			}
			
			float fls = fX+(fW/2)-260;
			float frs = fX+(fW/2)-10;
			float fiy = fY-(fH/2)+170;
			
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fls, fiy, ss.name != null?ss.name:"Minecraft Server", Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground());
			fiy+= FontHandler.ROUNDED_BOLD.get(14).getHeight()*2;
			
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), fls, fiy, ss.ip, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			boolean noPing = ss.serverData.serverMOTD.equalsIgnoreCase("can\'t connect to server") || ss.serverData.serverMOTD.equalsIgnoreCase("pinging...") || ss.serverData.serverMOTD.equalsIgnoreCase("can\'t resolve hostname");
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), fls, fiy+ FontHandler.ROUNDED_BOLD.get(12).getHeight(), noPing?"":ss.serverData.pingToServer+"ms Ping", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity));
			
			if (ss.serverData.populationInfo.contains("/")) {
				GuiHelper.drawLine(frs- FontHandler.ROUNDED_BOLD.get(12).getWidth(ss.serverData.populationInfo.split("/")[1])+8, fiy+(FontHandler.ROUNDED_BOLD.get(12).getHeight()*2.5f)+(FontHandler.ROUNDED_BOLD.get(14).getHeight()/3), frs- FontHandler.ROUNDED_BOLD.get(12).getWidth(ss.serverData.populationInfo.split("/")[1])-16, fiy+(FontHandler.ROUNDED_BOLD.get(12).getHeight()*3.5f)+(FontHandler.ROUNDED_BOLD.get(14).getHeight()/1.5f), Utilities.reAlpha(0xFF707070, 0.7f*opacity), 2f);
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fls, fiy+(FontHandler.ROUNDED_BOLD.get(12).getHeight()*3), "Online Players:", Utilities.reAlpha(0xFFe2e2e2, 1f*opacity));
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), frs- FontHandler.ROUNDED_BOLD.get(12).getWidth(ss.serverData.populationInfo.split("/")[1])-8, fiy+(FontHandler.ROUNDED_BOLD.get(12).getHeight()*2.5f)+(FontHandler.ROUNDED_BOLD.get(14).getHeight()/2), ss.serverData.populationInfo.split("/")[0], Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), Align.RIGHT, Align.CENTER);
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), frs, fiy+(FontHandler.ROUNDED_BOLD.get(12).getHeight()*3.5f)+(FontHandler.ROUNDED_BOLD.get(14).getHeight()/2), ss.serverData.populationInfo.split("/")[1], Utilities.reAlpha(0xFF565656, 1f*opacity), Align.RIGHT, Align.CENTER);
			}

			if (ss.website != null && ss.website.length() > 0) {
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fls, fiy+(FontHandler.ROUNDED_BOLD.get(12).getHeight()*3f)+(FontHandler.ROUNDED_BOLD.get(14).getHeight()*2), "Server Website:", Utilities.reAlpha(0xFFe2e2e2, 1f*opacity));
			}

			if (ss.webstore != null && ss.webstore.length() > 0) {
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fls, fiy+(FontHandler.ROUNDED_BOLD.get(12).getHeight()*3.5f)+(FontHandler.ROUNDED_BOLD.get(14).getHeight()*3), "Donation Store:", Utilities.reAlpha(0xFFe2e2e2, 1f*opacity));
			}
		}
		
		//Message
		GuiHelper.startClip(fX-(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)-40+(FontHandler.STANDARD.get(14).getHeight()/2), fX+(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)-40+(FontHandler.STANDARD.get(14).getHeight()*1.5f));
		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(14), fX, fY+(fH/2)-40-(FontHandler.STANDARD.get(14).getHeight(invalidMessage))-formOffset, invalidMessage, Utilities.reAlpha(0xFFFF5555, 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.endClip();
		
		super.render(opacity);
	}
}
