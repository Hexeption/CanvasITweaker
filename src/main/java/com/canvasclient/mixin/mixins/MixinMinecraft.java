package com.canvasclient.mixin.mixins;

import com.canvasclient.Canvas;
import com.canvasclient.event.boot.EventShutdown;
import com.canvasclient.event.input.EventKeyPressed;
import com.canvasclient.event.ui.EventTick;
import com.canvasclient.mixin.imp.IMixinMinecraft;
import com.canvasclient.utilities.UIUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MixinMinecraft implements IMixinMinecraft {


    @Shadow
    @Nullable
    public GuiScreen currentScreen;

    @Mutable
    @Shadow
    @Final
    private Session session;

    @Shadow public int displayWidth;

    @Shadow public int displayHeight;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setGameSize(CallbackInfo callbackInfo) {
        this.displayWidth = 1280;
        this.displayHeight = 720;
    }


    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;runGameLoop()V"))
    private void gameLoop(CallbackInfo callbackInfo) {

        Canvas.getCanvas().getEventManager().call(EventTick.get());
    }

    @Inject(method = "displayGuiScreen", at = @At(value = "RETURN"))
    private void changeUI(CallbackInfo callbackInfo) {

        if (Canvas.getCanvas().getUIHandler().getCurrentUI() != null) {
            Canvas.getCanvas().getUIHandler().changeUI();
        } else {
            Canvas.getCanvas().getUIHandler().changeUI(UIUtilities.newUI(currentScreen));
        }
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;unloadSounds()V"))
    private void shutdownMinecraft(CallbackInfo callbackInfo) {

        Canvas.getCanvas().getEventManager().call(EventShutdown.get());
    }

    @Inject(method = "resize", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;onResize(Lnet/minecraft/client/Minecraft;II)V"))
    private void resize(CallbackInfo callbackInfo) {

        Canvas.getCanvas().getUIHandler().onWindowResize();
    }

    @Inject(method = "runTickKeyboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;setKeyBindState(IZ)V"))
    private void keyPress(CallbackInfo callbackInfo) {

        Canvas.getCanvas().getEventManager().call(EventKeyPressed.get(Keyboard.getEventKey()));
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void loadWorld(CallbackInfo callbackInfo) {

        Canvas.getCanvas().getPlayerHandler().playerMap.clear();
    }

    @Override
    public Session getSession() {

        return session;
    }

    @Override
    public void setSession(Session session) {

        this.session = session;
    }
}
