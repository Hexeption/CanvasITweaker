package com.kiloclient.ui;

import com.kiloclient.KiLO;
import com.kiloclient.event.EventManager;
import com.kiloclient.event.base.Listener;
import com.kiloclient.event.input.EventKeyPressed;
import com.kiloclient.event.ui.EventTick;
import com.kiloclient.manager.NotificationManager;
import com.kiloclient.render.FontHandler;
import com.kiloclient.render.GuiHelper;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.resource.ResourceHelper;
import com.kiloclient.utilities.UIUtilities;
import com.kiloclient.utilities.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

public class UIHandler {
	public final int[] mouse = new int[2];
	private final int color = Utilities.blendColor(ColorHelper.WHITE.getColorCode(), ColorHelper.GREY.getColorCode(), 0.75f);

	private float uiFade = 1f;
	public boolean uiFadeIn;
	
	private UI currentUI, uiTo;
	
	public UIHandler() {
		EventManager.get().register(new Listener<EventKeyPressed>(){
			@Override
			public void call(EventKeyPressed event) {
				if (event.key == Keyboard.KEY_NONE) { 
					return; 
				}
				
				if (event.key == Keyboard.KEY_M) {
					Minecraft.getMinecraft().displayGuiScreen(new GuiGrabber());
					KiLO.getKiLO().getUIHandler().changeUI(new UIMusic(true));
					return;
				}
			}
		});
		EventManager.get().register(new Listener<EventTick>() {
			@Override
			public void call(EventTick event) {
				if (currentUI != null) {
					if (Keyboard.isCreated()) {
						Keyboard.enableRepeatEvents(true);
						while (Keyboard.next()) {
							if (Keyboard.getEventKeyState()) {
								KiLO.getKiLO().getUIHandler().onKeyPressed(Keyboard.getEventKey());
								KiLO.getKiLO().getUIHandler().onKeyTyped(Keyboard.getEventKey(), Keyboard.getEventCharacter());
							} else {
								KiLO.getKiLO().getUIHandler().onKeyRelease(Keyboard.getEventKey());
							}
						}
						Keyboard.enableRepeatEvents(false);
					}
					if (Mouse.isCreated()) {
						while (Mouse.next()) {
							if (Mouse.getEventButton() == -1) {
								if (Mouse.getEventDWheel() != 0)
									KiLO.getKiLO().getUIHandler().onMouseScroll(Mouse.getEventDWheel());
								mouse[0] = Mouse.getEventX();
								mouse[1] = Display.getHeight()-Mouse.getEventY();
							} else {
								if (Mouse.getEventButtonState())
									KiLO.getKiLO().getUIHandler().onMouseClick(mouse[0], mouse[1], Mouse.getEventButton());
								else
									KiLO.getKiLO().getUIHandler().onMouseRelease(mouse[0], mouse[1], Mouse.getEventButton());
							}
						}
					}
					currentUI.update(mouse[0], mouse[1]);
				}
				uiFade += uiFadeIn?((currentUI instanceof UIChat)?1f:0.2f):((uiTo instanceof UIChat)?-1f:-0.2f);//((uiFadeIn?1:0)-uiFade)/4f;
				uiFade = Math.min(Math.max(0f, uiFade), 1f);
				if (uiFadeIn) {
					if (uiFade > 0.95f) {
						currentUI = uiTo;
						uiFadeIn = false;
						if (currentUI != null) {
							currentUI.init();
						}
					}
				}
				
				if (Minecraft.getMinecraft().world != null) {
					UIChat.updateHistory(mouse[0], mouse[1]);
				}
			}
		});
	}
	
	public void render2D() {
		ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());

		float scale = scaledRes.getScaleFactor() / (float)Math.pow(scaledRes.getScaleFactor(), 2);
		GL11.glScalef(scale, scale, scale);

		GlStateManager.disableDepth();
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, 1000);

		if (Minecraft.getMinecraft().world != null && !Minecraft.getMinecraft().gameSettings.hideGUI) {
			int yPos = 1;
			int right = 0;
			int maxWidth = 0;
			final TrueTypeFont font = FontHandler.ROUNDED_BOLD.get(12);

			UIChat.renderHistory(1f);
		}
		
		if (currentUI != null) {
			if (!(currentUI instanceof UIChat) &&
					!(currentUI instanceof UIHistory) &&
					!(currentUI instanceof UIInGameMenu) &&
					!(currentUI instanceof UIInGameMenuQueue) &&
					!(currentUI instanceof UIInGameMenuFriend) &&
					!(currentUI instanceof UIInGameMenuPartyChat) &&
					!(currentUI instanceof UIMusic) &&
					!(currentUI instanceof UISleep)) {
				UI.drawDefaultBackground(UIUtilities.shouldDrawBranding(currentUI), uiTo!=null?1f:1f-Math.min(uiFade, 0.95f));
			}
			currentUI.render(1f-Math.min(uiFade, 0.95f));
		}
		
		if (Minecraft.getMinecraft().world != null && !Minecraft.getMinecraft().gameSettings.hideGUI) {
			NotificationManager.render(1f);
			
			if (Minecraft.getMinecraft().world != null && !Minecraft.getMinecraft().gameSettings.showDebugInfo) {
				GuiHelper.drawTexturedRectangle(16, 12, 79, 40, ResourceHelper.brandingSmall, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), 0.8f));
				GuiHelper.drawStringFromTTF(FontHandler.STANDARD.get(14), 92, 38, "", 0xFFFFFF);
			}
		}
		Display.setTitle(KiLO.getKiLO().getName() + " " + KiLO.getKiLO().getVersion() + " (" + Minecraft.getMinecraft().getDebugFPS() + ")");
		
		GlStateManager.popMatrix();
		GlStateManager.enableDepth();

		try {
			Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
		} catch (Exception e) {}
	}
	
	public void onWindowResize() {
		if (this.getCurrentUI() != null)
			this.getCurrentUI().init();
	}
	
	public void onMouseClick(int i, int j, int k) {
		if (uiFadeIn)
			return;
		currentUI.mouseClick(i, j, k);
	}
	
	public void onMouseRelease(int i, int j, int k) {
		if (uiFadeIn)
			return; 
		currentUI.mouseRelease(i, j, k);
	}
	
	public void onMouseScroll(int delta) {
		if (uiFadeIn)
			return;
		currentUI.mouseScroll(delta);
	}
	
	public void onKeyTyped(int keyCode, char typedChar) {
		currentUI.keyTyped(keyCode, typedChar);
	}
	
	public void onKeyPressed(int keyCode) {
		if (uiFadeIn)
			return;
		currentUI.keyboardPress(keyCode);
	}
	
	public void onKeyRelease(int keyCode) {
		if (uiFadeIn)
			return;
		currentUI.keyboardRelease(keyCode);
	}
	
	public UI getCurrentUI() {
		return this.currentUI;
	}
	
	public UI getUITo() {
		return this.uiTo;
	}
 	
	public void changeUI() {
		uiFadeIn = true;
		uiTo = UIUtilities.newUI(Minecraft.getMinecraft().currentScreen);
	}
	
	public void changeUI(UI ui) {
		uiFadeIn = true;
		uiTo = ui;
	}
	
	public void setCurrentUI(UI ui) {
		this.currentUI = ui;
	}
	
	public void keyTyped(int key, char keyChar) {
		currentUI.keyTyped(key, keyChar);
	}
	
}
