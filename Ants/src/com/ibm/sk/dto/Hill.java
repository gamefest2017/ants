package com.ibm.sk.dto;

import java.awt.Point;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ibm.sk.engine.PopulationHandler;

public class Hill extends WorldObject {

	private int population;
	private String name;
	private final List<IAnt> ants;

	public Hill(final int population, final double populationWarFactor, final String name, final Point position) {
		this.name = name;
		this.position = position;
		this.ants = new CopyOnWriteArrayList<>();

		final PopulationHandler populationHandler = new PopulationHandler();

		for (int i = 0; i < Math.ceil(population * (1.0 - populationWarFactor)); i++) {
			this.ants.add(populationHandler.breedAnt(this));
		}
		for (int i = 0; i < Math.floor(population * populationWarFactor); i++) {
			this.ants.add(populationHandler.breedWarrior(this));
		}
	}

	public boolean isMyHill(final Ant ant) {
		return ant != null && this.name.equals(ant.getMyHillName());
	}

	public void incrementPopulation(final int count) {
		this.population += count;
		System.out.println("Population of hill " + this.name + " increased by " + count + ".");
	}

	public void decrementPopulation(final int count) {
		this.population = Math.abs(this.population - count);
		System.out.println("Population of hill " + this.name + " decreased by " + count + ".");
	}

	public int getPopulation() {
		return this.population;
	}

	public void setPopulation(final int population) {
		this.population = population;
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
		return "Hill [population=" + this.population + ", name=" + this.name + "]";
	}

}
