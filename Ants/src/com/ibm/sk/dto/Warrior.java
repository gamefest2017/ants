package com.ibm.sk.dto;

import java.awt.Point;
import java.util.Random;

import com.ibm.sk.dto.enums.Direction;

public class Warrior extends AbstractWarrior {

	public Warrior(final int id, final Point position, final Hill myHill) {
		super(id, position, myHill);
	}

	/**
	 * @param vision
	 *            - Vision is the grid of 9 positions surrounding the ant. It's
	 *            get(Direction) method returns one of the game items (Ant,
	 *            Food, Hill) or null, if the position is empty.
	 *
	 * @return - the direction (cardinal), where the ant will move in the
	 *         current turn
	 */
	@Override
	public Direction move(final Vision vision) {
		Direction result = Direction.NO_MOVE;
		for (final Direction direction : Direction.values()) {
			final Object object = vision.look(direction);
			if (!Direction.NO_MOVE.equals(direction)
					&& object instanceof IAnt && this.isEnemy((IAnt) object)) {
				System.out.println("I see an enemy!");
				result = direction;
			}
		}

		if (Direction.NO_MOVE.equals(result)) {
			System.out.println("Where to go?");
			result = randomDirection();
		}
		// Add your implementation here

		return result;
	}

	private final Random RANDOM = new Random();

	public Direction randomDirection() {
		return Direction.values()[this.RANDOM.nextInt(Direction.values().length)];
	}
}
