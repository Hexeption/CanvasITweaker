package com.kiloclient.ui.interactable;

import com.kiloclient.KiLO;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.InteractableParent;
import com.kiloclient.ui.UIChat;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.utilities.ChatUtilities;
import com.kiloclient.utilities.Utilities;
import net.minecraft.util.text.TextComponentString;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;


public class ChatComponent extends Interactable {
	
	public TextComponentString component;
	public float anim;
	public final float animSpeed = 3f;
	
	public ChatComponent(InteractableParent p, TextComponentString icc, float x, float y, TrueTypeFont f, int fc, Align fh, Align fv) {
		super(p, InteractableType.CHAT_COMPONENT, x-(fh==Align.CENTER?f.getWidth(icc.getFormattedText())/2:(fh==Align.RIGHT?f.getWidth(icc.getFormattedText()):0)), y-(fv==Align.CENTER?f.getHeight()/2:(fv==Align.BOTTOM?f.getHeight():0)), 0, 0, f, fc, fh, fv);
		component = icc;
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		if (font != null) {
			width = font.getWidth(component.getFormattedText());
			height = font.getHeight(component.getFormattedText());
		}
		
		if (component.getStyle().getClickEvent() == null && component.getStyle().getHoverEvent() == null && component.getStyle().getInsertion() == null) {
			hover = false;
		}
		anim+= ((hover?1f:0f)-anim)/animSpeed;
		
		if (hover) {
			if (KiLO.getKiLO().getUIHandler().getCurrentUI() != null && KiLO.getKiLO().getUIHandler().getCurrentUI() instanceof UIChat) {
				((UIChat)KiLO.getKiLO().getUIHandler().getCurrentUI()).hoverComponent = component;
			}
		}
	}
	
	public void render(float opacity) {
		int color = Utilities.blendColor(ColorHelper.GREY.getColorCode(), ColorHelper.WHITE.getColorCode(), 0.25f);
		if (component != null && component.getStyle() != null && component.getStyle().getColor() != null) {
			org.newdawn.slick.Color c = ChatUtilities.getColorFromChar(component.getStyle().getColor().toString().toCharArray()[1]);
			Color rc = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
			color = rc.getRGB();
		}
		if (component.getStyle().getClickEvent() != null) {
			GuiHelper.drawRectangle(x, y+height+2, x+((width+2)*anim), y+height+4, Utilities.reAlpha(color, anim*opacity));
		}
		
		GuiHelper.drawStringWithShadowFromTTF(font, x+(width/2), y+2, component.getFormattedText(), Utilities.reAlpha(fontColor, 1f*opacity), Align.CENTER, Align.TOP);
	}
}
