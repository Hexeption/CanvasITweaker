package com.kiloclient.ui.interactable;

import org.newdawn.slick.TrueTypeFont;

import com.kiloclient.KiLO;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.InteractableParent;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.Utilities;


public class TextBoxAlt extends TextBox {
	
	private int underColor;
	private float underlineWidth;
	protected int fontColorPlaceholder = 666;
	protected boolean underline = true;

	public TextBoxAlt(InteractableParent parent, String placeholder, float x, float y, float width, float height, TrueTypeFont font, int fontColor, Align fontAlignH, Align fontAlignV) {
		this(parent, placeholder, x, y, width, height, font, fontColor, ColorHelper.DARK_BLUE.getColorCode(), 2f, fontAlignH, fontAlignV);
	}
	
	public TextBoxAlt(InteractableParent parent, String placeholder, float x, float y, float width, float height, TrueTypeFont font, int fontColor, int underColor, float underlineWidth, Align fontAlignH, Align fontAlignV) {
		super(parent, placeholder, x, y, width, height, font, fontColor, fontAlignH, fontAlignV);
		this.underColor = underColor;
		this.underlineWidth = underlineWidth;
	}
	
	public TextBoxAlt(InteractableParent parent, String placeholder, float x, float y, float width, float height, TrueTypeFont font, int fontColor, int fontColorPlaceholder, Align fontAlignH, Align fontAlignV, boolean underline) {
		super(parent, placeholder, x, y, width, height, font, fontColor, fontAlignH, fontAlignV);
		this.fontColorPlaceholder = fontColorPlaceholder;
		this.underline = underline;
		this.underColor = KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground();
		this.underlineWidth = 1.5F;
	}
	
	public void render(float opacity) {
		float cursorBlink = 1f;
		boolean cursor = (active?(timer.isTime(cursorBlink/2f)?true:false):false);

		if (timer.isTime(cursorBlink)) {
			timer.reset();
		}
		
		if (underline == true) {
			GuiHelper.drawRectangle(x - 8, y + height, x + width + 8, y+height+underlineWidth, Utilities.reAlpha(underColor, 1F * opacity));
		}
		
		GuiHelper.startClip(x, y, x+width, y+height);

		float xx = 0;
		
		if (!(cursorPos > text.length()))
			xx = x+scroll+font.getWidth(text.substring(0, cursorPos));

		if (active) {
			GuiHelper.drawRectangle(xx-1, y+(height/2)-(font.getHeight(text)/2), xx+1, y+(height/2)+(font.getHeight(text)/2), Utilities.reAlpha(fontColor, cursor?0.8f:0.2f*opacity));
			float cp = 0;
			
			if (!(cursorPos > text.length()))
				cp = (x+font.getWidth(text.substring(0, cursorPos))+scrollTo);
			if (cp < x) {
				scrollTo+= (x-cp);
				scrollTo-= Math.min(width, scrollTo);
			}
			if (cp > x+width) {
				scrollTo+= (x+width)-cp;
			}
		}
		
		scroll+= (scrollTo-scroll)/scrollSpeed;

		startSelect = Math.min(Math.max(0, startSelect), text.length());
		endSelect = Math.min(Math.max(0, endSelect), text.length());
		
		float startX = x+scroll+font.getWidth(text.substring(0, startSelect));
		float endX = x+scroll+font.getWidth(text.substring(0, endSelect));
		GuiHelper.drawRectangle(startX, y+(height/2)-(font.getHeight(text)/2), endX, y+(height/2)+(font.getHeight(text)/2), Utilities.reAlpha(0xFF55AAFF, 0.5f*opacity));
		
		if (this.fontColorPlaceholder == 666) {
			GuiHelper.drawStringFromTTF(font, x+scroll, y+(height/2), text.length() != 0 || active?text:placeholder, Utilities.reAlpha(fontColor, text.length() != 0?((anim/2f)+0.5f)*opacity:0.5f*opacity), fontAlignH, fontAlignV);
		} else {
			GuiHelper.drawStringFromTTF(font, x+scroll, y+(height/2), text.length() != 0 || active?text:placeholder, Utilities.reAlpha(active || text != placeholder ? fontColor : fontColorPlaceholder, text.length() != 0?((anim/2f)+0.5f)*opacity:0.5f*opacity), fontAlignH, fontAlignV);
		}
		
		GuiHelper.endClip();
	}
}
