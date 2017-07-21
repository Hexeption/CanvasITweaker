package com.kiloclient.ui.interactable.slotlist.slot;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.infrastructure.ServerManager;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.ui.interactable.slotlist.part.Server;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.ChatUtilities;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;

import net.minecraft.client.multiplayer.GuiConnecting;

public class ServerSlot extends Slot {
	
	public float moveUpFade, moveDownFade;
	public boolean moveUp, moveDown;
	
	public int index;
	
	public int clicks = 0;
	public Timer clickTimer = new Timer();
	
	public ServerSlot(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		index = i;
		
		clickTimer.reset();
		
		interactables.clear();
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		hover = mouseOver(mx, my) && parent.mouseOver(mx, my);

		float pad = 4;
		moveUp = (mx >= x+4 && my > y && mx < x+20 && my <= y+(height/2)) && index > 0;
		moveDown = (mx >= x+4 && my > y+(height/2) && mx < x+20 && my <= y+height) && index < ServerManager.getSize()-1;
		
		moveUpFade+= ((moveUp?1:0)-moveUpFade)/2;
		moveDownFade+= ((moveDown?1:0)-moveDownFade)/2;
		
		if (clicks == 2) {
			if (ServerManager.getSize() > 0) {
				if (ServerManager.getServer(index) != null) {
					mc.displayGuiScreen(new GuiConnecting(mc.currentScreen, mc, ServerManager.getServer(index).serverData));
				}
			}
			clicks = 0;
		}
		
		if (clickTimer.isTime(Utilities.doubleClickTimer)) {
			clickTimer.reset();
			clicks = 0;
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (parent.mouseOver(mx, my)) {
			active = hover;
		}
		
		if (hover) {
			if (clicks == 0) {
				clickTimer.reset();
			}
			clicks++;
		}
		
		if (moveUp) {
			clicks = 0;
			moveServer(ServerManager.getServer(index), "up");
		}
		if (moveDown) {
			clicks = 0;
			moveServer(ServerManager.getServer(index), "down");
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
					for(Slot s : parent.slots) {
						ServerSlot ser = (ServerSlot)s;
						ser.active = false;
						if (ServerManager.getServer(ser.index).ip == temp.ip) {
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
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF1A1A1A, activeOpacity*opacity));
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF0A0A0A, hoverOpacity*opacity));
		if (ServerManager.getServer(index) != null) {
			boolean noIcon = true;
			if (ServerManager.getServer(index).getServerIcon() != null) {
				if (ServerManager.getServer(index).getServerIcon().getTexture() != null) {
					GuiHelper.drawTexturedRectangle(x+24, y+8, 64, 64, ServerManager.getServer(index).getServerIcon().getTexture(), opacity);
					noIcon = false;
				}
			}
			if (noIcon) {
				GuiHelper.drawLoaderAnimation(x+56, y+40, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
			
			if (ServerManager.getServer(index).serverData != null && ServerManager.getServer(index).serverData.serverMOTD != null) {
				if (ServerManager.getServer(index).serverData.serverMOTD.equalsIgnoreCase("can\'t resolve hostname") || ServerManager.getServer(index).serverData.serverMOTD.equalsIgnoreCase("Pinging...") || ServerManager.getServer(index).serverData.serverMOTD.equalsIgnoreCase("can\'t connect to server")) {
					GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), x+104+((width-104)/2), y+40, "\u00a78"+ServerManager.getServer(index).serverData.serverMOTD.replace(".","")+": \u00a77"+(ServerManager.getServer(index).ip.contains(":25565")?ServerManager.getServer(index).ip.replace(":25565", ""):ServerManager.getServer(index).ip), Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
				} else {
					if (ServerManager.getServer(index).serverData.serverMOTD.length() == 0) {
						//Draw.string(Fonts.ttfComBold14, x+104+((width-104)/2), y+40, "\u00a78Loading server details", Util.reAlpha(Colors.WHITE.getColorCode(), 1f*opacity), Align.C, Align.C);
						GuiHelper.drawLoaderAnimation(x+104+((width-104)/2), y+40, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
					} else {
						String[] motdLines = ServerManager.getServer(index).serverData.serverMOTD.split("\n");
						int k = 0;
						for(String motd : motdLines) {
							GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), x+104, y+40-((FontHandler.ROUNDED_BOLD.get(14).getHeight()*motdLines.length)/2)+(FontHandler.ROUNDED_BOLD.get(14).getHeight()*k), motd, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.TOP);
							k++;
						}
					}
				}
			}
			
			if (ServerManager.getServer(index).serverData.populationInfo != null) {
				ServerManager.getServer(index).serverData.populationInfo = ChatUtilities.clearFormat(ServerManager.getServer(index).serverData.populationInfo);
				String[] parts = ServerManager.getServer(index).serverData.populationInfo.split("/");
				if (parts.length == 2) {
					GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), x+width-32, y+(height/2), parts[0]+"\u00a78/"+parts[1], Utilities.reAlpha(KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), 1f*opacity), Align.RIGHT, Align.CENTER);
					GuiHelper.drawTexturedRectangle(x+width-24, y+(height/2)-4, 8, 8, ResourceHelper.iconUser[0], Utilities.reAlpha(KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
				}
			}
			if (index > 0) {
				GuiHelper.drawRectangle(x+4, y, x+20, y+(height/2), Utilities.reAlpha(ColorHelper.DARK_BLUE.getColorCode(), (moveUpFade)*opacity));
				GuiHelper.drawTexturedRectangle(x+8, y+36-12, 8, 8, ResourceHelper.iconArrowUp[0]);
			}
			
			if (index < ServerManager.getSize()-1) {
				GuiHelper.drawRectangle(x+4, y+(height/2), x+20, y+height, Utilities.reAlpha(ColorHelper.DARK_BLUE.getColorCode(), (moveDownFade)*opacity));
				GuiHelper.drawTexturedRectangle(x+8, y+36+12, 8, 8, ResourceHelper.iconArrowDown[0]);
			}
		}
		super.render(opacity);
	}
	
}
