package com.ibm.sk.ff.gui.simple;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class SimpleGUIComponent {

	private final int MAGNIFICATIONX;
	private final int MAGNIFICATIONY;
	private final Color TEAM_COLOR;
	private final Image[] IMAGE;
	private final Image[] TRANSIT_IMAGE;
	
	
	private Image lastImage = null;

	private int x, y = 0;
	private int moveX = -1, moveY = -1;
	private int dx = 0, dy = 0;
	
	public SimpleGUIComponent(int MAGNIFICATIONX, int MAGNIFICATIONY, Image[] image, Image[] transitImage, Color teamColor) {
		this.MAGNIFICATIONX = MAGNIFICATIONX;
		this.MAGNIFICATIONY = MAGNIFICATIONY;
		this.IMAGE = image;
		this.TRANSIT_IMAGE = transitImage;
		this.lastImage = this.IMAGE[0];
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
	
	public Rectangle getActualRectangle() {
		return new Rectangle((x * MAGNIFICATIONX) + dx, (y * MAGNIFICATIONY) + dy, MAGNIFICATIONX, MAGNIFICATIONY);
	}

	public boolean paint(Graphics g) {
		if (isMovePresent()) {
			prepareDeltas();
		}
		
		boolean ret = !isMovePresent();
		
		Graphics2D g2 = (Graphics2D)g;
		try {
			if (TEAM_COLOR != null) {
				g2.setColor(TEAM_COLOR);
				g2.setStroke(new BasicStroke(3));
				g2.drawOval((x * MAGNIFICATIONX) + dx, (y * MAGNIFICATIONY) + dy, MAGNIFICATIONX, MAGNIFICATIONY);
			}
			g2.drawImage(ret ? getImageToDraw() : getFinalImageToDraw(), (x * MAGNIFICATIONX) + dx, (y * MAGNIFICATIONY) + dy, MAGNIFICATIONX, MAGNIFICATIONY, null);
		} catch (Exception e) {
		}
		if ((x * MAGNIFICATIONX) + dx == (moveX * MAGNIFICATIONX)) {
			dx = 0;
			x = moveX;
		}
		if ((y * MAGNIFICATIONY) + dy == (moveY * MAGNIFICATIONY)) {
			dy = 0;
			y = moveY;
		}
		if ((x * MAGNIFICATIONX) + dx == (moveX * MAGNIFICATIONX) && (y * MAGNIFICATIONY) + dy == (moveY * MAGNIFICATIONY)) {
			setLocation(moveX, moveY);
		}
		
		return ret;
	}
	
	private Image getImageToDraw() {
		return getImage((TRANSIT_IMAGE == null) ? IMAGE : TRANSIT_IMAGE);
	}
	
	private Image getFinalImageToDraw() {
		return getImage(IMAGE);
	}
	
	private Image getImage(Image [] images) {
		Image ret = null;
		if (images.length == 1) {
			ret = images[0]; 
		} else
		if (images.length == 2) {
			if (dx < 0) {
				ret = images[0];
				lastImage = ret;
			} else 
			if (dx > 0) {
				ret = images[1];
				lastImage = ret;
			} else {
				ret = lastImage;
			}
		}
		return ret;
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
