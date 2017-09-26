package com.ibm.sk.ff.gui.common.objects.gui;

public class Location {
	
	private int x;
	private int y;
	
	public Location() {
		x = 0;
		y = 0;
	}
	
	public Location(Location location) {
		this(location.x, location.y);
	}
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

}
