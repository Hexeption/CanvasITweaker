package com.canvasclient.mixin.imp;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.NetworkManager;

public interface IMixinGuiConnecting {

    NetworkManager getNetworkManager();

    GuiScreen getpreviousGuiScreen();

    String getIP();

    int getPort();
}
