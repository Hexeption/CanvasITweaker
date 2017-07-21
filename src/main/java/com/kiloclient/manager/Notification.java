package com.kiloclient.manager;

import com.kiloclient.render.FontHandler;
import org.lwjgl.opengl.Display;

import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.TextureImage;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;

public class Notification {

	public TextureImage image;
	public String message;
	public float x, y;
	public Timer timer = new Timer();
	private final float time = 5f, width = 400f, height = 48;
	private boolean multi;
	
	public Notification(String icon, String message, boolean multi) {
		timer.reset();
		
		this.message = message;
		
		this.x = (Display.getWidth()-width)/2;
		this.y = -height;
		
		if (icon != null) {
			this.image = ResourceHelper.downloadTexture(icon);
		}
		
		this.multi = multi;
	}
	
	public void update() {
		if (timer.isTime(time)) {
			y+= ((-height*2)-y)/5f;
		} else {
			if (y < -height/2) {
				timer.reset();
			}
			y+= (8-y)/5f;
		}
		
		if (y < -height) {
			NotificationManager.list.remove(this);
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF1A1A1A, 1f*opacity));
		
		if (image != null && image.getTexture() != null) {
			GuiHelper.drawTexturedRectangle(x+4, y+4, 40, 40, image.getTexture(), opacity);
		}
		
		if (message != null) {
			if (!multi) {
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), x+64, y+24, message, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.CENTER);
			} else {
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), x+(width/2), y+24, message, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
			}
		}
	}
	
}
