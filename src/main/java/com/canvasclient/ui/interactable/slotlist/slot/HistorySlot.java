package com.canvasclient.ui.interactable.slotlist.slot;

import com.canvasclient.Canvas;
import com.canvasclient.notification.HistoryManager;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.UIHistory;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.ui.popup.UIPopupHistory;
import com.canvasclient.users.Player;
import com.canvasclient.utilities.Utilities;

public class HistorySlot extends Slot {
	
	public Player player;
	
	public HistorySlot(SlotList p, Player pl, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		player = pl;
		
		interactables.clear();
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (parent.mouseOver(mx, my) && hover) {
			HistoryManager.current = player;
			if (Canvas.getCanvas().getUIHandler().getCurrentUI() instanceof UIHistory) {
				((UIHistory) Canvas.getCanvas().getUIHandler().getCurrentUI()).changePopup(new UIPopupHistory(Canvas.getCanvas().getUIHandler().getCurrentUI()));
			} else if (Canvas.getCanvas().getUIHandler().getUITo() instanceof UIHistory) {
				((UIHistory) Canvas.getCanvas().getUIHandler().getUITo()).changePopup(new UIPopupHistory(Canvas.getCanvas().getUIHandler().getCurrentUI()));
			}
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, hoverOpacity*opacity));
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, activeOpacity*opacity));
		String name = player.gameProfile.getName();
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
