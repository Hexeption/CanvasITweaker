package com.canvasclient.infrastructure;

import com.canvasclient.Canvas;
import com.canvasclient.mixin.imp.IMixinGuiScreen;
import com.canvasclient.ui.UIChat;
import com.canvasclient.ui.interactable.TextBox;
import com.canvasclient.ui.interactable.slotlist.part.ChatLine;
import com.canvasclient.utilities.IMinecraft;
import com.canvasclient.utilities.Utilities;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatManager implements IMinecraft {

    private static final Set PROTOCOLS = Sets.newHashSet(new String[] {"http", "https"});
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
	
	public static float chatWidth = 320;
	public static float chatHeight = 180;
	
	public static List<ChatLine> chatLines = new CopyOnWriteArrayList<ChatLine>();
	public static List<String> userHistory = new CopyOnWriteArrayList<String>(); 
	
	public static void addChatLine(final TextComponentString m) {
		chatLines.add(0, new ChatLine(m));
	}

	public static List<ChatLine> getList() {
		return chatLines;
	}
	
	public static void addChatLine(ChatLine s) {
		chatLines.add(s);
	}
	
	public static void addChatLine(int index, ChatLine s) {
		chatLines.add(index, s);
	}
	
	public static void removeChatLine(ChatLine s) {
		chatLines.remove(s);
	}
	
	public static void removeChatLine(int index) {
		chatLines.remove(chatLines.get(index));
	}
	
	public static ChatLine getChatLine(int index) {
		if (chatLines.size() == 0 || index >= chatLines.size()) {
			return null;
		}
		return chatLines.get(index);
	}
	
	public static ChatLine getChatLine(String m) {
		for(ChatLine s : chatLines) {
			if (s.unformatted.equalsIgnoreCase(m)) {
				return s;
			}
		}
		return null;
	}
	
	public static int getIndex(ChatLine s) {
		return chatLines.indexOf(s);
	}
	
	public static int getSize() {
		return getList().size();
	}
	
	public static void handleComponentClick(TextComponentString icc) {
		if (icc == null || mc.currentScreen == null)
        {
            return;
        }
        else
        {
            ClickEvent var2 = icc.getStyle().getClickEvent();

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
            {
                if (icc.getStyle().getInsertion() != null)
                {
                	if (Canvas.getCanvas().getUIHandler().getCurrentUI() instanceof UIChat) {
                		((TextBox) Canvas.getCanvas().getUIHandler().getCurrentUI().interactables.get(0)).text+= icc.getStyle().getInsertion();
                		((TextBox) Canvas.getCanvas().getUIHandler().getCurrentUI().interactables.get(0)).cursorPos = ((TextBox) Canvas.getCanvas().getUIHandler().getCurrentUI().interactables.get(0)).text.length();
                		((TextBox) Canvas.getCanvas().getUIHandler().getCurrentUI().interactables.get(0)).startSelect = ((TextBox) Canvas.getCanvas().getUIHandler().getCurrentUI().interactables.get(0)).cursorPos;
                	}
                }
            }
            else if (var2 != null)
            {
                URI var3;

                if (var2.getAction() == ClickEvent.Action.OPEN_URL)
                {
                    if (!mc.gameSettings.chatLinks)
                    {
                        return;
                    }

                    try
                    {
                        var3 = new URI(var2.getValue());

                        if (!PROTOCOLS.contains(var3.getScheme().toLowerCase()))
                        {
                            throw new URISyntaxException(var2.getValue(), "Unsupported protocol: " + var3.getScheme().toLowerCase());
                        }

                        if (mc.gameSettings.chatLinksPrompt)
                        {
                            ((IMixinGuiScreen) mc.currentScreen).setClickedLinkURI(var3);
                            mc.displayGuiScreen(new GuiConfirmOpenLink(mc.currentScreen, var2.getValue(), 31102009, false));
                        }
                        else
                        {
                            Utilities.openWeb(var3.getPath());
                        }
                    }
                    catch (URISyntaxException var4)
                    {
                        LOGGER.error("Can\'t open url for " + var2, var4);
                    }
                }
                else if (var2.getAction() == ClickEvent.Action.OPEN_FILE)
                {
                    var3 = (new File(var2.getValue())).toURI();
                    Utilities.openWeb(var3.getPath());
                }
                else if (var2.getAction() == ClickEvent.Action.SUGGEST_COMMAND)
                {
                    //this.setText(var2.getValue(), true);
                	if (Canvas.getCanvas().getUIHandler().getCurrentUI() instanceof UIChat) {
                		((TextBox) Canvas.getCanvas().getUIHandler().getCurrentUI().interactables.get(0)).text = var2.getValue();
                		((TextBox) Canvas.getCanvas().getUIHandler().getCurrentUI().interactables.get(0)).cursorPos = ((TextBox) Canvas.getCanvas().getUIHandler().getCurrentUI().interactables.get(0)).text.length();
                		((TextBox) Canvas.getCanvas().getUIHandler().getCurrentUI().interactables.get(0)).startSelect = ((TextBox) Canvas.getCanvas().getUIHandler().getCurrentUI().interactables.get(0)).cursorPos;
                	}
                }
                else if (var2.getAction() == ClickEvent.Action.RUN_COMMAND)
                {
                    mc.player.sendChatMessage(var2.getValue());
                }
                else
                {
                    LOGGER.error("Don\'t know how to handle " + var2);
                }

                return;
            }

            return;
        }
	}
	
	public static void handleComponentHover(TextComponentString icc, int x, int y)
    {
		x/= 2;
		y/= 2;
		GlStateManager.pushMatrix();
		GlStateManager.scale(2, 2, 2);
		if (mc.currentScreen == null) {
			GlStateManager.popMatrix();
			return;
		}
		mc.getTextureManager().bindTexture(Gui.ICONS);
		
        if (icc != null && icc.getStyle().getHoverEvent() != null)
        {
            HoverEvent var4 = icc.getStyle().getHoverEvent();

            if (var4.getAction() == HoverEvent.Action.SHOW_ITEM)
            {
                ItemStack var5 = null;

                try
                {
                    NBTTagCompound var6 = JsonToNBT.getTagFromJson(var4.getValue().getUnformattedText());

                }
                catch (NBTException var11)
                {
                    ;
                }

                if (var5 != null)
                {
                    mc.currentScreen.drawHoveringText(mc.currentScreen.getItemToolTip(var5), x,y);
                }
                else
                {
                    drawCreativeTabHoveringText(ChatFormatting.RED + "Invalid Item!", x, y);
                }
            }
            else
            {
                String var8;

                if (var4.getAction() == HoverEvent.Action.SHOW_TEXT)
                {
                	mc.currentScreen.drawHoveringText(NEWLINE_SPLITTER.splitToList(var4.getValue().getFormattedText()), x, y);
                }
            }

            GlStateManager.disableLighting();
        }
        GlStateManager.popMatrix();
    }

    private static void drawCreativeTabHoveringText(String tabName, int mouseX, int mouseY)
    {
        mc.currentScreen.drawHoveringText(Arrays.asList(tabName), mouseX, mouseY);
    }
}
