package com.kiloclient.ui.interactable.slotlist.slot;

import com.kiloclient.KiLO;
import com.kiloclient.friend.MessageManager;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.ui.UIInGameMenu;
import com.kiloclient.ui.interactable.IconButton;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.ui.interactable.slotlist.part.Message;
import com.kiloclient.ui.popup.UIPopupMessageReply;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;

public class MessageSlot extends Slot {
	
	public int index;
	
	public int clicks = 0;
	public Timer clickTimer = new Timer();
	
	public MessageSlot(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		index = i;
		
		clickTimer.reset();
		
		interactables.clear();

		interactables.add(new IconButton(this, x+10+40, y+20, 8, 8, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconBack[0]));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		if (clicks == 2) {
			doubleClick(mx, my);
			clicks = 0;
		}
		
		if (clickTimer.isTime(Utilities.doubleClickTimer)) {
			clickTimer.reset();
			clicks = 0;
		}

		interactables.get(0).x = x+10+40;
		interactables.get(0).y = y+20;
	}
	
	public void doubleClick(int mx, int my) {
		((UIInGameMenu)KiLO.getKiLO().getUIHandler().getCurrentUI()).changePopup(new UIPopupMessageReply(KiLO.getKiLO().getUIHandler().getCurrentUI(), MessageManager.getMessage(index).minecraftName, MessageManager.getMessage(index).message));
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
	
	public void handleInteraction(Interactable i) {
		switch (i.type) {
		default:
			break;
		case BUTTON:
			switch(interactables.indexOf(i)) {
			case 0:
				doubleClick(0, 0);
				break;
			}
			break;
		case SLIDER:
			break;
		case CHECK_BOX:
			break;
		case TEXT_BOX:
			break;
		case LINK:
			break;
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, hoverOpacity*opacity));
		if (MessageManager.getMessage(index) != null) {
			Message message = MessageManager.getMessage(index);
			boolean noIcon = true;
			if (message.icon != null) {
				if (message.icon.getTexture() != null) {
					GuiHelper.drawTexturedRectangle(x+4, y+4, 40, 40, message.icon.getTexture(), opacity);
					noIcon = false;
				}
			}
			if (noIcon) {
				GuiHelper.drawLoaderAnimation(x+24, y+24, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
			
			if (message.minecraftName != null && message.message != null) {
				String[] lines = new String[] {
						message.minecraftName,
						"\u00a77"+message.message
					};
				int k = 0;
				for(String l : lines) {
					for(int i = 0; i < l.length(); i++) {
						if (FontHandler.ROUNDED_BOLD.get(10).getWidth(l.substring(0, i)) > parent.w-76- FontHandler.ROUNDED_BOLD.get(10).getWidth("...")) {
							l = l.substring(0, i)+"...";
							break;
						}
					}
					GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(10), x+64, y+24-((FontHandler.ROUNDED_BOLD.get(10).getHeight()*(lines.length-1))/2)+(FontHandler.ROUNDED_BOLD.get(10).getHeight()*k), l, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.CENTER);
					k++;
				}
			}
		}
		super.render(opacity);
	}
	
}
