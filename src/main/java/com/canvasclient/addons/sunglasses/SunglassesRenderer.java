package com.canvasclient.addons.sunglasses;

import com.canvasclient.resource.ResourceHelper;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class SunglassesRenderer extends ModelBiped {
	
	private ModelSunglasses modelSunglasses;
	
	public SunglassesRenderer(ModelBiped modelBiped){
		this.modelSunglasses = new ModelSunglasses(modelBiped);
	}
	
	@Override 
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		GlStateManager.bindTexture(ResourceHelper.addonBlack.getTextureID());
		this.modelSunglasses.render();
    }
}
