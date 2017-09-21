package com.ibm.sk.dto;

import java.util.Map;

import com.ibm.sk.dto.enums.Direction;

public class Vision {

	private Map<Direction, Object> grid;

	public Vision(final Map<Direction, Object> grid) {
		this.grid = grid;
	}

	public Object look(final Direction direction) {
		return this.grid.get(direction);
	}

}
