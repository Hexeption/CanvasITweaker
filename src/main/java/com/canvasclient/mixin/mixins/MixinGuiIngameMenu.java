package com.canvasclient.mixin.mixins;

import net.minecraft.client.gui.GuiIngameMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu {


    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

}
