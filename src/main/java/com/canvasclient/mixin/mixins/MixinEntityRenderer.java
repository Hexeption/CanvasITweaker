package com.canvasclient.mixin.mixins;

import com.canvasclient.Canvas;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "updateCameraAndRender", at = @At("RETURN"))
    private void render2D(CallbackInfo callbackInfo) {
        GlStateManager.pushMatrix();
        Canvas.getCanvas().getUIHandler().render2D();
        GlStateManager.popMatrix();
    }

}
