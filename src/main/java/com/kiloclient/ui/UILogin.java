package com.kiloclient.ui;

import com.kiloclient.render.FontHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.Button;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.Link;
import com.kiloclient.ui.interactable.PasswordBox;
import com.kiloclient.ui.interactable.TextBox;
import com.kiloclient.users.UserControl;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.ChatUtilities;
import com.kiloclient.resource.IOHelper;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;

public class UILogin extends UI {

	private float formOffset;
	private boolean invalid, checking;
	
	private String invalidMessage = "";
	
	private Timer invalidTimer = new Timer();
	
	private float fX, fY, fW, fH, checkRotate, checkOpacity;
	private final float rotateSpeed = 10f;
	
	public UILogin(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = "Login";
		
		formOffset = 0;
		invalid = false;
		checking = false;
		checkRotate = 0;
		
		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2)+40;
		fW = 380;
		fH = 200;
		
		interactables.clear();
		interactables.add(new TextBox(this, "Username...", (Display.getWidth()/2)-(fW/2)+30, (Display.getHeight()/2)+40-(fH/2)+15, 380-60, 50, FontHandler.STANDARD.get(25), ColorHelper.DARK_GREY.getColorCode(), Align.LEFT, Align.CENTER));
		interactables.add(new PasswordBox(this, "Password...", (Display.getWidth()/2)-(fW/2)+30, (Display.getHeight()/2)+40-(fH/2)+65, 380-60, 50, FontHandler.STANDARD.get(25), ColorHelper.DARK_GREY.getColorCode(), Align.LEFT, Align.CENTER));
		interactables.add(new Button(this, "LOGIN", (Display.getWidth()/2)+20, (Display.getHeight()/2)+(fH/2)-20, (fW/2)-40, 40, FontHandler.ROUNDED_BOLD.get(20), 0xFF3baeda, ResourceHelper.iconSubmit[2], 24));
		interactables.add(new Link(this, "Create Account", (Display.getWidth()/2)-(fW/2)+30, (Display.getHeight()/2)+(fH/2)+8, FontHandler.ROUNDED_BOLD.get(14), 0xFF1DB8C2, Align.LEFT, Align.BOTTOM));
		
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
		
		checkRotate+= rotateSpeed;
		checkOpacity+= (checking?0.2:-0.2);
		checkOpacity = Math.min(Math.max(0f, checkOpacity), 1f);
		
		if (!invalid) {
			formOffset+= (0-formOffset)/5f;
		}
		
		((Button)interactables.get(2)).text = checking?(String)null:"LOGIN";
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch(interactables.indexOf(i)) {
		case 2:
			submit();
			break;
		case 3:
			KiLO.getKiLO().getUIHandler().changeUI(new UICreatePassport(this));
			break;
		}
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
					String username = ((TextBox)interactables.get(0)).getText();
					String password = ((TextBox)interactables.get(1)).getText();
					String[] login = APIHelper.login(username, password);
					if (login != null) {
						try {
							long a = Long.parseLong(login[0]);
							String[] cd = new String[8];
							cd = APIHelper.getClientDetails(login[0]);
		
							KiLO.getKiLO().setUserControl(new UserControl(cd[0], cd[1], cd[2], cd[3], cd[4], cd[5], cd[6], cd[7], cd[8], cd[9], cd[10], Boolean.parseBoolean(cd[11])));

							IOHelper.saveClientConfiguration();
							
							if (!KiLO.getKiLO().getUserControl().gameStatus.equalsIgnoreCase("verified") && !KiLO.getKiLO().getUserControl().gameStatus.equalsIgnoreCase("banned")) {
								KiLO.getKiLO().getUIHandler().changeUI(new UIVerifyAccount(null));
							} else {
								if (KiLO.getKiLO().getUserControl().gameStatus.equalsIgnoreCase("verified")) {
									System.out.println("Logged in!");
									KiLO.getKiLO().getUIHandler().changeUI(new UILoggedIn(null));
								} else {
									KiLO.getKiLO().getUIHandler().changeUI(new UIBanned(null));
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							invalidMessage = login[1];
							invalid = true;
							checking = false;
						}
					} else {
						invalidMessage = "There was a problem connecting to the database";
						invalid = true;
						checking = false;
					}
				}
			}.start();
		} else {
			if (!invalid) {
				invalidMessage = validate();
				invalid = true;
				checking = false;
			}
		}
		if (formOffset < (-FontHandler.STANDARD.get(14).getHeight()*1.5f)) {
			invalid = false;
		}
	}
	
	private String validate() {
		boolean username = ((TextBox)interactables.get(0)).getText().length() > 0;
		boolean usernameValid = ChatUtilities.isValidUsername(((TextBox)interactables.get(0)).getText());
		boolean password = ((TextBox)interactables.get(1)).getText().length() > 0;
		if (username) {
			if (usernameValid) {
				if (password) {
					return "";
				} else {
					return "Please enter a password";
				}
			} else {
				return "Please enter a valid username (a-z, 0-9 and _)";
			}
		} else {
			return "Please enter a username";
		}
	}
	
	public void render(float opacity) {
		//Dark gray background
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), 0xFF252525);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(40), Display.getWidth()/2, Display.getHeight()/2-(fH/2)-10, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2), fX+(fW/2), fY+(fH/2), Utilities.reAlpha(0xFFf2f2f2, 1f*opacity));

		GuiHelper.startClip((Display.getWidth()/2)-(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()/2), (Display.getWidth()/2)+(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()*1.5f));
		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(14), Display.getWidth()/2, fY+(fH/2)-(FontHandler.STANDARD.get(14).getHeight(invalidMessage))-formOffset, invalidMessage, Utilities.reAlpha(0xFFFF5555, 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.endClip();
		float width = Display.getWidth();
		float height = Display.getHeight();
		
		Texture welcomeLogo = ResourceHelper.brandingWelcome;
		
		float w = 78.5F;
		float h = 39F;
		int currentPointColor = 0xFF1DB8C2;
		int pointColor = 0xFF414141;
		
		GuiHelper.drawPoint((width / 16) * 15+27, (height / 16) * 15.2F-4, 14, pointColor);
		GuiHelper.drawPoint((width / 16) * 14.625F+27, (height / 16) * 15.2F-4, 14, pointColor);
		GuiHelper.drawPoint((width / 16) * 14.25F+27, (height / 16) * 15.2F-4, 14, currentPointColor);
		GuiHelper.drawPoint((width / 16) * 13.885F+27, (height / 16) * 15.2F-4, 14, pointColor);
		GuiHelper.drawTexturedRectangle(width / 26-4, (height / 16) * 14.75F-4, w, h, welcomeLogo, Utilities.reAlpha(0xffffff, 1F * opacity));
		//Draw.drawLoaderAnimation(fX-(fW/2)+32, fY+(fH/2)-32, 8, Util.reAlpha(Colors.WHITE.getColorCode(), checkOpacity*opacity));
		
		super.render(opacity);
	}
}
