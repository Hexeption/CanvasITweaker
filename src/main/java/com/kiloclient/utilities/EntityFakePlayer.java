package com.kiloclient.utilities;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class EntityFakePlayer extends AbstractClientPlayer {

    String name;

    public EntityFakePlayer() {

        this(getDefaultFakePlayer());
    }

    public EntityFakePlayer(String name) {

        super(null, new GameProfile(new UUID(0, 0), name));
        this.name = name;
        this.getDownloadImageSkin(this.getLocationSkin(), name);
    }

    public static String getDefaultFakePlayer() {

        return "Hexeption";
    }

    public double getDistanceSqToEntity(Entity entityIn) {

        return 0;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public float getBrightness() {

        return 255;
    }

    @Override
    public int getBrightnessForRender() {

        return 255;
    }


    @Override
    public boolean isGlowing() {

        return false;
    }

    @Override
    public String getSkinType() {

        return "default";
    }

    @Override
    public boolean isSpectator() {

        return false;
    }

    @Override
    public boolean hasSkin() {

        return true;
    }

    @Override
    public ResourceLocation getLocationSkin() {

        return new ResourceLocation("skin/" + name);
    }

    @Override
    public NetworkPlayerInfo getPlayerInfo() {

        return new NetworkPlayerInfo(new GameProfile(new UUID(0, 0), name));
    }


    @Override
    public Scoreboard getWorldScoreboard() {

        return new Scoreboard();
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {

        return new ItemStack(Blocks.AIR);
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void setDead() {

    }

    @Override
    public boolean canBePushed() {

        return false;
    }
}