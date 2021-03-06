package com.canvasclient.ui.interactable.slotlist.slot;

import com.canvasclient.Canvas;
import com.canvasclient.friend.FriendManager;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.UIInGameMenu;
import com.canvasclient.ui.UIInGameMenuFriend;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.utilities.Utilities;

public class FriendSlot extends Slot {
	
	public int index;
	
	public FriendSlot(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		index = i;
		
		interactables.clear();
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (hover) {
			Canvas.getCanvas().getUIHandler().setCurrentUI(new UIInGameMenuFriend(new UIInGameMenu(null, false), FriendManager.getFriend(index)));
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, hoverOpacity*opacity));
		if (FriendManager.getFriend(index) != null) {
			boolean noHead = true;
			if (FriendManager.getFriend(index).head != null) {
				if (FriendManager.getFriend(index).head.getTexture() != null) {
					GuiHelper.drawTexturedRectangle(x+4, y+4, 40, 40, FriendManager.getFriend(index).head.getTexture(), opacity);
					noHead = false;
				}
			}
			if (noHead) {
				GuiHelper.drawLoaderAnimation(x+24, y+24, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
			
			try {
				if (FriendManager.getFriend(index) != null && FriendManager.getFriend(index).canvasName != null) {
					String[] lines = new String[] {
						FriendManager.getFriend(index).canvasName+" \u00a77("+FriendManager.getFriend(index).mcname+")",
						FriendManager.getFriend(index).ip.length() > 0?"\u00a7a"+FriendManager.getFriend(index).ip:
							FriendManager.getFriend(index).status == "Offline"?"\u00a7c"+FriendManager.getFriend(index).status:
								FriendManager.getFriend(index).status == "Online"?"\u00a7b"+FriendManager.getFriend(index).status:
									FriendManager.getFriend(index).status == "Singleplayer"?"\u00a7b"+FriendManager.getFriend(index).status:
										FriendManager.getFriend(index).status == "Multiplayer"?"\u00a7a"+FriendManager.getFriend(index).status:"\u00a7cOffline"
						};
					int k = 0;
					for(String l : lines) {
						for(int i = 0; i < l.length(); i++) {
							if (FontHandler.ROUNDED_BOLD.get(10).getWidth(l.substring(0, i)) > parent.w-60- FontHandler.ROUNDED_BOLD.get(10).getWidth("...")) {
								l = l.substring(0, i)+"...";
								break;
							}
						}
						GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(10), x+48, y+24-((FontHandler.ROUNDED_BOLD.get(10).getHeight()*(lines.length-1))/2)+(FontHandler.ROUNDED_BOLD.get(10).getHeight()*k), l, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.CENTER);
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
