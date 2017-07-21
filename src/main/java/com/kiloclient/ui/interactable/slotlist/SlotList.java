package com.kiloclient.ui.interactable.slotlist;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.kiloclient.KiLO;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.slotlist.slot.Slot;
import com.kiloclient.utilities.Utilities;

public class SlotList {

	public List<Slot> slots = new CopyOnWriteArrayList<Slot>();
	public float x, y, w, h, ox, oy, scroll, scrollTo, sbx, sby, sbw, sbh, dragY, dragOffsetY;
	public boolean hover;
	protected boolean dragScroll;
	
	public SlotList(float sbw) {
		scroll = 0;
		scrollTo = 0;
		dragScroll = false;
		this.sbw = sbw;
	}

	public void update(int mx, int my) {
		if (String.valueOf(scrollTo).equalsIgnoreCase("nan")) {
			scrollTo = 0;
		}
		float sh = 0;
		float slotsHeight = 0;
		for(Slot s : slots) {
			slotsHeight+= s.height;
		}
		
		if (slotsHeight > h) {
			sh = Math.max(h*(1/(slotsHeight/h)), 20);
		} else {
			scrollTo = 0;
		}
		float sslh = h-sh;
		
		sbx = x+w;
		sby = y+oy+scroll;
		sbh = sh;
		
		if (dragScroll) {
			scrollTo = Math.min(Math.max(0, my-dragOffsetY), sslh);
			scroll = scrollTo;
		}
		scrollTo = Math.min(Math.max(0, scrollTo), String.valueOf(sslh).equalsIgnoreCase("nan")?0:sslh);
		
		if (!String.valueOf(scrollTo).equalsIgnoreCase("nan")) {
			scroll+= (scrollTo-scroll)/3;
		}

		float c = -((slotsHeight-h)*(scroll/(h-sh)));
		
		oy = -c;

		for(Slot s : slots) {
			s.x = x+s.offsetX;
			s.y = y+s.offsetY;
			s.update(mx, my);
		}
		
		hover = mouseOver(mx, my);
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (mx >= sbx && my > sby && mx < sbx+sbw && my <= sby+sbh) {
			dragOffsetY = (my+(sby-scroll))-sby;
			dragScroll = true;
		}
		if (!hover) { return; }
		for(Slot s : slots) {
			s.mouseClick(mx, my, b);
		}
	}
	
	public void mouseRelease(int mx, int my, int b) {
		dragScroll = false;
		for(Slot s : slots) {
			s.mouseRelease(mx, my, b);
		}
	}
	
	public void mouseScroll(float s) {
		float slotsHeight = 0;
		for(Slot ss : slots) {
			slotsHeight+= ss.height;
		}
		if (!dragScroll && hover) {
			scrollTo-= (s/120)*(h/slotsHeight)*(slotsHeight/slots.size());
		}
	}
	
	public boolean mouseOver(int mx, int my) {
		return mx >= x+ox && mx < x+w+ox && my > y+oy && my <= y+h+oy;
	}

	public void render(float opacity) {
		for(Slot s : slots) {
			s.render(opacity);
		}
		if (sbh > 0) {
			GuiHelper.drawRectangle(x+w+(sbw/2)-1, y+oy-1000, x+w+sbw-(sbw/2)+1, y+h+oy+1000, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 0.2f*opacity));
			GuiHelper.drawRectangle(sbx, sby+(sbw/2), sbx+sbw, sby+sbh-(sbw/2), KiLO.getKiLO().getColorSchemeHandler().getCurrentForeground());
			GuiHelper.drawArc(sbx+(sbw/2), sby+(sbw/2), 180, 360, (sbw/2)-1, KiLO.getKiLO().getColorSchemeHandler().getCurrentForeground());
			GuiHelper.drawArc(sbx+(sbw/2), sby+sbh-(sbw/2), 0, 180, (sbw/2)-1, KiLO.getKiLO().getColorSchemeHandler().getCurrentForeground());
		}
	}
}
