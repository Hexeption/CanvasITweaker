package com.kiloclient.ui.interactable.slotlist.slot;

import com.kiloclient.render.FontHandler;
import org.lwjgl.input.Keyboard;

import com.kiloclient.manager.MacroManager;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.ui.interactable.slotlist.part.Macro;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.Utilities;

import net.minecraft.client.Minecraft;

public class MacroSlot extends Slot {

	private final Minecraft mc = Minecraft.getMinecraft();
	
	public int index;
	
	public MacroSlot(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		index = i;
		
		interactables.clear();
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (parent.mouseOver(mx, my)) {
			active = hover;
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, hoverOpacity*opacity));
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, activeOpacity*opacity));
		if (MacroManager.getMacro(index) != null) {
			Macro w = MacroManager.getMacro(index);
			String name = w.name;
			for(int i = 0; i < name.length(); i++) {
				if (FontHandler.ROUNDED_BOLD.get(12).getWidth(name.substring(0, i)) > (parent.w/2)-60- FontHandler.ROUNDED_BOLD.get(12).getWidth("...")) {
					name = name.substring(0, i)+"...";
					break;
				}
			}
			
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), x+16, y+24, name, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.BOTTOM);
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), x+16, y+24, w.command, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity), Align.LEFT, Align.TOP);
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(10), x+width-16, y+24, "Keybind: "+Keyboard.getKeyName(w.keybind), Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity), Align.RIGHT, Align.CENTER);
		}
		super.render(opacity);
	}
	
}
