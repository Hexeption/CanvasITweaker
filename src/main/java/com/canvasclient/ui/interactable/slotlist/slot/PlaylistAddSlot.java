package com.canvasclient.ui.interactable.slotlist.slot;

import com.canvasclient.Canvas;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.UIMusic;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.ui.popup.UIPopupPlaylistSelect;
import com.canvasclient.utilities.Utilities;

public class PlaylistAddSlot extends Slot {
	
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
			if (((UIMusic) Canvas.getCanvas().getUIHandler().getCurrentUI()).popup instanceof UIPopupPlaylistSelect) {
				((UIPopupPlaylistSelect)((UIMusic) Canvas.getCanvas().getUIHandler().getCurrentUI()).popup).add(Canvas.getCanvas().getMusicHandler().getPlaylists().get(index));
			}
		}
	}
	
	@Override
	public void handleInteraction(final Interactable i) {
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, hoverOpacity*opacity));
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, activeOpacity*opacity));
		String name = Canvas.getCanvas().getMusicHandler().getPlaylists().get(index).name;
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
