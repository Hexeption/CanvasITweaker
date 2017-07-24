package com.canvasclient.utilities;

import com.canvasclient.Canvas;
import com.canvasclient.mixin.imp.IMixinGuiConnecting;
import com.canvasclient.ui.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;

public class UIUtilities {

	public static UI newUI(GuiScreen mcGUI) {
		
		if (mcGUI instanceof GuiMainMenu) {
			if (Canvas.getCanvas().getUserControl() == null) {
				return new UIWelcome(Canvas.getCanvas().getUIHandler().getCurrentUI());
			}
			if (!Canvas.getCanvas().getUserControl().gameStatus.equalsIgnoreCase("verified") && !Canvas.getCanvas().getUserControl().gameStatus.equalsIgnoreCase("banned")) {
				return new UIWelcome(Canvas.getCanvas().getUIHandler().getCurrentUI());
			} else {
				if (Canvas.getCanvas().getUserControl().gameStatus.equalsIgnoreCase("verified")) {
					return new UIMainMenu(Canvas.getCanvas().getUIHandler().getCurrentUI());
				} else {
					return new UIBanned(Canvas.getCanvas().getUIHandler().getCurrentUI());
				}
			}
		} else if (mcGUI instanceof GuiMultiplayer) {
			return new UIMultiplayer(Canvas.getCanvas().getUIHandler().getCurrentUI());
		} else if (mcGUI instanceof GuiChat) {
			if (!(mcGUI instanceof GuiSleepMP)) {
				return new UIChat(Canvas.getCanvas().getUIHandler().getCurrentUI());
			} else {
				return new UISleep(Canvas.getCanvas().getUIHandler().getCurrentUI());
			}
		} else if (mcGUI instanceof GuiIngameMenu) {
			return new UIInGameMenu(Canvas.getCanvas().getUIHandler().getCurrentUI(), true);
		} else if (mcGUI instanceof GuiWorldSelection) {
			return new UISingleplayer(Canvas.getCanvas().getUIHandler().getCurrentUI() == null?new UIMainMenu(null): Canvas.getCanvas().getUIHandler().getCurrentUI());
		} else if (mcGUI instanceof GuiCreateWorld) {
			return new UICreateWorld(Canvas.getCanvas().getUIHandler().getCurrentUI());
		} else if (mcGUI instanceof GuiConnecting) {
			return new UIConnecting(Canvas.getCanvas().getUIHandler().getCurrentUI(), ((IMixinGuiConnecting)mcGUI).getIP()+":"+((IMixinGuiConnecting)mcGUI).getPort());
		}
		
		return null;
	}
	
	public static float[] getScaledImage(float wi, float hi, float ws, float hs) {
		float ri = wi/hi;
		
		float rs = ws/hs;
		
		float dw = 0;
		float dh = 0;
		
		if (rs <= ri) {
			dw = wi * hs/hi;
			dh = hs;
		} else {
			dw = ws;
			dh = hi * ws/wi;
		}
		
		float x = (ws-dw)/2;
		float y = (hs-dh)/2;
		
		return new float[] {x, y, dw, dh};
	}
	
	public static boolean shouldDrawBackground(UI ui) {
		return ui instanceof UIBanned ||
				ui instanceof UILoggedIn ||
				ui instanceof UILogin ||
				ui instanceof UIMainMenu ||
				ui instanceof UINewAccount ||
				ui instanceof UIVerifyAccount ||
				ui instanceof UIVerified ||
				ui instanceof UIWelcome;
	}
	
	public static boolean shouldDrawBranding(UI ui) {
		return ui instanceof UIBanned ||
				ui instanceof UILoggedIn ||
				ui instanceof UILogin ||
				ui instanceof UINewAccount ||
				ui instanceof UIVerifyAccount ||
				ui instanceof UIVerified ||
				ui instanceof UIWelcome;
	}
}
