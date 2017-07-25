package com.canvasclient.ui;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.ui.interactable.Button;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.TextBox;
import com.canvasclient.utilities.ChatUtilities;
import com.canvasclient.utilities.Timer;
import com.canvasclient.utilities.Utilities;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class UIAddServer extends UI {

	private float formOffset;
	private boolean invalid, checking;
	
	private String invalidMessage = "";
	
	private Timer invalidTimer = new Timer();
	
	private float fX, fY, fW, fH;
	
	public UIAddServer(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = "Add Server";

		formOffset = 0;
		invalid = false;

		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2)+44;
		fW = Display.getWidth()-40;
		fH = Display.getHeight()-124;

		interactables.clear();
		int i = 0;
		interactables.add(new IconButton(this, 32+(64*(i++)), 32, 32, 32, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconReturn[3]));
		interactables.add(new Button(this, "Add Server", fX+(fW/2)-8-128, fY+(fH/2)-48+8, 128, 32, FontHandler.STANDARD.get(12), ColorHelper.GREEN.getColorCode(), ResourceHelper.iconAdd[1], 16));
		interactables.add(new TextBox(this, "Example: play.hivemc.com", fX-250-20, fY, 400, 40, FontHandler.ROUNDED_BOLD.get(25), ColorHelper.GREY.getColorCode(), Align.LEFT, Align.CENTER));
		interactables.add(new TextBox(this, "Port", fX-250-20+400+40, fY, 100, 40, FontHandler.ROUNDED_BOLD.get(25), ColorHelper.WHITE.getColorCode(), Align.LEFT, Align.CENTER));
		
		((TextBox)interactables.get(3)).setText("25565");
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
		
		((Button)interactables.get(1)).enabled = ((TextBox)interactables.get(2)).text.length() > 0 && ((TextBox)interactables.get(3)).text.length() > 0;
		((Button)interactables.get(1)).text = checking?(String)null:"Add Server";
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			Canvas.getCanvas().getUIHandler().changeUI(parent);
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
	
	public void keyboardPress(int key) {
		super.keyboardPress(key);
		switch (key) {
		case Keyboard.KEY_RETURN:
			submit();
			break;
		}
	}
	
	private void submit() {
		if (!invalid && validate() == "" && !checking) {
			checking = true;
			new Thread() {
				@Override
				public void run() {
					String ip= ((TextBox)interactables.get(2)).getText();
					String port = ((TextBox)interactables.get(3)).getText();
					
					String connect = APIHelper.addServer(ip, port);
					if (connect != null) {
						Canvas.getCanvas().getUIHandler().changeUI(parent);
					} else {
						invalidMessage = "Failed to add server to database";
						invalid = true;
					}
				}
			}.start();
		} else {
			if (invalid == false) {
				invalidMessage = validate();
				invalid = true;
			}
		}
		if (formOffset < (-FontHandler.STANDARD.get(14).getHeight()*1.5f)) {
			invalid = false;
		}
	}
	
	private String validate() {
		String[] input = new String[] {((TextBox)interactables.get(2)).getText(), ((TextBox)interactables.get(3)).getText()};
		boolean[] hasText = new boolean[] {input[0].length() > 0, input[1].length() > 0};
		boolean[] isValid = new boolean[] {!input[0].contains(" "), ChatUtilities.isNumber(input[1])};
		String [] hasTextError = new String[] {"Please enter a server IP", "Please enter a server port (e.g. 25565)"};
		String [] isValidError = new String[] {"That is not a valid server IP", "The server port can only be a number"};
		
		String message = "";
		for(int i = input.length-1; i >= 0; i--) {
			if (hasText[i]) {
				if (isValid[i]) {
				} else {
					message = isValidError[i];
				}
			} else {
				message = hasTextError[i];
			}
		}
		return message;
	}
	
	public void render(float opacity) {
		drawDarkerBackground(false, opacity);

		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2, fY-(fH/2)-(FontHandler.ROUNDED_BOLD.get(40).getHeight(title)/2)-32, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2), fX+(fW/2), fY+(fH/2)-48, Utilities.reAlpha(0xFF111111, 0.75f*opacity));
		
		GuiHelper.drawRectangle(fX-(fW/2), fY+(fH/2)-48, fX+(fW/2), fY+(fH/2), Utilities.reAlpha(0xFF0A0A0A, 0.75f*opacity));

		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), fX, fY-80, "Enter Server IP:", Utilities.reAlpha(ColorHelper.DARK_BLUE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(25), fX-250-20+400+20, fY, ":", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.TOP);
		GuiHelper.drawRectangle(fX-250-30, fY+(FontHandler.ROUNDED_BOLD.get(25).getHeight())+15, fX+250+30, fY+(FontHandler.ROUNDED_BOLD.get(25).getHeight())+20, Utilities.reAlpha(ColorHelper.DARK_BLUE.getColorCode(), 1f*opacity));
		
		//Message
		GuiHelper.startClip(fX-(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)-40+(FontHandler.STANDARD.get(14).getHeight()/2), fX+(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)-40+(FontHandler.STANDARD.get(14).getHeight()*1.5f));
		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(14), fX, fY+(fH/2)-40-(FontHandler.STANDARD.get(14).getHeight(invalidMessage))-formOffset, invalidMessage, Utilities.reAlpha(0xFFFF5555, 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.endClip();

		super.render(opacity);
	}
}
