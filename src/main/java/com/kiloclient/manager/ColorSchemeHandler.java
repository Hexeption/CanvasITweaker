package com.kiloclient.manager;

import com.kiloclient.KiLO;
import com.kiloclient.utilities.ColorScheme;

public class ColorSchemeHandler {
	
	private int currentScheme = -1;
	
	private final ColorScheme[] COLOR_SCHEMES = new ColorScheme[]{
		new ColorScheme(0xFFED5564, 0xFFDA4352, '4'),
		new ColorScheme(0xFFFC6C51, 0xFFE9573E, 'c'),
		new ColorScheme(0xFFFFCE55, 0xFFF6BB43, 'e'),
		new ColorScheme(0xFFA0D468, 0xFF8CC051, 'a'),
		new ColorScheme(0xFF48CFAE, 0xFF36BC9B, '3'),
		new ColorScheme(0xFF4FC0E8, 0xFF3BAEDA, 'b'),
		new ColorScheme(0xFF6099F2, 0xFF4B89DC, '9'),
		new ColorScheme(0xFFAC92ED, 0xFF967BDC, 'd'),
		new ColorScheme(0xFFEC87BF, 0xFFD870AD, 'd'),
		new ColorScheme(0xFFF6F7FB, 0xFFE6E9EE, '7'),
		new ColorScheme(0xFF656D78, 0xFF434A54, '8')
	};
	
	public ColorSchemeHandler() {}
	
	public void setScheme(int i){
		currentScheme = i;
	}
	
	public int getCurrentForeground(){
		if (KiLO.getKiLO().getUserControl() == null){
			return 0xFFFFFFFF;
		}
		return COLOR_SCHEMES[KiLO.getKiLO().getUserControl().colorSchemeID].getForeground();
	}
	
	public int getCurrentBackground(){
		if (KiLO.getKiLO().getUserControl() == null){
			return 0xFF202020;
		}
		return COLOR_SCHEMES[KiLO.getKiLO().getUserControl().colorSchemeID].getBackground();
	}
	
	public String getCurrentColorCode(){
		if (KiLO.getKiLO().getUserControl() == null){
			return "\247f";
		}
		return "\247"+COLOR_SCHEMES[KiLO.getKiLO().getUserControl().colorSchemeID].getColorCode();
	}
	
}
