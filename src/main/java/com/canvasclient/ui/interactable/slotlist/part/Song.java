package com.canvasclient.ui.interactable.slotlist.part;

import com.canvasclient.render.utilities.TextureImage;
import com.canvasclient.resource.ResourceHelper;

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