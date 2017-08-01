package com.canvasclient.ui;

import com.canvasclient.infrastructure.ChatManager;
import com.canvasclient.mixin.imp.IMixinGuiConnecting;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.interactable.Button;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.utilities.Utilities;
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
			if (((IMixinGuiConnecting)mc.currentScreen).getNetworkManager() != null) {
				((IMixinGuiConnecting)mc.currentScreen).getNetworkManager().closeChannel(new TextComponentString("Aborted") {
				});
            }

            mc.displayGuiScreen(((IMixinGuiConnecting)mc.currentScreen).getPreviousGuiScreen());
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
