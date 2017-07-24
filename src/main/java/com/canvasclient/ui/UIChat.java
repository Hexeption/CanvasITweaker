package com.canvasclient.ui;

import com.canvasclient.Canvas;
import com.canvasclient.infrastructure.ChatManager;
import com.canvasclient.mixin.imp.IMixinGuiChat;
import com.canvasclient.render.FontHandler;
import com.canvasclient.render.GuiHelper;
import com.canvasclient.render.utilities.Align;
import com.canvasclient.render.utilities.ColorHelper;
import com.canvasclient.ui.interactable.Interactable;
import com.canvasclient.ui.interactable.TextBox;
import com.canvasclient.ui.interactable.slotlist.ChatList;
import com.canvasclient.ui.interactable.slotlist.part.ChatLine;
import com.canvasclient.ui.interactable.slotlist.slot.ChatSlot;
import com.canvasclient.ui.interactable.slotlist.slot.Slot;
import com.canvasclient.utilities.ChatUtilities;
import com.canvasclient.utilities.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.TrueTypeFont;

import java.util.ArrayList;
import java.util.List;

public class UIChat extends UI {

	public static final TrueTypeFont font = FontHandler.ROUNDED.get(14);
	
	private int selectedIndex;
	private static float fX, fY, fW, fH, dY, dYTo;
	private static final float pad = 8f;
	
	private int selectedHistory = -1;
	
	public static ChatList sl = new ChatList(3f);
	
	public TextComponentString hoverComponent;
	
	private String pred = "";
	
	public UIChat(UI parent) {
		super(parent);
	}
	
	@Override
	public void init() {
		sl.slots.clear();
		fX = 4+(ChatManager.chatWidth/2);
		fW = ChatManager.chatWidth;
		fH = ChatManager.chatHeight;

		interactables.clear();
		interactables.add(new TextBox(this, "Enter message...", -100, 0, 10, 24, font, ColorHelper.WHITE.getColorCode(), Align.LEFT, Align.CENTER) {
			@Override
			public void render(float opacity) {
				if(!this.getText().isEmpty()) {
					
		        }
				super.render(opacity);
			}
		});
		interactables.get(0).active = true;
		
		((TextBox)interactables.get(0)).text = ((IMixinGuiChat)mc.currentScreen).getDefaultInputFieldText();
		((TextBox)interactables.get(0)).cursorPos = ((TextBox)interactables.get(0)).text.length();
		((TextBox)interactables.get(0)).startSelect = ((TextBox)interactables.get(0)).cursorPos;
	}
	
	public void update(int mx, int my) {
		interactables.get(0).active = true;
		if (((TextBox)interactables.get(0)).text.length() > 100) {
			((TextBox)interactables.get(0)).text = ((TextBox)interactables.get(0)).text.substring(0, 100);
		}
		
		hoverComponent = null;
		
		super.update(mx, my);
		
		interactables.get(0).x = fX-(fW/2)+(pad);
		interactables.get(0).y = fY+(fH/2)+(pad)+2+dY;
		interactables.get(0).width = fW-(pad*2);
	}
	
	public static void updateHistory(int mx, int my) {
		int scale = Math.max(1, Minecraft.getMinecraft().gameSettings.guiScale);
		ChatManager.chatWidth = (40+(Minecraft.getMinecraft().gameSettings.chatWidth*(320-40)))*scale;
		
		if (!(Canvas.getCanvas().getUIHandler().getCurrentUI() instanceof UIChat) && !(Canvas.getCanvas().getUIHandler().getUITo() instanceof UIChat)) {
			dY+= (0-dY)/2f;
			sl.scrollTo = 0;
			float chatSize = 0;
			for(Slot slot : sl.slots) {
				chatSize+= slot.height;
			}
			ChatManager.chatHeight = Math.min(chatSize, (20+(Minecraft.getMinecraft().gameSettings.chatHeightUnfocused*(180-20)))*scale);
		} else {
			ChatManager.chatHeight = (20+(Minecraft.getMinecraft().gameSettings.chatHeightFocused*(180-20)))*scale;
			if (Canvas.getCanvas().getUIHandler().getCurrentUI() instanceof UIChat) {
				dY+= ((-pad-2-(((UIChat) Canvas.getCanvas().getUIHandler().getCurrentUI()).interactables.get(0)).height)-dY)/2f;
			} else {
				dY+= ((-pad-2-(((UIChat) Canvas.getCanvas().getUIHandler().getUITo()).interactables.get(0)).height)-dY)/2f;
			}
		}

		fX = 4+(ChatManager.chatWidth/2)+65;
		fW = ChatManager.chatWidth + 120;
		fH = ChatManager.chatHeight;
		fY = Display.getHeight()-(fH/2)-1;
		
		float oldFY = fY;
		fY+= dY;
		
		if (sl.slots.size() != ChatManager.getSize()) {
			List<Slot> temp = new ArrayList<Slot>();
			int i = 0;
			for(ChatLine s : ChatManager.getList()) {
				ChatSlot tcs;
				temp.add(tcs = new ChatSlot(sl, ChatManager.getIndex(s), sl.x, sl.y, sl.w, 22, 0, i*22));
				tcs.update(mx, my);
				i++;
			}
			sl.slots = temp;
		}
		
		sl.x = fX-(fW/2)+pad;
		sl.y = fY-(fH/2)+pad-sl.oy;
		sl.w = fW-(pad*2)-sl.sbw;
		sl.h = fH-pad;
		
		sl.update(mx, my);
		
		if (ChatManager.getSize() > 100) {
			ChatManager.removeChatLine(ChatManager.getSize()-1);
		}
		
		fY = oldFY;
	}
	
	@Override
	public void handleInteraction(Interactable i) {
	}
	
	public void mouseClick(int mx, int my, int b) {
		super.mouseClick(mx, my, b);
		sl.mouseClick(mx, my, b);
	}
	
	public void mouseRelease(int mx, int my, int b) {
		super.mouseRelease(mx, my, b);
		sl.mouseRelease(mx, my, b);
	}
	
	public void mouseScroll(int s) {
		super.mouseScroll(s*3);
		sl.mouseScroll(s*3);
	}

	@Override
	public void keyTyped(int key, char typed) {
		super.keyTyped(key, typed);
	}

	public void keyboardPress(int key) {
		super.keyboardPress(key);
		boolean setText = false;
		TextBox tb = (TextBox)interactables.get(0);
		
		switch(key) {
		case Keyboard.KEY_TAB:
			if(!tb.getText().isEmpty()  && !tb.getText().endsWith(" ") && tb.getText().length() < this.pred.length()) {
				this.pred = StringUtils.stripControlCodes(this.pred);
				tb.setText(this.pred.substring(0, this.pred.indexOf(" ", tb.getText().length())).replace(",", ""));
				tb.cursorPos += pred.length();
				tb.startSelect = tb.cursorPos;
				tb.endSelect = tb.cursorPos;
				break;
			}
			String part = "";
			for(int i = Math.min(tb.cursorPos-1, tb.getText().length()-1); i >= 0; i--) {
				if (tb.getText().charAt(i) == ' ' || i == 0) {
					part = tb.getText().substring(i+(i == 0?0:1), Math.min(tb.cursorPos, tb.getText().length()));
					break;
				}
			}
			String name = "";
			for(Object o : mc.player.connection.getPlayerInfoMap()) {
				NetworkPlayerInfo npi = (NetworkPlayerInfo)o;
				String n = npi.getGameProfile().getName();
				if (n.toLowerCase().startsWith(part.toLowerCase())) {
					name = n.substring(part.length(), n.length());
					break;
				}
			}
			tb.setText(ChatUtilities.insertAt(tb.getText(), name, tb.cursorPos));
			tb.cursorPos+= name.length();
			tb.startSelect = tb.cursorPos;
			tb.endSelect = tb.cursorPos;
			break;
		case Keyboard.KEY_UP:
			selectedHistory++;
			setText = true;
			break;
		case Keyboard.KEY_DOWN:
			selectedHistory--;
			setText = true;
			break;
		case Keyboard.KEY_RETURN:
			if (interactables.get(0).active) {
				if (tb.text.length() > 0) {
					mc.player.sendChatMessage(tb.text);
					ChatManager.userHistory.add(tb.text);
				}
				tb.text = "";
				mc.displayGuiScreen(null);
			}
			break;
		case Keyboard.KEY_ESCAPE:
			mc.displayGuiScreen((GuiScreen)null);
			break;
		}
		
		selectedHistory = Math.min(Math.max(-1, selectedHistory), ChatManager.userHistory.size()-1);
		
		if (setText) {
			if (selectedHistory != -1) {
				tb.text = ChatManager.userHistory.get(ChatManager.userHistory.size()-1-selectedHistory);
				tb.cursorPos = tb.text.length();
				tb.startSelect = tb.cursorPos;
			} else {
				tb.text = "";
			}
		}
	}
	
	public void render(float opacity) {
		if (hoverComponent != null) {
			ChatManager.handleComponentHover(hoverComponent, Canvas.getCanvas().getUIHandler().mouse[0], Canvas.getCanvas().getUIHandler().mouse[1]);
		}
		super.render(opacity);
	}
	
	public static void renderHistory(float opacity) {
		opacity *= Minecraft.getMinecraft().gameSettings.chatOpacity;
		if (Minecraft.getMinecraft().gameSettings.chatVisibility != EnumChatVisibility.HIDDEN) {
			float oldFY = fY;
			fY+= dY;

			GuiHelper.drawRectangle(fX-(fW/2), fY-(fH/2), fX+(fW/2), fY+(fH/2)+(pad), Utilities.reAlpha(0xFF111111, 0.7f*opacity));
			//Draw.rect(fX-(fW/2), fY+(fH/2)+(pad), fX+(fW/2), fY+(fH/2)+(pad)+2, Util.reAlpha(ColorManager.getForeground(), 1f*opacity));
			GuiHelper.drawRectangle(fX-(fW/2), fY+(fH/2)+(pad), fX+(fW/2), fY+(fH/2)+(pad)+2+30, Utilities.reAlpha(Canvas.getCanvas().getColorSchemeHandler().getCurrentBackground(), 0.83f*opacity));

			GuiHelper.startClip(fX-(fW/2)+4, fY-(fH/2)+4, fX+(fW/2)+8, fY+(fH/2));
			sl.render(opacity);
			GuiHelper.endClip();
			
			fY = oldFY;
		}
	}
}
