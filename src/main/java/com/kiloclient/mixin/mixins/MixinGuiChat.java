package com.kiloclient.mixin.mixins;

import com.kiloclient.mixin.imp.IMixinGuiChat;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiChat.class)
public class MixinGuiChat implements IMixinGuiChat {


    @Shadow private String defaultInputFieldText;

    @Override
    public String getDefaultInputFieldText() {
        return this.defaultInputFieldText;
    }
}
