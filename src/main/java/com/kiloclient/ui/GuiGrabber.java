package com.kiloclient.ui;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.kiloclient.KiLO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiGrabber extends GuiScreen {
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}