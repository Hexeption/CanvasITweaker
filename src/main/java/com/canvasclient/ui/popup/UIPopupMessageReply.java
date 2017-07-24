package com.canvasclient.ui.popup;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.render.utilities.TextureImage;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.UI;
import com.canvasclient.ui.UIInGameMenu;
import com.canvasclient.ui.UIInGameMenuFriend;
import com.canvasclient.ui.UIInGameMenuPartyChat;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.TextBox;
import com.canvasclient.ui.interactable.TextBoxAlt;
import com.canvasclient.utilities.Timer;
import com.canvasclient.utilities.Utilities;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIPopupMessageReply extends UI {

	private float formOffset;
	private boolean invalid, deleting;
	
	private String invalidMessage = "";
	
	private Timer invalidTimer = new Timer();
	
	private boolean checking;

	private float fX, fY, fW, fH;
	
	private String minecraftName, message;
	private TextureImage head;
	private List<String> lines;

	public UIPopupMessageReply(UI parent, String minecraftName, String message) {
		super(parent);
		this.minecraftName = minecraftName;
		this.message = message;
		
		head = ResourceHelper.downloadTexture(String.format(APIHelper.PLAYER_HEAD, minecraftName, "128"));

		lines = new CopyOnWriteArrayList<String>();
		if (message == null) {
			return;
		}
		int i = 0;
		int j = 0;
		for(i = 0; i <= message.length(); i++) {
			String part = "";
			part = message.substring(j, i);
			
			if (FontHandler.ROUNDED_BOLD.get(20).getWidth(part) > 384) {
				int b = -1;
				for(int a = i-1; a > j; a--) {
					if (message.charAt(a) == ' ') {
						b = a+1;
						break;
					}
				}
				if (b >= j) {
					i = b;
				}
				lines.add(message.substring(j, i));
				j = i;
			}
		}
		if (i >= message.length()) {
			lines.add(message.substring(j, message.length()));
		}
	}

	@Override
	public void init() {
		title = "Reply to "+minecraftName;

		formOffset = 0;
		invalid = false;
		checking = false;
		
		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2);
		fW = 550;
		fH = 200;

		interactables.clear();
		interactables.add(new IconButton(this, fX+(fW/2)-40, fY-(fH/2)+12, 24, 24, 0x00000000, ResourceHelper.iconClose[3]));
		interactables.add(new IconButton(this, fX+(fW/2)-16-48-16-24, fY+(fH/2)-52, 24, 24, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconSend[2]));
		interactables.add(new TextBoxAlt(this, "Message...", fX-(fW/2)+16, fY+(fH/2)-56, fW-32-48-16-48, 32, FontHandler.ROUNDED_BOLD.get(20), -1, Align.LEFT, Align.CENTER));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		fH = 160+Math.max(((lines.size())* FontHandler.ROUNDED_BOLD.get(20).getHeight()), 0);
		
		interactables.get(0).y = fY-(fH/2)+12;
		interactables.get(1).y = fY+(fH/2)-52;
		interactables.get(2).y = fY+(fH/2)-56;
		
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
		case Keyboard.KEY_RETURN:
			submit();
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
		final String msg = ((TextBox)interactables.get(2)).text;
		
		if (msg != null && msg.length() > 0) {
			checking = true;
			new Thread() {
				@Override
				public void run() {
					if (APIHelper.sendMessage(Canvas.getCanvas().getUserControl().clientID, minecraftName, msg)) {
						((UIInGameMenu)parent).changePopup(null);
					} else {
						checking = false;
						invalidMessage = "There was a problem replying to this message";
						invalid = true;
					}
				}
			}.start();
		} else {
			invalidMessage = "Please enter a message";
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

		if (head != null && head.getTexture() != null) {
			GuiHelper.drawTexturedRectangle(fX-(fW/2)+16, fY-(fH/2)+48+16, 48, 48, head.getTexture(), Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		}
		
		float maxWidth = 400f;
		float maxHeight = 32+Math.max(((lines.size()-1)* FontHandler.ROUNDED_BOLD.get(20).getHeight()), 0);
		float rad = (maxHeight/2)-1;
		GuiHelper.drawArcEllipse(fX-(fW/2)+104, fY-(fH/2)+72+(maxHeight/2), 90, 270, 15, rad, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
		GuiHelper.drawArcEllipse(fX-(fW/2)+104+maxWidth, fY-(fH/2)+72+(maxHeight/2), -90, 90, 15, rad, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
		GuiHelper.drawRectangle(fX-(fW/2)+104, fY-(fH/2)+72, fX-(fW/2)+104+maxWidth, fY-(fH/2)+72+maxHeight, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1f*opacity));
		for(String s : lines) {
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+112, fY-(fH/2)+88+(lines.indexOf(s)* FontHandler.ROUNDED_BOLD.get(20).getHeight()), s, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.CENTER);
		}

		if (checking) {
			GuiHelper.drawLoaderAnimation(fX+(fW/2)-16-24,  fY+(fH/2)-16-24, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		} else {
			if (Canvas.getCanvas().getUserControl() != null && Canvas.getCanvas().getUserControl().canvasHead != null) {
				GuiHelper.drawTexturedRectangle(fX+(fW/2)-16-48, fY+(fH/2)-16-48, 48, 48, Canvas.getCanvas().getUserControl().canvasHead.getTexture(), Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
		}
		
		super.render(opacity);
		
		//Message
		GuiHelper.startClip(fX-(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()/2), fX+(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()*1.5f));
		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(14), fX, fY+(fH/2)-(FontHandler.STANDARD.get(14).getHeight(invalidMessage))-formOffset, invalidMessage, Utilities.reAlpha(0xFFFF5555, 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.endClip();
	}
}
