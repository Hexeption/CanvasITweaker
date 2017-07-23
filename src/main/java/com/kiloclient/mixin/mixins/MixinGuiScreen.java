package com.kiloclient.mixin.mixins;

import com.kiloclient.mixin.imp.IMixinGuiScreen;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.URI;

@Mixin(GuiScreen.class)
public class MixinGuiScreen implements IMixinGuiScreen {

    @Shadow private URI clickedLinkURI;

    @Override
    public URI getClickedLinkURI() {

        return clickedLinkURI;
    }

    @Override
    public void setClickedLinkURI(URI uri) {
        this.clickedLinkURI = uri;
    }


}
