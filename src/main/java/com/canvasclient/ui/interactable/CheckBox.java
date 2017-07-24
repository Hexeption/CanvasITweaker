package com.canvasclient.ui.interactable;

import org.newdawn.slick.TrueTypeFont;

import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.InteractableParent;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.utilities.Utilities;


public class CheckBox extends Interactable {
	
	public String text;
	public float anim;
	public final float animSpeed = 2f;
	
	public CheckBox(InteractableParent p, String t, float x, float y, TrueTypeFont f, int fc) {
		super(p, Interactable.InteractableType.CHECK_BOX, x, y, 22, 22, f, fc, Align.LEFT, Align.CENTER);
		text = t;
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		
		anim+= ((hover?1f:0f)-anim)/animSpeed;
	}

	public void mouseClick(int mx, int my, int b) {
		if (mouseOver(mx, my)) {
			parent.handleInteraction(this);
		}
	}
	
	public void render(float opacity) {
		float ext = 2*anim;
		GuiHelper.drawRectangle(x-ext, y-ext, x+width+ext, y+height+ext, Utilities.reAlpha(ColorHelper.GREEN.getColorCode(), anim*opacity));
		
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity));

		if (active) {
			float[] pointX = new float[] {5,7,-2,-7,-5,-2};
			float[] pointY = new float[] {-5,-3,6,1,-1,2};
			GuiHelper.drawPolygon(x+(width/2), y+(height/2), pointX, pointY, Utilities.reAlpha(ColorHelper.GREEN.getColorCode(), 1f*opacity));
		}

		GuiHelper.drawStringFromTTF(font, x+width+16, y+(height/2), text, Utilities.reAlpha(fontColor, 1f*opacity), fontAlignH, fontAlignV);
	}
}
