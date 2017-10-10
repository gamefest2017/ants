package com.ibm.sk.dto;

import java.util.Map;

import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.dto.enums.ObjectType;

public class Vision extends WorldObject {

	private Map<Direction, ObjectType> grid;

	public Vision(final Map<Direction, ObjectType> grid) {
		this.grid = grid;
	}

	public ObjectType look(final Direction direction) {
		return this.grid.get(direction);
	}

	@Override
	public String toString() {
		return "Vision";
	}

}
