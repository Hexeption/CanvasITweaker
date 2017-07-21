package com.kiloclient.mixin.mixins;

import net.minecraft.client.gui.GuiWorldSelection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GuiWorldSelection.class)
public class MixinGuiWorldSelection {

    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }
}
