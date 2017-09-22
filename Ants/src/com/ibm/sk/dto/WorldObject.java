package com.ibm.sk.dto;

import java.awt.Point;

public class WorldObject implements IWorldObject {

	protected Point position;
	
	@Override
	public Point getPosition() {
		return position;
	}

	@Override
	public void setPosition(Point position) {
		this.position = position;
	}
}
