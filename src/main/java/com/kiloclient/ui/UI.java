package com.kiloclient.ui;

import org.lwjgl.opengl.Display;

import com.kiloclient.KiLO;
import com.kiloclient.render.GuiHelper;

import net.minecraft.client.Minecraft;

public abstract class UI extends InteractableParent {

	protected final Minecraft mc = Minecraft.getMinecraft();
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
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground());
	}

	public static void drawDarkerBackground(boolean branding, float opacity) {
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground());
	}
}
