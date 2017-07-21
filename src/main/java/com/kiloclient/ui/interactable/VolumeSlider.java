package com.kiloclient.ui.interactable;

import com.kiloclient.KiLO;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.InteractableParent;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.Utilities;


public class VolumeSlider extends Interactable {
	
	public float volumeValue;
	
	public VolumeSlider(InteractableParent p, float x, float y, float w, float h) {
		super(p, InteractableType.SLIDER, x, y, w, h, FontHandler.STANDARD.get(10), ColorHelper.WHITE.getColorCode(), Align.LEFT, Align.TOP);
		volumeValue = (KiLO.getKiLO().getMusicHandler().getVolume() / 100F) * (h);
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		if (active) {
			volumeValue = (y +  height) - my;
		}
		volumeValue = Math.min(Math.max(0, volumeValue), height);
		
		float vol = (((1F / (height)) * volumeValue) * 100);
		KiLO.getKiLO().getMusicHandler().setVolume(vol);
	}
	
	public void mouseClick(int mx, int my, int b) {
		super.mouseClick(mx, my, b);
		if (mouseOver(mx, my)) {
			parent.handleInteraction(this);
			active = true;
		} else {
			active = false;
			parent.interactables.remove(this);
		}
	}
	
	public void mouseRelease(int mx, int my, int b) {
		active = false;
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x - 4, y - 4 - (width / 2), x +  width +  4, y +  height +  4 +  (width / 2), Utilities.reAlpha(0xFF1A1A1A, 1F * opacity));
		GuiHelper.drawRectangle(x, y +  height +  (width / 2), x +  width, y +  height +  (width / 2) - volumeValue, Utilities.reAlpha(KiLO.getKiLO().getColorSchemeHandler().getCurrentBackground(), 1F * opacity));
		GuiHelper.drawRectangle(x, y +  height - (width / 2) - volumeValue, x +  width, y +  height +  (width / 2) - volumeValue, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1F * opacity));
	}
}
