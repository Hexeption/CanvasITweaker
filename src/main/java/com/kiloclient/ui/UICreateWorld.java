package com.kiloclient.ui;

import java.util.Random;

import com.kiloclient.render.FontHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.kiloclient.KiLO;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.ui.interactable.Button;
import com.kiloclient.ui.interactable.CheckBox;
import com.kiloclient.ui.interactable.IconButton;
import com.kiloclient.ui.interactable.Interactable;
import com.kiloclient.ui.interactable.TextBox;
import com.kiloclient.ui.interactable.TextBoxAlt;
import com.kiloclient.render.utilities.Align;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.Timer;
import com.kiloclient.utilities.Utilities;


public class UICreateWorld extends UI {

	private float formOffset;
	private boolean invalid;
	
	private String invalidMessage = "";
	
	private Timer invalidTimer = new Timer();

	private float fX, fY, fW, fH;

	private String worldFileName = "", worldName = "", worldSeed = "", gamemode = "survival";
    private boolean generateStructures, cheats;
	private WorldType worldType = WorldType.DEFAULT;
    private static final String[] BLACK_LIST = new String[] {"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};
	
	public UICreateWorld(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		title = "Create New World";

		formOffset = 0;
		invalid = false;

		fX = Display.getWidth() / 2;
		fY = (Display.getHeight() / 2) + 44;
		fW = Display.getWidth() - 40;
		fH = Display.getHeight() - 124;

		interactables.clear();
		interactables.add(new IconButton(this, 32, 32, 32, 32, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconReturn[3]));
		int i = 0;
		interactables.add(new IconButton(this, fX + (fW / 2) - 24 - 32 - (48 * (i++)), fY - (fH / 2) + 16, 32, 32, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconHardcore[3]));
		interactables.add(new IconButton(this, fX + (fW / 2) - 24 - 32 - (48 * (i++)), fY - (fH / 2) + 16, 32, 32, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconSurvival[3]));
		interactables.add(new IconButton(this, fX + (fW / 2) - 24 - 32 - (48 * (i++)), fY - (fH / 2) + 16, 32, 32, ColorHelper.WHITE.getColorCode(), ResourceHelper.iconCreative[3]));
		TextBox tb;
		interactables.add(tb = new TextBoxAlt(this, "Enter World Name", fX - (fW / 2) + 24, fY - (fH / 2) + 8, 350, 52, FontHandler.ROUNDED_BOLD.get(20), ColorHelper.WHITE.getColorCode(), ColorHelper.WHITE.getColorCode(), 4f, Align.LEFT, Align.CENTER));
		tb.text = worldName;
		interactables.add(new Button(this, "Default", fX - (fW / 2) + 524, fY - (fH / 2) + 128 + 32, ((fW - 500 - 48) / 2) - 16, ((fH - 256) / 2) - 8, FontHandler.ROUNDED_BOLD.get(25), 0xFF777777, ColorHelper.GREEN.getColorCode(), ResourceHelper.iconTick[2], 24, Align.LEFT, Align.TOP));
		interactables.add(new Button(this, "Superflat", fX - (fW / 2) + 524 + ((fW - 500 - 48 + 16) / 2) + 8, fY - (fH / 2) + 128 + 32, ((fW - 500 - 48) / 2) - 16, ((fH - 256) / 2) - 8, FontHandler.ROUNDED_BOLD.get(25), 0xFF777777, ColorHelper.GREEN.getColorCode(), ResourceHelper.iconTick[2], 24, Align.LEFT, Align.TOP));
		interactables.add(new Button(this, "Large Biomes", fX - (fW / 2) + 524, fY - (fH / 2) + 128 + 32 + ((fH - 256) / 2) + 8, ((fW - 500 - 48) / 2) - 16, ((fH - 256) / 2) - 8, FontHandler.ROUNDED_BOLD.get(25), 0xFF777777, ColorHelper.GREEN.getColorCode(), ResourceHelper.iconTick[2], 24, Align.LEFT, Align.TOP));
		interactables.add(new Button(this, "AMPLIFIED", fX - (fW / 2) + 524 + ((fW - 500 - 48 + 16) / 2) + 8, fY - (fH / 2) + 128 + 32 + ((fH - 256) / 2) + 8, ((fW - 500 - 48) / 2) - 16, ((fH - 256) / 2) - 8, FontHandler.ROUNDED_BOLD.get(25), 0xFF777777, ColorHelper.GREEN.getColorCode(), ResourceHelper.iconTick[2], 24, Align.LEFT, Align.TOP));
		CheckBox cb;
		interactables.add(cb = new CheckBox(this, "Allow Cheats", fX - (fW / 2) + 24, fY + (fH / 2) - 32 - 11, FontHandler.ROUNDED_BOLD.get(14), ColorHelper.GREEN.getColorCode()));
		cb.active = cheats;
		interactables.add(new CheckBox(this, "Bonus Chest", fX - (fW / 2) + 24 + 22 + 16 + 24 + FontHandler.ROUNDED_BOLD.get(14).getWidth("Allow Cheats"), fY + (fH / 2) - 32 - 11, FontHandler.ROUNDED_BOLD.get(14), ColorHelper.GREEN.getColorCode()));
		interactables.add(cb = new CheckBox(this, "Generate Structures", fX - (fW / 2) + 24 + ((22 + 16 + 24) * 2) + FontHandler.ROUNDED_BOLD.get(14).getWidth("Allow Cheats") + FontHandler.ROUNDED_BOLD.get(14).getWidth("Bonus Chest"), fY + (fH / 2) - 32 - 11, FontHandler.ROUNDED_BOLD.get(14), ColorHelper.GREEN.getColorCode()));
		cb.active = generateStructures;
		interactables.add(new Button(this, "Create", fX + (fW / 2) - 16 - 128, fY + (fH / 2) - 48, 128, 32, FontHandler.STANDARD.get(12), ColorHelper.GREEN.getColorCode(), ResourceHelper.iconSubmit[1], 16));
		interactables.add(tb = new TextBoxAlt(this, "Enter World Seed", fX + (fW / 2) - 16 - 128 - 32 - 300, fY + (fH / 2) - 48, 300, 32, FontHandler.ROUNDED_BOLD.get(20), ColorHelper.GREY.getColorCode(), ColorHelper.GREY.getColorCode(), 2f, Align.LEFT, Align.CENTER));
		tb.text = worldSeed;
		
		setNewFileName();
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);
		if (invalid) {
			formOffset += (( - FontHandler.STANDARD.get(14).getHeight() * 2) - formOffset) / 5f;
			if (invalidTimer.isTime(2f)) {
				invalid = false;
			}
		} else {
			invalidTimer.reset();
		}
		if (!invalid) {
			formOffset += (0 - formOffset) / 5f;
		}
		
		int color = gamemode == "creative" ? ColorHelper.BLUE.getColorCode():gamemode == "survival" ? ColorHelper.RED.getColorCode() : ColorHelper.ORANGE.getColorCode();
		
		((IconButton)interactables.get(1)).buttonColor = (gamemode == "hardcore" ? ColorHelper.WHITE.getColorCode():color);
		((IconButton)interactables.get(2)).buttonColor = (gamemode == "survival" ? ColorHelper.WHITE.getColorCode():color);
		((IconButton)interactables.get(3)).buttonColor = (gamemode == "creative" ? ColorHelper.WHITE.getColorCode():color);

		((Button)interactables.get(5)).buttonColor = (worldType == WorldType.DEFAULT?0xFF3F3F3F:0xFF777777);
		((Button)interactables.get(6)).buttonColor = (worldType == WorldType.FLAT?0xFF3F3F3F:0xFF777777);
		((Button)interactables.get(7)).buttonColor = (worldType == WorldType.LARGE_BIOMES?0xFF3F3F3F:0xFF777777);
		((Button)interactables.get(8)).buttonColor = (worldType == WorldType.AMPLIFIED?0xFF3F3F3F:0xFF777777);
		
		((Button)interactables.get(5)).icon = (worldType == WorldType.DEFAULT?ResourceHelper.iconTick[3]:null);
		((Button)interactables.get(6)).icon = (worldType == WorldType.FLAT?ResourceHelper.iconTick[3]:null);
		((Button)interactables.get(7)).icon = (worldType == WorldType.LARGE_BIOMES?ResourceHelper.iconTick[3]:null);
		((Button)interactables.get(8)).icon = (worldType == WorldType.AMPLIFIED?ResourceHelper.iconTick[3]:null);
	}
	
	@Override
	public void handleInteraction(Interactable i) {
		switch (interactables.indexOf(i)) {
		case 0:
			KiLO.getKiLO().getUIHandler().changeUI(parent);
			break;
		case 1:
			gamemode = "hardcore";
			break;
		case 2:
			gamemode = "survival";
			break;
		case 3:
			gamemode = "creative";
			break;
		case 5:
			worldType = WorldType.DEFAULT;
			break;
		case 6:
			worldType = WorldType.FLAT;
			break;
		case 7:
			worldType = WorldType.LARGE_BIOMES;
			break;
		case 8:
			worldType = WorldType.AMPLIFIED;
			break;
		case 12:
			this.mc.displayGuiScreen((GuiScreen)null);

            long var2 = (new Random()).nextLong();
            String var4 = ((TextBox)interactables.get(13)).text;

            if (!StringUtils.isEmpty(var4))
            {
                try
                {
                    long var5 = Long.parseLong(var4);

                    if (var5 != 0L)
                    {
                        var2 = var5;
                    }
                }
                catch (NumberFormatException var7)
                {
                    var2 = (long)var4.hashCode();
                }
            }

            GameType var8 = GameType.getByName(gamemode);
            WorldSettings var6 = new WorldSettings(var2, var8, ((CheckBox)interactables.get(11)).active, gamemode == "hardcore", worldType);
            //var6.setWorldName(((TextBox)interactables.get(4)).text);

            if (((CheckBox)interactables.get(10)).active && gamemode != "hardcore")
            {
                var6.enableBonusChest();
            }

            if (((CheckBox)interactables.get(9)).active && gamemode != "hardcore")
            {
                var6.enableCommands();
            }

            this.mc.launchIntegratedServer(worldFileName, ((TextBox)interactables.get(4)).text, var6);
			break;
		}
		if (i instanceof CheckBox) {
			i.active = !i.active;
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
	}

	public void keyTyped(int key, char keyChar) {
		super.keyTyped(key, keyChar);
		setNewFileName();
	}
	
	private void setNewFileName() {
        worldFileName = ((TextBox)interactables.get(4)).text == null?"":((TextBox)interactables.get(4)).text;
        char[] var1 = ChatAllowedCharacters.ILLEGAL_FILE_CHARACTERS;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            char var4 = var1[var3];
            worldFileName = worldFileName.replace(var4, '_');
        }

        if (StringUtils.isEmpty(worldFileName))
        {
            worldFileName = "New World";
        }

        worldFileName = func_146317_a(mc.getSaveLoader(), worldFileName);
    }

    public static String func_146317_a(ISaveFormat p_146317_0_, String p_146317_1_)
    {
        p_146317_1_ = p_146317_1_.replaceAll("[\\. / \"]", "_");
        String[] var2 = BLACK_LIST;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            String var5 = var2[var4];

            if (p_146317_1_.equalsIgnoreCase(var5))
            {
                p_146317_1_ = "_"  +  p_146317_1_  +  "_";
            }
        }

        while (p_146317_0_.getWorldInfo(p_146317_1_) != null)
        {
            p_146317_1_ = p_146317_1_  +  " - ";
        }

        return p_146317_1_;
    }
    
    public void set(WorldInfo p_146318_1_)
    {
        worldName = I18n.format("selectWorld.newWorld.copyOf", new Object[] {p_146318_1_.getWorldName()});
        worldSeed = p_146318_1_.getSeed()  +  "";
        worldType = p_146318_1_.getTerrainType();
        generateStructures = p_146318_1_.isMapFeaturesEnabled();
        cheats = p_146318_1_.areCommandsAllowed();

        if (p_146318_1_.isHardcoreModeEnabled())
        {
            gamemode = "hardcore";
        }
        else if (p_146318_1_.getGameType().isSurvivalOrAdventure())
        {
        	gamemode = "survival";
        }
        else if (p_146318_1_.getGameType().isCreative())
        {
        	gamemode = "creative";
        }
    }
	
	public void render(float opacity) {
		drawDarkerBackground(false, opacity);
		
		String mode = gamemode.substring(0, 1).toUpperCase() + gamemode.substring(1, gamemode.length());
		int color = gamemode == "creative" ? ColorHelper.BLUE.getColorCode():gamemode == "survival" ? ColorHelper.RED.getColorCode() : ColorHelper.ORANGE.getColorCode();
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(40), Display.getWidth() / 2, fY - (fH / 2) - (FontHandler.ROUNDED_BOLD.get(40).getHeight(title) / 2) - 32, title, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1F * opacity), Align.CENTER, Align.CENTER);
		
		if (KiLO.getKiLO().getUserControl().minecraftPlayer.head.getTexture() != null) {
			Texture tex = KiLO.getKiLO().getUserControl().minecraftPlayer.head.getTexture();
			GuiHelper.drawTexturedRectangle(Display.getWidth() - 20 - 64, 20, 64, 64, tex, 1F * opacity);
			
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(12), Display.getWidth() - 20 - 64 - 16, 20 + 39 - (FontHandler.ROUNDED_BOLD.get(20).getHeight() / 2), "Logged in as", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1F * opacity), Align.RIGHT, Align.BOTTOM);
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), Display.getWidth() - 20 - 64 - 16, 20 + 40, KiLO.getKiLO().getUserControl().minecraftPlayer.gameProfile.getName(), Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1F * opacity), Align.RIGHT, Align.CENTER);
		}
		
		GuiHelper.drawRectangle(fX - (fW / 2), fY - (fH / 2), fX + (fW / 2), fY - (fH / 2) + 64, Utilities.reAlpha(ColorHelper.GREEN.getColorCode(), 1F * opacity));
		GuiHelper.drawRectangle(fX - (fW / 2), fY - (fH / 2) + 64, fX + (fW / 2), fY - (fH / 2) + 128, Utilities.reAlpha(0xFF111111, 1F * opacity));
		GuiHelper.drawRectangle(fX - (fW / 2), fY - (fH / 2) + 128, fX + (fW / 2), fY + (fH / 2) - 64, Utilities.reAlpha(color, 1F * opacity));
		GuiHelper.drawRectangle(fX - (fW / 2), fY + (fH / 2) - 64, fX + (fW / 2), fY + (fH / 2), Utilities.reAlpha(0xFF111111, 1F * opacity));
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fX - (fW / 2) + 48 + (interactables.get(4).width), fY - (fH / 2) + 32, "Will be saved in:", Utilities.reAlpha(ColorHelper.DARK_GREY.getColorCode(), 1F * opacity), Align.LEFT, Align.BOTTOM);
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fX - (fW / 2) + 48 + (interactables.get(4).width), fY - (fH / 2) + 32, worldFileName, Utilities.reAlpha(ColorHelper.DARK_GREY.getColorCode(), 1F * opacity), Align.LEFT, Align.TOP);
		
		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX + (fW / 2) - 24 - 16 - (48 * 3), fY - (fH / 2) + 32, "Game Mode:", Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 1F * opacity), Align.RIGHT, Align.CENTER);
		

		GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(20), fX - (fW / 2) + 24, fY - (fH / 2) + 64 + 32, mode + " Mode", Utilities.reAlpha(color, 1F * opacity), Align.LEFT, Align.CENTER);
		switch(gamemode) {
		case "survival":
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fX + (fW / 2) - 24, fY - (fH / 2) + 64 + 32, "Search for resources, crafting, gain levels, health and hunger.", Utilities.reAlpha(ColorHelper.GREEN.getColorCode(), 1F * opacity), Align.RIGHT, Align.CENTER);
			break;
		case "creative":
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fX + (fW / 2) - 24, fY - (fH / 2) + 64 + 32, "Let your creative side out; unlimited access to everything you need.", Utilities.reAlpha(ColorHelper.GREEN.getColorCode(), 1F * opacity), Align.RIGHT, Align.CENTER);
			break;
		case "hardcore":
			GuiHelper.drawStringFromTTF(FontHandler.ROUNDED_BOLD.get(14), fX + (fW / 2) - 24, fY - (fH / 2) + 64 + 32, "All of the fun of survival mode with a twist: once you die, you cannot respawn.", Utilities.reAlpha(ColorHelper.GREEN.getColorCode(), 1F * opacity), Align.RIGHT, Align.CENTER);
		}
		
		GuiHelper.drawTexturedRectangle(fX - (fW / 2) + 250 - (ResourceHelper.createWorld.getImageWidth() / 2), fY - (ResourceHelper.createWorld.getImageHeight() / 2) + 24, ResourceHelper.createWorld.getImageWidth(), ResourceHelper.createWorld.getImageHeight(), ResourceHelper.createWorld, 1F * opacity);

		GuiHelper.startClip(fX - (FontHandler.STANDARD.get(14).getWidth(invalidMessage) / 2), fY + (fH / 2) - 40 + (FontHandler.STANDARD.get(14).getHeight() / 2), fX + (FontHandler.STANDARD.get(14).getWidth(invalidMessage) / 2), fY + (fH / 2) - 40 + (FontHandler.STANDARD.get(14).getHeight() * 1.5f));
		GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(14), fX, fY + (fH / 2) - 40 - (FontHandler.STANDARD.get(14).getHeight(invalidMessage)) - formOffset, invalidMessage, Utilities.reAlpha(0xFFFF5555, 1F * opacity), Align.CENTER, Align.CENTER);
		GuiHelper.endClip();
		
		super.render(opacity);
	}
}
