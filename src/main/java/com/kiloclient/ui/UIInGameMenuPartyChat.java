package com.kiloclient.ui;

import com.kiloclient.render.FontHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.notification.ActivityManager;
import com.kiloclient.friend.FriendManager;
import com.kiloclient.notification.UpdateManager;
import com.kiloclient.friend.Message;
import com.kiloclient.friend.party.Party;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.IconButton;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.TextBox;
import com.kiloclient.ui.interactable.TextBoxAlt;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.ui.interactable.slotlist.part.Activity;
import com.kiloclient.ui.interactable.slotlist.part.Friend;
import com.kiloclient.ui.interactable.slotlist.slot.ActivitySlotFriendAccepted;
import com.kiloclient.ui.interactable.slotlist.slot.ActivitySlotFriendRequest;
import com.kiloclient.ui.interactable.slotlist.slot.ActivitySlotNewMessage;
import com.kiloclient.ui.interactable.slotlist.slot.ActivitySlotNewMessageGroup;
import com.kiloclient.ui.interactable.slotlist.slot.ActivitySlotServerInvite;
import com.kiloclient.ui.interactable.slotlist.slot.FriendSlot;
import com.kiloclient.ui.interactable.slotlist.slot.PartyMessageSlot;
import com.kiloclient.ui.interactable.slotlist.slot.PartySlot;
import com.kiloclient.ui.interactable.slotlist.slot.ProfileMessageSlot;
import com.kiloclient.ui.popup.UIPopupAddFriend;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.render.utilities.AnimationTimer;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.multiplayer.WorldClient;

public class UIInGameMenuPartyChat extends UI {

	private UI popup, popupTo;
	private boolean popupFade;
	private float xPosition, yPosition, width, height, popupOpacity;

	public SlotList friendsSlotList;
	public SlotList partiesSlotList;
	public SlotList activitySlotList;
	public SlotList messagesSlotList;
	
	public Party party;
	
	public boolean friendOnline;
	
	private AnimationTimer anim = new AnimationTimer(10);
	private boolean sendHover = false;
	
	private boolean sending = false;
	
	public UIInGameMenuPartyChat(UI parent, Party party) {
		super(parent);
		this.party = party;
	}
	
	@Override
	public void init() {
		
		new Thread() {
			@Override
			public void run() {
				UpdateManager.updateFriendsList();
			}
		}.start();
		
		new Thread() {
			@Override
			public void run() {
				UpdateManager.updateParties();
			}
		}.start();
		
		new Thread() {
			@Override
			public void run() {
				UpdateManager.updateLatestActivityList();
			}
		}.start();
		
		xPosition = Display.getWidth() / 2;
		yPosition = (Display.getHeight() / 2);
		width = 900;
		height = 530;

		friendsSlotList = new SlotList(3F);
		partiesSlotList = new SlotList(3F);
		activitySlotList = new SlotList(3F);
		messagesSlotList = new SlotList(7F);
		
		interactables.clear();
		float xx = xPosition - (width / 2) + 20;
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 12, 24, 24, 0x00000000, ResourceHelper.iconBack[2]));

		xx = xPosition + (width / 2) - 44;
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 12, 24, 24, 0x00000000, ResourceHelper.iconExit[2]));
		xx -= 32;
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 16, 16, 16, 0x00000000, ResourceHelper.iconSettings[1]));
		
		final float mw = (xPosition + (width / 2) - 16) - (xPosition - (width / 2) + 216);
		int i = 0;
		
		interactables.add(new IconButton(this, xPosition - (width / 2) + 200 - 17, yPosition - (height / 2) + 60, 8, 8, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconPlus[0]));
		
		if (mc.isSingleplayer() && !mc.getIntegratedServer().getPublic()) {
			xx -= 30;
			interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 16, 16, 16, 0x00000000, ResourceHelper.iconWifi[1]));
		}
		
		xx -= 30; 
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 16, 16, 16, 0x00000000, ResourceHelper.iconPizza[1]));
		
		interactables.add(new IconButton(this, xPosition - (width / 2) + 200 - 19, yPosition - (height / 2) + 228, 12, 12, KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), ResourceHelper.iconAdd[1]));
		
		interactables.add(new IconButton(this, xPosition - (width / 2) + 860, yPosition - (height / 2) + 490, 24, 24, 0xFFFFFFFF, ResourceHelper.iconSend[3]));
		
		interactables.add(new TextBoxAlt(this, "Message", xPosition + (width / 2) - 640, yPosition - (height / 2) + 482F, 575, 40, FontHandler.ROUNDED.get(20), Utilities.reAlpha(0xFFFFFFFF, 0.4F), 0xFFFFFFFF, Align.LEFT, Align.CENTER, false));
	}
	
	boolean hasScrolled = false;
	
	public void update(int mx, int my) {
		if (popup == null) {
			super.update(mx, my);
		}		
		
		boolean lanButton = (mc.isSingleplayer() && !mc.getIntegratedServer().getPublic());
		sendHover = ((IconButton)interactables.get(lanButton ? 7 : 6)).hover;
		anim.update(sendHover);
		
		for (Party party : KiLO.getKiLO().getPartyManager().getParties()) {
			if (party.partyID == this.party.partyID) {
				this.party = party;
			}
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
		
		messagesSlotList.x = xPosition - (width / 2) + 250;
		messagesSlotList.y = yPosition + (height / 2) - 8 - (48 * 4) - messagesSlotList.oy - 185;
		messagesSlotList.w = 628;
		messagesSlotList.h = 48 * 4 + 133;
		
		 new Thread(){
				@Override
				public void run() {
					if(!hasScrolled) {
						try {
							this.sleep(500L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						messagesSlotList.scrollTo = messagesSlotList.h;
						hasScrolled = true;
					}
				}
			}.start();
		
		if (friendsSlotList.slots.size() != FriendManager.getSize()) {
			friendsSlotList.slots.clear();
			int i = 0;
			for(Friend f : FriendManager.getList()) {
				friendsSlotList.slots.add(new FriendSlot(friendsSlotList, FriendManager.getIndex(f), friendsSlotList.x, friendsSlotList.y, friendsSlotList.w, 48, 0, i * 48));
				i++;
			}
		}
		
		if (partiesSlotList.slots.size() != KiLO.getKiLO().getPartyManager().getParties().size()) {
			partiesSlotList.slots.clear();
			int i = 0;
			for(Party p : KiLO.getKiLO().getPartyManager().getParties()) {
				partiesSlotList.slots.add(new PartySlot(partiesSlotList, KiLO.getKiLO().getPartyManager().getParties().indexOf(p), partiesSlotList.x, partiesSlotList.y, partiesSlotList.w, 48, 0, i * 48));
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
		
		messagesSlotList.slots.clear();
		int i1 = 36;
		for(Party.PartyMessage m : this.party.messages) {
			PartyMessageSlot messageSlot = new PartyMessageSlot(messagesSlotList, m, messagesSlotList.x, messagesSlotList.y, messagesSlotList.w, 36F, 0F, i1);
			messagesSlotList.slots.add(messageSlot);
			i1+=36*messageSlot.lines.size();
		}
		messagesSlotList.slots.add(new ProfileMessageSlot(messagesSlotList, new Message("", 1), messagesSlotList.x, messagesSlotList.y, messagesSlotList.w, 0F, 0F, i1));

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
				KiLO.getKiLO().getUIHandler().setCurrentUI(this.parent);
				break;
			case 1:
				mc.world.sendQuittingDisconnectingPacket();
				mc.loadWorld((WorldClient)null);
				mc.displayGuiScreen(new GuiMainMenu());
				KiLO.getKiLO().getPlayerHandler().playerMap.clear();
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
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.ORDER);
					}
				}.start();
				break;
			case 6:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.PARTY);
					}
				}.start();
				break;
			case 7:
				submit();
				break;
			}
		} else {
			switch(interactables.indexOf(i)) {
			case 0:
				KiLO.getKiLO().getUIHandler().setCurrentUI(this.parent);
				break;
			case 1:
				mc.world.sendQuittingDisconnectingPacket();
				mc.loadWorld((WorldClient)null);
				mc.displayGuiScreen(new GuiMainMenu());
				KiLO.getKiLO().getPlayerHandler().playerMap.clear();
				break;
			case 2:
				mc.displayGuiScreen(new GuiOptions(mc.currentScreen, mc.gameSettings));
				break;
			case 3:
				changePopup(new UIPopupAddFriend(this));
				break;
			case 4:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.ORDER);
					}
				}.start();
				break;
			case 5:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.PARTY);
					}
				}.start();
				break;
			case 6:
				submit();
				break;
			}
		}
	}
	
	public void submit() {
		final String msg = ((TextBox)interactables.get(mc.isSingleplayer() && !mc.getIntegratedServer().getPublic() ? 8 : 7)).text;
		
		if (msg != null && msg.length() > 0) {
			sending = true;
			new Thread() {
				@Override
				public void run() {
					APIHelper.sendPartyMessage(KiLO.getKiLO().getUserControl().clientID, party.partyID, ((TextBoxAlt)interactables.get(mc.isSingleplayer() && !mc.getIntegratedServer().getPublic() ? 8 : 7)).getText());
					UpdateManager.updateParties();
					((TextBox)interactables.get(mc.isSingleplayer() && !mc.getIntegratedServer().getPublic() ? 8 : 7)).text = "";
					
					Timer updateBuffer = new Timer();
					if (updateBuffer.isTime(2))
						sending = false;
				}
			}.start();
			
			 new Thread(){
					@Override
					public void run() {
						try {
							this.sleep(500L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						messagesSlotList.scrollTo = messagesSlotList.h;
					}
			 }.start();
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
			messagesSlotList.mouseScroll(s);
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
			}
			super.keyTyped(key, keyChar);
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
		
		GuiHelper.drawRectangle(xPosition - (width / 2), yPosition - (height / 2), xPosition + (width / 2), yPosition - (height / 2) + 48, Utilities.reAlpha(KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), 1f * opacity));

		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), xPosition - (width / 2) + 8, yPosition - (height / 2) + 56, "Friends:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), xPosition - (width / 2) + 8, yPosition - (height / 2) + 84 + friendsSlotList.h, "Parties:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), xPosition - (width / 2) + 8, yPosition + (height / 2) - 2 - (FontHandler.ROUNDED_BOLD.get(12).getHeight() * 1.5f) - (48 * 4) + 20, "Latest Activity:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
		
		float xP = Display.getWidth()/2-640;
		float yP = Display.getHeight()/2-400;
		
		try {
			if (party.partyImage != null) {
				if (party.partyImage.getTexture() != null) {
					GuiHelper.drawTexturedRectangle(xP+406, yP+200, 60, 60, party.partyImage.getTexture(), opacity);
				}
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		
		if (party.partyName != null)
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), xP+475, yP+210, party.partyName, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity));
		if (party.description != null)
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), xP+475, yP+230, party.description, Utilities.reAlpha(KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), opacity));
		
		GuiHelper.drawLine(xP+406, yP+275, xP+1075, yP+275, Utilities.reAlpha(KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), opacity), 1F);
		
		GuiHelper.drawRectangle(xPosition + (width / 2) - 700, yPosition - (height / 2) + 475, xPosition + (width / 2), yPosition - (height / 2) + 529.5F, Utilities.reAlpha(0xFF323232, 0.7f * opacity));
		GuiHelper.drawRectangle(xPosition + (width / 2) - 56, yPosition - (height / 2) + 475, xPosition + (width / 2), yPosition - (height / 2) + 529.5F, Utilities.reAlpha(KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), 1f - (float)anim.getValue()*0.4f));
		
		
		if (KiLO.getKiLO().getUserControl().kiloHead.getTexture() != null && !sending) {
			GuiHelper.drawTexturedRectangle(xPosition - (width / 2) + 210, yPosition - (height / 2) + 485, 35, 35, KiLO.getKiLO().getUserControl().kiloHead.getTexture(), 1f * opacity);
		} else {
			GuiHelper.drawLoaderAnimation(xPosition - (width / 2) + 227.5F, yPosition - (height / 2) + 502.5F, 15, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity));
		}
		
		GuiHelper.startClip(xPosition - (this.width / 2) + 8 , yPosition - (this.height / 2) + 78, xPosition - (this.width / 2) + 192 + friendsSlotList.sbw, yPosition - (this.height / 2) + 78 + (48 * 4) - 50);
		friendsSlotList.render(opacity);
		GuiHelper.endClip();
		
		GuiHelper.startClip(xPosition - (this.width / 2) + 8, yPosition - (height / 2) + 85 + friendsSlotList.h + 17, xPosition - (this.width / 2) + 192 + partiesSlotList.sbw, yPosition - (height / 2) + 85 + friendsSlotList.h + 96);
		partiesSlotList.render(opacity);
		GuiHelper.endClip();

		GuiHelper.startClip(xPosition - (this.width / 2) + 8, yPosition + (this.height / 2) - 8 - (48 * 4) + 20, xPosition - (this.width / 2) + 192 + activitySlotList.sbw, yPosition + (this.height / 2) - 8);
		activitySlotList.render(opacity);
		GuiHelper.endClip();
		
		GuiHelper.startClip(xP+406, yP+280, xP+1085, yP+605);
		messagesSlotList.render(opacity);
		GuiHelper.endClip();
		
		super.render(opacity);
		
		if (popup != null) {
			popup.render(opacity * (Math.max(popupOpacity, 0.05f)));
		}
	}
}