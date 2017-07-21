package com.kiloclient.ui;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.TextureImage;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.Link;
import com.kiloclient.ui.interactable.PasswordBoxAlt;
import com.kiloclient.ui.interactable.TextBoxAlt;
import com.kiloclient.users.UserControl;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Utilities;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

public class UICreatePassport extends UI {
	
	private TextureImage kiloHead;
	
	private String message = "";
	
	boolean verified;
	public UICreatePassport(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		float width = Display.getWidth();
		float height = Display.getHeight();
		title = "Create a KiLO Passport";
		this.kiloHead = ResourceHelper.downloadTexture(String.format(APIHelper.PLAYER_HEAD, Minecraft.getMinecraft().getSession().getUsername(), "512"));
		interactables.clear();
		int i = 0;
		interactables.add(new Link(this, "Login", (Display.getWidth() / 10) * 8+168, (Display.getHeight() / 18) / 1.1F+32, FontHandler.ROUNDED_LIGHT.get(16), 0xFFFFFFFF, Align.LEFT, Align.TOP));
		interactables.add(new TextBoxAlt(this, "John", width / 2.1F + FontHandler.ROUNDED_LIGHT.get(25).getWidth("First Name:  "), height / 2.4F, 250, FontHandler.ROUNDED_LIGHT.get(25).getHeight(), FontHandler.ROUNDED_BOLD.get(25), 0xFF252525, 0xFFD7D7D7, Align.LEFT, Align.CENTER, false));
		interactables.add(new TextBoxAlt(this, "john.doe@domain.com", width / 2.1F + FontHandler.ROUNDED_LIGHT.get(25).getWidth("Email Address:  "), height / 2.0F, 340, FontHandler.ROUNDED_LIGHT.get(25).getHeight(), FontHandler.ROUNDED_BOLD.get(25), 0xFF252525, 0xFFD7D7D7, Align.LEFT, Align.CENTER, false));
		String password = "password";
		password = password.replaceAll("(?s).", "\u00b0");
		interactables.add(new PasswordBoxAlt(this, password, width / 2.1F + FontHandler.ROUNDED_LIGHT.get(25).getWidth("MyKiLO Password:  "), height / 1.7F, 290, FontHandler.ROUNDED_LIGHT.get(25).getHeight(), FontHandler.ROUNDED_BOLD.get(25), 0xFF252525, 0xFFD7D7D7, Align.LEFT, Align.CENTER, false));
		interactables.add(new PasswordBoxAlt(this, password, width / 2.1F + FontHandler.ROUNDED_LIGHT.get(25).getWidth("Re-enter Password:  "), height / 1.5F, 270, FontHandler.ROUNDED_LIGHT.get(25).getHeight(), FontHandler.ROUNDED_BOLD.get(25), 0xFF252525, 0xFFD7D7D7, Align.LEFT, Align.CENTER, false));
		interactables.add(new Link(this, "CONTINUE",  (width / 1.25F), (height / 1.29F), FontHandler.ROUNDED_BOLD.get(30), 0xFF464646, Align.LEFT, Align.TOP));
	}
	
	
	public void render(float opacity) {
		
		GuiHelper.drawRectangle(0, 0, Display.getWidth(), Display.getHeight(), 0xFF252525);
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth() / 2, (Display.getHeight() / 9), title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		if (!(Minecraft.getMinecraft().getSession().getToken() == "0"))
			verified = true;

		Texture welcomeLogo = ResourceHelper.brandingWelcome;
		
		float w = 78.5F;
		float h = 39F;
		
		float width = Display.getWidth();
		float height = Display.getHeight();
		GuiHelper.drawTexturedRectangle(width / 26-4, (height / 16) * 14.75F-4, w, h, welcomeLogo, Utilities.reAlpha(0xffffff, 1F * opacity));
		
		GuiHelper.drawRectangle(width / 30, height / 5, width / 1.03F, (height / 10) * 8.9F, 0xFFf2f2f2);

		Texture passport = ResourceHelper.passport;
		
		GuiHelper.drawTexturedRectangle((width / 28) * 24-25, (height / 30) * 4.5F+60, 130, 130, passport, 0xFFFFFFFF);
		
		Texture head = this.kiloHead.getTexture();
		
		GuiHelper.drawRectangle(width / 30, height / 5, width / 2.3F, (height / 10) * 8.90F, 0xFFd7d7d7);
		
		GuiHelper.drawTexturedRectangle(width / 20+8, height / 5 + 27, width / 2.3F - width / 20 - 40, (height / 10) * 8.93F - height / 5 - 60, head, 0xFFFFFFFF);
		
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), width / 2.1F, height / 4+10, Minecraft.getMinecraft().getSession().getUsername(), 0xFF363636);

		Texture premiummc = ResourceHelper.mcVerified;
		
		if (verified)
			GuiHelper.drawTexturedRectangle(width / 2.1F + FontHandler.ROUNDED_BOLD.get(40).getWidth(Minecraft.getMinecraft().getSession().getUsername() + "  "), height / 4+11, 50, 50, premiummc, Utilities.reAlpha(0xffffff, 1F * opacity));
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(25), width / 2.1F, height / 2.4F, "First Name:", 0xFF464646);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(25), width / 2.1F, height / 2.0F, "Email Address:", 0xFF464646);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(25), width / 2.1F, height / 1.7F, "MyKiLO Password:", 0xFF464646);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(25), width / 2.1F, height / 1.5F, "Re-enter Password:", 0xFF464646);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(12), (width / 2.1F), (height / 1.29F), "By clicking continue, you are", 0xFF464646);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(12), (width / 2.1F), (height / 1.24F), "agreeing to our terms and conditions.", 0xFF464646);
		//Draw points at bottom
		int currentPointColor = 0xFF1DB8C2;
		int pointColor = 0xFF414141;
		
		GuiHelper.drawPoint((width / 16) * 15+27, (height / 16) * 15.2F-4, 14, pointColor);
		GuiHelper.drawPoint((width / 16) * 14.625F+27, (height / 16) * 15.2F-4, 14, pointColor);
		GuiHelper.drawPoint((width / 16) * 14.25F+27, (height / 16) * 15.2F-4, 14, currentPointColor);
		GuiHelper.drawPoint((width / 16) * 13.885F+27, (height / 16) * 15.2F-4, 14, pointColor);
		
		//Draw "Already have a passport section?"
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(16), (width / 10) * 8+93, (height / 18) / 2+28, "Already have a", 0xFF676767);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(16), (width / 10) * 8+81, (height / 18) / 1.1F+32, "passport?", 0xFF676767);
		//GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(16), (width / 10) * 8+168, (height / 18) / 1.1F+32, "Login", 0xFFFFFFFF);
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_LIGHT.get(16), (width / 1.246F), (height / 1.4F), message, ColorHelper.RED.getColorCode());

		super.render(opacity);
	}
	
	public boolean submit() {
		String ign = Minecraft.getMinecraft().getSession().getUsername();
		String name = ((TextBoxAlt)interactables.get(1)).getText();
		String email = ((TextBoxAlt)interactables.get(2)).getText();
		String password = "";
		
		if (!verified) {
			message = "Invalid session.";
			return false;
		}
		
		if (!((TextBoxAlt)interactables.get(3)).getText().equalsIgnoreCase(((TextBoxAlt)interactables.get(4)).getText())) {
			message = "Passwords don't match.";
			return false;
		} else {
			 password = ((TextBoxAlt)interactables.get(3)).getText();
			 
		}
		
		if(!(((TextBoxAlt)interactables.get(1)).getText().length() > 0)) {
			message = "Invalid name.";
			return false;
		}
		
		if(!((TextBoxAlt)interactables.get(2)).getText().contains("@") || !((TextBoxAlt)interactables.get(2)).getText().contains(".") || !(((TextBoxAlt)interactables.get(2)).getText().length() > 0)) {
			message = "Invalid email.";
			return false;
		}
		try {
			String[] response = APIHelper.createPassport(ign, name, email, password);
			if (response[1].equalsIgnoreCase("success")) {
				String clientID = response[0];	
				try { 
					if (clientID.length() > 0) {
						String[] clientDetails = APIHelper.getClientDetails(clientID);
						if (clientDetails.length == 12) {
							KiLO.getKiLO().setUserControl(new UserControl(clientDetails[0], clientDetails[1], clientDetails[2], clientDetails[3], clientDetails[4], clientDetails[5], clientDetails[6], clientDetails[7], clientDetails[8], clientDetails[9], clientDetails[10], Boolean.parseBoolean(clientDetails[11])));
							return true;
						} else {
							KiLO.getKiLO().setUserControl(null);
							return true;
						}
					} else {
						KiLO.getKiLO().setUserControl(null);
						return true;
					}
				} catch (Exception e) {
					KiLO.getKiLO().setUserControl(new UserControl(clientID, "User", "", "false", "", "verified", "", "30", "1", null, null, false));
					return true;
				}
			} else {
				message = response[1];
				return false;
			}
		} catch(Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}

	@Override
	public void handleInteraction(Interactable i) {
		switch (interactables.indexOf(i)) {
		case 0:
			KiLO.getKiLO().getUIHandler().changeUI(new UILogin(this));
			break;
		case 5 :
			if (submit())
				KiLO.getKiLO().getUIHandler().changeUI(new UIColorChooser(this));
		}
	}
}
