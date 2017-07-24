package com.canvasclient.ui.interactable;

import org.newdawn.slick.opengl.Texture;

import com.canvasclient.render.GuiHelper;
import com.canvasclient.ui.InteractableParent;

public class IconButtonSelectable extends IconButton {

	public boolean selected = false;
	
	public IconButtonSelectable(InteractableParent p, float x, float y, float w, float h, int bc, Texture i) {
		super(p, x, y, w, h, bc, i);
	}
	
	public void render(float opacity) {
		if (selected) {
			GuiHelper.drawTexturedRectangle(x, y, width, height, icon, 0xFFFFFFFF);
			GuiHelper.drawTexturedRectangle(x, y, width, height, icon, 0x64000000);
		} else {
			super.render(opacity);
		}
	}
	
	public void mouseClick(int mx, int my, int b) {

		if (mouseOver(mx, my)) {
			selected = true;
		} else {
			selected = false;
		}
		super.mouseClick(mx, my, b);
	}
	
	

}
