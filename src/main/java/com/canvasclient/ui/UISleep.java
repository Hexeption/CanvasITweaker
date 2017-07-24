package com.canvasclient.ui;


import com.canvasclient.render.FontHandler;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.lwjgl.opengl.Display;

import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.interactable.Button;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.utilities.Utilities;

public class UISleep extends UI {
	
	public UISleep(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = "Sleeping...";
		
		interactables.clear();
		interactables.add(new Button(this, "Wake Up", Display.getWidth()/2-200, Display.getHeight()-128, 400, 64, FontHandler.ROUNDED_BOLD.get(25), ColorHelper.RED.getColorCode(), null, 0));
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			NetHandlerPlayClient nethandlerplayclient = this.mc.player.connection;
			nethandlerplayclient.sendPacket(new CPacketEntityAction(this.mc.player, CPacketEntityAction.Action.STOP_SLEEPING));
			break;
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0.5f*opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth()/2, Display.getHeight()-192, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.BOTTOM);
		
		GuiHelper.drawLoaderAnimation(Display.getWidth()/2, Display.getHeight()/2, 32, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		
		super.render(opacity);
	}
}
