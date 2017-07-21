package com.kiloclient.render.utilities;

public class AnimationTimer {

	private final int delay;
	
	private int bottom, top;
	private int timer;
	private boolean wasRising; 
	
	public AnimationTimer(int delay){
		this.delay = delay;
		top = delay;
		bottom = 0;
	}
	
	public void update(boolean increment){
		if (increment){
			if (timer < delay){
				if (!wasRising){
					bottom = timer;
				}
				timer++;
			}
			wasRising = true;
		}
		else{
			if (timer > 0){
				if (wasRising){
					top = timer;
				}
				timer--;
			}
			wasRising = false;
		}
	}
	
	public double getValue()
	{
		if (wasRising){
			return Math.sin(((timer - bottom)/(double)(delay - bottom))*Math.PI/2);
		}
		else{
			return 1.0 - Math.cos((timer/(double)top)*Math.PI/2);
		}
	}
	
}
