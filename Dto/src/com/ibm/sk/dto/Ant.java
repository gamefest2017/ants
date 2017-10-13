package com.ibm.sk.dto;

import java.awt.Point;

import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.dto.enums.ObjectType;

public class Ant extends AbstractAnt {

	public Ant(final long id, final Point position, final Hill myHill) {
		super(id, position, myHill);
	}

	/**
	 * @param vision
	 *            - Vision is the grid of 8 positions surrounding the ant. It's
	 *            get(Direction) method returns enum representing one of the
	 *            game items (Ant, Enemy ant, Ant with food...
	 *
	 * @return - the direction (cardinal), where the ant will move in the
	 *         current round
	 */
	@Override
	public Direction move(final Vision vision) {
		Direction returnValue = Direction.NO_MOVE;
		if (!this.hasFood()) {
			for (final Direction direction : Direction.values()) {
				final ObjectType objectType = vision.look(direction);
				if (ObjectType.FOOD.equals(objectType)) {
					System.out.println("I see food!");
					returnValue = direction;
				}
			}

			if (Direction.NO_MOVE.equals(returnValue)) {
				do {
					System.out.println("Where to go?");
					returnValue = Direction.random();
				} while (ObjectType.BORDER.equals(vision.look(returnValue)));
			}
		} else {
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
		return Direction.get(this.position, this.getMyHill().position);
	}

}
