package com.canvasclient.mixin.imp;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.NetworkManager;

public interface IMixinGuiConnecting {

    NetworkManager getNetworkManager();

    GuiScreen getPreviousGuiScreen();

    String getIP();

    int getPort();
}
