package com.canvasclient.ui.popup;

import com.canvasclient.Canvas;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.UI;
import com.canvasclient.ui.UIInGameMenu;
import com.canvasclient.ui.UIInGameMenuFriend;
import com.canvasclient.ui.UIInGameMenuPartyChat;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.utilities.Utilities;

public class UIPopupMessage extends UI {

	private float fX, fY, fW, fH;
	
	private String message;
	
	public UIPopupMessage(UI parent, String title, String message) {
		super(parent);
		this.title = title;
		this.message = message;
	}
	
	@Override
	public void init() {
		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2);
		fW = 550;
		fH = 176;

		interactables.clear();
		interactables.add(new IconButton(this, fX+(fW/2)-40, fY-(fH/2)+12, 24, 24, 0x00000000, ResourceHelper.iconClose[3]));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			if (parent instanceof UIInGameMenu)
				((UIInGameMenu)parent).changePopup(null);
			if (parent instanceof UIInGameMenuFriend)
				((UIInGameMenuFriend)parent).changePopup(null);
			if (parent instanceof UIInGameMenuPartyChat)
				if (parent instanceof UIInGameMenuPartyChat)
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
			if (parent instanceof UIInGameMenu)
				((UIInGameMenu)parent).changePopup(null);
			if (parent instanceof UIInGameMenuFriend)
				((UIInGameMenuFriend)parent).changePopup(null);
			if (parent instanceof UIInGameMenuPartyChat)
				if (parent instanceof UIInGameMenuPartyChat)
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
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX, fY+24, message, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		super.render(opacity);
	}
}
