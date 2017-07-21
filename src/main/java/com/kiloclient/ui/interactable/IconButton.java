package com.kiloclient.ui.interactable;

import com.kiloclient.render.FontHandler;
import org.newdawn.slick.opengl.Texture;

import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.InteractableParent;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.render.utilities.AnimationTimer;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Utilities;


public class IconButton extends Interactable {
	
	public int buttonColor;
	AnimationTimer anim = new AnimationTimer(10);
	public Texture icon;
	
	public IconButton(InteractableParent p, float x, float y, float w, float h, int bc, Texture i) {
		super(p, Interactable.InteractableType.BUTTON, x, y, w, h, FontHandler.STANDARD.get(20), ColorHelper.WHITE.getColorCode(), Align.CENTER, Align.CENTER);
		buttonColor = bc;
		icon = i;
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		anim.update(hover);
	}
	
	
	//TODO: Quick fix, make fix better lol, not enough timeeee (a lot of buttons use the selected color for no reason and looks ugly)
	public void render(float opacity) {
		boolean color = icon == ResourceHelper.iconAdd[1] || icon == ResourceHelper.iconDecline[0] || icon == ResourceHelper.iconAccept[0] || icon == ResourceHelper.iconClose[0] || icon == ResourceHelper.iconGoto[0] || icon == ResourceHelper.iconPrev[1] || icon == ResourceHelper.iconPlay[1] || icon == ResourceHelper.iconPause[1]|| icon == ResourceHelper.iconNext[1] || icon == ResourceHelper.iconSearch[1] || icon == ResourceHelper.iconSend[4] || icon == ResourceHelper.iconAdd[1] || icon == ResourceHelper.iconPlus[0] || icon == ResourceHelper.iconPlay[3] || icon == ResourceHelper.iconPause[3];
		GuiHelper.drawTexturedRectangle(x, y, width, height, icon, Utilities.reAlpha(color ? buttonColor : 0xFFFFFF, 1f - (float)anim.getValue()*0.4f));
	}
}
