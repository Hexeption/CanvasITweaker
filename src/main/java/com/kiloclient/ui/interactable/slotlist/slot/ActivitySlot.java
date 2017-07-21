package com.kiloclient.ui.interactable.slotlist.slot;

import com.kiloclient.notification.ActivityManager;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;

public abstract class ActivitySlot extends Slot {
	
	public int index;
	
	public int clicks = 0;
	public Timer clickTimer = new Timer();
	
	public boolean checking;
	
	public ActivitySlot(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		index = i;
		
		clickTimer.reset();
		checking = false;
		
		interactables.clear();
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		hover = mouseOver(mx, my) && parent.mouseOver(mx, my);

		if (clicks == 2) {
			doubleClick(mx, my);
			clicks = 0;
		}
		
		if (clickTimer.isTime(Utilities.doubleClickTimer)) {
			clickTimer.reset();
			clicks = 0;
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (hover) {
			if (clicks == 0) {
				clickTimer.reset();
			}
			clicks++;
		}
		super.mouseClick(mx, my, b);
	}
	
	public void doubleClick(int mx, int my) {}
	
	public abstract void handleInteraction(Interactable i);
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF1A1A1A, hoverOpacity*opacity));
		if (checking) {
			GuiHelper.drawLoaderAnimation(x+(width/2), y+(height/2), 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			return;
		}
		if (ActivityManager.getActivity(index) != null) {
			boolean noIcon = true;
			if (ActivityManager.getActivity(index).icon != null) {
				if (ActivityManager.getActivity(index).icon.getTexture() != null) {
					GuiHelper.drawTexturedRectangle(x+4, y+4, 40, 40, ActivityManager.getActivity(index).icon.getTexture(), opacity);
					noIcon = false;
				}
			}
			if (noIcon) {
				GuiHelper.drawLoaderAnimation(x+24, y+24, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
		}
		super.render(opacity);
	}
	
}
