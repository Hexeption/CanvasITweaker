package com.kiloclient.addons.sunglasses;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelSunglasses extends ModelBase {
	
	private ModelRenderer front;
	private ModelRenderer front2;
	private ModelRenderer bridge;
	private ModelRenderer right;
	private ModelRenderer left;
	private ModelRenderer rightBridge;
	private ModelRenderer leftBridge;
	
	private ModelBiped modelBiped;

	public ModelSunglasses(ModelBiped modelBiped) {
		this.modelBiped = modelBiped;
		
		front = new ModelRenderer(modelBiped, 0, 0);
		front.addBox(-3F, -4F, -5F, 2, 2, 1);
		front2 = new ModelRenderer(modelBiped, 6, 0);
		front2.addBox(1F, -4F, -5F, 2, 2, 1);
		bridge = new ModelRenderer(modelBiped, 0, 4);
		bridge.addBox(-1F, -4F, -5F, 2, 1, 1);
		right = new ModelRenderer(modelBiped, 0, 6);
		right.addBox(4.0F, -4.0F, -4.0F, 1, 1, 4);
		left = new ModelRenderer(modelBiped, 12, 0);
		left.addBox(-5.0F, -4.0F, -4.0F, 1, 1, 4);
		leftBridge = new ModelRenderer(modelBiped, 6, 5);
		leftBridge.addBox(-5.0F, -4.0F, -5.0F, 2, 1, 1);
		rightBridge = new ModelRenderer(modelBiped, 6, 7);
		rightBridge.addBox(3.0F, -4.0F, -5.0F, 2, 1, 1);
	}
	
	
	public void render() {
		front.rotateAngleY = modelBiped.bipedHead.rotateAngleY;
		front.rotateAngleX = modelBiped.bipedHead.rotateAngleX;
		front.render(0.0625F);
		front2.rotateAngleY = modelBiped.bipedHead.rotateAngleY;
		front2.rotateAngleX = modelBiped.bipedHead.rotateAngleX;
		front2.render(0.0625F);
		bridge.rotateAngleY = modelBiped.bipedHead.rotateAngleY;
		bridge.rotateAngleX = modelBiped.bipedHead.rotateAngleX;
		bridge.render(0.0625F);
		right.rotateAngleY = modelBiped.bipedHead.rotateAngleY;
		right.rotateAngleX = modelBiped.bipedHead.rotateAngleX;
		right.render(0.0625F);
		left.rotateAngleY = modelBiped.bipedHead.rotateAngleY;
		left.rotateAngleX = modelBiped.bipedHead.rotateAngleX;
		left.render(0.0625F);
		leftBridge.rotateAngleY = modelBiped.bipedHead.rotateAngleY;
		leftBridge.rotateAngleX = modelBiped.bipedHead.rotateAngleX;
		leftBridge.render(0.0625F);
		rightBridge.rotateAngleY = modelBiped.bipedHead.rotateAngleY;
		rightBridge.rotateAngleX = modelBiped.bipedHead.rotateAngleX;
		rightBridge.render(0.0625F);
	}
	
}
