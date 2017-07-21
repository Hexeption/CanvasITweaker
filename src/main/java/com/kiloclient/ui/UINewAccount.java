package com.kiloclient.ui;

import com.kiloclient.render.FontHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.Button;
import com.kiloclient.ui.interactable.CheckBox;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.Link;
import com.kiloclient.ui.interactable.PasswordBoxAlt;
import com.kiloclient.ui.interactable.TextBox;
import com.kiloclient.users.UserControl;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.ChatUtilities;
import com.kiloclient.resource.IOHelper;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;

public class UINewAccount extends UI {

	private float formOffset;
	private boolean invalid, checking;
	
	private String invalidMessage = "";
	
	private Timer invalidTimer = new Timer();
	
	private float fX, fY, fW, fH;
	
	public UINewAccount(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = "Create a KiLO Account";
		
		formOffset = 0;
		invalid = false;
		
		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2);
		fW = Display.getWidth() >= 1280?1260:840;
		fH = 550;
		
		interactables.clear();
		//interactables.add(new TextBoxAlt(this, "Enter In-Game Name...", fX-(fW/2)+110, fY-(fH/2)+55, 160, 30, FontHandler.ROUNDED_BOLD.get(12), -1, Align.LEFT, Align.CENTER));
		//interactables.add(new TextBoxAlt(this, "Enter First Name...", fX-(fW/2)+210, fY-(fH/2)+165, 160, 30, FontHandler.ROUNDED_BOLD.get(12), -1, Align.LEFT, Align.CENTER));
		//interactables.add(new TextBoxAlt(this, "Enter Email Address...", fX-(fW/2)+600, fY-(fH/2)+165, 160, 30, FontHandler.ROUNDED_BOLD.get(12), -1, Align.LEFT, Align.CENTER));
		interactables.add(new PasswordBoxAlt(this, "Enter Password...", fX-(fW/2)+210, fY-(fH/2)+210, 160, 30, FontHandler.ROUNDED_BOLD.get(12), -1, Align.LEFT, Align.CENTER));
		interactables.add(new PasswordBoxAlt(this, "Enter Password Again...", fX-(fW/2)+600, fY-(fH/2)+210, 160, 30, FontHandler.ROUNDED_BOLD.get(12), -1, Align.LEFT, Align.CENTER));
		interactables.add(new Button(this, "Create", fX-(fW/2)+664, fY+(fH/2)-60, 150, 40, FontHandler.STANDARD.get(20), ColorHelper.GREEN.getColorCode(), ResourceHelper.iconSubmit[2], 24));
		interactables.add(new Link(this, "Log in", 20+ FontHandler.ROUNDED_BOLD.get(14).getWidth("Already have an account? "), Display.getHeight()-21, FontHandler.ROUNDED_BOLD.get(14), ColorHelper.BLUE.getColorCode(), Align.LEFT, Align.BOTTOM));
		interactables.add(new Link(this, APIHelper.ACCOUNT_ABOUT, fX-(fW/2)+860+ FontHandler.ROUNDED_BOLD.get(16).getWidth("Learn more at "), fY-(fH/2)+59, FontHandler.ROUNDED_BOLD.get(16), ColorHelper.WHITE.getColorCode(), Align.LEFT, Align.TOP));
		interactables.add(new Link(this, APIHelper.ACCOUNT, fX-(fW/2)+60+ FontHandler.ROUNDED_BOLD.get(12).getWidth("This is what you will use to login to "), fY-(fH/2)+260, FontHandler.ROUNDED_BOLD.get(12), ColorHelper.DARK_BLUE.getColorCode(), Align.LEFT, Align.TOP));
		interactables.add(new CheckBox(this, "I agree to the KiLO Terms and Conditions, as well as the Privacy Policy.", fX-(fW/2)+70, fY-(fH/2)+370, FontHandler.ROUNDED_BOLD.get(14), 0xFF999999));
		interactables.add(new CheckBox(this, "I wish to receive information about KiLO to my email address.", fX-(fW/2)+70, fY-(fH/2)+430, FontHandler.ROUNDED_BOLD.get(14), 0xFF999999));
		interactables.add(new Link(this, "Terms and Conditions", fX-(fW/2)+664-16, fY+(fH/2)-40, FontHandler.ROUNDED_BOLD.get(12), 0xFF666666, Align.RIGHT, Align.CENTER));
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
		
		((Button)interactables.get(5)).text = checking?(String)null:"Create";
		((Link)interactables.get(7)).enabled = Display.getWidth() >= 1280;
		((Link)interactables.get(7)).shown = Display.getWidth() >= 1280;
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch (i.type) {
		case CHECK_BOX:
			i.active = !i.active;
			break;
		default:
			switch(interactables.indexOf(i)) {
			case 5:
				submit();
				break;
			case 6:
				KiLO.getKiLO().getUIHandler().changeUI(parent);
				break;
			case 7:
				Utilities.openWeb(APIHelper.ACCOUNT_ABOUT);
				break;
			case 8:
				Utilities.openWeb(APIHelper.ACCOUNT);
				break;
			case 11:
				Utilities.openWeb(APIHelper.TERMS_OF_SERVICE);
				break;
			}
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
					String primaryAccount= ((TextBox)interactables.get(0)).getText();
					String name = ((TextBox)interactables.get(1)).getText();
					String email = ((TextBox)interactables.get(2)).getText();
					String password = ((TextBox)interactables.get(3)).getText();
					String passwordConfirm = ((TextBox)interactables.get(4)).getText();
					boolean tac = ((CheckBox)interactables.get(9)).active;
					boolean info = ((CheckBox)interactables.get(10)).active;
					
					String[] connect = APIHelper.createAccount(primaryAccount, name, email, password, info);
					if (connect != null) {
						try {
							long a = Long.parseLong(connect[0]);
							String[] cd = APIHelper.getClientDetails(connect[0]);
		
							KiLO.getKiLO().setUserControl(new UserControl(cd[0], cd[1], cd[2], cd[3], cd[4], cd[5], cd[6], cd[7], cd[8], cd[9], cd[10], Boolean.parseBoolean(cd[11])));
		
							IOHelper.saveClientConfiguration();
		
							KiLO.getKiLO().getUIHandler().changeUI(new UIVerifyAccount(null));
						} catch (Exception e) {
							invalidMessage = connect[1];
							checking = false;
							invalid = true;
						}
					} else {
						invalidMessage = "There was a problem connecting to the database";
						checking = false;
						invalid = true;
					}
				}
			}.start();
		} else {
			if (invalid == false) {
				invalidMessage = validate();
				checking = false;
				invalid = true;
			}
		}
		if (formOffset < (-FontHandler.STANDARD.get(14).getHeight()*1.5f)) {
			invalid = false;
		}
	}
	
	private String validate() {
		String[] input = new String[] {((TextBox)interactables.get(0)).getText(), ((TextBox)interactables.get(1)).getText(), ((TextBox)interactables.get(2)).getText(), ((TextBox)interactables.get(3)).getText(), ((TextBox)interactables.get(4)).getText()};
		boolean[] hasText = new boolean[] {input[0].length() > 0, input[1].length() > 0, input[2].length() > 0, input[3].length() > 0, input[4].length() > 0};
		boolean[] isLength = new boolean[] {input[0].length() >= 1, input[1].length() >= 2, input[2].length() >= 0, input[3].length() >= 4, input[4].length() >= 4};
		boolean[] isValid = new boolean[] {true, ChatUtilities.isValidUsername(input[1]), ChatUtilities.isValidEmail(input[2]), input[3].equalsIgnoreCase(input[4]), input[4].equalsIgnoreCase(input[3])};
		String [] hasTextError = new String[] {"Please enter a primary in-game name", "Please enter a nickname", "Please enter an email address", "Please enter a passwords", "Passwords don't match"};
		String [] isLengthError = new String[] {"Please use a longer primary in-game name", "Please use a longer nickname", "Please use a longer email address", "Please use a longer password", "Please use a longer password"};
		String [] isValidError = new String[] {"That is not a valid primary in-game name", "That is not a valid nickname", "That is not a valid email address", "Passwords don't match", "Passwords don't match"};
		
		String message = "";
		if (!((CheckBox)interactables.get(9)).active) {
			message = "Please accept the Terms and Conditions";
			return message;
		}
		for(int i = input.length-1; i >= 0; i--) {
			if (hasText[i]) {
				if (isLength[i]) {
					if (isValid[i]) {
					} else {
						message = isValidError[i];
					}
				} else {
					message = isLengthError[i];
				}
			} else {
				message = hasTextError[i];
			}
		}
		return message;
	}
	
	public void render(float opacity) {
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2, fY-(fH/2)-(FontHandler.ROUNDED_BOLD.get(40).getHeight(title)/2)-10, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		//Form boxing/sectioning
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2), fX+(fW/2), fY+(fH/2), Utilities.reAlpha(0xFF000000, 0.5f*opacity));
		if (Display.getWidth() >= 1280) {
			GuiHelper.drawRectangle(fX-(fW/2)+839, fY-(fH/2)+20, fX-(fW/2)+841, fY+(fH/2)-20, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 0.3f*opacity));
		}
		
		//Numbering
		GuiHelper.drawCircle(fX-(fW/2)+56, fY-(fH/2)+56, 26, Utilities.reAlpha(ColorHelper.DARK_BLUE.getColorCode(), 1f*opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+55, fY-(fH/2)+56, "1", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+100, fY-(fH/2)+46, "Your Primary In-Game Name", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.LEFT, Align.CENTER);

		GuiHelper.drawCircle(fX-(fW/2)+56, fY-(fH/2)+136, 26, Utilities.reAlpha(ColorHelper.DARK_BLUE.getColorCode(), 1f*opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+56, fY-(fH/2)+136, "2", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+100, fY-(fH/2)+124, "Your Details", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));

		GuiHelper.drawCircle(fX-(fW/2)+56, fY-(fH/2)+320, 26, Utilities.reAlpha(ColorHelper.DARK_BLUE.getColorCode(), 1f*opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+56, fY-(fH/2)+320, "3", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+100, fY-(fH/2)+308, "Terms and Conditions", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));

		//Details info
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), fX-(fW/2)+195, fY-(fH/2)+172, "Nickname (First Name):",Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.RIGHT, Align.TOP);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), fX-(fW/2)+585, fY-(fH/2)+172, "Email Address:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.RIGHT, Align.TOP);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), fX-(fW/2)+195, fY-(fH/2)+217, "Password:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.RIGHT, Align.TOP);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), fX-(fW/2)+585, fY-(fH/2)+217, "Confirm Password:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.RIGHT, Align.TOP);
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), fX-(fW/2)+60, fY-(fH/2)+260, "This is what you will use to login to", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity));
		
		//TAC
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fX-(fW/2)+108, fY-(fH/2)+390, "I'm also aware these can change from time to time.", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity));

		//Data prot
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fX-(fW/2)+26, fY+(fH/2)-20- FontHandler.ROUNDED_BOLD.get(14).getHeight(), "We take data protection seriously.", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity), Align.LEFT, Align.BOTTOM);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fX-(fW/2)+26, fY+(fH/2)-20, "No information is shared unless you authorise it yourself.", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity), Align.LEFT, Align.BOTTOM);

		//Go to login page
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), 20, Display.getHeight()-20- FontHandler.ROUNDED_BOLD.get(14).getHeight(), "Already have an account?", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
		
		//Why need account
		if (Display.getWidth() >= 1280) {
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+860, fY-(fH/2)+21, "Why do I need a KiLO Account?", Utilities.reAlpha(ColorHelper.AQUA.getColorCode(), 1f*opacity));
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(16), fX-(fW/2)+860, fY-(fH/2)+60, "Learn more at", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity));
		}
		
		//Wizard pic
		if (Display.getWidth() >= 1280) {
			float xoff = ((fW-860)/2)-(231/2);
			float yoff = 100f;
			GuiHelper.drawTexturedRectangle(fX-(fW/2)+860+xoff, fY-(fH/2)+80+yoff, 231, 256, ResourceHelper.wizard, Utilities.reAlpha(0x00FFFFFF, opacity));
		}

		//Message
		GuiHelper.startClip((Display.getWidth()/2)-(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()/2), (Display.getWidth()/2)+(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()*1.5f));
		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(14), Display.getWidth()/2, fY+(fH/2)-(FontHandler.STANDARD.get(14).getHeight(invalidMessage))-formOffset, invalidMessage, Utilities.reAlpha(0xFFFF5555, 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.endClip();

		super.render(opacity);
	}
}
