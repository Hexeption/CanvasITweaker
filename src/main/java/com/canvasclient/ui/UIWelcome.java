package com.canvasclient.ui;

import com.canvasclient.Canvas;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.Link;
import com.canvasclient.utilities.Utilities;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

public class UIWelcome extends UI {

	public UIWelcome(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		
		interactables.clear();
		interactables.add(new Link(this, "GET STARTED", Display.getWidth() / 2  -  ((FontHandler.ROUNDED_LIGHT.get(30).getWidth(">>")) / 2)  -  16, (Display.getHeight() / 2 + (Display.getHeight() / 3) - 70), FontHandler.ROUNDED_LIGHT.get(30), 0xff464646, Align.CENTER, Align.CENTER));
		
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			Canvas.getCanvas().getUIHandler().changeUI(new UICreatePassport(this));
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), 0xFF252525);
		float screenWidth = Display.getWidth();
		float screenHeight = Display.getHeight();
		
		Texture logo = ResourceHelper.brandingWelcome;
		float width = 400;
		float height = 200;
		float xPosition = Math.round((Display.getWidth() / 2) - (width / 2));
		float yPosition = Math.round((Display.getHeight() / 2) - (height / 2) - 50);
		GuiHelper.drawTexturedRectangle(xPosition, yPosition, width, height, logo, Utilities.reAlpha(0xffffff, 1f * opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(30), Display.getWidth() / 2  +  ((FontHandler.ROUNDED_LIGHT.get(30).getWidth("GET STARTED")) / 2) - 15, (Display.getHeight() / 2 + (Display.getHeight() / 3) - 90), ">>", 0xFF595959);
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(12), 0, 0, "", Utilities.reAlpha(0xffffff, 1f * opacity), Align.RIGHT, Align.BOTTOM);

		int currentPointColor = 0xFF1DB8C2;
		int pointColor = 0xFF414141;
		
		GuiHelper.drawPoint((screenWidth  /  16) * 15 + 27, (screenHeight  /  16)  *  15.2F - 4, 14, pointColor);
		GuiHelper.drawPoint((screenWidth  /  16) * 14.625F + 27, (screenHeight  /  16)  *  15.2F - 4, 14, pointColor);
		GuiHelper.drawPoint((screenWidth  /  16) * 14.25F + 27, (screenHeight  /  16)  *  15.2F - 4, 14, pointColor);
		GuiHelper.drawPoint((screenWidth  /  16) * 13.885F + 27, (screenHeight  /  16)  *  15.2F - 4, 14, currentPointColor);
		
		super.render(opacity);
	}
}
