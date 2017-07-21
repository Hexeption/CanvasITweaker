package com.kiloclient.ui;

import com.kiloclient.manager.ChatManager;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.Button;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.Utilities;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.opengl.Display;

public class UIConnecting extends UI {

	private String ip;
	
	public UIConnecting(UI parent, String ip) {
		super(parent);
		this.ip = ip;
	}
	
	@Override
	public void init() {
		title = "Connecting to";
    	ChatManager.chatLines.clear();
		
		interactables.clear();
		interactables.add(new Button(this, "Cancel", Display.getWidth()/2-200, Display.getHeight()-128, 400, 64, FontHandler.ROUNDED_BOLD.get(25), ColorHelper.RED.getColorCode(), null, 0));
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			if (((GuiConnecting)mc.currentScreen).networkManager != null) {
				((GuiConnecting)mc.currentScreen).networkManager.closeChannel(new TextComponentString("Aborted") {
				});
            }

            mc.displayGuiScreen(((GuiConnecting)mc.currentScreen).previousGuiScreen);
			break;
		}
	}
	
	public void render(float opacity) {
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()/2, Display.getHeight()-192, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.BOTTOM);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()/2, Display.getHeight()-192, ip, Utilities.reAlpha(ColorHelper.DARK_BLUE.getColorCode(), 1f*opacity), Align.CENTER, Align.TOP);
		
		GuiHelper.drawLoaderAnimation(Display.getWidth()/2, Display.getHeight()/2, 32, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		
		super.render(opacity);
	}
}
