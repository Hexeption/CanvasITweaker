package com.kiloclient.ui.interactable.slotlist.slot;

import com.kiloclient.KiLO;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.UIMusic;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.ui.popup.UIPopupPlaylistSelect;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.Utilities;

import net.minecraft.client.Minecraft;

public class PlaylistAddSlot extends Slot {

	private final Minecraft mc = Minecraft.getMinecraft();
	
	public int index;
	
	public PlaylistAddSlot(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		index = i;
		
		interactables.clear();
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (hover) {
			if (((UIMusic)KiLO.getKiLO().getUIHandler().getCurrentUI()).popup instanceof UIPopupPlaylistSelect) {
				((UIPopupPlaylistSelect)((UIMusic)KiLO.getKiLO().getUIHandler().getCurrentUI()).popup).add(KiLO.getKiLO().getMusicHandler().getPlaylists().get(index));
			}
		}
	}
	
	@Override
	public void handleInteraction(final Interactable i) {
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, hoverOpacity*opacity));
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, activeOpacity*opacity));
		String name = KiLO.getKiLO().getMusicHandler().getPlaylists().get(index).name;
		for(int i = 0; i < name.length(); i++) {
			if (FontHandler.ROUNDED_BOLD.get(14).getWidth(name.substring(0, i)) > parent.w-60- FontHandler.ROUNDED_BOLD.get(14).getWidth("...")) {
				name = name.substring(0, i)+"...";
				break;
			}
		}
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), x+16, y+24, name, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.CENTER);
		super.render(opacity);
	}
	
}
