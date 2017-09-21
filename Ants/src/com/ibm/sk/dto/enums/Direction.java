package com.ibm.sk.dto.enums;

import java.awt.Point;

public enum Direction {
	NORD(new Point(0,1)),
	NORDEAST(new Point(-1,1)),
	NORDWEST(new Point(1,1)),
	SOUTH(new Point(0,-1)),
	SOUTHEAST(new Point(-1,-1)),
	SOUTHWEST(new Point(1,-1)),
	EAST(new Point(1,0)),
	WEST(new Point(-1,0)),
	NO_MOVE(new Point(0,0));
	private final Point point;
	
	Direction(final Point point) {
		this.point = point;
	}
	public Point getPositionChange() {
		return this.point;
	}
}
