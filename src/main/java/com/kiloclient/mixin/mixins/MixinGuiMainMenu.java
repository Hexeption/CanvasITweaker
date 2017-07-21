package com.kiloclient.mixin.mixins;

import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu {

    /**
     * @author
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }
}
