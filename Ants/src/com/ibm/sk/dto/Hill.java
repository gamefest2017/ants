package com.ibm.sk.dto;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.ibm.sk.engine.PopulationHandler;

public class Hill extends WorldObject {

	private int population;
	private String name;
	private List<Ant> ants;

	public Hill(final int population, final String name, final Point position) {
		this.name = name;
		this.position = position;
		this.ants = new ArrayList<>();
		
		PopulationHandler populationHandler = new PopulationHandler();
		
		for (int i = 0; i < population; i++) {
			this.ants.add(populationHandler.breedAnt(this));
		}
	}

	public boolean isMyHill(final Ant ant) {
		return ant != null && this.name.equals(ant.getMyHillName());
	}

	public void incrementPopulation(final int count) {
		this.population += count;
	}

	public void decrementPopulation(final int count) {
		this.population = Math.abs(this.population - count);
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Ant> getAnts() {
		return ants;
	}

}
