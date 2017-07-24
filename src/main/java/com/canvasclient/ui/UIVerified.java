package com.canvasclient.ui;

import org.lwjgl.opengl.Display;

import com.canvasclient.Canvas;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.utilities.Timer;
import com.canvasclient.utilities.Utilities;

public class UIVerified extends UI {

	private Timer timer = new Timer();
	
	public UIVerified(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = "Welcome ";
		
		interactables.clear();
		if (timer != null) {
			timer.reset();
		} else {
			timer = new Timer();
		}
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		if (timer.isTime(3f)) {
			Canvas.getCanvas().getUIHandler().changeUI(new UIMainMenu(null));
		}
	}
	
	@Override
	public void handleInteraction(Interactable i) {
	}
	
	public void render(float opacity) {
		String name = Canvas.getCanvas().getUserControl().minecraftName;
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(40).getWidth(title+name)/2), Display.getHeight()/2-(FontHandler.ROUNDED_BOLD.get(40).getHeight()/2), title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(40).getWidth(title+name)/2)+ FontHandler.ROUNDED_BOLD.get(40).getWidth(title), Display.getHeight()/2-(FontHandler.ROUNDED_BOLD.get(40).getHeight()/2), name, Utilities.reAlpha(ColorHelper.GREEN.getColorCode(), 1f*opacity));
		super.render(opacity);
	}
}
