package com.canvasclient.ui.popup;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.UI;
import com.canvasclient.ui.UIMusic;
import com.canvasclient.ui.interactable.Button;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.slotlist.part.Playlist;
import com.canvasclient.utilities.Timer;
import com.canvasclient.utilities.Utilities;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class UIPopupPlaylistDelete extends UI {

	private float formOffset;
	private boolean invalid;
	
	private String invalidMessage = "";
	
	private Timer invalidTimer = new Timer();
	
	private float fX, fY, fW, fH;
	
	private Playlist playlist;
	private boolean deleting;
	
	public UIPopupPlaylistDelete(UI parent, Playlist playlist) {
		super(parent);
		this.playlist = playlist;
	}
	
	@Override
	public void init() {
		title = "Delete Playlist?";

		formOffset = 0;
		invalid = false;
		
		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2);
		fW = 400;
		fH = 96+96;

		interactables.clear();
		interactables.add(new IconButton(this, fX+(fW/2)-40, fY-(fH/2)+12, 24, 24, 0x00000000, ResourceHelper.iconClose[3]));
		interactables.add(new Button(this, "Yes", fX-(fW/2)+24, fY+(fH/2)-24-48, (fW/2)-24-12, 48, FontHandler.ROUNDED_BOLD.get(20), Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), null, 0));
		interactables.add(new Button(this, "No", fX+(fW/2)-24-((fW/2)-24-12), fY+(fH/2)-24-48, (fW/2)-24-12, 48, FontHandler.ROUNDED_BOLD.get(20), ColorHelper.RED.getColorCode(), null, 0));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);

		if (invalid) {
			formOffset+= ((-FontHandler.STANDARD.get(14).getHeight()*2)-formOffset)/5f;
			if (invalidTimer.isTime(2f)) {
				invalid = false;
			}
		} else {
			invalidTimer.reset();
			formOffset+= (0-formOffset)/5f;
		}
		
		interactables.get(1).shown = !deleting;
		interactables.get(1).enabled = !deleting;
		interactables.get(2).shown = !deleting;
		interactables.get(2).enabled = !deleting;
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			((UIMusic)parent).changePopup(null);
			break;
		case 1:
			if (!deleting) {
				deleting = true;
				new Thread(() -> {
                    if (APIHelper.playlistRemove(Canvas.getCanvas().getUserControl().clientID, playlist.id)) {
                        ((UIMusic)parent).changePopup(null);
                        Canvas.getCanvas().getMusicHandler().getPlaylists().remove(playlist);
                    } else {
                        invalid = true;
                        invalidMessage = "There was a problem removing this playlist";
                        deleting = false;
                    }
                }).start();
			}
			break;
		case 2:
			((UIMusic)parent).changePopup(null);
			break;
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		super.mouseClick(mx, my, b);
	}
	
	public void mouseRelease(int mx, int my, int b) {
		super.mouseRelease(mx, my, b);
	}
	
	public void mouseScroll(int s) {
		super.mouseScroll(s);
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
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0.7f*opacity));
		
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2)+48, fX+(fW/2), fY+(fH/2), Utilities.reAlpha(0xFF202020, 1f*opacity));
		
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2), fX+(fW/2), fY-(fH/2)+48, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(25), fX, fY-(fH/2)+24, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(16), fX, fY-(fH/2)+48+24, "Are you sure you want to delete the playlist:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(16), fX, fY-(fH/2)+48+24+(FontHandler.ROUNDED.get(16).getHeight()), playlist.name, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		if (deleting) {
			GuiHelper.drawLoaderAnimation(fX, fY+48, 16, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		}
		
		super.render(opacity);
		
		GuiHelper.startClip((Display.getWidth()/2)-(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()/2), (Display.getWidth()/2)+(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()*1.5f));
		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(14), Display.getWidth()/2, fY+(fH/2)-(FontHandler.STANDARD.get(14).getHeight(invalidMessage))-formOffset, invalidMessage, Utilities.reAlpha(0xFFFF5555, 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.endClip();
	}
}
