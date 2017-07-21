package com.kiloclient.ui;

import com.kiloclient.render.FontHandler;
import org.lwjgl.opengl.Display;

import com.kiloclient.KiLO;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.Utilities;

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
		String name = KiLO.getKiLO().getUserControl().kiloName;
		String email = KiLO.getKiLO().getUserControl().email;
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2, Display.getHeight()/2-20, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.BOTTOM);
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(20).getWidth("In-Game Name: "+name)/2), Display.getHeight()/2+20, "In-Game Name:", Utilities.reAlpha(KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(20).getWidth("In-Game Name: "+name)/2)+ FontHandler.ROUNDED_BOLD.get(20).getWidth("In-Game Name:  "), Display.getHeight()/2+20, name, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity));

		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(20).getWidth("Email: "+email)/2), Display.getHeight()/2+50, "Email:", Utilities.reAlpha(KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()/2-(FontHandler.ROUNDED_BOLD.get(20).getWidth("Email: "+email)/2)+ FontHandler.ROUNDED_BOLD.get(20).getWidth("Email:  "), Display.getHeight()/2+50, email, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity));
		super.render(opacity);
	}
}
