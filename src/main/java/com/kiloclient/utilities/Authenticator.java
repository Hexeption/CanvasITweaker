package com.kiloclient.utilities;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.util.Session;

import java.net.Proxy;

public class Authenticator implements IMinecraft {

    private final YggdrasilUserAuthentication authentication;

    public Authenticator() {

        this.authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
    }

    public static Authenticator instance() {

        return new Authenticator();
    }

    public final Authenticator username(String username) {

        authentication.setUsername(username);
        return this;
    }

    public final Authenticator password(String password) {

        authentication.setPassword(password);
        return this;
    }

    public final boolean login() {
        Session session = getSession();
        if(session == null){
            return false;
        }

        mixinMC.setSession(session);
        return true;
    }

    private final Session getSession() {
        try{
            authentication.logIn();
            GameProfile profile = authentication.getSelectedProfile();
            return new Session(profile.getName(), profile.getId().toString(), authentication.getAuthenticatedToken(), "MOJANG");
        }catch (AuthenticationException e){
            e.printStackTrace();
            return null;
        }
    }


}
