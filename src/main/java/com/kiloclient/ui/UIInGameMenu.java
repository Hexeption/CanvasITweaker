package com.kiloclient.ui;

import com.kiloclient.render.FontHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.notification.ActivityManager;
import com.kiloclient.addons.AddonManager;
import com.kiloclient.friend.FriendManager;
import com.kiloclient.notification.UpdateManager;
import com.kiloclient.friend.party.Party;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.Button;
import com.kiloclient.ui.interactable.IconButton;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.ui.interactable.slotlist.part.Activity;
import com.kiloclient.ui.interactable.slotlist.part.Friend;
import com.kiloclient.ui.interactable.slotlist.slot.ActivitySlotFriendAccepted;
import com.kiloclient.ui.interactable.slotlist.slot.ActivitySlotFriendRequest;
import com.kiloclient.ui.interactable.slotlist.slot.ActivitySlotNewMessage;
import com.kiloclient.ui.interactable.slotlist.slot.ActivitySlotNewMessageGroup;
import com.kiloclient.ui.interactable.slotlist.slot.ActivitySlotServerInvite;
import com.kiloclient.ui.interactable.slotlist.slot.FriendSlot;
import com.kiloclient.ui.interactable.slotlist.slot.PartySlot;
import com.kiloclient.ui.popup.UIPopupAddFriend;
import com.kiloclient.ui.popup.UIPopupInviteFriends;
import com.kiloclient.ui.popup.UIPopupMessageCreate;
import com.kiloclient.users.User;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Utilities;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.multiplayer.WorldClient;

public class UIInGameMenu extends UI {

	private UI popup, popupTo;
	private boolean popupFade;
	private float xPosition, yPosition, width, height, popupOpacity;

	public SlotList friendsSlotList;
	public SlotList partiesSlotList;
	public SlotList activitySlotList;
	
	public boolean queue;
	
	public UIInGameMenu(UI parent, boolean queue) {
		super(parent);
		this.queue = queue;
	}
	
	@Override
	public void init() {
		
		if (queue && !KiLO.getKiLO().getUserControl().isPremium) {
			KiLO.getKiLO().getUIHandler().setCurrentUI(new UIInGameMenuQueue(this));
		}
		
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
		
		title = "Me";
		
		xPosition = Display.getWidth() / 2;
		yPosition = (Display.getHeight() / 2);
		width = 900;
		height = 530;

		friendsSlotList = new SlotList(3F);
		partiesSlotList = new SlotList(3F);
		activitySlotList = new SlotList(3F);
		
		interactables.clear();
		float xx = xPosition - (width / 2) + 20;
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 12, 24, 24, 0x00000000, ResourceHelper.iconBack[2]));

		xx = xPosition + (width / 2) - 44;
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 12, 24, 24, 0x00000000, ResourceHelper.iconExit[2]));
		xx -= 32;
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 16, 16, 16, 0x00000000, ResourceHelper.iconSettings[1]));
		
		final float mw = (xPosition + (width / 2) - 16) - (xPosition - (width / 2) + 216);
		int i = 0;
		
		interactables.add(new Button(this, "Invite Friend", xPosition - (width / 2) + 510 + (((mw / 4)) * (i++)), yPosition - (height / 2) + 400, (mw / 4) + 7, 32, FontHandler.ROUNDED_LIGHT.get(12), 0x161616, null, 0, Align.CENTER, Align.CENTER));
		((Button)interactables.get(interactables.size() - 1)).enabled = !(mc.isSingleplayer() || (mc.getIntegratedServer() != null && mc.getIntegratedServer().getPublic()));
		interactables.add(new Button(this, "Customise Apperance", xPosition - (width / 2) + 224 + (((mw / 4)) * (i++)), yPosition - (height / 2) + 112, (mw / 4) + 10, 0, FontHandler.ROUNDED_BOLD.get(12), ColorHelper.GREY.getColorCode(), null, 0, Align.CENTER, Align.CENTER));
		interactables.add(new Button(this, "New Private Message", xPosition - (width / 2) + 530 + (((mw / 4))), yPosition - (height / 2) + 400, (mw / 4) + 8, 32, FontHandler.ROUNDED_LIGHT.get(12), 0x161616, null, 0, Align.CENTER, Align.CENTER));
		interactables.add(new Button(this, (String)null, xPosition - (width / 2) + 224 + (((mw / 4)) * (i++)), yPosition - (height / 2) + 112, (mw / 4) - 16, 0, FontHandler.ROUNDED_BOLD.get(12), ColorHelper.GREY.getColorCode(), null, 0, Align.CENTER, Align.CENTER));

		interactables.add(new IconButton(this, xPosition - (width / 2) + 200 - 17, yPosition - (height / 2) + 60, 8, 8, KiLO.getKiLO().getColorSchemeHandler().getCurrentForeground(), ResourceHelper.iconPlus[0]));
		interactables.add(new IconButton(this, xPosition + (width / 2) - 52, yPosition + (height / 2) - 160 - (48 * 4), 24, 24, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconWidgets[3]));
		
		if (mc.isSingleplayer() && !mc.getIntegratedServer().getPublic()) {
			xx -= 30;
			interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 16, 16, 16, 0x00000000, ResourceHelper.iconWifi[1]));
		}
		xx -= 30; 
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 16, 16, 16, 0x00000000, ResourceHelper.iconPizza[1]));
		
		interactables.add(new IconButton(this, xPosition - (width / 2) + 200 - 19, yPosition - (height / 2) + 228, 12, 12, KiLO.getKiLO().getColorSchemeHandler().getCurrentForeground(), ResourceHelper.iconAdd[1]));
		
		interactables.add(new IconButton(this, xPosition - (width / 2) + 800, yPosition + (height / 2) - 50 - (48 * 4), 16, 16, KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), ResourceHelper.iconPrev[1]));
		interactables.add(new IconButton(this, xPosition - (width / 2) + 820, yPosition + (height / 2) - 50 - (48 * 4), 16, 16, KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), ResourceHelper.iconPlay[1]));
		interactables.add(new IconButton(this, xPosition - (width / 2) + 840, yPosition + (height / 2) - 50 - (48 * 4), 16, 16, KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), ResourceHelper.iconNext[1]));
		
		interactables.add(new IconButton(this, xPosition - (width / 2) + 418, yPosition + (height / 2) - (48 * 1) - 30, 75, 20, Utilities.reAlpha(0xFFc4c4c4, 1f), ResourceHelper.customise));
		
		new Thread() {
			@Override
			public void run() {
				APIHelper.getStatus();
			}
		}.start();
		((Button)interactables.get(6)).text = APIHelper.hideStatus;
	}
	
	public void update(int mx, int my) {
		if (popup == null) {
			super.update(mx, my);
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
		
		if (mc.isSingleplayer() && !mc.getIntegratedServer().getPublic()) {
			((IconButton)interactables.get(13)).icon = KiLO.getKiLO().getMusicHandler().isPlaying() ? ResourceHelper.iconPause[1] : ResourceHelper.iconPlay[1];
		} else {
			((IconButton)interactables.get(12)).icon = KiLO.getKiLO().getMusicHandler().isPlaying() ? ResourceHelper.iconPause[1] : ResourceHelper.iconPlay[1];
		}
		
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
				if (a.type !=null)
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
		
		((Button)interactables.get(6)).text = APIHelper.hideStatus;
		
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
				mc.displayGuiScreen(null);
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
				changePopup(new UIPopupInviteFriends(this));
				break;
			case 4:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.ADDONS_MANAGE);
					}
				}.start();
				break;
			case 5:
				changePopup(new UIPopupMessageCreate(this));
				break;
			case 6:
				new Thread() {
					@Override
					public void run() {
						APIHelper.hideStatus();
					}
				}.start();
				break;
			case 7:
				changePopup(new UIPopupAddFriend(this));
				break;
			case 8:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.WIDGETS);
					}
				}.start();
				break;
			case 9:
	            mc.displayGuiScreen(new GuiShareToLan(mc.currentScreen));
				break;
			case 10:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.ORDER);
					}
				}.start();
				break;
			case 11:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.PARTY);
					}
				}.start();
				break;
			case 12:
				KiLO.getKiLO().getMusicHandler().skipToPrevious();
				break;
			case 13:
				if (KiLO.getKiLO().getMusicHandler().isPlaying()) {
					KiLO.getKiLO().getMusicHandler().pauseCurrentSong();
				} else {
					KiLO.getKiLO().getMusicHandler().resumeCurrentSong();;
				}
				break;
			case 14:
				KiLO.getKiLO().getMusicHandler().skipToNext();
				break;
			case 15:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.CUSTOMISE);
					}
				}.start();
				break;
			}
		} else {
			switch(interactables.indexOf(i)) {
			case 0:
				mc.displayGuiScreen(null);
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
				changePopup(new UIPopupInviteFriends(this));
				break;
			case 4:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.ADDONS_MANAGE);
					}
				}.start();
				break;
			case 5:
				changePopup(new UIPopupMessageCreate(this));
				break;
			case 6:
				new Thread() {
					@Override
					public void run() {
						APIHelper.hideStatus();
					}
				}.start();
				break;
			case 7:
				changePopup(new UIPopupAddFriend(this));
				break;
			case 8:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.WIDGETS);
					}
				}.start();
				break;
			case 9:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.ORDER);
					}
				}.start();
				break;
			case 10:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb(APIHelper.PARTY);
					}
				}.start();
				break;
			case 11:
				KiLO.getKiLO().getMusicHandler().skipToPrevious();
				break;
			case 12:
				if (KiLO.getKiLO().getMusicHandler().isPlaying()) {
					KiLO.getKiLO().getMusicHandler().pauseCurrentSong();
				} else {
					KiLO.getKiLO().getMusicHandler().resumeCurrentSong();;
				}
				break;
			case 13:
				KiLO.getKiLO().getMusicHandler().skipToNext();
				break;
			case 14:
				new Thread() {
					@Override
					public void run() {
						Utilities.openWeb("");
					}
				}.start();
				break;
			}
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (popup == null) {
			super.mouseClick(mx, my, b);
			friendsSlotList.mouseClick(mx, my, b);
			partiesSlotList.mouseClick(mx, my, b);
			activitySlotList.mouseClick(mx, my, b);
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
	
			
			if (mc.world != null && mc.world.getEntityByID( - 3) != null) {
				float size = 200;
				User user = AddonManager.users.get(mc.world.getEntityByID( - 3).getDisplayName().getUnformattedText());
	            if (user != null) {
	            	if (user.sizeEnabled) {
	            		size /= user.size;
	            	}
	            }
				float ratio = 200 / 150;
				float ratio2 = 64 / 150;
				
				GuiHelper.startClip(xPosition - (width / 2) + 200, yPosition - (height / 2) + 47, xPosition + (width / 2), yPosition + (height / 2));
				GuiHelper.drawEntityOnScreen((int)(xPosition - (width / 2) + 350), (int)(yPosition + 220), (int)size, ((xPosition - (width / 2) + 350) - KiLO.getKiLO().getUIHandler().mouse[0]) / 4, ((yPosition) - (KiLO.getKiLO().getUIHandler().mouse[1])) / 2, (EntityOtherPlayerMP)mc.world.getEntityByID( - 3), Utilities.reAlpha(0xFFFFFFFF, 1f * opacity));
				GuiHelper.endClip();
			} else {
				if (mc.world != null) {
					EntityOtherPlayerMP a;
					mc.world.addEntityToWorld( - 3, a = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile()));
					a.setPositionAndUpdate(0,  - 100, 0);
				}
			}
			
			GuiHelper.drawRectangle(xPosition - (width / 2), yPosition - (height / 2), xPosition + (width / 2), yPosition - (height / 2) + 48, Utilities.reAlpha(KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), 1f * opacity));
			
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), xPosition - (width / 2) + 8, yPosition - (height / 2) + 56, "Friends:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
			
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), xPosition - (width / 2) + 8, yPosition - (height / 2) + 84 + friendsSlotList.h, "Parties:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
			
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), xPosition - (width / 2) + 8, yPosition + (height / 2) - 2 - (FontHandler.ROUNDED_BOLD.get(12).getHeight() * 1.5f) - (48 * 4) + 20, "Latest Activity:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
			
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(25), xPosition, yPosition - (height / 2) + 24, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1F * opacity), Align.CENTER, Align.CENTER);
			
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), xPosition - (width / 2) + 508, yPosition + (height / 2) - 80 - (48 * 4) - 100, "Hi " + KiLO.getKiLO().getUserControl().kiloName, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
			//GuiHelper.drawTexturedRectangle(xPosition - (width / 2) + 418, yPosition + (height / 2) - 10 - (48 * 1) - 100, 60, 16, ResourceHelper.customise, Utilities.reAlpha(0xFFc4c4c4, 1f));
			
			if (KiLO.getKiLO().getMusicHandler().getCurrentSong() != null) {
				GuiHelper.drawRectangle(xPosition - (width / 2) + 508, yPosition + (height / 2) - 80 - (48 * 4), xPosition - (width / 2) + 870, yPosition + (height / 2) - 80 - (48 * 4) + 75, Utilities.reAlpha(0xFF161616, opacity));
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), xPosition - (width / 2) + 595, yPosition + (height / 2) - 70 - (48 * 4), "Now Playing", Utilities.reAlpha(ColorHelper.DARK_GREY.getColorCode(), opacity));
				if (KiLO.getKiLO().getMusicHandler().getCurrentSong().image != null) {
					GuiHelper.drawTexturedRectangle(xPosition - (width / 2) + 508, yPosition + (height / 2) - 80 - (48 * 4), 75, 75, KiLO.getKiLO().getMusicHandler().getCurrentSong().image.getTexture());
				}
				String trimmed = GuiHelper.trimStringToWidth(FontHandler.ROUNDED.get(14), KiLO.getKiLO().getMusicHandler().getCurrentSong().title, 200, true);
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), xPosition - (width / 2) + 595, yPosition + (height / 2) - 50 - (48 * 4), trimmed, Utilities.reAlpha(KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), opacity));
			} else {
				GuiHelper.drawRectangle(xPosition - (width / 2) + 508, yPosition + (height / 2) - 80 - (48 * 4), xPosition - (width / 2) + 870, yPosition + (height / 2) - 80 - (48 * 4) + 75, Utilities.reAlpha(0xFF161616, opacity));
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(25), xPosition - (width / 2) + 555, yPosition + (height / 2) - 60 - (48 * 4), "No Music Playing", Utilities.reAlpha(ColorHelper.DARK_GREY.getColorCode(), opacity));
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
			
			super.render(opacity);
			
			if (popup != null) {
				popup.render(opacity * (Math.max(popupOpacity, 0.05f)));
			}
	}
}