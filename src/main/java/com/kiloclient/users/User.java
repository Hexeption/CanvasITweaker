package com.kiloclient.users;

public class User {

	public boolean sizeEnabled, earsEnabled, flipEnabled, sunglassesEnabled, braceletEnabled, wingsEnabled, topHatEnabled;
	public float size;
	public String minecraftName, earSize;
	
	public User(String minecraftName, boolean sizeEnabled, boolean earsEnabled, boolean flipEnabled, boolean sunglassesEnabled, boolean braceletEnabled, boolean wingsEnabled, boolean topHatEnabled, float size, String earSize) {
		this.minecraftName = minecraftName;
		this.sizeEnabled = sizeEnabled;
		this.earsEnabled = earsEnabled;
		this.flipEnabled = flipEnabled;
		this.sunglassesEnabled = sunglassesEnabled;
		this.braceletEnabled = braceletEnabled;
		this.topHatEnabled = topHatEnabled;
		this.wingsEnabled = wingsEnabled;
		this.size = size;
		this.earSize = earSize;
	}
}
