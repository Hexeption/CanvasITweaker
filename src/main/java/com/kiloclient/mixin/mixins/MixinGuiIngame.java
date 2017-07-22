package com.kiloclient.mixin.mixins;

import com.kiloclient.KiLO;
import com.kiloclient.event.boot.EventStartup;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    // FIXME: 22/07/2017 
    @Inject(method = "setDefaultTitlesTimes", at = @At(value = "HEAD"))
    private void init(CallbackInfo callbackInfo) {

        KiLO.getKiLO().getEventManager().call(EventStartup.get());
    }
}
