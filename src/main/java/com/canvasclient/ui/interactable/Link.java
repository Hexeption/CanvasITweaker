package com.canvasclient.ui.interactable;

import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.AnimationTimer;
import com.canvasclient.ui.InteractableParent;
import com.canvasclient.utilities.Utilities;
import org.newdawn.slick.TrueTypeFont;


public class Link extends Interactable {
	
	public String text;
	public AnimationTimer anim = new AnimationTimer(10);
	public final float animSpeed = 3f;
	
	public Link(InteractableParent p, String t, float x, float y, TrueTypeFont f, int fc, Align fh, Align fv) {
		super(p, Interactable.InteractableType.LINK, x-(fh==Align.CENTER?f.getWidth(t)/2:(fh==Align.RIGHT?f.getWidth(t):0)), y-(fv==Align.CENTER?f.getHeight()/2:(fv==Align.BOTTOM?f.getHeight():0)), 0, 0, f, fc, fh, fv);
		text = t;
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		if (font != null) {
			width = font.getWidth(text);
			height = font.getHeight(text);
		}
		
		anim.update(hover);
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y+height+3, x+((width+2)*(float)anim.getValue()), y+height+5, Utilities.reAlpha(fontColor, (float)anim.getValue()*opacity));
		GuiHelper.startClip(x-1, y, x+width+1, y+height);
		
		GuiHelper.drawStringFromTTF(font, x+(width/2), y+(height/2)+((height*1.5f)*(float)anim.getValue()), text, Utilities.reAlpha(fontColor, 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.drawStringFromTTF(font, x+(width/2), y+(height/2)-(height*1.5f)+((height*1.5f)*(float)anim.getValue()), text, Utilities.reAlpha(fontColor, 1f*opacity), Align.CENTER, Align.CENTER);
		
		GuiHelper.endClip();
	}
}
