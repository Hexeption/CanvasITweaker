package com.kiloclient.mixin.mixins;

import com.kiloclient.infrastructure.ChatManager;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {

    /**
     * @author Hexeption
     */
    @Overwrite
    public void drawChat(int updateCounter) {


    }

    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"))
    private void addChat(ITextComponent chatComponent, int chatLineId, CallbackInfo callbackInfo) {

        ChatManager.addChatLine(new TextComponentString(chatComponent.getFormattedText()));
    }
}
