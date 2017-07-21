package com.kiloclient.utilities;

import com.kiloclient.mixin.imp.IMixinMinecraft;
import net.minecraft.client.Minecraft;

public interface IMC {
    Minecraft mc = Minecraft.getMinecraft();

    IMixinMinecraft mixinMC = (IMixinMinecraft) mc;
}
