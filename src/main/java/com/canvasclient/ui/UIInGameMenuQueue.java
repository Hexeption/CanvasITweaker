package com.canvasclient.ui;

import java.util.Random;

import com.canvasclient.Canvas;
import com.canvasclient.render.FontHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.Link;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.utilities.Timer;
import com.canvasclient.utilities.Utilities;

public class UIInGameMenuQueue extends UI {

	private UI popup, popupTo;
	private boolean popupFade;
	private float xPosition, yPosition, width, height, popupOpacity;
	
	int time;
	private Timer queueTimer;
	
	public UIInGameMenuQueue(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		Random random = new Random();
		time = random.nextInt((10 - 3) + 1) + 3;
		queueTimer = new Timer();
		queueTimer.reset();
		
		xPosition = Display.getWidth() / 2;
		yPosition = (Display.getHeight() / 2);
		width = 900;
		height = 530;
		
		interactables.clear();
		float xx = xPosition - (width / 2) + 20;
		interactables.add(new IconButton(this, xx, yPosition - (height / 2) + 12, 24, 24, 0x00000000, ResourceHelper.iconBack[2]));
	}
	
	public void update(int mx, int my) {
		if (queueTimer.isTime(time)) {
			if (parent instanceof UIInGameMenu) {
				Canvas.getCanvas().getUIHandler().setCurrentUI(new UIInGameMenu(null, false));
			}
			if (parent instanceof UIMusic) {
				Canvas.getCanvas().getUIHandler().setCurrentUI(new UIMusic(false));
			}
		}
			
		
		if (popup == null) {
			super.update(mx, my);
		}		
		
		if (interactables.size() < 2)
			interactables.add(new Link(this, "Premium users automatically skip the queue", xPosition, yPosition + (Display.getHeight() / 3) - Display.getHeight() / 15, FontHandler.ROUNDED.get(20), ColorHelper.WHITE.getColorCode(), Align.CENTER, Align.CENTER));
		
		if (popup != null) {
			popup.update(mx, my);
		}
		popupOpacity += popupFade? - 0.2f:0.2f;
		popupOpacity = Math.min(Math.max(0f, popupOpacity), 1f);
		if (popupFade) {
			if (popupOpacity < 0.05f) {
				popup = popupTo;
				popupFade = false;
				if (popup != null) {
					popup.init();
				}
			}
		}
	}
	
	@Override
	public void handleInteraction(final Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			mc.displayGuiScreen(null);
			break;
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (popup == null) {
			super.mouseClick(mx, my, b);
		} else {
			popup.mouseClick(mx, my, b);
		}
	}
	
	public void mouseRelease(int mx, int my, int b) {
		if (popup == null) {
			super.mouseRelease(mx, my, b);
		} else {
			popup.mouseRelease(mx, my, b);
		}
	}
	
	public void mouseScroll(int s) {
		if (popup == null) {
			super.mouseScroll(s);
		} else {
			popup.mouseScroll(s);
		}
	}
	
	public void keyboardPress(int key) {
		if (popup == null) {
			super.keyboardPress(key);
			switch (key) {
			case Keyboard.KEY_ESCAPE:
				mc.displayGuiScreen(null);
				break;
			}
		} else {
			popup.keyboardPress(key);
		}
	}
	
	public void keyTyped(int key, char keyChar) {
		if (popup == null) {
			super.keyTyped(key, keyChar);
		} else {
			popup.keyTyped(key, keyChar);
		}
	}
	
	public void changePopup(UI u) {
		popupTo = u;
		popupFade = true;
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0.7f * opacity));
		GuiHelper.drawRectangle(xPosition - (width / 2), yPosition - (height / 2) + 48, xPosition + (width / 2), yPosition + (height / 2), Utilities.reAlpha(0xFF242424, 1f * opacity));
		
		GuiHelper.drawRectangle(xPosition - (width / 2), yPosition - (height / 2), xPosition + (width / 2), yPosition - (height / 2) + 48, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1f * opacity));
		
		GuiHelper.drawLoaderAnimation(xPosition, yPosition - Display.getHeight() / 15, 70, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED.get(40), xPosition, yPosition + (Display.getHeight() / 4)  - Display.getHeight() / 15, "You are in the queue...", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity), Align.CENTER, Align.CENTER);
		
		super.render(opacity);
		
		if (popup != null) {
			popup.render(opacity * (Math.max(popupOpacity, 0.05f)));
		}
	}
}