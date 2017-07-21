package com.kiloclient.ui.interactable.slotlist.slot;

import java.util.ArrayList;

import com.kiloclient.KiLO;
import com.kiloclient.message.Message;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.Utilities;

import net.minecraft.client.Minecraft;

public class ProfileMessageSlot extends Slot {

	private final Minecraft mc = Minecraft.getMinecraft();
	
	public Message message;
	
	public ArrayList<String> lines;
	
	public ProfileMessageSlot(SlotList p, Message m, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		this.message = m;
		
		String mes = this.message.getMessage();
		interactables.clear();
		
		
		lines = new ArrayList();
		if (mes == null) {
			return;
		}
		int i = 0;
		int j = 0;
		for(i = 0; i <= mes.length(); i++) {
			String part = "";
			part = mes.substring(j, i);
			
			if (FontHandler.ROUNDED_BOLD.get(14).getWidth(part) > 200) {
				int b = -1;
				for(int a = i-1; a > j; a--) {
					if (mes.charAt(a) == ' ') {
						b = a+1;
						break;
					}
				}
				if (b >= j) {
					i = b;
				}
				lines.add(mes.substring(j, i));
				j = i;
			}
		}
		if (i >= mes.length()) {
			lines.add(mes.substring(j, mes.length()));
		}
		this.height = 36 * lines.size();
	}
	
	public void render(float opacity) {
		if (message.getMessage() == "")
			return;
		float yC = y;
		float biggestWidth = 0;
		if (this.message.getType() == 1 && message.getMessage() != "") {
			for (String s : lines) {
				if (FontHandler.ROUNDED.get(14).getWidth(s) > biggestWidth) {
					biggestWidth = FontHandler.ROUNDED.get(14).getWidth(s);
				}
			}
			float offset = lines.size() > 1 ? biggestWidth : FontHandler.ROUNDED.get(14).getWidth(this.message.getMessage());
			GuiHelper.drawRoundedRectangle(this.x, this.y - 5, this.x + offset + 20, this.y + lines.size() * 24, 0xFF464646);
			for (String s : lines) {
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14),  this.x + 10, yC, s, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity));
				yC += 24;
			}	
		} else {
			boolean oneLine = lines.size() == 1;
			for (String s : lines) {
				
				float xOffset = oneLine ? width - FontHandler.ROUNDED.get(14).getWidth(s) - 25 : width / 2;
				float lineOffset = 0;
				String biggestLine = "";
				for (String m : lines) {
					if (FontHandler.ROUNDED.get(14).getWidth(m) > FontHandler.ROUNDED.get(14).getWidth(biggestLine))
						biggestLine = m;
					if (FontHandler.ROUNDED.get(14).getWidth(biggestLine) < width / 2 && !oneLine) {
						float testOffset = width / 2 - FontHandler.ROUNDED.get(14).getWidth(biggestLine) - 15;
							lineOffset = testOffset;
					}
				}
				if (oneLine)
					GuiHelper.drawRoundedRectangle(this.x + xOffset - 10, yC - 15, this.x + xOffset - 10 + FontHandler.ROUNDED.get(14).getWidth(s) + 20, this.y - 20 + this.height, KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground());
				else
					GuiHelper.drawRoundedRectangle(this.x + xOffset - 10 + lineOffset - 10, yC - 15, this.x + xOffset - 10 + FontHandler.ROUNDED.get(14).getWidth(biggestLine) + 10 + lineOffset, this.y + lines.size() * 24 - 5, KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground());
				if (oneLine)
					GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), this.x + xOffset, yC, s, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity), Align.LEFT, Align.CENTER);
				else
					GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), this.x + xOffset + lineOffset - 10, yC, s, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity), Align.LEFT, Align.CENTER);
				yC += 24;
			}
		}
		
		super.render(opacity);
	}
	
}