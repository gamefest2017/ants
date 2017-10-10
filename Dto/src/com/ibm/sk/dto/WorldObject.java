package com.ibm.sk.dto;

import java.awt.Point;

public class WorldObject implements IWorldObject {

	protected long id;
	protected Point position;
	
	@Override
	public Point getPosition() {
		return position;
	}

	@Override
	public void setPosition(Point position) {
		this.position = position;
	}
	
	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

}