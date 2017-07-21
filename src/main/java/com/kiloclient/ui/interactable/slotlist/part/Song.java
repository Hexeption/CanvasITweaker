package com.kiloclient.ui.interactable.slotlist.part;

import com.kiloclient.render.utilities.TextureImage;
import com.kiloclient.resource.ResourceHelper;

public class Song {

	public int id, duration;
	public String title, artwork;
	public TextureImage image;
	public boolean starred;
	
	public Song(int i, String t, String a, int d, boolean s) {
		id = i;
		title = t;
		artwork = a;
		duration = d;
		starred = s;
		
		if (a != null) {
			image = ResourceHelper.downloadTexture(a);
		}
	}
}