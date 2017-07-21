package com.kiloclient.utilities;

import com.kiloclient.KiLO;
import com.kiloclient.mixin.imp.IMixinGuiConnecting;
import com.kiloclient.ui.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;

public class UIUtilities {

	public static UI newUI(GuiScreen mcGUI) {
		
		if (mcGUI instanceof GuiMainMenu) {
			if (KiLO.getKiLO().getUserControl() == null) {
				return new UIWelcome(KiLO.getKiLO().getUIHandler().getCurrentUI());
			}
			if (!KiLO.getKiLO().getUserControl().gameStatus.equalsIgnoreCase("verified") && !KiLO.getKiLO().getUserControl().gameStatus.equalsIgnoreCase("banned")) {
				return new UIWelcome(KiLO.getKiLO().getUIHandler().getCurrentUI());
			} else {
				if (KiLO.getKiLO().getUserControl().gameStatus.equalsIgnoreCase("verified")) {
					return new UIMainMenu(KiLO.getKiLO().getUIHandler().getCurrentUI());
				} else {
					return new UIBanned(KiLO.getKiLO().getUIHandler().getCurrentUI());
				}
			}
		} else if (mcGUI instanceof GuiMultiplayer) {
			return new UIMultiplayer(KiLO.getKiLO().getUIHandler().getCurrentUI());
		} else if (mcGUI instanceof GuiChat) {
			if (!(mcGUI instanceof GuiSleepMP)) {
				return new UIChat(KiLO.getKiLO().getUIHandler().getCurrentUI());
			} else {
				return new UISleep(KiLO.getKiLO().getUIHandler().getCurrentUI());
			}
		} else if (mcGUI instanceof GuiIngameMenu) {
			return new UIInGameMenu(KiLO.getKiLO().getUIHandler().getCurrentUI(), true);
		} else if (mcGUI instanceof GuiWorldSelection) {
			return new UISingleplayer(KiLO.getKiLO().getUIHandler().getCurrentUI() == null?new UIMainMenu(null):KiLO.getKiLO().getUIHandler().getCurrentUI());
		} else if (mcGUI instanceof GuiCreateWorld) {
			return new UICreateWorld(KiLO.getKiLO().getUIHandler().getCurrentUI());
		} else if (mcGUI instanceof GuiConnecting) {
			return new UIConnecting(KiLO.getKiLO().getUIHandler().getCurrentUI(), ((IMixinGuiConnecting)mcGUI).getIP()+":"+((IMixinGuiConnecting)mcGUI).getPort());
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
