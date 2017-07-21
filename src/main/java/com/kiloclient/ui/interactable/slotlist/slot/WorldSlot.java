package com.kiloclient.ui.interactable.slotlist.slot;

import com.kiloclient.manager.WorldManager;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.slotlist.SlotList;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.WorldSummary;
import org.apache.commons.lang3.StringUtils;
import org.newdawn.slick.opengl.Texture;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorldSlot extends Slot {

	private final Minecraft mc = Minecraft.getMinecraft();
    private final DateFormat dateFormat = new SimpleDateFormat();
	
	public int index;
	
	public int clicks = 0;
	public Timer clickTimer = new Timer();
	
	public WorldSlot(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, x, y, w, h, ox, oy);
		index = i;
		
		clickTimer.reset();
		
		interactables.clear();
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		hover = mouseOver(mx, my) && parent.mouseOver(mx, my);

		if (clicks == 2) {
			if (WorldManager.getSize() > 0) {
				if (WorldManager.getWorld(index) != null) {
					loadWorld(index);
				}
			}
			clicks = 0;
		}
		
		if (clickTimer.isTime(Utilities.doubleClickTimer)) {
			clickTimer.reset();
			clicks = 0;
		}
	}
	
	public void mouseClick(int mx, int my, int b) {
		if (parent.mouseOver(mx, my)) {
			active = hover;
		}
		
		if (hover) {
			if (clicks == 0) {
				clickTimer.reset();
			}
			clicks++;
		}
	}
	
	public void loadWorld(int index)
    {
        mc.displayGuiScreen((GuiScreen)null);

        String var2 = WorldManager.getWorld(index).getFileName();

        if (var2 == null)
        {
            var2 = "World" + index;
        }

        String var3 = getWorldName(index);

        if (var3 == null)
        {
            var3 = "World" + index;
        }

        if (mc.getSaveLoader().canLoadWorld(var2))
        {
            mc.launchIntegratedServer(var2, var3, (WorldSettings)null);
        }
    }
	
	public String getWorldName(int index) {
        String var2 = WorldManager.getWorld(index).getDisplayName();

        if (StringUtils.isEmpty(var2))
        {
            var2 = I18n.format("selectWorld.world", new Object[0]) + " " + (index + 1);
        }

        return var2;
	}
	
	public void render(float opacity) {
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF1A1A1A, activeOpacity*opacity));
		GuiHelper.drawRectangle(x, y, x+width, y+height, Utilities.reAlpha(0xFF0A0A0A, hoverOpacity*opacity));
		if (WorldManager.getWorld(index) != null) {

			WorldSummary world = WorldManager.getWorld(index);

			Texture icon = null;
			int color = ColorHelper.WHITE.getColorCode();
			switch (world.getEnumGameType()) {
			case ADVENTURE:
				icon = ResourceHelper.iconAdventure[4];
				color = ColorHelper.YELLOW.getColorCode();
				break;
			case CREATIVE:
				icon = ResourceHelper.iconCreative[4];
				color = ColorHelper.BLUE.getColorCode();
				break;
			case NOT_SET:
				break;
			case SPECTATOR:
				icon = ResourceHelper.iconSpectator[4];
				color = ColorHelper.WHITE.getColorCode();
				break;
			case SURVIVAL:
				if (world.isHardcoreModeEnabled()) {
					color = ColorHelper.ORANGE.getColorCode();
					icon = ResourceHelper.iconHardcore[4];
				} else {
					icon = ResourceHelper.iconSurvival[4];
					color = ColorHelper.RED.getColorCode();
				}
				break;
				
			}
			
			if (icon != null) {
				GuiHelper.drawTexturedRectangle(x+24, y+16, 48, 48, icon, Utilities.reAlpha(color, 1f*opacity));
			} else {
				GuiHelper.drawLoaderAnimation(x+48, y+30, 8, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1f*opacity));
			}
			
			List<String> info = new ArrayList<String>();
			if (world.isHardcoreModeEnabled()) { info.add("\u00a7cHardcore"); }
			if (world.getCheatsEnabled()) { info.add("\u00a7aCheats"); }
			String infoLine = "";
			for(String s : info) {
				infoLine+= s;
				if (info.indexOf(s) < info.size()-1) {
					infoLine+= " \u00a77| ";
				}
			}
			
			if (world.requiresConversion()) {
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), x+104+((width-104)/2), y+40, "This world needs to be converted!", Utilities.reAlpha(ColorHelper.RED.getColorCode(), 1f*opacity), Align.CENTER, Align.CENTER);
			} else {
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), x+104, y+(infoLine.length() == 0?44:32), world.getDisplayName(), Utilities.reAlpha(color, 1f*opacity), Align.LEFT, Align.BOTTOM);
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), x+104, y+(infoLine.length() == 0?44:40), world.getFileName()+" ("+dateFormat.format(new Date(world.getLastTimePlayed()))+")", Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 0.5f*opacity), Align.LEFT, (infoLine.length() == 0?Align.TOP:Align.CENTER));
				GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), x+104, y+48, infoLine, Utilities.reAlpha(ColorHelper.GREY.getColorCode(), 0.5f*opacity), Align.LEFT, Align.TOP);
			}
		}
		super.render(opacity);
	}
}
