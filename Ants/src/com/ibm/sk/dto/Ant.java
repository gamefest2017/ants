package com.ibm.sk.dto;

import java.awt.Point;
import java.util.Random;

import com.ibm.sk.dto.enums.Direction;

public class Ant extends AbstractAnt {

	public Ant(final int id, final Point position, final Hill myHill) {
		super(id, position, myHill);
	}

	/**
	 * @param vision
	 *            - Vision is the grid of 8 positions surrounding the ant. It's
	 *            get(Direction) method returns one of the game items (Ant,
	 *            Food, Hill) or null, if the position is empty.
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
		}
		// Add your implementation here

		return returnValue;
	}
}
