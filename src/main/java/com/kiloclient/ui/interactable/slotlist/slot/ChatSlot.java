package com.kiloclient.ui.interactable.slotlist.slot;

import com.kiloclient.KiLO;
import com.kiloclient.manager.ChatManager;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.UIChat;
import com.kiloclient.ui.interactable.ChatComponent;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatSlot extends Slot {

	private final Minecraft mc = Minecraft.getMinecraft();
	
	public int index;
	
	public ChatSlot(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		index = i;
		
		interactables.clear();

		TextComponentString message = ChatManager.getChatLine(index).formatted;

		float xx = 0;
		for(int j = 0; j < message.getSiblings().size(); j++) {
			TextComponentString icc = (TextComponentString)message.getSiblings().get(j);
			if (j > 0) {
				TextComponentString iccPre = (TextComponentString)message.getSiblings().get(j-1);
				xx+= UIChat.font.getWidth(iccPre.getFormattedText());
			}
			interactables.add(new ChatComponent(this, icc, xx, y, UIChat.font, ColorHelper.WHITE.getColorCode(), Align.LEFT, Align.CENTER));
		}
	}
	
	public void update(int mx, int my) {
		if (ChatManager.getChatLine(index) == null) {
			return;
		}

		TextComponentString message = ChatManager.getChatLine(index).formatted;

		float xx = x+4;
		float yy = y;
		int k = 1;
		for(int j = 0; j < message.getSiblings().size(); j++) {
			TextComponentString icc = (TextComponentString)message.getSiblings().get(j);
			
			if (j > 0) {
				TextComponentString iccPre = (TextComponentString)message.getSiblings().get(j-1);
				xx+= UIChat.font.getWidth(iccPre.getFormattedText());
				if (xx+UIChat.font.getWidth(icc.getFormattedText()) > ChatManager.chatWidth-12) {
					xx = x+4;
					yy+= UIChat.font.getHeight();
					k++;
				}
			}
			
			if (j <= interactables.size()-1) {
				interactables.get(j).x = xx;
				interactables.get(j).y = yy;
			}
			height = (UIChat.font.getHeight()*k)+5;
		}

		super.update(mx, my);
		
		hover = mouseOver(mx, my) && enabled && KiLO.getKiLO().getUIHandler().getCurrentUI() instanceof UIChat;
		
		activeOpacity+= ((active?1f:0f)-activeOpacity)/2f;
		activeOpacity = Math.min(Math.max(0, activeOpacity), 1f);
		
		hoverOpacity+= ((hover?1f:0f)-hoverOpacity)/2f;
		hoverOpacity = Math.min(Math.max(0, hoverOpacity), 1f);
	}
	
	public void mouseClick(int mx, int my, int b) {
		super.mouseClick(mx, my, b);
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch (i.type) {
		default:
			break;
		case CHAT_COMPONENT:
			if (ChatManager.getChatLine(index) != null) {
				ChatManager.handleComponentClick(((ChatComponent)i).component);
			}
			break;
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF010101, 0.5f*hoverOpacity*opacity));
		float py = parent.y+parent.oy;
		float pad = 20;
		if (y+height >= py-pad && y <= py+parent.h+pad) {
			super.render(opacity);
		}
	}
	
}
