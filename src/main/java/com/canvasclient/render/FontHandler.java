package com.canvasclient.render;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;

import com.canvasclient.resource.ResourceHelper;
import org.newdawn.slick.TrueTypeFont;

public class FontHandler {
	
	private static final String FONT_STANDARD = "roboto";
	private static final String FONT_ROUNDED = "comfortaa";
	
	private static final TrueTypeFont STANDARD_12 = loadTTF(FONT_STANDARD + "-regular.ttf", Font.PLAIN, 12);
	private static final TrueTypeFont STANDARD_14 = loadTTF(FONT_STANDARD + "-regular.ttf", Font.PLAIN, 14);
	private static final TrueTypeFont STANDARD_20 = loadTTF(FONT_STANDARD + "-regular.ttf", Font.PLAIN, 20);
	private static final TrueTypeFont STANDARD_25 = loadTTF(FONT_STANDARD + "-regular.ttf", Font.PLAIN, 25);
	private static final TrueTypeFont STANDARD_40 = loadTTF(FONT_STANDARD + "-regular.ttf", Font.PLAIN, 40);
	public static final HashMap<String, TrueTypeFont> STANDARD = new HashMap() {{
		put(12, STANDARD_12);
		put(14, STANDARD_14);
		put(20, STANDARD_20);
		put(25, STANDARD_25);
		put(40, STANDARD_40);
	}};
	
	private static final TrueTypeFont ROUNDED_10 = loadTTF(FONT_ROUNDED + "-regular.ttf", Font.PLAIN, 10);
	private static final TrueTypeFont ROUNDED_12 = loadTTF(FONT_ROUNDED + "-regular.ttf", Font.PLAIN, 12);
	private static final TrueTypeFont ROUNDED_14 = loadTTF(FONT_ROUNDED + "-regular.ttf", Font.PLAIN, 14);
	private static final TrueTypeFont ROUNDED_16 = loadTTF(FONT_ROUNDED + "-regular.ttf", Font.PLAIN, 16);
	private static final TrueTypeFont ROUNDED_20 = loadTTF(FONT_ROUNDED + "-regular.ttf", Font.PLAIN, 20);
	private static final TrueTypeFont ROUNDED_25 = loadTTF(FONT_ROUNDED + "-regular.ttf", Font.PLAIN, 25);
	private static final TrueTypeFont ROUNDED_30 = loadTTF(FONT_ROUNDED + "-regular.ttf", Font.PLAIN, 30);
	private static final TrueTypeFont ROUNDED_40 = loadTTF(FONT_ROUNDED + "-regular.ttf", Font.PLAIN, 40);
	private static final TrueTypeFont ROUNDED_50 = loadTTF(FONT_ROUNDED + "-regular.ttf", Font.PLAIN, 50);
	public static final HashMap<String, TrueTypeFont> ROUNDED = new HashMap() {{
		put(10, ROUNDED_10);
		put(12, ROUNDED_12);
		put(14, ROUNDED_14);
		put(16, ROUNDED_16);
		put(20, ROUNDED_20);
		put(25, ROUNDED_25);
		put(30, ROUNDED_30);
		put(40, ROUNDED_40);
		put(50, ROUNDED_50);
	}};
	
	private static final TrueTypeFont ROUNDED_BOLD_10 = loadTTF(FONT_ROUNDED + "-bold.ttf", Font.PLAIN, 10);
	private static final TrueTypeFont ROUNDED_BOLD_12 = loadTTF(FONT_ROUNDED + "-bold.ttf", Font.PLAIN, 12);
	private static final TrueTypeFont ROUNDED_BOLD_14 = loadTTF(FONT_ROUNDED + "-bold.ttf", Font.PLAIN, 14);
	private static final TrueTypeFont ROUNDED_BOLD_16 = loadTTF(FONT_ROUNDED + "-bold.ttf", Font.PLAIN, 16);
	private static final TrueTypeFont ROUNDED_BOLD_20 = loadTTF(FONT_ROUNDED + "-bold.ttf", Font.PLAIN, 20);
	private static final TrueTypeFont ROUNDED_BOLD_25 = loadTTF(FONT_ROUNDED + "-bold.ttf", Font.PLAIN, 25);
	private static final TrueTypeFont ROUNDED_BOLD_30 = loadTTF(FONT_ROUNDED + "-bold.ttf", Font.PLAIN, 30);
	private static final TrueTypeFont ROUNDED_BOLD_40 = loadTTF(FONT_ROUNDED + "-bold.ttf", Font.PLAIN, 40);
	private static final TrueTypeFont ROUNDED_BOLD_50 = loadTTF(FONT_ROUNDED + "-bold.ttf", Font.PLAIN, 50);
	public static final HashMap<String, TrueTypeFont> ROUNDED_BOLD = new HashMap() {{
		put(10, ROUNDED_BOLD_10);
		put(12, ROUNDED_BOLD_12);
		put(14, ROUNDED_BOLD_14);
		put(16, ROUNDED_BOLD_16);
		put(20, ROUNDED_BOLD_20);
		put(25, ROUNDED_BOLD_25);
		put(30, ROUNDED_BOLD_30);
		put(40, ROUNDED_BOLD_40);
		put(50, ROUNDED_BOLD_50);
	}};
	
	private static final TrueTypeFont ROUNDED_LIGHT_12 = loadTTF(FONT_ROUNDED + "-light.ttf", Font.PLAIN, 12);
	private static final TrueTypeFont ROUNDED_LIGHT_16 = loadTTF(FONT_ROUNDED + "-light.ttf", Font.PLAIN, 16);
	private static final TrueTypeFont ROUNDED_LIGHT_25 = loadTTF(FONT_ROUNDED + "-light.ttf", Font.PLAIN, 25);
	private static final TrueTypeFont ROUNDED_LIGHT_30 = loadTTF(FONT_ROUNDED + "-light.ttf", Font.PLAIN, 30);
	private static final TrueTypeFont ROUNDED_LIGHT_40 = loadTTF(FONT_ROUNDED + "-light.ttf", Font.PLAIN, 40);
	public static final HashMap<String, TrueTypeFont> ROUNDED_LIGHT = new HashMap() {{
		put(12, ROUNDED_LIGHT_12);
		put(16, ROUNDED_LIGHT_16);
		put(25, ROUNDED_LIGHT_25);
		put(30, ROUNDED_LIGHT_30);
		put(40, ROUNDED_LIGHT_40);
	}};
	
	
	public static TrueTypeFont loadTTF(String path, int style, float size) {
		path = ResourceHelper.ASSETS_FONTS + path;
		try {
			InputStream inputStream	= FontHandler.class.getResourceAsStream(path);
	
			Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			font = font.deriveFont(style, size);
			inputStream.close();
			return new TrueTypeFont(font, true);
	 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new TrueTypeFont(new Font("Tahoma", Font.PLAIN, (int)size), true);
	}
}
