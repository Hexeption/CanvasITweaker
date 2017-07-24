package com.canvasclient.ui.interactable.slotlist.part;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class ChatLine {

	public TextComponentString formatted;
	public String unformatted;
	
	public ChatLine(TextComponentString message) {
		formatted = message.createCopy();
		formatted.getSiblings().clear();
		
		List list = GuiUtilRenderComponents.splitText(message, 1000, Minecraft.getMinecraft().fontRenderer, false, false);
		
		if (list.size() > 0) {
			message = (TextComponentString)list.get(0);
		}
		
		if (message.getSiblings().size() > 0) {
			for(Object o : message.getSiblings()) {
				TextComponentString icc = (TextComponentString)o;
				Style cs = icc.getStyle();
				String s = icc.getUnformattedText();
				
				List<TextComponentString> newList = new ArrayList<TextComponentString>();
				
				if (cs.getHoverEvent() != null || cs.getClickEvent() != null) {
					formatted.appendSibling(new TextComponentString(s).setStyle(cs));
					continue;
				}
				
				String[] parts = s.split(" ");
				if (parts.length > 1) {
					for(String ss : parts) {
						newList.add((TextComponentString) new TextComponentString(ss+" ").setStyle(cs));
					}
				} else {
					newList.add((TextComponentString) new TextComponentString(s).setStyle(cs));
				}
				
				for(TextComponentString newICC : newList) {
					formatted.appendSibling(newICC);
				}
			}
		} else {
			TextComponentString icc = message;
			Style cs = icc.getStyle();
			
			List<TextComponentString> newList = new ArrayList<TextComponentString>();
			
			String s = icc.getUnformattedText();
			
			String[] parts = s.split(" ");
			if (parts.length > 1) {
				for(String ss : parts) {
					newList.add((TextComponentString) new TextComponentString(ss+" ").setStyle(cs));
				}
			} else {
				newList.add((TextComponentString) new TextComponentString(s).setStyle(cs));
			}
			
			for(TextComponentString newICC : newList) {
				formatted.appendSibling(newICC);
			}
		}
		
		this.unformatted = message.getUnformattedText();
	}
}
