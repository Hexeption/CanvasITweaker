package com.kiloclient.ui.interactable.slotlist.slot;

import com.kiloclient.KiLO;
import com.kiloclient.notification.HistoryManager;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.UIHistory;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.ui.popup.UIPopupHistory;
import com.kiloclient.users.Player;
import com.kiloclient.utilities.Utilities;

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
			if (KiLO.getKiLO().getUIHandler().getCurrentUI() instanceof UIHistory) {
				((UIHistory)KiLO.getKiLO().getUIHandler().getCurrentUI()).changePopup(new UIPopupHistory(KiLO.getKiLO().getUIHandler().getCurrentUI()));
			} else if (KiLO.getKiLO().getUIHandler().getUITo() instanceof UIHistory) {
				((UIHistory)KiLO.getKiLO().getUIHandler().getUITo()).changePopup(new UIPopupHistory(KiLO.getKiLO().getUIHandler().getCurrentUI()));
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
