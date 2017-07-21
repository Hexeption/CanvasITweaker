package com.kiloclient.addons.wings;

import com.kiloclient.resource.ResourceHelper;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class WingsRenderer extends ModelBiped {
	
	private ModelWings modelWings;
	
	public WingsRenderer(ModelBiped modelBiped){
		this.modelWings = new ModelWings(modelBiped);
	}
	
	@Override 
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		GlStateManager.bindTexture(ResourceHelper.addonWhite.getTextureID());
		this.modelWings.render();
    }
	
}
