package com.ibm.sk.dto;

import java.awt.Point;

import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.dto.enums.ObjectType;

public class Warrior extends AbstractWarrior {

	public Warrior(final long id, final Point position, final Hill myHill) {
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
			final ObjectType objectType = vision.look(direction);
			if (!Direction.NO_MOVE.equals(direction)) {
				if (ObjectType.ENEMY_ANT.equals(objectType) || ObjectType.ENEMY_ANT_WITH_FOOD.equals(objectType)
						|| ObjectType.ENEMY_WARRIOR.equals(objectType)) {
					System.out.println("I see an enemy!");
					result = direction;
				} else if (ObjectType.ANT.equals(objectType) || ObjectType.ANT_WITH_FOOD.equals(objectType)
						|| ObjectType.WARRIOR.equals(objectType)) {
					System.out.println("I see a friend!");
					result = Direction.opposite(direction);
				}
			}
		}

		if (Direction.NO_MOVE.equals(result)) {
			System.out.println("Where to go?");
			result = Direction.random();
		}
		// Add your implementation here

		return result;
	}

}
