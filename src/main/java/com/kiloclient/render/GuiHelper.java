package com.kiloclient.render;

import com.kiloclient.render.utilities.Align;
import com.kiloclient.render.utilities.ColorHelper;
import com.kiloclient.utilities.IMinecraft;
import com.kiloclient.utilities.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

public class GuiHelper implements IMinecraft {
	
	public static void startClip(float x1, float y1, float x2, float y2) {
		float temp;
		if (y1 > y2) {
			temp = y2;
			y2 = y1;
			y1 = temp;
		}
		
		GL11.glScissor((int)x1, (int)(Display.getHeight() - y2), (int)(x2 - x1), (int)(y2 - y1));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void endClip() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void drawRectangle(float left, float top, float right, float bottom, int color) {
		if (left < right)
		{
			float i = left;
			left = right;
			right = i;
		}

		if (top < bottom)
		{
			float j = top;
			top = bottom;
			bottom = j;
		}

		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(f, f1, f2, f3);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
		bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
		bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
		bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	public static void drawRectangleBorder(float x1, float y1, float x2, float y2, int outline) {
		drawRectangle(x1, y2 - 1, x2, y2, outline);
		drawRectangle(x1, y1, x2, y1 + 1, outline);
		drawRectangle(x1, y1, x1 + 1, y2, outline);
		drawRectangle(x2 - 1, y1, x2, y2, outline);
	}
	
    public static void drawGradientRectangle(float left, float top, float right, float bottom, int startColor, int endColor) {
		float f = (float)(startColor >> 24 & 255) / 255.0F;
		float f1 = (float)(startColor >> 16 & 255) / 255.0F;
		float f2 = (float)(startColor >> 8 & 255) / 255.0F;
		float f3 = (float)(startColor & 255) / 255.0F;
		float f4 = (float)(endColor >> 24 & 255) / 255.0F;
		float f5 = (float)(endColor >> 16 & 255) / 255.0F;
		float f6 = (float)(endColor >> 8 & 255) / 255.0F;
		float f7 = (float)(endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((double)right, (double)top, (double)0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos((double)left, (double)top, (double)0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos((double)left, (double)bottom, (double)0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos((double)right, (double)bottom, (double)0).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
    }
    
    public static void setupOverlayRendering()
    {
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, Display.getWidth(), Display.getHeight(), 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
    }
    
	public static void drawLine(float x1, float y1, float x2, float y2, int color, float width) {
		GlStateManager.color(0, 0, 0);
		GL11.glColor4f(0, 0, 0, 0);
		GL11.glLineWidth(width);
		
        float var11 = (float)(color >> 24 & 255) / 255.0F;
        float var6 = (float)(color >> 16 & 255) / 255.0F;
        float var7 = (float)(color >> 8 & 255) / 255.0F;
        float var8 = (float)(color & 255) / 255.0F;
        

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBegin(GL11.GL_LINE);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x2, y2);
		GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}
	
	public static void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, int fill) {
		GlStateManager.color(0, 0, 0);
		GL11.glColor4f(0, 0, 0, 0);
		
        float var11 = (float)(fill >> 24 & 255) / 255.0F;
        float var6 = (float)(fill >> 16 & 255) / 255.0F;
        float var7 = (float)(fill >> 8 & 255) / 255.0F;
        float var8 = (float)(fill & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x3, y3);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}
	
	public static void drawCircle(float x, float y, float radius, int fill) {
		drawArc(x, y, 0, 360, radius-1, fill);
	}
	
	public static void drawEllipse(float x, float y, float w, float h, int fill) {
		drawArcEllipse(x, y, 0, 360, w-1, h-1, fill);
	}
	
	public static void drawPoint(float x, float y, float size, int fill) {
		GlStateManager.color(0, 0, 0);
		GL11.glColor4f(0, 0, 0, 0);
		
		float var11 = (float)(fill >> 24 & 255) / 255.0F;
        float var6 = (float)(fill >> 16 & 255) / 255.0F;
        float var7 = (float)(fill >> 8 & 255) / 255.0F;
        float var8 = (float)(fill & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);

        GL11.glEnable(GL11.GL_POINT_SMOOTH);
        GL11.glPointSize(size);
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_POINT_SMOOTH);
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}
	
	public static void drawStringFromTTF(TrueTypeFont font, float x, float y, String text, int color) {
		drawStringFromTTF(font, x, y, text, color, Align.LEFT, Align.TOP, false);
	}
	
	public static void drawStringWithShadowFromTTF(TrueTypeFont font, float x, float y, String text, int color) {
		drawStringWithShadowFromTTF(font, x, y, text, color, Align.LEFT, Align.TOP);
	}
	
	public static void drawStringWithShadowFromTTF(TrueTypeFont font, float x, float y, String text, int color, Align hAlign, Align vAlign) {
		float opacity = Math.max((color >> 24 & 255)/255f, 0.05f);
		drawStringFromTTF(font, x+1, y+1, text, Utilities.reAlpha(0xFF000000, opacity), hAlign, vAlign, true);
		drawStringFromTTF(font, x, y, text, Utilities.reAlpha(color, opacity), hAlign, vAlign, false);
	}
	
	public static void drawStringFromTTF(TrueTypeFont font, float x, float y, String text, int color, Align hAlign, Align vAlign) {
		drawStringFromTTF(font, x, y, text, color, hAlign, vAlign, false);
	}
	
	public static void drawStringFromTTF(TrueTypeFont font, float x, float y, String text, int color, Align hAlign, Align vAlign, boolean shadow) {
		GlStateManager.color(0, 0, 0);
		GL11.glColor4f(0, 0, 0, 0);

		int offsetY = 0;
		int offsetX = 0;

		switch (hAlign) {
		case CENTER:
			offsetX = -font.getWidth(text)/2;
			break;
		case RIGHT:
			offsetX = -font.getWidth(text);
			break;
		default:
			break;
		}
		
		switch (vAlign) {
		case CENTER:
			offsetY = -font.getHeight(text)/2;
			break;
		case BOTTOM:
			offsetY = -font.getHeight(text);
			break;
		default:
			break;
		}

        mc.getTextureManager().bindTexture(new ResourceLocation("textures/font/ascii.png"));
        GlStateManager.enableBlend();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		TextureImpl.bindNone();
		font.drawString((int)Math.round(x+offsetX), (int)Math.round(y+offsetY), text, new Color(color), shadow);
        GlStateManager.disableBlend();
	}
	
	public static void drawArc(float x, float y, float start, float end, float radius, int color) {
		drawArcEllipse(x, y, start, end, radius, radius, color);
	}
	
	public static void drawArcEllipse(float x, float y, float start, float end, float w, float h, int color) {
		GlStateManager.color(0, 0, 0);
		GL11.glColor4f(0, 0, 0, 0);

		float temp = 0;
		if (start > end) {
			temp = end;
			end = start;
			start = temp;
		}
		
		float var11 = (float)(color >> 24 & 255) / 255.0F;
        float var6 = (float)(color >> 16 & 255) / 255.0F;
        float var7 = (float)(color >> 8 & 255) / 255.0F;
        float var8 = (float)(color & 255) / 255.0F;

        
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        
        if (var11 > 0.5F) {
        	GL11.glEnable(GL11.GL_LINE_SMOOTH);
        	GL11.glLineWidth(2f);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for(float i = end; i >= start; i-= (360/90)) {
            	float ldx = (float)Math.cos(i * Math.PI / 180) * (w * 1.001F);
            	float ldy = (float)Math.sin(i * Math.PI / 180)*(h*1.001f);
            	GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
        	GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for(float i = end; i >= start; i-= (360/90)) {
        	float ldx = (float)Math.cos(i * Math.PI / 180) * w;
        	float ldy = (float)Math.sin(i * Math.PI / 180) * h;
        	GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}
	
	public static void drawPolygon(float x, float y, float[] xPoints, float[] yPoints, int fill) {
		GlStateManager.color(0, 0, 0);
		GL11.glColor4f(0, 0, 0, 0);
		
        float var11 = (float)(fill >> 24 & 255) / 255.0F;
        float var6 = (float)(fill >> 16 & 255) / 255.0F;
        float var7 = (float)(fill >> 8 & 255) / 255.0F;
        float var8 = (float)(fill & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for(int i = xPoints.length - 1; i >= 0; i--) {
        	GL11.glVertex2f(x + xPoints[i], y + yPoints[i]);
        }
        GL11.glEnd();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}
	
	public static void drawTexturedRectangle(float x, float y, float w, float h, Texture texture, int color) {
		if (texture == null) {
			return;
		}
		GlStateManager.color(0, 0, 0);
		GL11.glColor4f(0, 0, 0, 0);

		x = Math.round(x);
		w = Math.round(w);
		y = Math.round(y);
		h = Math.round(h);
		
		float var11 = (float)(color >> 24 & 255) / 255.0F;
        float var6 = (float)(color >> 16 & 255) / 255.0F;
        float var7 = (float)(color >> 8 & 255) / 255.0F;
        float var8 = (float)(color & 255) / 255.0F;
        
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		texture.bind();
		
		float tw = (w / texture.getTextureWidth())/(w/texture.getImageWidth());
		float th = (h / texture.getTextureHeight())/(h/texture.getImageHeight());
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(0, th);
			GL11.glVertex2f(x, y + h);
			GL11.glTexCoord2f(tw, th);
			GL11.glVertex2f(x + w, y + h);
			GL11.glTexCoord2f(tw, 0);
			GL11.glVertex2f(x + w, y);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}
	
	public static void drawTexturedRectangle(float x, float y, float w, float h, Texture texture, float opacity) {
		drawTexturedRectangle(x, y, w, h, texture, Utilities.reAlpha(ColorHelper.WHITE.getColorCode(), opacity));
	}
	
	public static void drawTexturedRectangle(float x, float y, float w, float h, Texture texture) {
		drawTexturedRectangle(x, y, w, h, texture, -1);
	}
	
	public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_, int color)
    {
		float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        
        GlStateManager.color(r, g, b, a);
        
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_147046_0_, (float)p_147046_1_, 50.0F);
        GlStateManager.scale((float)(-p_147046_2_), (float)p_147046_2_, (float)p_147046_2_);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(0 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        p_147046_5_.renderYawOffset = (float)Math.atan((double)(0 / 40.0F)) * 20.0F;
        p_147046_5_.rotationYaw = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 40.0F;
        p_147046_5_.rotationPitch = -((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
        var11.setPlayerViewY(180.0F);
        var11.setRenderShadow(false);
        var11.doRenderEntity(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, true);
        var11.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.disableDepth();
        GlStateManager.disableColorMaterial();
    }
	
	public static void drawRoundedRectangle(float x, float y, float x1, float y1, int c) {
		drawRoundedRectangle(x, y, x1 - x, y1 - y, 3F, c);
	}
	
	public static void drawRoundedRectangle(float x, float y, float l, float w, float r, int c) {
		if(r < 0) {
			r = 0;
		}

		float a = (float)(c >> 24 & 0xff) / 255F;
		float r1 = (float)(c >> 16 & 0xff) / 255F;
		float g = (float)(c >> 8 & 0xff) / 255F;
		float b = (float)(c & 0xff) / 255F;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glColor4f(r1, g, b, a);


		GL11.glBegin(GL11.GL_TRIANGLE_FAN);

		GL11.glVertex2d(x + (l / 2), y + (w / 2)); // rectangle center

		for(int i = 0; i < 90; i += 6) {
			GL11.glVertex2d(x + l - r + (Math.sin((i * Math.PI / 180)) * r), y + w - r + (Math.cos((i * Math.PI / 180)) * r));
		}

		for(int i = 90; i < 180; i += 6) {
			GL11.glVertex2d(x + l - r + (Math.sin((i * Math.PI / 180)) * r), y + r + (Math.cos((i * Math.PI / 180)) * r));
		}

		for(int i = 180; i < 270; i += 6) {
			GL11.glVertex2d(x + r + (Math.sin((i * Math.PI / 180)) * r), y + r + (Math.cos((i * Math.PI / 180)) * r));
		}

		for(int i = 270; i < 360; i += 6) {
			GL11.glVertex2d(x + r + (Math.sin((i * Math.PI / 180)) * r), y + w - r + (Math.cos((i * Math.PI / 180)) * r));
		}

		GL11.glVertex2d(x + l - r, y + w);
		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
	
	public static void drawLoaderAnimation(float x, float y, float gap, int color) {
		float rot = (System.nanoTime()/5000000F) % 360F;
		for(int i = 0; i < 360; i+= 360/7f) {
			float xx = (float)Math.cos((i + rot) * Math.PI / 180) * gap;
			float yy = (float)Math.sin((i + rot) * Math.PI / 180) * gap;
			GuiHelper.drawCircle(x + xx, y + yy, gap / 4, color);
		}
	}
	
	public static String trimStringToWidth(TrueTypeFont font, String string, float width, boolean showDots) {
		String trim = string;
		boolean trimmed = false;
		while (font.getWidth(trim) > width) {
			trim = trim.substring(0, trim.length() - 1);
			trimmed = true;
		}
		
		if (trimmed && showDots) {
			boolean space = (trim.charAt(trim.length() - 1) == ' ');
		
			String dots = space ? ". . ." : " . . .";
			trim = trim.substring(0, trim.length() - 3) + dots;
		}
		return trim;
	}
}
