package com.ibm.sk.ant.implementation;

import java.awt.Point;
import java.util.Random;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.Vision;
import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.dto.enums.ObjectType;

public class HomesickAnt extends AbstractAnt {
	private int maxDistance = 4 + new Random().nextInt(6);
	public HomesickAnt(long id, Point position, Hill myHill) {
		super(id, position, myHill);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Direction move(Vision vision) {
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
				int distance = Math.max(Math.abs(this.position.x - this.getMyHill().getPosition().x), 
						Math.abs(this.position.y - this.getMyHill().getPosition().y));
				if (distance > this.maxDistance) {
					returnValue = findWayHome();
				} else {
					do {
						System.out.println("Where to go?");
						returnValue = Direction.random();
					} while (ObjectType.BORDER.equals(vision.look(returnValue)));
				}
			}
		} else {
			returnValue = findWayHome();
			System.out.println("Going home!" + " [" + this.position.x + ", " + this.position.y + "] >> ["
					+ returnValue.getPositionChange().x + ", " + returnValue.getPositionChange().y + "] ~> ["
					+ this.getMyHill().getPosition().x + ", " + this.getMyHill().getPosition().y + "]");
		}

		return returnValue;
	}
	
	/**
	 * Finds the Direction towards home based on current coordinates and
	 * coordinates of the Anthill.
	 *
	 * @return Direction home
	 */
	private Direction findWayHome() {
		return Direction.get(this.position, this.getMyHill().getPosition());
	}


}
