package com.kiloclient.ui;

import com.kiloclient.render.FontHandler;
import org.lwjgl.opengl.Display;

import com.kiloclient.KiLO;
import com.kiloclient.api.APIHelper;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.Link;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;

public class UIVerifyAccount extends UI {

	private boolean verified;
	private float checkRotate;
	private float fX, fY, fW, fH;
	
	private final float rotateSpeed = 10f;
	
	private Timer timer = new Timer();
	
	public UIVerifyAccount(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = "Verify Your Account";
		
		verified = false;
		checkRotate = 0;
		
		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2);
		fW = 1100;
		fH = 350;
		
		interactables.clear();
		interactables.add(new Link(this, APIHelper.SUPPORT_EMAIL, fX-(fW/2)+20+ FontHandler.ROUNDED_BOLD.get(14).getWidth("Please email us at "), fY+(fH/2)-21, FontHandler.ROUNDED_BOLD.get(14), ColorHelper.GREEN.getColorCode(), Align.LEFT, Align.BOTTOM));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		
		if (verified) {
			checkRotate = 0;
			return;
		}
		checkRotate+= rotateSpeed;
		
		if (timer.isTime(10f)) {
			new Thread() {
				public void run() {
					String[] cd = APIHelper.getClientDetails(KiLO.getKiLO().getUserControl().clientID);
					if (cd[5].equalsIgnoreCase("verified")) {
						KiLO.getKiLO().getUIHandler().changeUI(new UIVerified(null));
						verified = true;
					} else if (cd[5].equalsIgnoreCase("banned")) {
						KiLO.getKiLO().getUIHandler().changeUI(new UIWelcome(null));
					}
				}
			}.start();
			timer.reset();
		}
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch(interactables.indexOf(i)) {
		case 0:
			Utilities.openWeb("mailto:"+APIHelper.SUPPORT_EMAIL);
			break;
		}
	}
	
	public void render(float opacity) {
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth()/2, fY-(fH/2)-(FontHandler.ROUNDED_BOLD.get(40).getHeight(title)/2)-10, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
		
		//Form boxing
		GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2), fX+(fW/2), fY+(fH/2), Utilities.reAlpha(0xFF000000, 0.5f*opacity));
		
		//Data problem
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fX-(fW/2)+20, fY+(fH/2)-20- FontHandler.ROUNDED_BOLD.get(14).getHeight(), "Not there? Firstly, sorry about the inconvenience.", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity), Align.LEFT, Align.BOTTOM);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fX-(fW/2)+20, fY+(fH/2)-20, "Please email us at ", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity), Align.LEFT, Align.BOTTOM);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fX-(fW/2)+20+ FontHandler.ROUNDED_BOLD.get(14).getWidth("Please email use at "+APIHelper.SUPPORT_EMAIL), fY+(fH/2)-20, "and we will be happy to help!", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 1f*opacity), Align.LEFT, Align.BOTTOM);

		//User email
		if (KiLO.getKiLO().getUserControl() != null) {
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+30, fY-(fH/2)+30, "Hi "+KiLO.getKiLO().getUserControl().minecraftName, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+30, fY-(fH/2)+30+ FontHandler.ROUNDED_BOLD.get(20).getHeight(), "To continue using KiLO, you are required to verify your email.", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX-(fW/2)+30, fY-(fH/2)+30+(FontHandler.ROUNDED_BOLD.get(20).getHeight()*2), "Please check your emails for your verification email on the account:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(50), fX, fY, KiLO.getKiLO().getUserControl().email, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
			
			GuiHelper.drawLoaderAnimation(fX+(fW/2)-32, fY+(fH/2)-32, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX+(fW/2)-62, fY+(fH/2)-20, "Checking Verification", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity), Align.RIGHT, Align.BOTTOM);
		}
		
		super.render(opacity);
	}
}
