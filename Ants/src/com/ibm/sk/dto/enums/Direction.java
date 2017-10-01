package com.ibm.sk.dto.enums;

import java.awt.Point;
import java.util.Random;

public enum Direction {
	NORTH(new Point(0,-1)),
	NORTHEAST(new Point(1,-1)),
	NORTHWEST(new Point(-1,-1)),
	SOUTH(new Point(0,1)),
	SOUTHEAST(new Point(1,1)),
	SOUTHWEST(new Point(-1,1)),
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

	public static Direction opposite(final Direction to) {
		Direction result = NO_MOVE;
		switch (to) {
		case NORTH:
			result = SOUTH;
			break;
		case NORTHEAST:
			result = SOUTHWEST;
			break;
		case NORTHWEST:
			result = SOUTHEAST;
			break;
		case WEST:
			result = EAST;
			break;
		case EAST:
			result = WEST;
			break;
		case SOUTH:
			result = NORTH;
			break;
		case SOUTHEAST:
			result = NORTHWEST;
			break;
		case SOUTHWEST:
			result = Direction.NORTHEAST;
			break;
		default:
			break;
		}
		return result;
	}

	public static Direction get(final Point from, final Point to) {
		Direction result = NO_MOVE;
		final int horizontal = Integer.compare(to.x, from.x);
		final int vertical = Integer.compare(to.y, from.y);
		final Point pointer = new Point(horizontal, vertical);
		for (final Direction direction : values()) {
			if (direction.getPositionChange().equals(pointer)) {
				result = direction;
			}
		}
		return result;
	}
	public static Direction addHorizontalAndVerticalD(final Direction horizontalD, final Direction verticalD) {
		Direction returnValue = Direction.NO_MOVE;
		if (horizontalD == Direction.NO_MOVE) {
			returnValue = verticalD;
		} else if (horizontalD == Direction.WEST) {
			switch (verticalD) {
			case NORTH: 		returnValue = Direction.NORTHWEST;
			break;
			case NO_MOVE: 	returnValue = Direction.WEST;
			break;
			case SOUTH: 	returnValue = Direction.SOUTHWEST;
			break;
			default:
				break;
			}
		} else {
			switch (verticalD) {
			case NORTH: 		returnValue = Direction.NORTHEAST;
			break;
			case NO_MOVE: 	returnValue = Direction.EAST;
			break;
			case SOUTH: 	returnValue = Direction.SOUTHEAST;
			break;
			default:
				break;
			}
		}
		return returnValue;
	}

	public static Direction add(final Direction d1, final Direction d2) {
		Direction returnValue = Direction.NO_MOVE;
		final int x = d1.point.x + d2.point.x;
		final int y = d1.point.y + d2.point.y;

		if (x == 0) {
			switch (y) {
			case -1: case -2: returnValue = Direction.SOUTH;
			break;
			case 0: returnValue = Direction.NO_MOVE;
			break;
			case 1: case 2: returnValue = Direction.NORTH;
			}
		} else if (x<0) {
			switch (y) {
			case -1: case -2: returnValue = Direction.SOUTHWEST;
			break;
			case 0: returnValue = Direction.WEST;
			break;
			case 1: case 2: returnValue = Direction.NORTHWEST;
			}
		} else {
			switch (y) {
			case -1: case -2: returnValue = Direction.SOUTHEAST;
			break;
			case 0: returnValue = Direction.EAST;
			break;
			case 1: case 2: returnValue = Direction.NORTHEAST;
			}
		}

		return returnValue;
	}

	private static final Random RANDOM = new Random();

	public static Direction random() {
		return Direction.values()[RANDOM.nextInt(Direction.values().length - 1)];
	}
}
