package com.ibm.sk.ff.gui.simple;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class SimpleGUIComponent {
	
	private final int MAGNIFICATION;
	private final Color FALLBACK_COLOR;
	private final Image IMAGE;
	
	private int x, y = 0;
	private int moveX = -1, moveY = -1;
	
	public SimpleGUIComponent(int MAGNIFICATION, Color fallback, Image image) {
		this.MAGNIFICATION = MAGNIFICATION;
		this.FALLBACK_COLOR = fallback;
		this.IMAGE = image;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void moveToLocation(int x, int y) {
//		moveX = x;
//		moveY = y;
		this.x = x;
		this.y = y;
	}
	
	public void paint(Graphics g) {
		try {
			g.drawImage(IMAGE, x * MAGNIFICATION, y * MAGNIFICATION, MAGNIFICATION, MAGNIFICATION, null);
		} catch (Exception e) {
			g.setColor(FALLBACK_COLOR);
			g.fillRect(x * MAGNIFICATION, y * MAGNIFICATION, MAGNIFICATION, MAGNIFICATION);
		}
	}
	
	public boolean isMovePresent() {
		return (moveX != -1 && moveY != -1);
	}
	
	public void move(Graphics g) {
		//TODO
	}

}
