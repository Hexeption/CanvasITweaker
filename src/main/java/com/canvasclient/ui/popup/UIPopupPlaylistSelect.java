package com.canvasclient.ui.popup;

import com.canvasclient.Canvas;
import com.canvasclient.render.FontHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.canvasclient.api.APIHelper;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.UI;
import com.canvasclient.ui.UIMusic;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.slotlist.SlotList;
import com.canvasclient.ui.interactable.slotlist.part.Playlist;
import com.canvasclient.ui.interactable.slotlist.part.Song;
import com.canvasclient.ui.interactable.slotlist.slot.PlaylistAddSlot;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.utilities.Utilities;

public class UIPopupPlaylistSelect extends UI {

	private float fX, fY, fW, fH;

	public SlotList sl;
	private Song song;
	
	public UIPopupPlaylistSelect(UI parent, Song song) {
		super(parent);
		this.song = song;
	}
	
	@Override
	public void init() {
		title = "Select a Playlist";
		
		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2);
		fW = 400;
		fH = 48+48+(48*Math.min(8, Canvas.getCanvas().getMusicHandler().getPlaylists().size()));

		sl = new SlotList(7f);
		
		interactables.clear();
		interactables.add(new IconButton(this, fX+(fW/2)-40, fY-(fH/2)+12, 24, 24, 0x00000000, ResourceHelper.iconClose[2]));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		
		if (fH != 48+48+(48*Math.min(8, Canvas.getCanvas().getMusicHandler().getPlaylists().size()))) {
			init();
		}

		sl.x = fX-(fW/2)+24;
		sl.y = fY-(fH/2)+72-sl.oy;
		sl.w = fW-48;
		sl.h = fH-96;
		
		if (sl.slots.size() != Canvas.getCanvas().getMusicHandler().getPlaylists().size()) {
			sl.slots.clear();
			int i = 0;
			for(Playlist f : Canvas.getCanvas().getMusicHandler().getPlaylists()) {
				sl.slots.add(new PlaylistAddSlot(sl, Canvas.getCanvas().getMusicHandler().getPlaylists().indexOf(f), sl.x, sl.y, sl.w, 48, 0, i*48));
				i++;
			}
		}

		sl.update(mx, my);
	}
	
	@Override
	public void handleInteraction(final Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			((UIMusic)parent).changePopup(null);
			break;
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		super.mouseClick(mx, my, b);
		sl.mouseClick(mx, my, b);
	}
	
	public void mouseRelease(int mx, int my, int b) {
		super.mouseRelease(mx, my, b);
		sl.mouseRelease(mx, my, b);
	}
	
	public void mouseScroll(int s) {
		super.mouseScroll(s);
		sl.mouseScroll(s);
	}
	
	public void keyboardPress(int key) {
		super.keyboardPress(key);
		switch (key) {
		case Keyboard.KEY_ESCAPE:
			((UIMusic)parent).changePopup(null);
			break;
		}
	}
	
	public void keyTyped(int key, char keyChar) {
		super.keyTyped(key, keyChar);
	}
	
	public void add(Playlist playlist) {
		if (song != null && playlist != null) {
			if (APIHelper.playlistAddSong(Canvas.getCanvas().getUserControl().clientID, playlist.id, String.valueOf(song.id))) {
				playlist.songs = APIHelper.getPlaylistSongs(Canvas.getCanvas().getUserControl().clientID, playlist.id);
				((UIMusic)parent).changePopup(null);
			}
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0.7f*opacity));
		
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2)+48, fX+(fW/2), fY+(fH/2), Utilities.reAlpha(0xFF202020, 1f*opacity));

		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2), fX+(fW/2), fY-(fH/2)+48, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(25), fX, fY-(fH/2)+24, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		GuiHelper.startClip(fX-(fW/2)+24, fY-(fH/2)+72, fX+(fW/2)-24+sl.sbw, fY+(fH/2)-24);
		sl.render(opacity);
		GuiHelper.endClip();

		super.render(opacity);
	}
}
