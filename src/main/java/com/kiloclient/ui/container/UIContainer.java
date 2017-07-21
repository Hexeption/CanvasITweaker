package com.kiloclient.ui.container;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import com.kiloclient.ui.interactable.Interactable;

public class UIContainer {
	
	public float xCoordinate;
	
	public float x1Coordinate;
	
	public float yCoordinate;
	
	public float y1Coordinate;
	
	public float width;
	
	public float height;
	
	public float percentX;
	
	public float percentY;
	
	public float percentX1;
	
	public float percentY1;
	
	float displayWidth = Display.getWidth();
	
	float displayHeight = Display.getHeight();
	
	public UIContainer(float percentX, float percentY, float percentX1, float percentY1) {
		this.percentX = percentX;
		this.percentY = percentY;
		this.percentX1 = percentX1;
		this.percentY1 = percentY1;
		
		
		this.xCoordinate = (this.percentX * displayWidth) / 100;
		this.yCoordinate = (this.percentY * displayHeight) / 100;
		this.x1Coordinate = (this.percentX1 * displayWidth) / 100;
		this.y1Coordinate = (this.percentY1 * displayHeight) / 100;
		
		this.width = this.x1Coordinate - this.xCoordinate;
		this.height = this.y1Coordinate - this.yCoordinate;
	}
	
	public void update() {
		this.xCoordinate = (this.percentX * displayWidth) / 100;
		this.yCoordinate = (this.percentY * displayHeight) / 100;
		this.x1Coordinate = (this.percentX1 * displayWidth) / 100;
		this.y1Coordinate = (this.percentY1 * displayHeight) / 100;
		
		this.width = this.x1Coordinate - this.xCoordinate;
		this.height = this.y1Coordinate - this.yCoordinate;
	}
	
	public void render() {
		
	}
}
