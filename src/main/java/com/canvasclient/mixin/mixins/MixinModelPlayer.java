package com.canvasclient.mixin.mixins;

import com.canvasclient.Canvas;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPlayer.class)
public class MixinModelPlayer {

    @Inject(method = "render", at = @At("RETURN"))
    private void renderAddons(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo callbackInfo) {

        GlStateManager.pushMatrix();

        if ( Canvas.getCanvas().getAddonRenderer().modelBipedMain == null) {
            Canvas.getCanvas().getAddonRenderer().init((ModelPlayer)(Object) this);
        }
        Canvas.getCanvas().getAddonRenderer().render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);


        GL11.glPopMatrix();
    }

}
