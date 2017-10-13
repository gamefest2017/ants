package com.ibm.sk.dto;


import java.awt.Point;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Hill extends WorldObject {

	private int food = 0;
	private String name;
	private final List<IAnt> ants;

	public Hill(final String name, final Point position) {
		this.name = name;
		this.position = position;
		this.ants = new CopyOnWriteArrayList<>();
	}

	public boolean isMyHill(final Ant ant) {
		return ant != null && this.name.equals(ant.getMyHillName());
	}

	public int getPopulation() {
		return this.ants.size();
	}

	public void setFood(final int foodAmount) {
		this.food = foodAmount;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Collection<IAnt> getAnts() {
		return this.ants;
	}

	@Override
	public String toString() {
		return "Hill [population=" + this.ants.size() + ", name=" + this.name + ", food=" + this.food + "]";
	}

	public int getFood() {
		return this.food;
	}

}
