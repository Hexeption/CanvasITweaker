package com.canvasclient.ui.interactable.slotlist.slot;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.UIMusic;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.ui.interactable.slotlist.part.Playlist;
import com.canvasclient.ui.interactable.slotlist.part.Song;
import com.canvasclient.ui.popup.UIPopupPlaylistSelect;
import com.canvasclient.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SongSlot extends Slot {
	
	public Song song;
	
	public SongSlot(SlotList p, Song s, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		song = s;

		interactables.clear();
		interactables.add(new IconButton(this, x+height-10, y+height-24, 16, 16, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconPlay[1]));
		interactables.add(new IconButton(this, x+height-4, y+8, 8, 8, ColorHelper.GREY.getColorCode(), ResourceHelper.iconStar[0]));
		
		boolean isInPlaylist = false;
		for(Playlist pl : Canvas.getCanvas().getMusicHandler().getPlaylists()) {
			if (pl.songs.contains(song)) {
				interactables.add(new IconButton(this, x+height-4, y+20, 8, 8, ColorHelper.GREY.getColorCode(), ResourceHelper.iconDelete[0]));
				isInPlaylist = true;
				break;
			}
		}
		if (!isInPlaylist) {
			interactables.add(new IconButton(this, x+height-4, y+20, 8, 8, ColorHelper.GREY.getColorCode(), ResourceHelper.iconAdd[0]));
		}
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		
		interactables.get(0).x = x+height-10;
		interactables.get(0).y = y+height-24;

		interactables.get(1).x = x+height-4;
		interactables.get(1).y = y+8;
		((IconButton)interactables.get(1)).buttonColor = song.starred?ColorHelper.YELLOW.getColorCode():ColorHelper.GREY.getColorCode();
		
		if (interactables.size() >= 3) {
			interactables.get(2).x = x+height-4;
			interactables.get(2).y = y+20;
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		super.mouseClick(mx, my, b);
		active = hover;
	}

	@Override
	public void handleInteraction(final Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			play();
			break;
		case 1:
			if (!song.starred) {
				APIHelper.songAddStar(Canvas.getCanvas().getUserControl().clientID, String.valueOf(song.id));
			} else {
				APIHelper.songRemoveStar(Canvas.getCanvas().getUserControl().clientID, String.valueOf(song.id));
			}
			song.starred = !song.starred;
			break;
		case 2:
			Playlist pl = null;
			for(Playlist pll : Canvas.getCanvas().getMusicHandler().getPlaylists()) {
				if (pll.songs.contains(song)) {
					pl = pll;
					break;
				}
			}
			if (pl != null) {
				APIHelper.playlistRemoveSong(Canvas.getCanvas().getUserControl().clientID, pl.id, String.valueOf(song.id));
				pl.songs.remove(song);
			} else {
				((UIMusic) Canvas.getCanvas().getUIHandler().getCurrentUI()).changePopup(new UIPopupPlaylistSelect(Canvas.getCanvas().getUIHandler().getCurrentUI(), song));
			}
			break;
		}
	}
	
	public void play() {
		List<Song> list = new ArrayList<Song>();
		for(Slot s : parent.slots) {
			list.add(((SongSlot)s).song);
		}
		Canvas.getCanvas().getMusicHandler().playMusic(list, song);
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF2A2A2A, activeOpacity*opacity));
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF1A1A1A, hoverOpacity*opacity));
		GuiHelper.drawRectangle(x, y-1, x+width, y, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 0.1f*opacity));
		if (song != null) {
			try {
				if (song.image != null && song.image.getTexture() != null) {
					GuiHelper.drawTexturedRectangle(x+8, y+8, height-16, height-16, song.image.getTexture(), 1f*opacity);
				}
				String title = song.title;
				for(int i = 0; i < title.length(); i++) {
					if (FontHandler.ROUNDED.get(14).getWidth(title.substring(0, i)) > (parent.w-height-24-32- FontHandler.ROUNDED.get(12).getWidth("00:00:00"))- FontHandler.ROUNDED.get(14).getWidth("...")) {
						title = title.substring(0, i)+"...";
						break;
					}
				}
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(14), x+height+24, y+(height/2), title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.CENTER);

				int millis = song.duration;
				String time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
					    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
					    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(12), x+width-16, y+(height/2), time, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.RIGHT, Align.CENTER);
			} catch (Exception e) {
				
			}
		} else {
			parent.slots.remove(this);
		}
		super.render(opacity);
	}
	
}
