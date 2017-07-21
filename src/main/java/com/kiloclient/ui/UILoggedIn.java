package com.kiloclient.ui;

import com.kiloclient.render.FontHandler;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.kiloclient.KiLO;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;

public class UILoggedIn extends UI {

	private Timer timer = new Timer();
	
	public UILoggedIn(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = "Hi ";
		
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
			KiLO.getKiLO().getUIHandler().changeUI(new UIColorChooser(null));
		}
	}
	
	@Override
	public void handleInteraction(Interactable i) {
	}
	
	public void render(float opacity) {
		//Dark gray background
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), 0xFF252525);
		String name = KiLO.getKiLO().getUserControl().kiloName;
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(40).getWidth(title+name)/2), Display.getHeight()/2-(FontHandler.ROUNDED_BOLD.get(40).getHeight()/2), title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(40).getWidth(title+name)/2)+ FontHandler.ROUNDED_BOLD.get(40).getWidth(title), Display.getHeight()/2-(FontHandler.ROUNDED_BOLD.get(40).getHeight()/2), name, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		int currentPointColor = 0xFF1DB8C2;
		int pointColor = 0xFF414141;
		float width = Display.getWidth();
		float height = Display.getHeight();
		GuiHelper.drawPoint((width / 16) * 15+27, (height / 16) * 15.2F-4, 14, pointColor);
		GuiHelper.drawPoint((width / 16) * 14.625F+27, (height / 16) * 15.2F-4, 14, pointColor);
		GuiHelper.drawPoint((width / 16) * 14.25F+27, (height / 16) * 15.2F-4, 14, currentPointColor);
		GuiHelper.drawPoint((width / 16) * 13.885F+27, (height / 16) * 15.2F-4, 14, pointColor);
		Texture welcomeLogo = ResourceHelper.brandingWelcome;
		
		float w = 78.5F;
		float h = 39F;
		
		GuiHelper.drawTexturedRectangle(width / 26-4, (height / 16) * 14.75F-4, w, h, welcomeLogo, Utilities.reAlpha(0xffffff, 1F * opacity));
		super.render(opacity);
	}
}
