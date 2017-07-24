package com.canvasclient.ui;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.infrastructure.WorldManager;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.render.utilities.TextureImage;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.interactable.Button;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.ui.interactable.slotlist.slot.Slot;
import com.canvasclient.ui.interactable.slotlist.slot.WorldSlot;
import com.canvasclient.ui.popup.UIPopupWorldRename;
import com.canvasclient.utilities.Timer;
import com.canvasclient.utilities.Utilities;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;


public class UISingleplayer extends UI {

	private UI popup, popupTo;
	private boolean popupFade;

	private float formOffset;
	private boolean invalid, deleting, loadingWorld;

	private String invalidMessage = "";

	private Timer invalidTimer = new Timer();

	private int selectedIndex;
	private float fX, fY, fW, fH, popupOpacity;

	public SlotList wsl;

	public TextureImage model;

	public UISingleplayer(UI parent) {
		super(parent);
	}

	@Override
	public void init() {
		title = "Singleplayer";

		model = ResourceHelper.downloadTexture(String.format(APIHelper.PLAYER_MODEL, Canvas.getCanvas().getUserControl().minecraftName));

		formOffset = 0;
		invalid = false;

		WorldManager.loadWorlds();
		wsl = new SlotList(6f);

		fX = Display.getWidth() / 1.5f + 32;
		fY = (Display.getHeight() / 2) + 44;
		fW = Display.getWidth() / 1.5f - 38 - 64;
		fH = Display.getHeight() - 124;

		interactables.clear();
		int i = 0;
		interactables.add(new IconButton(this, 32 + (64 * (i ++ )), 32, 32, 32, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconReturn[3]));
		interactables.add(new Button(this, "Play Selected World", fX - (fW / 2) + 24, fY + (fH / 2) - 24 - 48 - 12 - 48, (fW / 2) - 48, 48, FontHandler.ROUNDED_BOLD.get(20), Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), null, 0));
		interactables.add(new Button(this, "Create New World", fX + 24, fY + (fH / 2) - 24 - 48 - 12 - 48, (fW / 2) - 48, 48, FontHandler.ROUNDED_BOLD.get(20), Canvas.getCanvas().getColorSchemeHandler().getCurrentForeground(), null, 0));
		interactables.add(new Button(this, "Rename", fX - (fW / 2) + 24, fY + (fH / 2) - 24 - 48, ((fW / 2) - 48) / 2 - 12, 48, FontHandler.ROUNDED_BOLD.get(20), 0xFF303030, null, 0));
		interactables.add(new Button(this, "Map Store", (((fX + (fW / 2) - 48) / 2) + 24) + 80, fY + (fH / 2) - 24 - 48, ((fW / 2) - 48) / 2 - 12, 48, FontHandler.ROUNDED_BOLD.get(20), 0xFF303030, null, 0));
		interactables.add(new Button(this, "Re - Create", fX + 24, fY + (fH / 2) - 24 - 48, (((fW / 2) - 48) / 2) - 12, 48, FontHandler.ROUNDED_BOLD.get(20), 0xFF303030, null, 0));
		interactables.add(new Button(this, "Delete", fX + 24 + (((fW / 2) - 48) / 2) + 12, fY + (fH / 2) - 24 - 48, (((fW / 2) - 48) / 2) - 12, 48, FontHandler.ROUNDED_BOLD.get(20), 0xFF303030, null, 0));
	}

	public void update(int mx, int my) {
		if (popup == null) {
			super.update(mx, my);
		}
		if (invalid) {
			formOffset += (( - FontHandler.STANDARD.get(14).getHeight() * 2) - formOffset) / 5f;
			if (invalidTimer.isTime(2f)) {
				invalid = false;
			}
		} else {
			invalidTimer.reset();
		}
		if (!invalid) {
			formOffset += (0 - formOffset) / 5f;
		}

		WorldSummary activeWorld = null;
		for (Slot w : wsl.slots) {
			if (w.active) {
				activeWorld = WorldManager.getWorld(((WorldSlot)w).index);
				break;
			}
		}
		((Button)interactables.get(1)).enabled = activeWorld != null;
		((Button)interactables.get(3)).enabled = activeWorld != null;
		((Button)interactables.get(5)).enabled = activeWorld != null;
		((Button)interactables.get(6)).enabled = activeWorld != null;

		wsl.x = fX - (fW / 2) + 32;
		wsl.y = fY - (fH / 2) + 32 - wsl.oy;
		wsl.w = fW - 64;
		wsl.h = fH - 24 - 48 - 12 - 48 - 24 - 64;

		if (wsl.slots.size() != WorldManager.getSize()) {
			wsl.slots.clear();
			int i = 0;
			for (WorldSummary sfc : WorldManager.getList()) {
				wsl.slots.add(new WorldSlot(wsl, WorldManager.getIndex(sfc), wsl.x, wsl.y, wsl.w, 80, 0, i * 80));
				i ++ ;
			}
		}

		wsl.update(mx, my);

		if (popup != null) {
			popup.update(mx, my);
		}
		popupOpacity += popupFade ? -0.2f : 0.2f;
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
	public void handleInteraction(Interactable i) {
		WorldSummary activeWorld = null;
		int index =  - 1;

		int a = 0;
		for (Slot s : wsl.slots) {
			if (s.active) {
				index = a;
				activeWorld = WorldManager.getWorld(((WorldSlot)s).index);
				break;
			}
			a ++ ;
		}
		switch(interactables.indexOf(i)) {
		case 0:
            mc.displayGuiScreen(new GuiMainMenu());
			break;
		case 1:
			loadWorld(index);
			break;
		case 2:
            mc.displayGuiScreen(new GuiCreateWorld(mc.currentScreen));
			break;
		case 3:
			changePopup(new UIPopupWorldRename(this, activeWorld));
			break;
		case 4:
			Utilities.openWeb(APIHelper.MAP_STORE);
			break;
		case 5:
			if (index !=  - 1) {
                GuiCreateWorld var5 = new GuiCreateWorld(mc.currentScreen);
                ISaveHandler var6 = this.mc.getSaveLoader().getSaveLoader(WorldManager.getWorld(index).getFileName(), false);
                WorldInfo var4 = var6.loadWorldInfo();
                var6.flush();
                var5.recreateFromExistingWorld(var4);
                this.mc.displayGuiScreen(var5);
                ((UICreateWorld) Canvas.getCanvas().getUIHandler().getUITo()).set(var4);
			}
			break;
		case 6:
			removeWorld(activeWorld);
			break;
		}
	}

	public void mouseClick(int mx, int my, int b) {
		if (popup == null) {
			super.mouseClick(mx, my, b);
			wsl.mouseClick(mx, my, b);
		} else {
			popup.mouseClick(mx, my, b);
		}
	}

	public void mouseRelease(int mx, int my, int b) {
		if (popup == null) {
			super.mouseRelease(mx, my, b);
			wsl.mouseRelease(mx, my, b);
		} else {
			popup.mouseRelease(mx, my, b);
		}
	}

	public void mouseScroll(int s) {
		if (popup == null) {
			super.mouseScroll(s);
			wsl.mouseScroll(s);
		} else {
			popup.mouseScroll(s);
		}
	}

	public void keyboardPress(int key) {
		if (popup == null) {
			WorldSummary activeWorld = null;
			int index =  - 1;

			int a = 0;
			for (Slot s : wsl.slots) {
				if (s.active) {
					index = a;
					activeWorld = WorldManager.getWorld(((WorldSlot)s).index);
					break;
				}
				a ++ ;
			}

			super.keyboardPress(key);
			switch (key) {
			case Keyboard.KEY_RETURN:
				break;
			case Keyboard.KEY_DELETE:
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

	public void removeWorld(WorldSummary s) {
        ISaveFormat var3 = this.mc.getSaveLoader();
        var3.flushCache();
        var3.deleteWorldDirectory(s.getFileName());

        WorldManager.loadWorlds();
	}

	public void loadWorld(int index)
    {
        mc.displayGuiScreen((GuiScreen)null);

        if (!loadingWorld)
        {
            loadingWorld = true;
            String var2 = WorldManager.getWorld(index).getFileName();

            if (var2 == null)
            {
                var2 = "World"  +  index;
            }

            String var3 = getWorldName(index);

            if (var3 == null)
            {
                var3 = "World"  +  index;
            }

            if (mc.getSaveLoader().canLoadWorld(var2))
            {
                mc.launchIntegratedServer(var2, var3, (WorldSettings)null);
            }
        }
    }

	public String getWorldName(int index) {
        String var2 = WorldManager.getWorld(index).getDisplayName();

        if (StringUtils.isEmpty(var2))
        {
            var2 = I18n.format("selectWorld.world", new Object[0])  +  " "  +  (index  +  1);
        }

        return var2;
	}

	public void render(float opacity) {
		drawDarkerBackground(false, opacity);

		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth() / 2, fY - (fH / 2) - (FontHandler.ROUNDED_BOLD.get(40).getHeight(title) / 2) - 32, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity), Align.CENTER, Align.CENTER);

		GuiHelper.drawRectangle(fX - (fW / 2), fY - (fH / 2), fX + (fW / 2), fY + (fH / 2), Utilities.reAlpha(0xFF171717, 0.90f * opacity));

		GuiHelper.drawRectangle(fX - (fW / 2) + 24, fY + (fH / 2) - 24 - 48 - 12 - 48 - 26, fX + (fW / 2) - 24, fY + (fH / 2) - 24 - 48 - 12 - 48 - 24, Utilities.reAlpha(0xFF363636, 0.90f * opacity));

		if (Canvas.getCanvas().getUserControl().minecraftPlayer.head.getTexture() != null) {
			Texture tex = Canvas.getCanvas().getUserControl().minecraftPlayer.head.getTexture();
			GuiHelper.drawTexturedRectangle(Display.getWidth() - 20 - 64, 20, 64, 64, tex, 1f * opacity);

			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), Display.getWidth() - 20 - 64 - 16, 20 + 39 - (FontHandler.ROUNDED_BOLD.get(20).getHeight() / 2), "Logged in as", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity), Align.RIGHT, Align.BOTTOM);
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth() - 20 - 64 - 16, 20 + 40, Canvas.getCanvas().getUserControl().minecraftPlayer.gameProfile.getName(), Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f * opacity), Align.RIGHT, Align.CENTER);
		}

		GuiHelper.startClip(fX - (fW / 2) + 32, fY - (fH / 2) + 32, fX + (fW / 2) - 31 + wsl.sbw, fY + (fH / 2) - 24 - 48 - 12 - 48 - 24 - 32);
		wsl.render(opacity);
		GuiHelper.endClip();

		// FIXME: 22/07/2017
//        GuiHelper.drawEntityOnScreen((int) (32 + 200), 600, 200, ((fX - (fW)) - Canvas.getCanvas().getUIHandler().mouse[0]) / 4, ((fY) - (Canvas.getCanvas().getUIHandler().mouse[1])) / 2, new EntityFakePlayer("Hexeption"), Utilities.reAlpha(0xFFFFFFFF, 1f * opacity));

        if (model.getTexture() != null) {
			float scale = 0.7f;
			GuiHelper.drawTexturedRectangle(((fX - (fW / 2)) / 2) - (model.getTexture().getImageWidth() / 2 * scale), (Display.getHeight() / 2) - (model.getTexture().getImageHeight() / 2 * scale) + 40, model.getTexture().getImageWidth() * scale, model.getTexture().getImageHeight() * scale, model.getTexture(), 1f * opacity);
		}

		GuiHelper.startClip(fX - (FontHandler.STANDARD.get(14).getWidth(invalidMessage) / 2), fY + (fH / 2) - 40 + (FontHandler.STANDARD.get(14).getHeight() / 2), fX + (FontHandler.STANDARD.get(14).getWidth(invalidMessage) / 2), fY + (fH / 2) - 40 + (FontHandler.STANDARD.get(14).getHeight() * 1.5f));
		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(14), fX, fY + (fH / 2) - 40 - (FontHandler.STANDARD.get(14).getHeight(invalidMessage)) - formOffset, invalidMessage, Utilities.reAlpha(0xFFFF5555, 1f * opacity), Align.CENTER, Align.CENTER);
		GuiHelper.endClip();

		super.render(opacity);

		if (popup != null) {
			popup.render(opacity * (Math.max(popupOpacity, 0.05f)));
		}
	}
}
