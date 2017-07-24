package com.canvasclient.ui.interactable.slotlist.part;

public class Macro {

	public String name, command;
	public int keybind;
	
	public Macro(String name, String command, int keybind) {
		this.name = name;
		this.command = command;
		this.keybind = keybind;
	}
}
