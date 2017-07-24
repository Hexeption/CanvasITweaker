package com.canvasclient.ui.popup;

import java.util.ArrayList;
import java.util.List;

import com.canvasclient.render.FontHandler;
import net.minecraft.client.network.NetworkPlayerInfo;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.UI;
import com.canvasclient.ui.UIInGameMenu;
import com.canvasclient.ui.UIInGameMenuFriend;
import com.canvasclient.ui.UIInGameMenuPartyChat;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.TextBox;
import com.canvasclient.ui.interactable.TextBoxAlt;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.utilities.Timer;
import com.canvasclient.utilities.Utilities;

public class UIPopupMessageCreate extends UI {

	private float formOffset;
	private boolean invalid, deleting;
	
	private String invalidMessage = "";
	
	private Timer invalidTimer = new Timer();
	
	private boolean checking;

	private float fX, fY, fW, fH;
	private int acSelect;
	
	public UIPopupMessageCreate(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = "Create Message";

		formOffset = 0;
		invalid = false;
		checking = false;

		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2);
		fW = 550;
		fH = 176;

		interactables.clear();
		interactables.add(new IconButton(this, fX+(fW/2)-40, fY-(fH/2)+12, 24, 24, 0x00000000, ResourceHelper.iconClose[3]));
		interactables.add(new IconButton(this, fX+(fW/2)-16-48-16-24, fY+(fH/2)-52, 24, 24, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconSend[2]));
		interactables.add(new TextBoxAlt(this, "Enter ingame name...", fX-(fW/2)+16, fY-(fH/2)+64, fW-32, 32, FontHandler.ROUNDED_BOLD.get(20), -1, Align.LEFT, Align.CENTER));
		interactables.add(new TextBoxAlt(this, "Message...", fX-(fW/2)+16, fY+(fH/2)-56, fW-32-48-16-48, 32, FontHandler.ROUNDED_BOLD.get(20), -1, Align.LEFT, Align.CENTER));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		if (invalid) {
			formOffset+= ((-FontHandler.STANDARD.get(14).getHeight()*2)-formOffset)/5f;
			if (invalidTimer.isTime(2f)) {
				invalid = false;
			}
		} else {
			invalidTimer.reset();
		}
		if (!invalid) {
			formOffset+= (0-formOffset)/5f;
		}
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			if (parent instanceof UIInGameMenu)
				((UIInGameMenu)parent).changePopup(null);
			if (parent instanceof UIInGameMenuFriend)
				((UIInGameMenuFriend)parent).changePopup(null);
			if (parent instanceof UIInGameMenuPartyChat)
				if (parent instanceof UIInGameMenuPartyChat)
			break;
		case 1:
			submit();
			break;
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		super.mouseClick(mx, my, b);
	}
	
	public void mouseRelease(int mx, int my, int b) {
		super.mouseRelease(mx, my, b);
	}
	
	public void mouseScroll(int s) {
		super.mouseScroll(s);
	}
	
	public void keyboardPress(int key) {
		super.keyboardPress(key);
		switch (key) {
		case Keyboard.KEY_DOWN:
			acSelect++;
			break;
		case Keyboard.KEY_UP:
			acSelect--;
			break;
		case Keyboard.KEY_RETURN:
			if (acSelect != -1) {
				if (((TextBox)interactables.get(2)).getText().length() > 0 && (interactables.get(2).active)) {
					String ac = ((TextBox)interactables.get(2)).getText();
					List<String> acnames = new ArrayList<String>();
					for(Object o : mc.player.connection.getPlayerInfoMap()) {
						NetworkPlayerInfo npi = (NetworkPlayerInfo)o;
						String n = npi.getGameProfile().getName();
						if (n.toLowerCase().startsWith(ac.toLowerCase()) && !n.equalsIgnoreCase(ac)) {
							acnames.add(n);
						}
					}
					String name = acnames.get(acSelect);
					name = name.substring(((TextBox)interactables.get(2)).text.length(), name.length());
					
					((TextBox)interactables.get(2)).setText(((TextBox)interactables.get(2)).text+name);
					((TextBox)interactables.get(2)).cursorPos+= name.length();
					((TextBox)interactables.get(2)).startSelect = ((TextBox)interactables.get(2)).cursorPos;
					((TextBox)interactables.get(2)).endSelect = ((TextBox)interactables.get(2)).cursorPos;
				}
			} else {
				submit();
			}
			break;
		case Keyboard.KEY_ESCAPE:
			if (parent instanceof UIInGameMenu)
				((UIInGameMenu)parent).changePopup(null);
			if (parent instanceof UIInGameMenuFriend)
				((UIInGameMenuFriend)parent).changePopup(null);
			if (parent instanceof UIInGameMenuPartyChat)
				if (parent instanceof UIInGameMenuPartyChat)
			break;
		}
	}
	
	public void keyTyped(int key, char keyChar) {
		super.keyTyped(key, keyChar);
	}
	
	public void submit() {
		final String name = ((TextBox)interactables.get(2)).text;
		final String msg = ((TextBox)interactables.get(3)).text;
		
		if (name != null && name.length() > 0 && msg != null && msg.length() > 0) {
			checking = true;
			new Thread() {
				@Override
				public void run() {
					if (APIHelper.sendMessage(Canvas.getCanvas().getUserControl().clientID, name, msg)) {
						((UIInGameMenu)parent).changePopup(null);
					} else {
						checking = false;
						invalidMessage = "There was a problem sending this message";
						invalid = true;
					}
				}
			}.start();
		} else if (name != null && name.length() > 0){
			invalidMessage = "Please enter a message";
			invalid = true;
		} else {
			invalidMessage = "Please enter an ingame name";
			invalid = true;
		}
		if (formOffset < (-FontHandler.STANDARD.get(14).getHeight()*1.5f)) {
			invalid = false;
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0.7f*opacity));
		
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2)+48, fX+(fW/2), fY+(fH/2), Utilities.reAlpha(0xFF202020, 1f*opacity));
		
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2), fX+(fW/2), fY-(fH/2)+48, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(25), fX, fY-(fH/2)+24, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		if (checking) {
			GuiHelper.drawLoaderAnimation(fX+(fW/2)-16-24, fY+(fH/2)-16-24, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		} else {
			if (Canvas.getCanvas().getUserControl() != null && Canvas.getCanvas().getUserControl().canvasHead != null) {
				GuiHelper.drawTexturedRectangle(fX+(fW/2)-16-48, fY+(fH/2)-16-48, 48, 48, Canvas.getCanvas().getUserControl().canvasHead.getTexture(), Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
		}
		
		super.render(opacity);
		
		if (((TextBox)interactables.get(2)).getText().length() > 0 && (interactables.get(2).active)) {
			String ac = ((TextBox)interactables.get(2)).getText();
			List<String> acnames = new ArrayList<String>();
			for(Object o : mc.player.connection.getPlayerInfoMap()) {
				NetworkPlayerInfo npi = (NetworkPlayerInfo)o;
				String n = npi.getGameProfile().getName();
				if (n.toLowerCase().startsWith(ac.toLowerCase()) && !n.equalsIgnoreCase(ac)) {
					acnames.add(n);
				}
			}
			
			if (!acnames.isEmpty()) {
				acSelect = Math.min(Math.max(0, acSelect), acnames.size()-1);
				
				float xx = ((TextBox)interactables.get(2)).x;
				float yy = ((TextBox)interactables.get(2)).y;
				float ww = ((TextBox)interactables.get(2)).width;
				float hh = ((TextBox)interactables.get(2)).height;
				
				GuiHelper.drawRectangle(xx-4, yy+hh, xx+ww+4, yy+hh+((acnames.size())* FontHandler.ROUNDED_BOLD.get(14).getHeight()), Utilities.reAlpha(ColorHelper.BLACK.getColorCode(), 0.8f*opacity));
				for(int i = 0; i < acnames.size(); i++) {
					GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), xx+2, yy+hh+((i)* FontHandler.ROUNDED_BOLD.get(14).getHeight()), acnames.get(i), Utilities.reAlpha(acSelect == i? Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground():ColorHelper.WHITE.getColorCode(), 1f*opacity));
				}
			} else {
				acSelect = -1;
			}
		} else {
			acSelect = -1;
		}
		
		//Message
		GuiHelper.startClip(fX-(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()/2), fX+(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()*1.5f));
		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(14), fX, fY+(fH/2)-(FontHandler.STANDARD.get(14).getHeight(invalidMessage))-formOffset, invalidMessage, Utilities.reAlpha(0xFFFF5555, 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.endClip();
	}
}
