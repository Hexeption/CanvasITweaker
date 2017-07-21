package com.kiloclient.mixin.mixins;

import com.kiloclient.mixin.imp.IMixinGuiConnecting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting implements IMixinGuiConnecting {
    @Shadow
    private NetworkManager networkManager;

    @Shadow
    @Final
    private GuiScreen previousGuiScreen;

    public String ip;

    public int port;

//    @Inject(method = "<init>(Lnet/minecraft/client/gui/GuiScreen;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/multiplayer/ServerData;)V", at = @At("HEAD"))
//    private void GuiConnecting(GuiScreen parent, Minecraft mcIn, ServerData serverDataIn, CallbackInfo callbackInfo) {
//        ServerAddress serveraddress = ServerAddress.fromString(serverDataIn.serverIP);
//        ip = serveraddress.getIP();
//        port = serveraddress.getPort();
//    }
//
//    @Inject(method = "<init>(Lnet/minecraft/client/gui/GuiScreen;Lnet/minecraft/client/Minecraft;Ljava/lang/String;I)V", at = @At("HEAD"))
//    private void GuiConnecting(GuiScreen parent, Minecraft mcIn, String hostName, int port, CallbackInfo callbackInfo) {
//        ip = hostName;
//        this.port = port;
//    }

    @Override
    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }

    @Override
    public GuiScreen getpreviousGuiScreen() {
        return this.previousGuiScreen;
    }

    @Override
    public String getIP() {
        return ip;
    }

    @Override
    public int getPort() {
        return port;
    }
}
