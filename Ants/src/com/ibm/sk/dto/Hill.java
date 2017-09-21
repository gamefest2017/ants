package com.ibm.sk.dto;

import java.awt.Point;

public class Hill {

	private int population;
	private String name;
	private Point position;

	public Hill(final int population, final String name, final Point position) {
		this.population = population;
		this.name = name;
		this.position = position;
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

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

}
