package com.ibm.sk.dto;

import java.awt.Point;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ibm.sk.WorldConstans;
import com.ibm.sk.engine.PopulationHandler;

public class Hill extends WorldObject {

	final PopulationHandler populationHandler;
	private int food = 0;
	private String name;
	private final List<IAnt> ants;

	public Hill(final int population, final double populationWarFactor, final String name, final Point position) {
		this.name = name;
		this.position = position;
		this.ants = new CopyOnWriteArrayList<>();

		this.populationHandler = new PopulationHandler();

		for (int i = 0; i < Math.ceil(population * (1.0 - populationWarFactor)); i++) {
			this.ants.add(this.populationHandler.breedAnt(this));
		}
		for (int i = 0; i < Math.floor(population * populationWarFactor); i++) {
			this.ants.add(this.populationHandler.breedWarrior(this));
		}
	}

	public boolean isMyHill(final Ant ant) {
		return ant != null && this.name.equals(ant.getMyHillName());
	}

	public void incrementFood(final int count) {
		this.food += count;
		System.out.println(
				"The food in hill '" + this.name + "' increased by " + count + ". Food amount is now " + this.food);
		if (this.food % WorldConstans.NEW_ANT_FOOD_COST == 0) {
			this.populationHandler.breedAnt(this);
		}
	}

	public void decrementFood(final int count) {
		this.food = Math.max(this.food - count, 0);
		System.out.println(
				"The food in hill '" + this.name + "' descreased by " + count + ". Food amount is now " + this.food);
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
