package com.canvasclient.ui.interactable;

import com.canvasclient.Canvas;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.InteractableParent;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.utilities.Utilities;


public class VolumeSlider extends Interactable {
	
	public float volumeValue;
	
	public VolumeSlider(InteractableParent p, float x, float y, float w, float h) {
		super(p, InteractableType.SLIDER, x, y, w, h, FontHandler.STANDARD.get(10), ColorHelper.WHITE.getColorCode(), Align.LEFT, Align.TOP);
		volumeValue = (Canvas.getCanvas().getMusicHandler().getVolume() / 100F) * (h);
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		if (active) {
			volumeValue = (y +  height) - my;
		}
		volumeValue = Math.min(Math.max(0, volumeValue), height);
		
		float vol = (((1F / (height)) * volumeValue) * 100);
		Canvas.getCanvas().getMusicHandler().setVolume(vol);
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
		GuiHelper.drawRectangle(x, y +  height +  (width / 2), x +  width, y +  height +  (width / 2) - volumeValue, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 1F * opacity));
		GuiHelper.drawRectangle(x, y +  height - (width / 2) - volumeValue, x +  width, y +  height +  (width / 2) - volumeValue, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1F * opacity));
	}
}
