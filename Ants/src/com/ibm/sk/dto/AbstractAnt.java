package com.ibm.sk.dto;

import java.awt.Point;
import java.util.Random;

import com.ibm.sk.dto.enums.Direction;

public abstract class AbstractAnt extends WorldObject implements IAnt {

	private int id;
	private Food food;
	private final Hill myHill;

	public AbstractAnt(final int id, final Point position, final Hill myHill) {
		this.id = id;
		this.position = position;
		this.myHill = myHill;
	}

	@Override
	public String getMyHillName() {
		return this.myHill.getName();
	}

	@Override
	public Hill getMyHill() {
		return this.myHill;
	}

	@Override
	public boolean isEnemy(final IAnt otherAnt) {
		return otherAnt != null && this.getMyHillName() != null
				&& !this.getMyHillName().equals(otherAnt.getMyHillName());
	}

	@Override
	public abstract Direction move(final Vision vision);

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(final int id) {
		this.id = id;
	}

	@Override
	public void pickUpFood(final Food food) {
		System.out.println("Picked: " + food);
		this.food = food;
	}

	@Override
	public Food dropFood() {
		final Food retValue = this.food;
		this.food = null;
		return retValue;
	}

	public boolean hasFood() {
		return this.food != null;
	}

	@Override
	public String toString() {
		return "Ant " + this.id + " from " + this.getMyHillName() + " on: [" + this.position.x + ", " + this.position.y
				+ "]" + " with "
				+ (this.hasFood() ? String.valueOf(this.food.getAmount()) : "no") + " food";
	}
	
	/**
	 * Finds the Direction towards home based on current coordinates and coordinates of the Anthill.
	 * @return Direction home
	 */
	protected Direction findWayHome() {
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
		} else {
			verticalDirection = Direction.NORTH;
		}

		return Direction.add(horizontalDirection, verticalDirection);
	}
	
	private static final Random RANDOM = new Random();

	public Direction randomDirection() {
		return Direction.values()[RANDOM.nextInt(Direction.values().length - 1)];
	}

}
