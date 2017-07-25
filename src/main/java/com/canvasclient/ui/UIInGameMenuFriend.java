package com.canvasclient.ui;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.friend.FriendManager;
import com.canvasclient.friend.Message;
import com.canvasclient.friend.party.Party;
import com.canvasclient.notification.ActivityManager;
import com.canvasclient.notification.UpdateManager;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.render.utilities.TextureImage;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.interactable.*;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.ui.interactable.slotlist.part.Activity;
import com.canvasclient.ui.interactable.slotlist.part.Friend;
import com.canvasclient.ui.interactable.slotlist.slot.*;
import com.canvasclient.ui.popup.UIPopupAddFriend;
import com.canvasclient.utilities.Timer;
import com.canvasclient.utilities.Utilities;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class UIInGameMenuFriend extends UI {

	private UI popup, popupTo;
	private boolean popupFade;
	private float xPosition, yPosition, width, height, popupOpacity;

	public SlotList friendsSlotList;
	public SlotList partiesSlotList;
	public SlotList activitySlotList;
	
	public SlotList messagesSlotList;
	
	public Friend friend;
	
	public TextureImage coverImage, friendHead;
	public Texture premium, goTo, send;
	
	public boolean friendOnline;
	
	private Link serverLink;
	
	private int index;
	
	private ArrayList<String> lines;
	
	private boolean sending = false;
	
	private String error = "";
	
	public UIInGameMenuFriend(UI parent, Friend friend) {
		super(parent);
		this.friend = friend;
		index = FriendManager.getIndex(friend);
		this.init();
	}
	
	@Override
	public void init() {
		
		new Thread(UpdateManager::updateFriendsList).start();
		
		new Thread(UpdateManager::updateParties).start();
		
		new Thread(UpdateManager::updateLatestActivityList).start();
		
		try {
			this.coverImage = ResourceHelper.downloadTexture(String.format(APIHelper.FRIEND_COVER_IMAGE, this.friend.mcname));
			this.friendHead = ResourceHelper.downloadTexture(String.format(APIHelper.PLAYER_HEAD, this.friend.mcname, 650));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.premium = ResourceHelper.iconShield[2];
		this.goTo = ResourceHelper.iconGoto[1];
		this.send = ResourceHelper.iconSend[4];
		
		xPosition = Display.getWidth() / 2;
		yPosition = (Display.getHeight() / 2);
		width = 900;
		height = 530;
		
		lines = new ArrayList();
		String mes = null;
		try {
			mes = friend.description;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mes == null) {
			return;
		}
		int i = 0;
		int j = 0;
		for(i = 0; i <= mes.length(); i++) {
			String part = "";
			part = mes.substring(j, i);
			
			if (FontHandler.ROUNDED_BOLD.get(14).getWidth(part) > 200) {
				int b = -1;
				for(int a = i-1; a > j; a--) {
					if (mes.charAt(a) == ' ') {
						b = a+1;
						break;
					}
				}
				if (b >= j) {
					i = b;
				}
				lines.add(mes.substring(j, i));
				j = i;
			}
		}
		if (i >= mes.length()) {
			lines.add(mes.substring(j, mes.length()));
		}


		friendsSlotList = new SlotList(3F);
		partiesSlotList = new SlotList(3F);
		activitySlotList = new SlotList(3F);
		messagesSlotList = new SlotList(5F);
		
		
		interactables.clear();
		float xx = xPosition - (width / 2) + 20;
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 12, 24, 24, 0x00000000, ResourceHelper.iconBack[2]));

		xx = xPosition + (width / 2) - 44;
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 12, 24, 24, 0x00000000, ResourceHelper.iconExit[2]));
		xx -= 32;
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 16, 16, 16, 0x00000000, ResourceHelper.iconSettings[1]));
		
		final float mw = (xPosition + (width / 2) - 16) - (xPosition - (width / 2) + 216);
		int i1 = 0;
		
		interactables.add(new IconButton(this, xPosition - (width / 2) + 200 - 17, yPosition - (height / 2) + 60, 8, 8, Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), ResourceHelper.iconPlus[0]));
		
		if (mc.isSingleplayer() && !mc.getIntegratedServer().getPublic()) {
			xx -= 30;
			interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 16, 16, 16, 0x00000000, ResourceHelper.iconWifi[1]));
		}
		
		xx -= 30; 
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 16, 16, 16, 0x00000000, ResourceHelper.iconPizza[1]));
		
		interactables.add(new TextBoxAlt(this, "Message", xPosition - (width / 2) + 520, yPosition - (height / 2) + 478, 320, 30, FontHandler.ROUNDED.get(16), ColorHelper.GREY.getColorCode(), ColorHelper.GREY.getColorCode(), Align.LEFT, Align.CENTER, true));
		interactables.add(new IconButton(this, xPosition - (width / 2) + 860, yPosition - (height / 2) + 485,16, 16, Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), this.send));
		
		interactables.add(new IconButton(this, xPosition - (width / 2) + 200 - 19, yPosition - (height / 2) + 228, 12, 12, Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), ResourceHelper.iconAdd[1]));
		
		interactables.add(new IconButton(this, xPosition + (width / 2) - 25, yPosition - (height / 2) + 60, 12, 12, 0xFF000000, ResourceHelper.iconClose[2]));
		interactables.add(new IconButton(this, xPosition + (width / 2) - 50, yPosition - (height / 2) + 60, 12, 12, 0xFF000000, ResourceHelper.iconExpand[2]));
	}
	
	boolean hasScrolled = false;
	
	public void update(int mx, int my) {
		if (popup == null) {
			super.update(mx, my);
		}
		
		this.friend = FriendManager.getFriend(index);
		
		if (this.friend != null)
			if (this.friend.status != null)
				this.friendOnline = this.friend.status.equalsIgnoreCase("multiplayer") ? true : false;
		
		if (serverLink == null && friend.ip != null) {
			serverLink = new Link(this, "\u00a7a" + friend.ip, xPosition - (width / 2) + 318 + FontHandler.ROUNDED_BOLD.get(14).getWidth("Online - Playing on "), yPosition - (height / 2) + 130 + FontHandler.ROUNDED_BOLD.get(25).getHeight(this.friend.canvasName), FontHandler.ROUNDED_BOLD.get(14), 0xFF67ac2a, Align.LEFT, Align.LEFT);
		}
		
		serverLink.x = xPosition - (width / 2) + 320 + FontHandler.ROUNDED_BOLD.get(14).getWidth("Online - Playing on ");
		serverLink.y = yPosition - (height / 2) + 127 + FontHandler.ROUNDED_BOLD.get(30).getHeight(this.friend.canvasName) - 1;
		serverLink.fontColor = ColorHelper.WHITE.getColorCode();
		
		if (friendOnline && friend.ip != null && !interactables.contains(this.serverLink)) {
			interactables.add(serverLink);
		}
		
		if (!friendOnline && interactables.contains(this.serverLink)) {
			interactables.remove(serverLink);
		}
		
		friendsSlotList.x = xPosition - (width / 2) + 8;
		friendsSlotList.y = yPosition - (height / 2) + 78 - friendsSlotList.oy;
		friendsSlotList.w = 178;
		friendsSlotList.h = 48 * 4 - 50;
		
		partiesSlotList.x = xPosition - (width / 2) + 8;
		partiesSlotList.y = yPosition - (height / 2) + 78 - partiesSlotList.oy + friendsSlotList.h +  25;
		partiesSlotList.w = 178;
		partiesSlotList.h = 48 * 4 - 115;
		
		activitySlotList.x = xPosition - (width / 2) + 8;
		activitySlotList.y = yPosition + (height / 2) - 8 - (48 * 4) - activitySlotList.oy + 20;
		activitySlotList.w = 178;
		activitySlotList.h = 48 * 4 - 20;
		
		messagesSlotList.x = xPosition - (width / 2) + 470;
		messagesSlotList.y = yPosition + (height / 2) - 8 - (48 * 4) - messagesSlotList.oy - 90;
		messagesSlotList.w = 420;
		messagesSlotList.h = 48 * 4 + 30;

		new Thread(() -> {
			if(!hasScrolled) {
				try {
					sleep(500L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				messagesSlotList.scrollTo = messagesSlotList.h;
				hasScrolled = true;
			}
		}).start();

		if (friendsSlotList.slots.size() != FriendManager.getSize()) {
			friendsSlotList.slots.clear();
			int i = 0;
			for(Friend f : FriendManager.getList()) {
				friendsSlotList.slots.add(new FriendSlot(friendsSlotList, FriendManager.getIndex(f), friendsSlotList.x, friendsSlotList.y, friendsSlotList.w, 48, 0, i * 48));
				i++;
			}
		}
		
		messagesSlotList.slots.clear();
		int i1 = 36;
		for(Message m : this.friend.messages) {
			ProfileMessageSlot messageSlot = new ProfileMessageSlot(messagesSlotList, m, messagesSlotList.x, messagesSlotList.y, messagesSlotList.w, 24F, 0F, i1);
			messagesSlotList.slots.add(messageSlot);
			i1+=36*messageSlot.lines.size();
		}
		messagesSlotList.slots.add(new ProfileMessageSlot(messagesSlotList, new Message("", 1), messagesSlotList.x, messagesSlotList.y, messagesSlotList.w, 0F, 0F, i1));
		
		if (partiesSlotList.slots.size() != Canvas.getCanvas().getPartyManager().getParties().size()) {
			partiesSlotList.slots.clear();
			int i = 0;
			for(Party p : Canvas.getCanvas().getPartyManager().getParties()) {
				partiesSlotList.slots.add(new PartySlot(partiesSlotList, Canvas.getCanvas().getPartyManager().getParties().indexOf(p), partiesSlotList.x, partiesSlotList.y, partiesSlotList.w, 48, 0, i * 48));
				i++;
			}
		}
		
		if (activitySlotList.slots.size() != ActivityManager.getSize()) {
			activitySlotList.slots.clear();
			int i = 0;
			for(Activity a : ActivityManager.getList()) {
				switch (a.type) {
				case NEW_MESSAGE:
					activitySlotList.slots.add(new ActivitySlotNewMessage(activitySlotList, ActivityManager.getIndex(a), activitySlotList.x, activitySlotList.y, activitySlotList.w, 48, 0, i * 48));
					break;
				case FRIEND_ACCEPTED:
					activitySlotList.slots.add(new ActivitySlotFriendAccepted(activitySlotList, ActivityManager.getIndex(a), activitySlotList.x, activitySlotList.y, activitySlotList.w, 48, 0, i * 48));
					break;
				case FRIEND_REQUEST:
					activitySlotList.slots.add(new ActivitySlotFriendRequest(activitySlotList, ActivityManager.getIndex(a), activitySlotList.x, activitySlotList.y, activitySlotList.w, 48, 0, i * 48));
					break;
				case INVITE:
					activitySlotList.slots.add(new ActivitySlotServerInvite(activitySlotList, ActivityManager.getIndex(a), activitySlotList.x, activitySlotList.y, activitySlotList.w, 48, 0, i * 48));
					break;
				case NEW_MESSAGE_GROUP:
					activitySlotList.slots.add(new ActivitySlotNewMessageGroup(activitySlotList, ActivityManager.getIndex(a), activitySlotList.x, activitySlotList.y, activitySlotList.w, 48, 0, i * 48));
					break;
				default:
					break;
				}
				i++;
			}
		}

		friendsSlotList.update(mx, my);
		partiesSlotList.update(mx, my);
		activitySlotList.update(mx, my);
		messagesSlotList.update(mx, my);
		
		if (popup != null) {
			popup.update(mx, my);
		}
		popupOpacity += popupFade? - 0.2f:0.2f;
		popupOpacity = Math.min(Math.max(0f, popupOpacity), 1f);
		if (popupFade) {
			if (popupOpacity < 0.05f) {
				popup = popupTo;
				popupFade = false;
				if (popup != null) {
					popup.init();
				}
			}
		}
	}
	
	@Override
	public void handleInteraction(final Interactable i) {
		if (mc.isSingleplayer() && !mc.getIntegratedServer().getPublic()) {
			switch(interactables.indexOf(i)) {
			case 0:
				Canvas.getCanvas().getUIHandler().setCurrentUI(this.parent);
				break;
			case 1:
	            mc.world.sendQuittingDisconnectingPacket();
	            mc.loadWorld((WorldClient)null);
	            mc.displayGuiScreen(new GuiMainMenu());
	            Canvas.getCanvas().getPlayerHandler().playerMap.clear();
				break;
			case 2:
	            mc.displayGuiScreen(new GuiOptions(mc.currentScreen, mc.gameSettings));
				break;
			case 3:
				changePopup(new UIPopupAddFriend(this));
				break;
			case 4:
	            mc.displayGuiScreen(new GuiShareToLan(mc.currentScreen));
				break;
			case 5:
				new Thread(() -> Utilities.openWeb(APIHelper.ORDER)).start();
				break;
			case 7:
				submit();
				break;
			case 8:
				new Thread(() -> Utilities.openWeb(APIHelper.PARTY)).start();
				break;
			case 9:
				Canvas.getCanvas().getUIHandler().setCurrentUI(this.parent);
				break;
			case 10:
				new Thread(() -> Utilities.openWeb(APIHelper.FRIEND)).start();
				break;
			case 11:
				mc.world.sendQuittingDisconnectingPacket();
	            mc.loadWorld((WorldClient)null);
	            Canvas.getCanvas().getPlayerHandler().playerMap.clear();
				mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, new ServerData("", friend.ip, false)));
				break;
		
			}
		} else {
			switch(interactables.indexOf(i)) {
			case 0:
				Canvas.getCanvas().getUIHandler().setCurrentUI(this.parent);
				break;
			case 1:
	            mc.world.sendQuittingDisconnectingPacket();
	            mc.loadWorld((WorldClient)null);
	            mc.displayGuiScreen(new GuiMainMenu());
	            Canvas.getCanvas().getPlayerHandler().playerMap.clear();
				break;
			case 2:
	            mc.displayGuiScreen(new GuiOptions(mc.currentScreen, mc.gameSettings));
				break;
			case 3:
				changePopup(new UIPopupAddFriend(this));
				break;
			case 4:
				new Thread(() -> Utilities.openWeb(APIHelper.ORDER)).start();
				break;
			case 6:
				submit();
				break;
			case 7:
				new Thread(() -> Utilities.openWeb(APIHelper.PARTY)).start();
				break;
			case 8:
				Canvas.getCanvas().getUIHandler().setCurrentUI(this.parent);
				break;
			case 9:
				new Thread(() -> Utilities.openWeb(APIHelper.FRIEND)).start();
				break;
			case 10:
				mc.world.sendQuittingDisconnectingPacket();
	            mc.loadWorld((WorldClient)null);
	            Canvas.getCanvas().getPlayerHandler().playerMap.clear();
				mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, new ServerData("", friend.ip, false)));
				break;
			}
		}
	}
	
	public void submit() {
		final String msg = ((TextBox)interactables.get(mc.isSingleplayer() && !mc.getIntegratedServer().getPublic() ? 6 : 5)).text;
		
		if (msg != null && msg.length() > 0) {
			sending = true;
			new Thread(() -> {
                if (APIHelper.sendMessage(Canvas.getCanvas().getUserControl().clientID, friend.mcname, msg)) {
                    error = "";
                } else {
                    sending = false;
                }

                UpdateManager.updateFriendsList();
                ((TextBox)interactables.get(mc.isSingleplayer() && !mc.getIntegratedServer().getPublic() ? 6 : 5)).text = "";
                ((TextBox)interactables.get(mc.isSingleplayer() && !mc.getIntegratedServer().getPublic() ? 6 : 5)).fontAlignH = Align.LEFT;
                Timer updateBuffer = new Timer();
                if (updateBuffer.isTime(2))
                    sending = false;
            }).start();
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (popup == null) {
			super.mouseClick(mx, my, b);
			friendsSlotList.mouseClick(mx, my, b);
			partiesSlotList.mouseClick(mx, my, b);
			activitySlotList.mouseClick(mx, my, b);
			messagesSlotList.mouseClick(mx, my, b);
		} else {
			popup.mouseClick(mx, my, b);
		}
	}
	
	public void mouseRelease(int mx, int my, int b) {
		if (popup == null) {
			super.mouseRelease(mx, my, b);
			friendsSlotList.mouseRelease(mx, my, b);
			partiesSlotList.mouseRelease(mx, my, b);
			activitySlotList.mouseRelease(mx, my, b);
			messagesSlotList.mouseRelease(mx, my, b);
		} else {
			popup.mouseRelease(mx, my, b);
		}
	}
	
	public void mouseScroll(int s) {
		if (popup == null) {
			super.mouseScroll(s);
			friendsSlotList.mouseScroll(s);
			partiesSlotList.mouseScroll(s);
			activitySlotList.mouseScroll(s);
			messagesSlotList.mouseScroll(s);;
		} else {
			popup.mouseScroll(s);
		}
	}
	
	public void keyboardPress(int key) {
		if (popup == null) {
			super.keyboardPress(key);
			switch (key) {
			case Keyboard.KEY_ESCAPE:
				mc.displayGuiScreen(null);
				break;
			}
		} else {
			popup.keyboardPress(key);
		}
	}
	
	public void keyTyped(int key, char keyChar) {
		if (popup == null) {
			if (key == Keyboard.KEY_RETURN) {
				submit();
			} else {
				super.keyTyped(key, keyChar);
			}
		} else {
			popup.keyTyped(key, keyChar);
		}
	}
	
	public void changePopup(UI u) {
		popupTo = u;
		popupFade = true;
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0.7f * opacity));
		
		GuiHelper.drawRectangle(xPosition - (width / 2), yPosition - (height / 2) + 48, xPosition - (width / 2) + 200, yPosition + (height / 2), Utilities.reAlpha(0xFF1c1c1c, 1f * opacity));
		GuiHelper.drawRectangle(xPosition - (width / 2) + 200, yPosition - (height / 2) + 48, xPosition + (width / 2), yPosition + (height / 2), Utilities.reAlpha(0xFF242424, 1f * opacity));
		
		GuiHelper.drawRectangle(xPosition - (width / 2) + 450, yPosition - (height / 2) + 48, xPosition + (width / 2), yPosition + (height / 2), Utilities.reAlpha(0xFF1c1c1c, 1f * opacity));
		
		GuiHelper.drawRectangle(xPosition - (width / 2), yPosition - (height / 2), xPosition + (width / 2), yPosition - (height / 2) + 48, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1f * opacity));

		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), xPosition - (width / 2) + 8, yPosition - (height / 2) + 56, "Friends:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), xPosition - (width / 2) + 8, yPosition - (height / 2) + 84 + friendsSlotList.h, "Parties:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), xPosition - (width / 2) + 8, yPosition + (height / 2) - 2 - (FontHandler.ROUNDED_BOLD.get(12).getHeight() * 1.5f) - (48 * 4) + 20, "Latest Activity:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
		
		if (coverImage.getTexture() != null) {
			float scale = 0.822F;
			GuiHelper.drawTexturedRectangle(xPosition - (width / 2) + 200, yPosition - (height / 2) + 48, coverImage.getTexture().getImageWidth() * scale, coverImage.getTexture().getImageHeight() * (scale / 1.5F), coverImage.getTexture(), 1f * opacity);
			GuiHelper.drawRectangle(xPosition - (width / 2) + 200, yPosition - (height / 2) + 48, xPosition - (width / 2) + 200 + coverImage.getTexture().getImageWidth() * scale, coverImage.getTexture().getImageHeight() * (scale / 1.5F) + yPosition - (height / 2) + 48, Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0.6F));
		} else {
			float scale = 0.822F;
			GuiHelper.drawRectangle(xPosition - (width / 2) + 200, yPosition - (height / 2) + 48, 852 * scale + xPosition - (width / 2) + 200, 320 * (scale / 1.5F) + yPosition - (height / 2) + 48, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1F));
			GuiHelper.drawRectangle(xPosition - (width / 2) + 200, yPosition - (height / 2) + 48, xPosition - (width / 2) + 200 + 852 * scale, 320 * (scale / 1.5F) + yPosition - (height / 2) + 48, Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0.6F));
		}
		
		if (friendOnline) {
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), xPosition - (width / 2) + 319, yPosition - (height / 2) + 127 + FontHandler.ROUNDED_BOLD.get(30).getHeight(this.friend.canvasName), "\u00a7aOnline", ColorHelper.WHITE.getColorCode());
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), xPosition - (width / 2) + 319 + FontHandler.ROUNDED_BOLD.get(14).getWidth("Online "), yPosition - (height / 2) + 127 + FontHandler.ROUNDED_BOLD.get(30).getHeight(this.friend.canvasName), "- Playing on", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1F));
			GuiHelper.drawTexturedRectangle(xPosition - (width / 2) + 322 + FontHandler.ROUNDED_BOLD.get(14).getWidth("Online - Playing on " + friend.ip + " "), yPosition - (height / 2) + 127 + FontHandler.ROUNDED_BOLD.get(30).getHeight(this.friend.canvasName), 10, 20, this.goTo, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1F));
		} else {
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), xPosition - (width / 2) + 319, yPosition - (height / 2) + 127 + FontHandler.ROUNDED_BOLD.get(30).getHeight(this.friend.canvasName), "Offline", Utilities.reAlpha(ColorHelper.RED.getColorCode(), 1F));
		}
		
		if (friendHead.getTexture() != null) {
			GuiHelper.drawTexturedRectangle(xPosition - (width / 2) + 220, yPosition - (height / 2) + 120, 80, 80, friendHead.getTexture(), 1f * opacity);
		}
		
		if (Canvas.getCanvas().getUserControl().canvasHead.getTexture() != null && !sending) {
			GuiHelper.drawTexturedRectangle(xPosition - (width / 2) + 475, yPosition - (height / 2) + 480, 30, 30, Canvas.getCanvas().getUserControl().canvasHead.getTexture(), 1f * opacity);
		} else {
			GuiHelper.drawLoaderAnimation(xPosition - (width / 2) + 485, yPosition - (height / 2) + 490, 15, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity));
		}
		
		GuiHelper.drawTexturedRectangle(xPosition - (width / 2) + 219, yPosition - (height / 2) + 385, 20, 20, ResourceHelper.iconProfile[1], !(friend.location.length() == 0) ? Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), opacity) : Utilities.reAlpha(ColorHelper.GREY.getColorCode(), opacity));
		GuiHelper.drawTexturedRectangle(xPosition - (width / 2) + 219, yPosition - (height / 2) + 420, 20, 20, ResourceHelper.iconSkype[1], !(friend.skype.length() == 0) ? Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), opacity) : Utilities.reAlpha(ColorHelper.GREY.getColorCode(), opacity));
		GuiHelper.drawTexturedRectangle(xPosition - (width / 2) + 219, yPosition - (height / 2) + 455, 20, 20, ResourceHelper.iconSteam[1], !(friend.steam.length() == 0) ? Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), opacity) : Utilities.reAlpha(ColorHelper.GREY.getColorCode(), opacity));
		GuiHelper.drawTexturedRectangle(xPosition - (width / 2) + 219, yPosition - (height / 2) + 490, 20, 20, ResourceHelper.iconWebsite[1], !(friend.website.length() == 0) ? Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), opacity) : Utilities.reAlpha(ColorHelper.GREY.getColorCode(), opacity));
		
		float yC = yPosition - (height / 2) + 240;
		
		if (lines != null) {
			for (String s : lines) {
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), xPosition - (width / 2) + 220, yC, s, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity));
				yC += 25;
			}
		}
		
		if (!(friend.location.length() == 0))
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), xPosition - (width / 2) + 250, yPosition - (height / 2) + 385, GuiHelper.trimStringToWidth(FontHandler.ROUNDED.get(14), friend.location, 200, true), Utilities.reAlpha(0xFFcbcbcb, opacity));
		if (!(friend.skype.length() == 0))
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), xPosition - (width / 2) + 250, yPosition - (height / 2) + 419, GuiHelper.trimStringToWidth(FontHandler.ROUNDED.get(14), friend.skype, 200, true), Utilities.reAlpha(0xFFcbcbcb, opacity));
		if (!(friend.steam.length() == 0))	
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), xPosition - (width / 2) + 250, yPosition - (height / 2) + 454, GuiHelper.trimStringToWidth(FontHandler.ROUNDED.get(14), friend.steam, 200, true), Utilities.reAlpha(0xFFcbcbcb, opacity));
		if (!(friend.website.length() == 0))
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), xPosition - (width / 2) + 250, yPosition - (height / 2) + 489, GuiHelper.trimStringToWidth(FontHandler.ROUNDED.get(14), friend.website, 200, true), Utilities.reAlpha(0xFFcbcbcb, opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(25), xPosition - (width / 2) + 319, yPosition - (height / 2) + 133, this.friend.canvasName, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 0.8F));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(25), xPosition - (width / 2) + 319 + FontHandler.ROUNDED_BOLD.get(25).getWidth(this.friend.canvasName + " "), yPosition - (height / 2) + 133, "(" + this.friend.mcname + ")", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1F));
		
		GuiHelper.startClip(xPosition - (this.width / 2) + 8 , yPosition - (this.height / 2) + 78, xPosition - (this.width / 2) + 192 + friendsSlotList.sbw, yPosition - (this.height / 2) + 78 + (48 * 4) - 50);
		friendsSlotList.render(opacity);
		GuiHelper.endClip();
		
		GuiHelper.startClip(xPosition - (this.width / 2) + 8, yPosition - (height / 2) + 85 + friendsSlotList.h + 17, xPosition - (this.width / 2) + 192 + partiesSlotList.sbw, yPosition - (height / 2) + 85 + friendsSlotList.h + 96);
		partiesSlotList.render(opacity);
		GuiHelper.endClip();

		GuiHelper.startClip(xPosition - (this.width / 2) + 8, yPosition + (this.height / 2) - 8 - (48 * 4) + 20, xPosition - (this.width / 2) + 192 + activitySlotList.sbw, yPosition + (this.height / 2) - 8);
		activitySlotList.render(opacity);
		GuiHelper.endClip();
		
		GuiHelper.startClip(xPosition - (width / 2) + 450, yPosition - (height / 2) + 240, xPosition + (width / 2), yPosition + (height / 2) - 68);
		messagesSlotList.render(opacity);
		GuiHelper.endClip();
		
		super.render(opacity);
		
		if (popup != null) {
			popup.render(opacity * (Math.max(popupOpacity, 0.05f)));
		}
	}
}