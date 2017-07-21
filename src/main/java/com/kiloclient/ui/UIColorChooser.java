package com.kiloclient.ui;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.TextureImage;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.IconButtonSelectable;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.Link;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Utilities;

public class UIColorChooser extends UI {
	
	private TextureImage kiloHead;
	
	float width = Display.getWidth();
	float height = Display.getHeight();
	
	public UIColorChooser(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		float width = Display.getWidth();
		float height = Display.getHeight();
		
		title = "Customise KiLO";
		interactables.clear();
		int i = 0;
		interactables.add(new Link(this, "CONTINUE",  width / 1.225F, height / 1.31F + ((height / 1.125F - height / 1.325F) / 4), FontHandler.ROUNDED_BOLD.get(30), 0xFF464646, Align.LEFT, Align.TOP));
		interactables.add(new IconButtonSelectable(this, width / 6, height / 1.345F + ((height / 1.125F - height / 1.325F) / 4), 64, 64, 0xFFFFFFFF, ResourceHelper.colorChooserColors[0]));
		interactables.add(new IconButtonSelectable(this, width / 4.2F, height / 1.345F + ((height / 1.125F - height / 1.325F) / 4), 64, 64, 0xFFFFFFFF, ResourceHelper.colorChooserColors[1]));
		interactables.add(new IconButtonSelectable(this, width / 3.225F, height / 1.345F + ((height / 1.125F - height / 1.325F) / 4), 64, 64, 0xFFFFFFFF, ResourceHelper.colorChooserColors[2]));
		interactables.add(new IconButtonSelectable(this, width / 2.625F, height / 1.345F + ((height / 1.125F - height / 1.325F) / 4), 64, 64, 0xFFFFFFFF, ResourceHelper.colorChooserColors[3]));
		interactables.add(new IconButtonSelectable(this, width / 2.2125F, height / 1.345F + ((height / 1.125F - height / 1.325F) / 4), 64, 64, 0xFFFFFFFF, ResourceHelper.colorChooserColors[4]));
		interactables.add(new IconButtonSelectable(this, width / 1.9125F, height / 1.345F + ((height / 1.125F - height / 1.325F) / 4), 64, 64, 0xFFFFFFFF, ResourceHelper.colorChooserColors[5]));
		interactables.add(new IconButtonSelectable(this, width / 1.6875F, height / 1.345F + ((height / 1.125F - height / 1.325F) / 4), 64, 64, 0xFFFFFFFF, ResourceHelper.colorChooserColors[6]));
		interactables.add(new IconButtonSelectable(this, width / 1.505F, height / 1.345F + ((height / 1.125F - height / 1.325F) / 4), 64, 64, 0xFFFFFFFF, ResourceHelper.colorChooserColors[7]));
		interactables.add(new IconButtonSelectable(this, width / 1.36F, height / 1.345F + ((height / 1.125F - height / 1.325F) / 4), 64, 64, 0xFFFFFFFF, ResourceHelper.colorChooserColors[8]));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
	}
	
	public void render(float opacity) {
		
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), 0xFF252525);
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth() / 2, (height / 9) / 2+32, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		

		Texture welcomeLogo = ResourceHelper.brandingWelcome;
		
		float w = 78.5F;
		float h = 39F;
		
		float width = Display.getWidth();
		float height = Display.getHeight();
		
		GuiHelper.drawTexturedRectangle(width / 26-4, (height / 16) * 14.75F-4, w, h, welcomeLogo, Utilities.reAlpha(0xffffff, 1F * opacity));
		
		GuiHelper.drawRectangle(width / 28, height / 5, (width / 1.0375F), height / 1.125F, 0xFFd7d7d7);
		
		Texture colourchooser = ResourceHelper.colourChooserImage;
		
		GuiHelper.drawRectangle(width / 28, height / 1.325F, (width / 1.0375F), height / 1.125F, 0xFFffffff);
		
		GuiHelper.drawTexturedRectangle(width / 14, height / 4.3F, width / 1.1F - width / 14, height / 1.325F - height / 4.3F, colourchooser, 0xFFFFFFFF);

		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(40), width / 18, height / 1.325F + ((height / 1.125F - height / 1.325F) / 4), "I LIKE...", 0xFF464646);

		int currentPointColor = 0xFF1DB8C2;
		int pointColor = 0xFF414141;
		
		GuiHelper.drawPoint((width / 16) * 15+27, (height / 16) * 15.2F-4, 14, pointColor);
		GuiHelper.drawPoint((width / 16) * 14.625F+27, (height / 16) * 15.2F-4, 14, currentPointColor);
		GuiHelper.drawPoint((width / 16) * 14.25F+27, (height / 16) * 15.2F-4, 14, pointColor);
		GuiHelper.drawPoint((width / 16) * 13.885F+27, (height / 16) * 15.2F-4, 14, pointColor);

		super.render(opacity);
	}
	
	public void submit(int colorScheme) {
		try {
			APIHelper.setColorScheme(KiLO.getKiLO().getUserControl().clientID, colorScheme);
			KiLO.getKiLO().getUserControl().colorSchemeID = colorScheme;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}
	
	

	@Override
	public void handleInteraction(Interactable i) {
		switch (interactables.indexOf(i)) {
		case 0:
			boolean selected = false;
			int count = 0;
			for (Interactable interactable : this.interactables) {
				if (interactable instanceof IconButtonSelectable) {		
					IconButtonSelectable cast = (IconButtonSelectable)interactable;
					if (cast.selected) {
						System.out.println(interactables.indexOf(cast));
						this.submit(count);
						selected = true;
					}
					count++;
				}
			}
			if (selected)
				KiLO.getKiLO().getUIHandler().changeUI(new UICompleted(this));
			break;
		}
	}
}