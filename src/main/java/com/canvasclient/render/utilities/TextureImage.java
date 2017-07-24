package com.canvasclient.render.utilities;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.ByteArrayInputStream;

public class TextureImage {

	public byte[] pixels;
	public Texture texture;
	public String location;
	
	public Texture getTexture() {
    	if (texture == null) {
    		if (pixels != null) {
    			try {
    				ByteArrayInputStream bias = new ByteArrayInputStream(pixels);
    				texture = TextureLoader.getTexture("PNG", bias);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    	}
		return texture;
	}
}
