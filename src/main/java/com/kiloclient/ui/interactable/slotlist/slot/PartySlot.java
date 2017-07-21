package com.kiloclient.ui.interactable.slotlist.slot;

import com.kiloclient.KiLO;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.UIInGameMenuPartyChat;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.Utilities;

import net.minecraft.client.Minecraft;

public class PartySlot extends Slot {

	private final Minecraft mc = Minecraft.getMinecraft();
	
	public int index;
	
	public PartySlot(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		index = i;
		
		interactables.clear();
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (hover) {
			KiLO.getKiLO().getUIHandler().setCurrentUI(new UIInGameMenuPartyChat(KiLO.getKiLO().getUIHandler().getCurrentUI(), KiLO.getKiLO().getPartyManager().getParties().get(index)));
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, hoverOpacity*opacity));
		if (KiLO.getKiLO().getPartyManager().getParties().size() >= index) {
			boolean noImage = true;
			try {
				if (KiLO.getKiLO().getPartyManager().getParties().get(index).partyImage != null) {
					if (KiLO.getKiLO().getPartyManager().getParties().get(index).partyImage.getTexture() != null) {
						GuiHelper.drawTexturedRectangle(x+4, y+4, 40, 40, KiLO.getKiLO().getPartyManager().getParties().get(index).partyImage.getTexture(), opacity);
						noImage = false;
					}
				}
			} catch (Exception localException) {
				localException.printStackTrace();
			}
			if (noImage) {
				GuiHelper.drawLoaderAnimation(x+24, y+24, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
			
			try {
				if (KiLO.getKiLO().getPartyManager().getParties().get(index) != null && KiLO.getKiLO().getPartyManager().getParties().get(index).partyName != null && KiLO.getKiLO().getPartyManager().getParties().get(index).description != null) {
					String[] lines = new String[] {
							KiLO.getKiLO().getPartyManager().getParties().get(index).partyName,
							KiLO.getKiLO().getPartyManager().getParties().get(index).description
						};
					int k = 0;
					for(String l : lines) {
						for(int i = 0; i < l.length(); i++) {
							if (FontHandler.ROUNDED_BOLD.get(10).getWidth(l.substring(0, i)) > parent.w-60- FontHandler.ROUNDED_BOLD.get(10).getWidth("...")) {
								l = l.substring(0, i)+"...";
								break;
							}
						}
						GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(10), x+48, y+24-((FontHandler.ROUNDED_BOLD.get(10).getHeight()*(lines.length-1))/2)+(FontHandler.ROUNDED_BOLD.get(10).getHeight()*k), l, Utilities.reAlpha(l == lines[1] ? KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground() : ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.CENTER);
						k++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.render(opacity);
	}
	
}