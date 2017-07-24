package com.canvasclient.ui.popup;

import com.canvasclient.Canvas;
import com.canvasclient.notification.HistoryManager;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.UI;
import com.canvasclient.ui.UIHistory;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.utilities.Utilities;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.List;

public class UIPopupHistory extends UI {

	private float formOffset;
	
	private float fX, fY, fW, fH;
	
	private List names;
	
	public UIPopupHistory(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = HistoryManager.current.gameProfile.getName();

		formOffset = 0;

		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2);
		fW = 350;
		if (names != null) {
			fH = 48+48+Math.max(FontHandler.ROUNDED_BOLD.get(20).getHeight(), ((names.size())* FontHandler.ROUNDED_BOLD.get(20).getHeight()));
		} else {
			fH = 48+48+ FontHandler.ROUNDED_BOLD.get(20).getHeight();
			new Thread() {
				@Override
				public void run() {
					try {
						names = Utilities.getNamesFromUUID(Utilities.getUUIDFromName(HistoryManager.current.gameProfile.getName()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}

		interactables.clear();
		interactables.add(new IconButton(this, fX+(fW/2)-40, fY-(fH/2)+12, 24, 24, 0x00000000, ResourceHelper.iconClose[2]));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		
		if (names != null) {
			if (fH != 48+48+Math.max(FontHandler.ROUNDED_BOLD.get(20).getHeight(), ((names.size())* FontHandler.ROUNDED_BOLD.get(20).getHeight()))) {
				init();
			}
		} else {
			fH = 48+48+ FontHandler.ROUNDED_BOLD.get(20).getHeight();
		}
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			((UIHistory)parent).changePopup(null);
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
			((UIHistory)parent).changePopup(null);
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
		
		if (names != null) {
			for(Object s : names) {
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+24, fY-(fH/2)+48+24+(names.indexOf(s)* FontHandler.ROUNDED_BOLD.get(20).getHeight()), s.toString(), Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity));
			}
		} else {
			GuiHelper.drawLoaderAnimation(fX, fY-(fH/2)+48+((fH-48)/2), 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity));
		}
		
		super.render(opacity);
	}
}
