package com.canvasclient.addons.bracelet;

import com.canvasclient.resource.ResourceHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class BraceletRenderer extends ModelBiped {
	
	private ModelBracelet braceletModel;
	
	public BraceletRenderer(ModelBiped modelBiped){
		braceletModel = new ModelBracelet(modelBiped);
	}
	
	@Override 
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {		
		GlStateManager.bindTexture(ResourceHelper.addonBlack.getTextureID());
		braceletModel.render();
    }
}
