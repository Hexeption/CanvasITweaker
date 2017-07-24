package com.canvasclient.addons.bracelet;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelBracelet extends ModelBase {
	
	private ModelRenderer bipedFrontBracelet;
	private ModelRenderer bipedLeftBracelet;
	private ModelRenderer bipedBackBracelet;
	private ModelRenderer bipedRightBracelet;
	
	private ModelBiped modelPlayer; 
	
	public ModelBracelet(ModelBiped modelPlayer) {
		this.modelPlayer = modelPlayer;
		
		bipedFrontBracelet = new ModelRenderer(modelPlayer, 0, 0);
		bipedFrontBracelet.addBox(4F, 9F, -3F, 4, 1, 1);
		bipedLeftBracelet = new ModelRenderer(modelPlayer, 10, 0);
		bipedLeftBracelet.addBox(3F, 9F, -3F, 1, 1, 6);
		bipedBackBracelet = new ModelRenderer(modelPlayer, 0, 2);
		bipedBackBracelet.addBox(4F, 9F, 2F, 4, 1, 1);
		bipedBackBracelet.setRotationPoint(0F, 0F, 0F);
		bipedRightBracelet = new ModelRenderer(modelPlayer, 0, 4);
		bipedRightBracelet.addBox(8F, 9F, -3F, 1, 1, 6);
		bipedRightBracelet.setRotationPoint(0F, 0F, 0F);
	}
	
	public void render() {
		bipedFrontBracelet.rotateAngleY = this.modelPlayer.bipedLeftArm.rotateAngleY;
		bipedFrontBracelet.rotateAngleX = this.modelPlayer.bipedLeftArm.rotateAngleX;
		bipedFrontBracelet.rotationPointX = 0.0F;
		bipedFrontBracelet.rotationPointY = 0.0F;
		bipedFrontBracelet.render(0.0625F);
		bipedLeftBracelet.rotateAngleY = this.modelPlayer.bipedLeftArm.rotateAngleY;
		bipedLeftBracelet.rotateAngleX = this.modelPlayer.bipedLeftArm.rotateAngleX;
		bipedLeftBracelet.rotationPointX = 0.0F;
		bipedLeftBracelet.rotationPointY = 0.0F;
		bipedLeftBracelet.render(0.0625F);
		bipedBackBracelet.rotateAngleY = this.modelPlayer.bipedLeftArm.rotateAngleY;
		bipedBackBracelet.rotateAngleX = this.modelPlayer.bipedLeftArm.rotateAngleX;
		bipedBackBracelet.rotationPointX = 0.0F;
		bipedBackBracelet.rotationPointY = 0.0F;
		bipedBackBracelet.render(0.0625F);
		bipedRightBracelet.rotateAngleY = this.modelPlayer.bipedLeftArm.rotateAngleY;
		bipedRightBracelet.rotateAngleX = this.modelPlayer.bipedLeftArm.rotateAngleX;
		bipedRightBracelet.rotationPointX = 0.0F;
		bipedRightBracelet.rotationPointY = 0.0F;
		bipedRightBracelet.render(0.0625F);
	}
	
}
