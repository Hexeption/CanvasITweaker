package com.kiloclient.ui.interactable.slotlist.slot;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.notification.ActivityManager;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.IconButton;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.ui.interactable.slotlist.part.Activity;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Utilities;

public class ActivitySlotFriendAccepted extends ActivitySlot {

	public ActivitySlotFriendAccepted(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, i, x, y, w, h, ox, oy);
		
		interactables.add(new IconButton(this, x+w-12, y+20, 8, 8, ColorHelper.RED.getColorCode(), ResourceHelper.iconClose[0]));
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
		checking = true;
		new Thread() {
			@Override
			public void run() {
				APIHelper.removeActivity(KiLO.getKiLO().getUserControl().clientID, ActivityManager.getActivity(index).id);
				ActivityManager.removeActivity(ActivityManager.getActivity(index));
			}
		}.start();
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch (i.type) {
		default:
			break;
		case BUTTON:
			switch (interactables.indexOf(i)) {
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
		super.render(opacity);
		if (ActivityManager.getActivity(index) != null && !checking) {
			if (ActivityManager.getActivity(index).kiloName != null) {
				String[] lines = new String[] {
						((Activity)ActivityManager.getActivity(index)).minecraftName,
						"\u00a77Accepted your friend",
						"\u00a77request"
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
	}
	
}
