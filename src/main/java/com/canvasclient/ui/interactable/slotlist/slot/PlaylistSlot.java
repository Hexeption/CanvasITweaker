package com.canvasclient.ui.interactable.slotlist.slot;

import com.canvasclient.Canvas;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.UIMusic;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.ui.popup.UIPopupPlaylistDelete;
import com.canvasclient.ui.popup.UIPopupPlaylistEdit;
import com.canvasclient.utilities.Utilities;

public class PlaylistSlot extends Slot {
	
	public int index;
	
	public PlaylistSlot(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		index = i;
		
		interactables.clear();
		interactables.add(new IconButton(this, x+width-12, y+(height/2)-4, 8, 8, ColorHelper.GREEN.getColorCode(), ResourceHelper.iconPlay[0]));
		interactables.add(new IconButton(this, x+width-24, y+(height/2)-4, 8, 8, ColorHelper.GREY.getColorCode(), ResourceHelper.iconEdit[0]));
		interactables.add(new IconButton(this, x+width-36, y+(height/2)-4, 8, 8, ColorHelper.RED.getColorCode(), ResourceHelper.iconDelete[0]));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		
		interactables.get(0).x = x+width-12;
		interactables.get(0).y = y+(height/2)-4;

		interactables.get(1).x = x+width-24;
		interactables.get(1).y = y+(height/2)-4;
		
		interactables.get(2).x = x+width-36;
		interactables.get(2).y = y+(height/2)-4;
	}
	
	public void mouseClick(int mx, int my, int b) {
		super.mouseClick(mx, my, b);
		active = hover;
	}
	
	@Override
	public void handleInteraction(final Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			if (!Canvas.getCanvas().getMusicHandler().getPlaylists().get(index).songs.isEmpty()) {
				Canvas.getCanvas().getMusicHandler().playMusic(Canvas.getCanvas().getMusicHandler().getPlaylists().get(index).songs, Canvas.getCanvas().getMusicHandler().getPlaylists().get(index).songs.get(0));
			}
			break;
		case 1:
			if (Canvas.getCanvas().getUIHandler().getCurrentUI() instanceof UIMusic) {
				((UIMusic) Canvas.getCanvas().getUIHandler().getCurrentUI()).changePopup(new UIPopupPlaylistEdit(Canvas.getCanvas().getUIHandler().getCurrentUI(), Canvas.getCanvas().getMusicHandler().getPlaylists().get(index)));
			}
			break;
		case 2:
			if (Canvas.getCanvas().getUIHandler().getCurrentUI() instanceof UIMusic) {
				((UIMusic) Canvas.getCanvas().getUIHandler().getCurrentUI()).changePopup(new UIPopupPlaylistDelete(Canvas.getCanvas().getUIHandler().getCurrentUI(), Canvas.getCanvas().getMusicHandler().getPlaylists().get(index)));
			}
			break;
		}
	}
	
	public void render(float opacity) {
		if (Canvas.getCanvas().getMusicHandler().getPlaylists().size() >= index) {
			if (Canvas.getCanvas().getMusicHandler().getPlaylists().get(index) != null) {
				try {
					if (Canvas.getCanvas().getMusicHandler().getPlaylists().get(index) != null && Canvas.getCanvas().getMusicHandler().getPlaylists().get(index).name != null) {
						GuiHelper.drawTexturedRectangle(x+8, y+(height/2)-4, 8, 8, ResourceHelper.iconMusic[0], Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), (Math.max((hoverOpacity/2f), (activeOpacity/2))+0.5f)*opacity));
						
						String title = Canvas.getCanvas().getMusicHandler().getPlaylists().get(index).name;
						for(int i = 0; i < title.length(); i++) {
							if (FontHandler.ROUNDED.get(14).getWidth(title.substring(0, i)) > (parent.w-64)- FontHandler.ROUNDED.get(14).getWidth("...")) {
								title = title.substring(0, i)+"...";
								break;
							}
						}
						
						GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), x+24, y+(height/2), title, Utilities.reAlpha(active?ColorHelper.GREEN.getColorCode():ColorHelper.WHITE.getColorCode(), !active?((hoverOpacity/2)+0.5f):1f*opacity), Align.LEFT, Align.CENTER);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		super.render(opacity);
	}
	
}
