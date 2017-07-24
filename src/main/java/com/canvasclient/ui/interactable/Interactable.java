package com.canvasclient.ui.interactable;

import com.canvasclient.render.utilities.Align;
import com.canvasclient.ui.InteractableParent;
import org.newdawn.slick.TrueTypeFont;

public class Interactable {

	public InteractableParent parent;
	public InteractableType type;
	public boolean shown, enabled, hover, active;
	public float x, y, width, height;
	public TrueTypeFont font;
	public int fontColor;
	public Align fontAlignH, fontAlignV;
	
	public Interactable(InteractableParent p, InteractableType t, float xx, float yy, float w, float h, TrueTypeFont f, int fc, Align fh, Align fv) {
		parent = p;
		type = t;
		x = xx;
		y = yy;
		width = w;
		height = h;
		font = f;
		fontColor = fc;
		fontAlignH = fh;
		fontAlignV = fv;
		shown = true;
		enabled = true;
	}
	
	public void update(int mx, int my) {
		hover = mouseOver(mx, my) && enabled;
	}

	public void mouseClick(int mx, int my, int b) {
		if (mouseOver(mx, my)) {
			parent.handleInteraction(this);
			active = true;
		} else {
			active = false;
		}
	}
	
	public void mouseRelease(int mx, int my, int b) {}
	public void mouseScroll(int s) {}
	public boolean mouseOver(int mx, int my) {
		return parent.hover && shown && mx >= x && mx < x+width && my > y && my <= y+height;
	}
	
	public void keyboardPress(int key) {}
	public void keyboardRelease(int key) {}
	public void keyTyped(int key, char keyChar) {}
	
	public void render(float opacity) {}
	
	public enum InteractableType {
		BUTTON,
		SLIDER,
		CHECK_BOX,
		TEXT_BOX,
		LINK,
		CHAT_COMPONENT;
	}

}
