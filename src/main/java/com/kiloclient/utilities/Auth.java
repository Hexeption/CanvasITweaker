package com.kiloclient.utilities;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.util.Session;

import java.net.Proxy;

public class Auth implements IMC {

    private final YggdrasilUserAuthentication authentication;

    public Auth() {

        this.authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
    }

    public static Auth instance() {

        return new Auth();
    }

    public final Auth username(String username) {

        authentication.setUsername(username);
        return this;
    }

    public final Auth password(String password) {

        authentication.setPassword(password);
        return this;
    }

    public final boolean login() {
        Session session = session();
        if(session == null){
            return false;
        }

        mixinMC.setSession(session);
        return true;
    }

    private final Session session() {
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
