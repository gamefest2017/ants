package com.ibm.sk.dto;

import java.awt.Point;
import java.util.Random;

import com.ibm.sk.dto.enums.Direction;

public class Ant extends AbstractAnt {

	public Ant(final long id, final Point position, final Hill myHill) {
		super(id, position, myHill);
	}

	/**
	 * @param vision
	 *            - Vision is the grid of 8 positions surrounding the ant. It's
	 *            get(Direction) method returns one of the game items (Ant,
	 *            Food, Hill, Border) or null, if the position is empty.
	 *
	 * @return - the direction (cardinal), where the ant will move in the
	 *         current round
	 */
	@Override
	public Direction move(final Vision vision) {
		Direction returnValue = Direction.NO_MOVE;
		if (!this.hasFood()) {
			for (final Direction direction : Direction.values()) {
				final Object item = vision.look(direction);
				if (item instanceof Food) {
					System.out.println("I see food!");
					returnValue = direction;
				}
			}

			if (Direction.NO_MOVE.equals(returnValue)) {
				System.out.println("Where to go?");
				returnValue = randomDirection();
			}
		} else {
			System.out.println("Going home!");
			returnValue = findWayHome();
			System.out.println("Going home!" + " [" + this.position.x + ", " + this.position.y + "] >> ["
					+ returnValue.getPositionChange().x + ", " + returnValue.getPositionChange().y + "] ~> ["
					+ this.getMyHill().position.x + ", " + this.getMyHill().position.y + "]");
		}
		// Add your implementation here

		return returnValue;
	}

	/**
	 * Finds the Direction towards home based on current coordinates and
	 * coordinates of the Anthill.
	 * 
	 * @return Direction home
	 */
	private Direction findWayHome() {
		Direction result = Direction.NO_MOVE;
		final int horizontal = Integer.compare(this.getMyHill().position.x, this.position.x);
		final int vertical = Integer.compare(this.getMyHill().position.y, this.position.y);
		final Point pointer = new Point(horizontal, vertical);
		for (final Direction direction : Direction.values()) {
			if (direction.getPositionChange().equals(pointer)) {
				result = direction;
			}
		}
		return result;
	}

	private static final Random RANDOM = new Random();

	public Direction randomDirection() {
		return Direction.values()[RANDOM.nextInt(Direction.values().length - 1)];
	}
}
