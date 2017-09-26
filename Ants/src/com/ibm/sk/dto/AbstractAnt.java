package com.ibm.sk.dto;

import java.awt.Point;

import com.ibm.sk.dto.enums.Direction;

public abstract class AbstractAnt extends WorldObject implements IAnt {

	private int id;
	private Food food;
	private Hill myHill;

	public AbstractAnt(final int id, final Point position, final Hill myHill) {
		this.id = id;
		this.position = position;
		this.myHill = myHill;
	}

	@Override
	public String getMyHillName() {
		return myHill.getName();
	}

	@Override
	public Hill getMyHill() {
		return this.myHill;
	}

	@Override
	public boolean isEnemy(IAnt otherAnt) {
		return otherAnt != null && this.getMyHillName() != null
				&& !this.getMyHillName().equals(otherAnt.getMyHillName());
	}

	@Override
	public abstract Direction move(final Vision vision);

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void pickUpFood(final Food food) {
		this.food = food;
	}

	@Override
	public Food dropFood() {
		Food retValue = this.food;
		this.food = null;
		return retValue;
	}
	
	public boolean hasFood() {
		return (this.food != 0);
	}

	@Override
	public String toString() {
		return "Ant " + this.id + " on position: " + (int) this.position.getX() + ", " + (int) this.position.getY();
	}

}
