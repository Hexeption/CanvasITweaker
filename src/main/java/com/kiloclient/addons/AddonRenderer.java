package com.kiloclient.addons;

import com.kiloclient.addons.bracelet.BraceletRenderer;
import com.kiloclient.addons.sunglasses.SunglassesRenderer;
import com.kiloclient.addons.tophat.TopHatRenderer;
import com.kiloclient.addons.wings.WingsRenderer;
import com.kiloclient.users.User;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class AddonRenderer {
	
	public ModelBiped modelBipedMain;
	
	public void init(ModelBiped modelBipedMain) {
		this.modelBipedMain = modelBipedMain;
	}
	
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		EntityPlayer entityPlayer;
		if (par1Entity instanceof EntityPlayer) {
			entityPlayer = (EntityPlayer)par1Entity;
		} else {
			return;
		}
		User currentPlayer = AddonManager.users.get(entityPlayer.getDisplayName().getUnformattedText());
		if (currentPlayer == null)
			return;
		
		if (currentPlayer.topHatEnabled) {
			TopHatRenderer topHatRenderer = new TopHatRenderer();
			topHatRenderer.render(par1Entity, par2, par3, par4, par5, par6, par7);
		}
			
		if (currentPlayer.sunglassesEnabled) {
	        SunglassesRenderer sunglassesRenderer = new SunglassesRenderer(this.modelBipedMain);
	        sunglassesRenderer.render(par1Entity, par2, par3, par4, par5, par6, par7);
		}
		
		if (currentPlayer.braceletEnabled) {
	        BraceletRenderer braceletRenderer = new BraceletRenderer(this.modelBipedMain);
	        braceletRenderer.render(par1Entity, par2, par3, par4, par5, par6, par7);
		}
		if (currentPlayer.wingsEnabled) {
	        WingsRenderer wingsRenderer = new WingsRenderer(this.modelBipedMain);
	        wingsRenderer.render(par1Entity, par2, par3, par4, par5, par6, par7);
		}
	}
	
}
