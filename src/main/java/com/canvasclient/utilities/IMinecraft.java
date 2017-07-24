package com.canvasclient.utilities;

import com.canvasclient.mixin.imp.IMixinMinecraft;
import net.minecraft.client.Minecraft;

public interface IMinecraft {
    Minecraft mc = Minecraft.getMinecraft();

    IMixinMinecraft mixinMC = (IMixinMinecraft) mc;

}
