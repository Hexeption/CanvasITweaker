package com.canvasclient.ui.interactable.slotlist.slot;

import com.canvasclient.Canvas;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.UIInGameMenuPartyChat;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.utilities.Utilities;

public class PartySlot extends Slot {
	
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
			Canvas.getCanvas().getUIHandler().setCurrentUI(new UIInGameMenuPartyChat(Canvas.getCanvas().getUIHandler().getCurrentUI(), Canvas.getCanvas().getPartyManager().getParties().get(index)));
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, hoverOpacity*opacity));
		if (Canvas.getCanvas().getPartyManager().getParties().size() >= index) {
			boolean noImage = true;
			try {
				if (Canvas.getCanvas().getPartyManager().getParties().get(index).partyImage != null) {
					if (Canvas.getCanvas().getPartyManager().getParties().get(index).partyImage.getTexture() != null) {
						GuiHelper.drawTexturedRectangle(x+4, y+4, 40, 40, Canvas.getCanvas().getPartyManager().getParties().get(index).partyImage.getTexture(), opacity);
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
				if (Canvas.getCanvas().getPartyManager().getParties().get(index) != null && Canvas.getCanvas().getPartyManager().getParties().get(index).partyName != null && Canvas.getCanvas().getPartyManager().getParties().get(index).description != null) {
					String[] lines = new String[] {
							Canvas.getCanvas().getPartyManager().getParties().get(index).partyName,
							Canvas.getCanvas().getPartyManager().getParties().get(index).description
						};
					int k = 0;
					for(String l : lines) {
						for(int i = 0; i < l.length(); i++) {
							if (FontHandler.ROUNDED_BOLD.get(10).getWidth(l.substring(0, i)) > parent.w-60- FontHandler.ROUNDED_BOLD.get(10).getWidth("...")) {
								l = l.substring(0, i)+"...";
								break;
							}
						}
						GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(10), x+48, y+24-((FontHandler.ROUNDED_BOLD.get(10).getHeight()*(lines.length-1))/2)+(FontHandler.ROUNDED_BOLD.get(10).getHeight()*k), l, Utilities.reAlpha(l == lines[1] ? Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground() : ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.CENTER);
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