package com.kiloclient.ui;

import com.kiloclient.render.FontHandler;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.kiloclient.KiLO;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.Link;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Utilities;

public class UICompleted extends UI {

	public UICompleted(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		//title = "KiLO";
		interactables.clear();
		interactables.add(new Link(this, "GO TO MAIN MENU", Display.getWidth()/2, (Display.getHeight()/2+50), FontHandler.ROUNDED_LIGHT.get(30), 0xff464646, Align.CENTER, Align.CENTER));
		
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			KiLO.getKiLO().getUIHandler().changeUI(new UIMainMenu(this));
		}
	}
	
	public void render(float opacity) {
		//Dark gray background
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), 0xFF252525);
		float width = Display.getWidth();
		float height = Display.getHeight();
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2 - ((FontHandler.ROUNDED_BOLD.get(40).getWidth("Your KiLO experience is ready"))/2), (Display.getHeight()/2)-50, "Your KiLO experience is ready", 0xFFffffff);
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(12), 0, 0, "", Utilities.reAlpha(0xffffff, 1f*opacity), Align.RIGHT, Align.BOTTOM);
		Texture welcomeLogo = ResourceHelper.brandingWelcome;
		
		float w = 78.5F;
		float h = 39F;
		
		GuiHelper.drawTexturedRectangle(width / 26-4, (height / 16) * 14.75F-4, w, h, welcomeLogo, Utilities.reAlpha(0xffffff, 1F * opacity));
		int currentPointColor = 0xFF1DB8C2;
		int pointColor = 0xFF414141;
		
		GuiHelper.drawPoint((width / 16) * 15+27, (height / 16) * 15.2F-4, 14, currentPointColor);
		GuiHelper.drawPoint((width / 16) * 14.625F+27, (height / 16) * 15.2F-4, 14, pointColor);
		GuiHelper.drawPoint((width / 16) * 14.25F+27, (height / 16) * 15.2F-4, 14, pointColor);
		GuiHelper.drawPoint((width / 16) * 13.885F+27, (height / 16) * 15.2F-4, 14, pointColor);
		
		super.render(opacity);
	}
}
