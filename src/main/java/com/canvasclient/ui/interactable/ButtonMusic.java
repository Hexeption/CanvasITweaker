package com.canvasclient.ui.interactable;

import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.InteractableParent;
import com.canvasclient.utilities.Utilities;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;


public class ButtonMusic extends Interactable {
	
	public String text;
	public int buttonColor, iconColor;
	public float anim;
	public final float animSpeed = 2f;
	public Texture icon;
	public float size;
	
	public ButtonMusic(InteractableParent p, String t, float x, float y, float w, float h, TrueTypeFont f, int bc, int ic, Texture i, float s) {
		super(p, Interactable.InteractableType.BUTTON, x, y, w, h, f, ColorHelper.WHITE.getColorCode(), Align.LEFT, Align.CENTER);
		text = t;
		buttonColor = bc;
		iconColor = ic;
		icon = i;
		size = s;
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		
		anim+= ((hover?1f:0f)-anim)/animSpeed;
	}
	
	public void render(float opacity) {
		float prevY = y;
		
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(Utilities.blendColor(buttonColor, 0xFF000000, enabled?1f:0.4f), (buttonColor!=0x00000000)?opacity:0f));
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), (anim*0.4f)*opacity));

		GuiHelper.startClip(x, y, x+width, y+height);

		float iconX = x+8;
		float textX = x+24;
		
		if (icon != null) {
			GuiHelper.drawTexturedRectangle(iconX, y+(height/2)-(size/2), size, size, icon, Utilities.reAlpha(Utilities.blendColor(iconColor, 0xFF000000, enabled || buttonColor == 0x00000000?1f:0.4f), 1f*opacity));
		}
		
		if (text != null) {
			GuiHelper.drawStringFromTTF(font, textX, y+(height/2), text, Utilities.reAlpha(Utilities.blendColor(fontColor, 0xFF000000, enabled || buttonColor == 0x00000000?1f:0.4f), 1f*opacity), fontAlignH, fontAlignV);
		} else {
			float gap = Math.min(width, height)/4;
			GuiHelper.drawLoaderAnimation(x+(width/2), y+(height/2), gap, Utilities.reAlpha(Utilities.blendColor(fontColor, 0xFF000000, enabled || buttonColor == 0x00000000?1f:0.4f), 1f*opacity));
		}
		
		GuiHelper.endClip();
	}
}
