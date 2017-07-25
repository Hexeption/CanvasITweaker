package com.canvasclient.ui.interactable.slotlist.slot;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.friend.FriendManager;
import com.canvasclient.notification.ActivityManager;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.UIInGameMenu;
import com.canvasclient.ui.UIInGameMenuFriend;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.ui.interactable.slotlist.part.Activity;
import com.canvasclient.ui.interactable.slotlist.part.Friend;
import com.canvasclient.utilities.Utilities;

public class ActivitySlotNewMessage extends ActivitySlot {

	public ActivitySlotNewMessage(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, i, x, y, w, h, ox, oy);
		
		interactables.add(new IconButton(this, x+w-12, y+20, 8, 8, ColorHelper.GREEN.getColorCode(), ResourceHelper.iconGoto[0]));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);

		interactables.get(0).x = x+width-12;
		interactables.get(0).y = y+20;
	}
	
	public void mouseClick(int mx, int my, int b) {
		super.mouseClick(mx, my, b);
	}
	
	public void doubleClick(int mx, int my) {
		Friend friend = FriendManager.getFriend(((Activity)ActivityManager.getActivity(index)).minecraftName);
		
		Canvas.getCanvas().getUIHandler().setCurrentUI(new UIInGameMenuFriend(new UIInGameMenu(null, false), friend));
		new Thread(() -> {
            APIHelper.removeActivity(Canvas.getCanvas().getUserControl().clientID, ActivityManager.getActivity(index).id);
            ActivityManager.removeActivity(ActivityManager.getActivity(index));;
        }).start();
	}
	
	@Override
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
		try {
			super.render(opacity);
			
			if (ActivityManager.getActivity(index) != null) {
				if (ActivityManager.getActivity(index).canvasName != null) {
					String[] lines = new String[] {
							((Activity)ActivityManager.getActivity(index)).minecraftName,
							"\u00a77Sent you a message"
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
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	
}
