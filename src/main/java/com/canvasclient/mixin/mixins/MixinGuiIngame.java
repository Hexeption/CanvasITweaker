package com.canvasclient.mixin.mixins;

import com.canvasclient.Canvas;
import com.canvasclient.event.boot.EventStartup;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "setDefaultTitlesTimes", at = @At(value = "HEAD"))
    private void init(CallbackInfo callbackInfo) {

        Canvas.getCanvas().getEventManager().call(EventStartup.get());
    }
}
