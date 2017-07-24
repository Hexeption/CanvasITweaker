package com.canvasclient.render.utilities;

public enum ColorHelper {

	BLACK(0xFF010101),
	BLUE(0xFF00D2C6),
	DARK_BLUE(0xFF1077AA),
	GREEN(0xFF6DCE27),
	DARK_GREEN(0xFF549F1E),
	WHITE(0xFFFEFEFE),
	AQUA(0xFF27E2FB),
	DARK_AQUA(0xFF037c8c),
	GREY(0xFF999999),
	DARK_GREY(0xFF444444),
	RED(0xFFFF5555),
	DARK_RED(0xFF880000),
	ORANGE(0xFFFFAA55),
	DARK_ORANGE(0xFF884400),
	YELLOW(0xFFFFFF55),
	DARK_YELLOW(0xFF888800),
	MAGENTA(0xFFFF55FF),
	DARK_MAGENTA(0xFF880088);
	
	private int colorCode;
	
	ColorHelper(int colorCode) {
		this.colorCode = colorCode;
	}
	
	public int getColorCode() {
		return this.colorCode;
	}
	
}
