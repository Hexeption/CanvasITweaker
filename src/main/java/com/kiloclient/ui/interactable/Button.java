package com.kiloclient.ui.interactable;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.InteractableParent;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.render.utilities.AnimationTimer;
import com.kiloclient.utilities.Utilities;


public class Button extends Interactable {
	
	public String text;
	public int buttonColor, iconColor;
	public AnimationTimer anim = new AnimationTimer(10);
	public final float animSpeed = 2f;
	public Texture icon;
	public float size;
	
	public Button(InteractableParent p, String t, float x, float y, float w, float h, TrueTypeFont f, int bc, Texture i, float s) {
		this(p, t, x, y, w, h, f, bc, i, s, Align.CENTER, Align.CENTER);
	}
	
	public Button(InteractableParent p, String t, float x, float y, float w, float h, TrueTypeFont f, int bc, Texture i, float s, Align ha, Align va) {
		this(p, t, x, y, w, h, f, bc, ColorHelper.WHITE.getColorCode(), i, s, ha, va);
	}
	
	public Button(InteractableParent p, String t, float x, float y, float w, float h, TrueTypeFont f, int bc, int ic, Texture i, float s, Align ha, Align va) {
		super(p, Interactable.InteractableType.BUTTON, x, y, w, h, f, ColorHelper.WHITE.getColorCode(), ha, va);
		text = t;
		buttonColor = bc;
		iconColor = ic;
		icon = i;
		size = s;
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		anim.update(hover);
	}
	
	public void render(float opacity) {
		float prevY = y;
		y = (float)(Math.floor(y));
		
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(Utilities.blendColor(buttonColor, 0xFF000000, enabled?1f:0.4f), (buttonColor!=0x00000000)?opacity:0f));
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), ((float)anim.getValue()*0.4f)*opacity));

		GuiHelper.startClip(x, y, x+width, y+height);

		Align ha = fontAlignH;
		float iconX = x+width-16-size;
		float textX = x+16;
		
		switch(ha) {
		case CENTER:
			textX = x+(width/2);
			break;
		case RIGHT:
			textX = x+width-8;
			iconX = x+8;
			break;
		default:
			break;
		}
		
		if (icon != null && text != null) {
			GuiHelper.drawTexturedRectangle(iconX, fontAlignV == Align.TOP?y+16:fontAlignV == Align.CENTER?y+(height/2)-(size/2):y+height-16-size, size, size, icon, Utilities.reAlpha(Utilities.blendColor(iconColor, 0xFF000000, enabled || buttonColor == 0x00000000?1f:0.4f), 1f*opacity));
			if (ha == Align.CENTER) {
				ha = Align.LEFT;
				textX = x+16;
			}
		}
		
		if (text != null) {
			GuiHelper.drawStringFromTTF(font, textX, fontAlignV == Align.TOP?y+16:fontAlignV == Align.CENTER?y+(height/2):y+height-16, text, Utilities.reAlpha(Utilities.blendColor(fontColor, 0xFF000000, enabled || buttonColor == 0x00000000?1f:0.4f), 1f*opacity), ha, fontAlignV);
		} else {
			float gap = Math.min(width, height)/4;
			GuiHelper.drawLoaderAnimation(x+(width/2), y+(height/2), gap, Utilities.reAlpha(Utilities.blendColor(fontColor, 0xFF000000, enabled || buttonColor == 0x00000000?1f:0.4f), 1f*opacity));
		}
		
		GuiHelper.endClip();
		y = prevY;
	}
}
