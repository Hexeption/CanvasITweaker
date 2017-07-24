package com.canvasclient.ui.popup;

import com.canvasclient.Canvas;
import com.canvasclient.infrastructure.WorldManager;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.UI;
import com.canvasclient.ui.UISingleplayer;
import com.canvasclient.ui.interactable.IconButton;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.TextBox;
import com.canvasclient.ui.interactable.TextBoxAlt;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.resource.ResourceHelper;
import com.canvasclient.utilities.Timer;
import com.canvasclient.utilities.Utilities;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class UIPopupWorldRename extends UI {

	private float formOffset;
	private boolean invalid, deleting;
	
	private String invalidMessage = "";
	
	private Timer invalidTimer = new Timer();
	
	private float fX, fY, fW, fH;
	
	private WorldSummary world;

	public UIPopupWorldRename(UI parent, WorldSummary world) {
		super(parent);
		this.world = world;
	}

	@Override
	public void init() {
		title = "Rename";

		formOffset = 0;
		invalid = false;
		
		fX = Display.getWidth()/2;
		fY = (Display.getHeight()/2);
		fW = 450;
		fH = 175;

		interactables.clear();
		interactables.add(new IconButton(this, fX+(fW/2)-40, fY-(fH/2)+12, 24, 24, 0x00000000, ResourceHelper.iconClose[3]));
		interactables.add(new IconButton(this, fX+(fW/2)-16-24, fY+(fH/2)-48, 24, 24, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconSubmit[2]));
		interactables.add(new TextBoxAlt(this, "Enter new world name...", fX-(fW/2)+24, fY-(fH/2)+48+24, fW-64, 32, FontHandler.ROUNDED_BOLD.get(20), -1, Align.LEFT, Align.CENTER));
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
			((UISingleplayer)parent).changePopup(null);
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
			((UISingleplayer)parent).changePopup(null);
			break;
		}
	}
	
	public void keyTyped(int key, char keyChar) {
		super.keyTyped(key, keyChar);
	}
	
	public void submit() {
		String newName = ((TextBox)interactables.get(2)).text;
		
		if (newName != null && newName.length() > 0) {
            ISaveFormat var2 = this.mc.getSaveLoader();
            var2.renameWorld(world.getFileName(), newName);
            WorldManager.loadWorlds();
			((UISingleplayer)parent).changePopup(null);
		} else {
			invalidMessage = "Please enter a new world name";
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

		super.render(opacity);
		
		//Message
		GuiHelper.startClip(fX-(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()/2), fX+(FontHandler.STANDARD.get(14).getWidth(invalidMessage)/2), fY+(fH/2)+(FontHandler.STANDARD.get(14).getHeight()*1.5f));
		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(14), fX, fY+(fH/2)-(FontHandler.STANDARD.get(14).getHeight(invalidMessage))-formOffset, invalidMessage, Utilities.reAlpha(0xFFFF5555, 1f*opacity), Align.CENTER, Align.CENTER);
		GuiHelper.endClip();
	}
}
