package com.canvasclient.ui.interactable.slotlist.slot;

import com.canvasclient.ui.InteractableParent;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.utilities.IMinecraft;

public class Slot extends InteractableParent implements IMinecraft {

	public SlotList parent;
	public boolean shown, enabled, hover, active;
	public float offsetX, offsetY;
	public float hoverOpacity, activeOpacity;

	public Slot(SlotList p, float xx, float yy, float w, float h, float ox, float oy) {
		super(xx, yy, w, h);
		parent = p;
		shown = true;
		enabled = true;
		offsetX = ox;
		offsetY = oy;
	}
	
	public void update(int mx, int my) {
		hover = mouseOver(mx, my) && enabled;
		
		activeOpacity+= ((active?1f:0f)-activeOpacity)/2f;
		activeOpacity = Math.min(Math.max(0, activeOpacity), 1f);
		
		hoverOpacity+= ((hover?1f:0f)-hoverOpacity)/2f;
		hoverOpacity = Math.min(Math.max(0, hoverOpacity), 1f);
		
		super.update(mx, my);
	}
	
	public void mouseClick(int mx, int my, int b) {
		active = hover;
		super.mouseClick(mx, my, b);
	}
	
	public boolean mouseOver(int mx, int my) {
		return parent.hover && super.mouseOver(mx, my);
	}

	@Override
	public void handleInteraction(Interactable i) {
	}
}
