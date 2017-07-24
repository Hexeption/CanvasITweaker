package com.canvasclient.ui;

import com.canvasclient.Canvas;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.utilities.IMinecraft;
import org.lwjgl.opengl.Display;

public abstract class UI extends InteractableParent implements IMinecraft {

	protected UI parent;
	public String title;
	
	public UI(UI p) {
		super(-1, -1, -1, -1);
		parent = p;
		if (!(this instanceof UIInGameMenuFriend))	
			init();
	}
	
	public abstract void init();

	public static void drawDefaultBackground(boolean branding, float opacity) {
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground());
	}

	public static void drawDarkerBackground(boolean branding, float opacity) {
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground());
	}
}
