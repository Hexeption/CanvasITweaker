package com.canvasclient.ui.interactable;

import com.canvasclient.render.utilities.Align;
import com.canvasclient.ui.InteractableParent;
import org.newdawn.slick.TrueTypeFont;


public class PasswordBox extends TextBox {
	
	public PasswordBox(InteractableParent parent, String placeholder, float x, float y, float width, float height, TrueTypeFont font, int fontColor, Align fontAlignH, Align fontAlignV) {
		super(parent, placeholder, x, y, width, height, font, fontColor, fontAlignH, fontAlignV);
	}
	
	public void update(int mx, int my) {
		String prev = text;
		text = text.replaceAll("(?s).", "\u00b0");
		super.update(mx, my);
		text = prev;
	}
	
	public void mouseClick(int mx, int my, int b) {
		String prev = text;
		text = text.replaceAll("(?s).", "\u00b0");
		super.mouseClick(mx, my, b);
		text = prev;
	}
	
	public void mouseRelease(int mx, int my, int b) {
		String prev = text;
		text = text.replaceAll("(?s).", "\u00b0");
		super.mouseRelease(mx, my, b);
		text = prev;
	}
	
	public void keyboardPress(int key) {
		super.keyboardPress(key);
	}
	
	public void keyboardRelease(int key) {
		super.keyboardRelease(key);
	}
	
	public void keyTyped(int key, char keyChar) {
		super.keyTyped(key, keyChar);
	}

	public void render(float opacity) {
		String prev = text;
		text = text.replaceAll("(?s).", "\u00b0");
		super.render(opacity);
		text = prev;
	}
}
