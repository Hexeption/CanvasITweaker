package com.kiloclient.ui.interactable.slotlist.part;

import net.minecraft.item.ItemStack;

public class NukerBlock {

	public ItemStack stack;
	public String display, name;
	
	public NukerBlock(ItemStack stack, String display, String name) {
		this.stack = stack;
		this.display = display;
		this.name = name;
	}
}
