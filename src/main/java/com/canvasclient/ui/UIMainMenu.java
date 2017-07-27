package com.canvasclient.ui;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.friend.FriendManager;
import com.canvasclient.infrastructure.ChatManager;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.Link;
import com.canvasclient.ui.interactable.slotlist.part.Friend;
import com.canvasclient.utilities.Utilities;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiWorldSelection;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import java.util.ArrayList;
import java.util.List;

public class UIMainMenu extends UI {

	public UIMainMenu(UI parent) {
		super(parent);
	}
	
	boolean changed = false;
	@Override
	public void init() {
		interactables.clear();
		int i = 0;
		interactables.add(new IconButton(this, Display.getWidth() - 80 - (120 * (i ++ )), 32, 48, 48, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconExit[4]));
		interactables.add(new IconButton(this, Display.getWidth() - 80 - (120 * (i ++ )), 32, 48, 48, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconSettings[4]));
		interactables.add(new IconButton(this, Display.getWidth() - 80 - (120 * (i ++ )), 32, 48, 48, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconMultiplayer[4]));
		interactables.add(new IconButton(this, Display.getWidth() - 80 - (120 * (i ++ )), 32, 48, 48, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconSingleplayer[4]));
		interactables.add(new Link(this, "Pods", (Display.getWidth() /  2) - (FontHandler.ROUNDED_BOLD.get(14).getWidth("  -  Forums  -  ") /  2), Display.getHeight() - 30, FontHandler.ROUNDED_BOLD.get(12), ColorHelper.WHITE.getColorCode(), Align.RIGHT, Align.BOTTOM));
		interactables.add(new Link(this, "Forums", Display.getWidth() /  2, Display.getHeight() - 30, FontHandler.ROUNDED_BOLD.get(12), ColorHelper.WHITE.getColorCode(), Align.CENTER, Align.BOTTOM));
		interactables.add(new Link(this, "Help", (Display.getWidth() /  2) + (FontHandler.ROUNDED_BOLD.get(14).getWidth("  -  Forums  -  ") /  2), Display.getHeight() - 30, FontHandler.ROUNDED_BOLD.get(12), ColorHelper.WHITE.getColorCode(), Align.LEFT, Align.BOTTOM));
		interactables.add(new Link(this, "Go to MyCanvas", 110, Display.getHeight() - 94 + (32) + (FontHandler.ROUNDED_BOLD.get(12).getHeight() /  2), FontHandler.ROUNDED_LIGHT.get(12), 0xFFFFFFFF, Align.LEFT, Align.TOP));
		if (Canvas.getCanvas().outOfDate())
			interactables.add(new Link(this, "New Update Available", Display.getWidth() /  2 - 85, Display.getHeight() - 100, FontHandler.ROUNDED.get(16), 0xFFFFFFFF, Align.LEFT, Align.TOP));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		
		if (ChatManager.chatLines.size() > 0) {
			ChatManager.chatLines.clear();
		}
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch (interactables.indexOf(i)) {
		case 0:
			mc.shutdown();
			break;
		case 1:
            mc.displayGuiScreen(new GuiOptions(mc.currentScreen, mc.gameSettings));
            break;
		case 2:
            mc.displayGuiScreen(new GuiMultiplayer(mc.currentScreen));
            break;
		case 3:
            mc.displayGuiScreen(new GuiWorldSelection(mc.currentScreen));
            break;
		case 4:
			Utilities.openWeb(APIHelper.PODS);
			break;
		case 5:
			Utilities.openWeb(APIHelper.FORUMS);
			break;
		case 6:
			Utilities.openWeb(APIHelper.HELP);
			break;
		case 7:
			Utilities.openWeb(APIHelper.ACCOUNT);
			break;
		case 8:
			if (Canvas.getCanvas().outOfDate()) {
				Utilities.openWeb(Canvas.getCanvas().getUserControl().currentVersionURL);
			}
			break;
		}
	}
	
	public void render(float opacity) {
		Texture logo = null;
		float w;
		float h;
		if (Display.getHeight() < 800 || Display.getWidth() < 1400){
			w = 702;
			h = 200;
			logo = ResourceHelper.brandingSmaller;
		}
		else {
			w = 1180;
			h = 325;
			logo = ResourceHelper.branding;
		}

		float x = Math.round((Display.getWidth() /  2) - (w /  2));
		float y = Math.round((Display.getHeight() /  2)  -  (h /  2));
		GuiHelper.drawTexturedRectangle(x, y, w, h, logo, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1F  *  opacity));
		GuiHelper.drawRoundedRectangle((Display.getWidth() /  2)  -  (FontHandler.ROUNDED_BOLD.get(14).getWidth(" -  Forums  - ") /  2)  -  FontHandler.ROUNDED_BOLD.get(14).getWidth("Pods")  -  15, Display.getHeight() - 52, (Display.getWidth() /  2)  +  (FontHandler.ROUNDED_BOLD.get(14).getWidth(" -  Forums  - ") /  2)  +  FontHandler.ROUNDED_BOLD.get(14).getWidth("Help")  +  15, Display.getHeight() - 22, Utilities.reAlpha(0xFF2F2F2F, 0.71f));

		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(40), (Display.getWidth() /  2) - (FontHandler.ROUNDED_BOLD.get(14).getWidth(" -  Forums  - ") /  2) + 1, Display.getHeight() - 30 - FontHandler.ROUNDED_BOLD.get(14).getHeight() - 12, "\u00B7", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(40), (Display.getWidth() /  2) + (FontHandler.ROUNDED_BOLD.get(14).getWidth("Forums  - ") /  2) - 3, Display.getHeight() - 30 - FontHandler.ROUNDED_BOLD.get(14).getHeight() - 12, "\u00B7", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), Display.getWidth() - 20, Display.getHeight() - 20 - FontHandler.ROUNDED_BOLD.get(12).getHeight(), "Copyright 2017 Team Canvas - All rights reserved!", Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1f * opacity), Align.RIGHT, Align.BOTTOM);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), Display.getWidth() - 20, Display.getHeight() - 20, "Copyright Mojang AB. Do not distribute!", Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), 1f * opacity), Align.RIGHT, Align.BOTTOM);
		
		String name = Canvas.getCanvas().getUserControl().canvasName;
		String nametag = Canvas.getCanvas().getUserControl().ircTag;
		boolean premium = Canvas.getCanvas().getUserControl().isPremium;
		List<Friend> friends = FriendManager.getList();
		List<Friend> friendsOnline = new ArrayList<Friend>();

		for(Friend f : friends) {
			if (!(f.status.equalsIgnoreCase("offline"))) {
				friendsOnline.add(f);
			}
		}
		
		String[] accountInfo = new String[] {nametag + " " + name, friends.size() + " Friends (" + friendsOnline.size() + " Online)", "Go to MyCanvas"};
		float maxW = 0;
		for(String s : accountInfo) {
			float curW = FontHandler.ROUNDED_BOLD.get(12).getWidth(s);
			if (s.equalsIgnoreCase(accountInfo[0])) {
				curW = FontHandler.ROUNDED_BOLD.get(14).getWidth(s);
			}
			if (curW > maxW) {
				maxW = curW;
			}
		}

		float pad = 10f;
		w = 64;
		h = 64;
		x = 30;
		y = Display.getHeight() - 30 - h;

		GuiHelper.drawRoundedRectangle(x - pad, y - pad, x + w + 30 + maxW + pad, y + h + pad, Utilities.reAlpha(0xFF2F2F2F, 0.71f * opacity));

		if (Canvas.getCanvas().getUserControl().canvasHead.getTexture() != null) {
			GuiHelper.drawTexturedRectangle(x, y, w, h, Canvas.getCanvas().getUserControl().canvasHead.getTexture(), 1f * opacity);
		}

		int a = (int)(255 * opacity);
		String displayName = nametag == null || nametag.length() == 0 ? name : nametag  +  " "  +  name;

		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), x + w + 16, y + (h /  2) - (FontHandler.ROUNDED_BOLD.get(12).getHeight() /  2), "Hi "  +  displayName, Utilities.reAlpha(0xFFFFFFFF, 1f * opacity), Align.LEFT, Align.BOTTOM);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(12), x + w + 16, y + (h /  2), friends.size() + " Friends", Utilities.reAlpha(0xFFCCCCCC, 1f * opacity), Align.LEFT, Align.CENTER);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(12), x + w + 16 +  FontHandler.ROUNDED_BOLD.get(12).getWidth(friends.size() + " Friends"), y + (h /  2), (friendsOnline.size() >= 0?(" (" + friendsOnline.size() + " Online)"):""), Utilities.reAlpha(0xFFCCCCCC, 1f * opacity), Align.LEFT, Align.CENTER);

		if (premium) {
			GuiHelper.drawTexturedRectangle(x + w + ((w + (pad * 2)) /  2) - 50, y + 13 + ((h + (pad * 2)) /  2), 16, 16, ResourceHelper.iconShield[3], Utilities.reAlpha(ColorHelper.YELLOW.getColorCode(), 1f * opacity));
		}
		
		super.render(opacity);
	}
}
