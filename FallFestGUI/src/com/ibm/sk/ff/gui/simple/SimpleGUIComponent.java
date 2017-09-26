package com.ibm.sk.ff.gui.simple;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class SimpleGUIComponent {

	private final int MAGNIFICATION;
	private final Color FALLBACK_COLOR;
	private final Color TEAM_COLOR;
	private final Image IMAGE;

	private int x, y = 0;
	private int moveX = -1, moveY = -1;
	private int dx = 0, dy = 0;

	public SimpleGUIComponent(int MAGNIFICATION, Color fallback, Image image, Color teamColor) {
		this.MAGNIFICATION = MAGNIFICATION;
		this.FALLBACK_COLOR = fallback;
		this.IMAGE = image;
		this.TEAM_COLOR = teamColor;
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
		dx = 0;
		dy = 0;
		moveX = -1;
		moveY = -1;
		this.x = x;
		this.y = y;
	}

	public void moveToLocation(int x, int y) {
		dx = 0;
		dy = 0;
		moveX = x;
		moveY = y;
	}

	public boolean paint(Graphics g) {
		if (isMovePresent()) {
			prepareDeltas();
		}
		Graphics2D g2 = (Graphics2D)g;
		try {
			if (TEAM_COLOR != null) {
				g2.setColor(TEAM_COLOR);
				g2.setStroke(new BasicStroke(3));
				g2.drawOval((x * MAGNIFICATION) + dx, (y * MAGNIFICATION) + dy, MAGNIFICATION, MAGNIFICATION);
			}
			g2.drawImage(IMAGE, (x * MAGNIFICATION) + dx, (y * MAGNIFICATION) + dy, MAGNIFICATION, MAGNIFICATION, null);
		} catch (Exception e) {
			g2.setColor(FALLBACK_COLOR);
			g2.fillRect(x * MAGNIFICATION, y * MAGNIFICATION, MAGNIFICATION, MAGNIFICATION);
		}
		if ((x * MAGNIFICATION) + dx == (moveX * MAGNIFICATION)) {
			dx = 0;
			x = moveX;
		}
		if ((y * MAGNIFICATION) + dy == (moveY * MAGNIFICATION)) {
			dy = 0;
			y = moveY;
		}
		if ((x * MAGNIFICATION) + dx == (moveX * MAGNIFICATION) && (y * MAGNIFICATION) + dy == (moveY * MAGNIFICATION)) {
			setLocation(moveX, moveY);
		}
		return !isMovePresent();
	}

	private void prepareDeltas() {
		if (moveX == -1 && moveY == -1) {
			dx = 0;
			dy = 0;
		} else {
			if (moveX > x) dx++;
			else if (moveX < x)	dx--;
			
			if (moveY > y) dy++;
			else if (moveY < y)	dy--;
		}
	}

	private boolean isMovePresent() {
		return (moveX != -1 && moveY != -1);
	}
	
}
