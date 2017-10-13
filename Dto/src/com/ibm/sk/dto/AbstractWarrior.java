package com.ibm.sk.dto;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import com.ibm.sk.dto.enums.Direction;

public abstract class AbstractWarrior extends WorldObject implements IAnt {

	private final Hill myHill;
	private final Set<Long> kills = new HashSet<>();

	public AbstractWarrior(final long id, final Point position, final Hill myHill) {
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
	public String toString() {
		return "Warrior " + this.id + " from " + this.getMyHillName() + " on: [" + this.position.x + ", "
				+ this.position.y + "] with " + this.kills.size() + " victories.";
	}

	public void killed(final IAnt enemy) {
		System.out.println("Warrior " + this.id + " defeated " + enemy.toString());
		this.kills.add(Long.valueOf(enemy.getId()));
	}

}
