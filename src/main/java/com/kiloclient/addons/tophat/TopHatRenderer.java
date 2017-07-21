package com.kiloclient.addons.tophat;

import com.kiloclient.resource.ResourceHelper;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class TopHatRenderer extends ModelBiped {
	
	ModelTopHat.ModelTopHatBlack hatBlack;
	ModelTopHat.ModelTopHatWhite hatBand;
	
	public TopHatRenderer(){
		hatBlack = new ModelTopHat.ModelTopHatBlack();
		hatBand = new ModelTopHat.ModelTopHatWhite();
	}
	
	@Override 
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		
		float yRotation = par5 / (180F / (float)Math.PI);
		float xRotation = par6 / (180F / (float)Math.PI);
		
		GlStateManager.bindTexture(ResourceHelper.addonBlack.getTextureID());
		hatBlack.render(yRotation, xRotation);
		GlStateManager.bindTexture(ResourceHelper.addonWhite.getTextureID());
		hatBand.render(yRotation, xRotation);
    }
	
}
