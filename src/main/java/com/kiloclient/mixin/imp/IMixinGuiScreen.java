package com.kiloclient.mixin.imp;

import java.net.URI;

public interface IMixinGuiScreen {

    URI getClickedLinkURI();

    void setClickedLinkURI(URI uri);

}
