package com.kiloclient.utilities;

public class ColorScheme {

	private final int schemeForeground, schemeBackground;
	private final char colorCode;
	
	public ColorScheme(int foreground, int background, char colorCode)
	{
		this.schemeForeground = foreground;
		this.schemeBackground = background;
		this.colorCode = colorCode;
	}
	
	public final int getForeground() {
		return this.schemeForeground;
	}
	
	public final int getBackground() {
		return this.schemeBackground;
	}
	
	public final char getColorCode() {
		return this.colorCode;
	}
}
