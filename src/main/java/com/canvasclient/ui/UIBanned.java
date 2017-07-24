package com.canvasclient.ui;

import com.canvasclient.Canvas;
import com.canvasclient.render.FontHandler;
import org.lwjgl.opengl.Display;

import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.utilities.Utilities;

public class UIBanned extends UI {

	public UIBanned(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = "This Account Has Been Banned";
	}
	
	@Override
	public void handleInteraction(Interactable i) {
	}
	
	public void render(float opacity) {
		String name = Canvas.getCanvas().getUserControl().canvasName;
		String email = Canvas.getCanvas().getUserControl().email;
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2, Display.getHeight()/2-20, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.BOTTOM);
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(20).getWidth("In-Game Name: "+name)/2), Display.getHeight()/2+20, "In-Game Name:", Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(20).getWidth("In-Game Name: "+name)/2)+ FontHandler.ROUNDED_BOLD.get(20).getWidth("In-Game Name:  "), Display.getHeight()/2+20, name, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity));

		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(20).getWidth("Email: "+email)/2), Display.getHeight()/2+50, "Email:", Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(20).getWidth("Email: "+email)/2)+ FontHandler.ROUNDED_BOLD.get(20).getWidth("Email:  "), Display.getHeight()/2+50, email, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity));
		super.render(opacity);
	}
}
