package com.ibm.sk.models;

import java.awt.Point;

import com.ibm.sk.dto.WorldObject;

public class WorldBorder extends WorldObject {

	public WorldBorder(final Point position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Border of the ants word on position: [" + this.position.x + ", " + this.position.y + "]";
	}

}
