package com.kiloclient.ui.interactable.slotlist.part;

import com.kiloclient.render.utilities.ColorHelper;

import net.minecraft.client.Minecraft;

public class Macro {

	public String name, command;
	public int keybind;
	
	public Macro(String name, String command, int keybind) {
		this.name = name;
		this.command = command;
		this.keybind = keybind;
	}
}
