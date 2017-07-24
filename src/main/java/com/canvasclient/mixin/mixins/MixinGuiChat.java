package com.canvasclient.mixin.mixins;

import com.canvasclient.mixin.imp.IMixinGuiChat;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiChat.class)
public class MixinGuiChat implements IMixinGuiChat {


    @Shadow
    protected GuiTextField inputField;

    @Shadow
    private String defaultInputFieldText;

    @Override
    public String getDefaultInputFieldText() {

        return this.defaultInputFieldText;
    }

    /**
     * @author Hexeption
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }
}
