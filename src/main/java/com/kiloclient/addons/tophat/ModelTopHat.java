package com.kiloclient.addons.tophat;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelTopHat {
	
	public static class ModelTopHatWhite extends ModelBase {
		private ModelRenderer hat;
		
		public ModelTopHatWhite() {
			float offset = 5F;
			int brimHeight = 1;
			hat = new ModelRenderer(this, 0, 0);
			hat.textureWidth = 16;
			hat.textureHeight = 16;
			hat.addBox(-4.5F, offset + brimHeight, -4.5F, 9, brimHeight, 9, 0.0F);
		}
		
		public void render(float rotationYAngle, float rotationXAngle){
			hat.rotateAngleY = rotationYAngle;
			hat.rotateAngleX = rotationXAngle + (float)Math.PI;
			
			
			hat.render(1F / 15F);
		}
	}
	
	public static class ModelTopHatBlack extends ModelBase {
		private ModelRenderer hat;
		
		public ModelTopHatBlack() {
			float offset = 5f;
			int brimHeight = 1;
			hat = new ModelRenderer(this, 0, 0);
			hat.textureWidth = 16;
			hat.textureHeight = 16;
			hat.addBox(-4F, offset + brimHeight, -4F, 8, 9, 8, 0F);
			hat.addBox(-6.5F, offset, -6.5F, 13, brimHeight, 13, 0F);
		}
		
		public void render(float rotationYAngle, float rotationXAngle){
			hat.rotateAngleY = rotationYAngle;
			hat.rotateAngleX = rotationXAngle + (float)Math.PI;
			
			
			hat.render(1F / 15F);
		}
	}
	
}
