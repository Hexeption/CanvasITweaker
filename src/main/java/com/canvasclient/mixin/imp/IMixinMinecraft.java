package com.canvasclient.mixin.imp;

import net.minecraft.util.Session;

public interface IMixinMinecraft {

    Session getSession();

    void setSession(Session session);

}
