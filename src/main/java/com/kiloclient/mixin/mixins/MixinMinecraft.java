package com.kiloclient.mixin.mixins;

import com.kiloclient.KiLO;
import com.kiloclient.event.boot.EventShutdown;
import com.kiloclient.event.boot.EventStartup;
import com.kiloclient.event.input.EventKeyPressed;
import com.kiloclient.event.ui.EventTick;
import com.kiloclient.mixin.imp.IMixinMinecraft;
import com.kiloclient.utilities.UIUtilities;
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

    @Shadow
    @Final
    @Mutable
    private Session session;

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo callbackInfo) {

        KiLO.getKiLO().getEventManager().call(EventStartup.get());
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;runGameLoop()V"))
    private void gameLoop(CallbackInfo callbackInfo) {

        KiLO.getKiLO().getEventManager().call(EventTick.get());
    }

    @Inject(method = "displayGuiScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;"))
    private void changeUI(CallbackInfo callbackInfo) {

        if (KiLO.getKiLO().getUIHandler().getCurrentUI() != null) {
            KiLO.getKiLO().getUIHandler().changeUI();
        } else {
            KiLO.getKiLO().getUIHandler().changeUI(UIUtilities.newUI(currentScreen));
        }
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;unloadSounds()V"))
    private void shutdownMinecraft(CallbackInfo callbackInfo) {

        KiLO.getKiLO().getEventManager().call(EventShutdown.get());
    }

    @Inject(method = "resize", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;onResize(Lnet/minecraft/client/Minecraft;II)V"))
    private void resize(CallbackInfo callbackInfo) {

        KiLO.getKiLO().getUIHandler().onWindowResize();
    }

    @Inject(method = "runTickKeyboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;setKeyBindState(IZ)V"))
    private void keyPress(CallbackInfo callbackInfo) {

        KiLO.getKiLO().getEventManager().call(EventKeyPressed.get(Keyboard.getEventKey()));
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void loadWorld(CallbackInfo callbackInfo) {

        KiLO.getKiLO().getPlayerHandler().playerMap.clear();
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
