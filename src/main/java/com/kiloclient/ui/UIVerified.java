package com.kiloclient.ui;

import org.lwjgl.opengl.Display;

import com.kiloclient.KiLO;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;

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
			KiLO.getKiLO().getUIHandler().changeUI(new UIMainMenu(null));
		}
	}
	
	@Override
	public void handleInteraction(Interactable i) {
	}
	
	public void render(float opacity) {
		String name = KiLO.getKiLO().getUserControl().minecraftName;
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(40).getWidth(title+name)/2), Display.getHeight()/2-(FontHandler.ROUNDED_BOLD.get(40).getHeight()/2), title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(40).getWidth(title+name)/2)+ FontHandler.ROUNDED_BOLD.get(40).getWidth(title), Display.getHeight()/2-(FontHandler.ROUNDED_BOLD.get(40).getHeight()/2), name, Utilities.reAlpha(ColorHelper.GREEN.getColorCode(), 1f*opacity));
		super.render(opacity);
	}
}
