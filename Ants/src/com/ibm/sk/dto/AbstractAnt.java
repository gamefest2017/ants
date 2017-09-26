package com.ibm.sk.dto;

import java.awt.Point;

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

}
