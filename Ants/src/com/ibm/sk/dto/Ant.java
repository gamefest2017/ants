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
			for (Direction direction : Direction.values()) {
				Object item = vision.look(direction);
				if (item instanceof Food) {
					returnValue = direction;
				}
			}
	
			if (Direction.NO_MOVE.equals(returnValue)) {
				returnValue = randomDirection();
			} 
		} else {
			returnValue = findWayHome();			
		} 
		// Add your implementation here

		return returnValue;
	}
	
	
	/**
	 * Finds the Direction towards home based on current coordinates and coordinates of the Anthill.
	 * @return Direction home
	 */
	private Direction findWayHome() {
		Direction horizontalDirection = Direction.NO_MOVE;
		Direction verticalDirection = Direction.NO_MOVE;
		
		if (this.getMyHill().position.x < this.position.x) {
			horizontalDirection = Direction.WEST;
		}else if (this.getMyHill().position.x == this.position.x) {
			horizontalDirection = Direction.NO_MOVE;
		}else {
			horizontalDirection = Direction.EAST;
		}
		
		if (this.getMyHill().position.y < this.position.y) {
			verticalDirection = Direction.SOUTH;
		} else if  (this.getMyHill().position.y == this.position.y) {
			verticalDirection = Direction.NO_MOVE;
		} else verticalDirection = Direction.NORD;
		
		return Direction.add(horizontalDirection, verticalDirection);		
	}



	private static final Random RANDOM = new Random();

	public Direction randomDirection() {
		return Direction.values()[RANDOM.nextInt(Direction.values().length - 1)];		
	}
}
