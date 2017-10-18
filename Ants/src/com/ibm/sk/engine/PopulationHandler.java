package com.ibm.sk.engine;

import java.awt.Point;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ibm.sk.WorldConstans;
import com.ibm.sk.ant.facade.AntFactory;
import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.engine.exceptions.InvalidWorldPositionException;

public class PopulationHandler {

	private final Random RANDOMIZER = new Random();
	private final Map<String, AntFactory> implementations;
	private final World world;

	public PopulationHandler(final World world, final AntFactory[] implementations) {
		this.world = world;
		this.implementations = Arrays.asList(implementations).stream()
				.collect(Collectors.toMap(AntFactory::getTeamName, Function.identity()));
	}

	public AbstractAnt breedAnt(final Hill hill) {
		System.out.println("Welcome new worker of this world! From now on you belong to " + hill.getName()
		+ "! But don't be affraid, you are not alone, he has other " + hill.getPopulation() + " ants.");
		final Point homePosition = new Point(hill.getPosition());
		final AntFactory factory = this.implementations.get(hill.getName());
		final AbstractAnt ant = factory.createWorker(this.world.idSequence++, homePosition, hill);
		try {
			this.world.placeObject(ant);
		} catch (final InvalidWorldPositionException e) {
			System.out.println("Invalid position.");
		}

		return ant;
	}

	public void killAnt(final IAnt ant) {
		System.out.println("You shall be no more in this world! Good bye forewer dear ant " + ant.getId());
		if (ant instanceof AbstractAnt) {
			leaveFood((AbstractAnt) ant);
		}
		this.world.removeObject(ant.getPosition());
		ant.getMyHill().getAnts().remove(ant);
	}

	private void leaveFood(final AbstractAnt ant) {
		final Food remains = ant.dropFood();
		if (remains != null) {
			final Point here = ant.getPosition();
			Point dropPosition = null;
			do {
				final Point randomDirection = Direction.random().getPositionChange();
				dropPosition = new Point(here);
				dropPosition.translate(randomDirection.x, randomDirection.y);
			} while (this.world.getWorldObject(dropPosition) != null);
			if (this.world.getWorldObject(dropPosition) == null) {
				remains.setPosition(dropPosition);
				try {
					this.world.placeObject(remains);
				} catch (final InvalidWorldPositionException e) {
					System.err.println("Invalid position to drop food from killed ant." + e);
				}
				System.out.println("Dropped: " + remains);
			}
		}
	}

	public AbstractWarrior breedWarrior(final Hill hill) {
		System.out.println("Welcome new warrior of this world! From now on you belong to " + hill.getName()
		+ "! But don't be affraid, you are not alone, he has other " + hill.getPopulation() + " ants.");
		final Point homePosition = new Point(hill.getPosition());
		final AntFactory factory = this.implementations.get(hill.getName());
		final AbstractWarrior warrior = factory.createWarrior(this.world.idSequence++, homePosition, hill);
		try {
			this.world.placeObject(warrior);
		} catch (final InvalidWorldPositionException e) {
			System.out.println("Invalid position.");
		}

		return warrior;
	}

	public IAnt breedAntOrWarrior(final Hill hill) {
		IAnt result;
		if (this.RANDOMIZER.nextDouble() < WorldConstans.POPULATION_WAR_FACTOR) {
			result = breedWarrior(hill);
		} else {
			result = breedAnt(hill);
		}
		return result;
	}

	public void initHill(final Hill hill, final int population, final double populationWarFactor) {
		for (int i = 0; i < Math.ceil(population * (1.0 - populationWarFactor)); i++) {
			hill.getAnts().add(breedAnt(hill));
		}
		for (int i = 0; i < Math.floor(population * populationWarFactor); i++) {
			hill.getAnts().add(breedWarrior(hill));
		}
	}

	public void incrementFood(final Hill hill, final int count) {
		hill.setFood(hill.getFood() + count);
		System.out.println(
				"The food in hill '" + hill.getName() + "' increased by " + count + ". Food amount is now " + hill.getFood());
		if (hill.getFood() % WorldConstans.NEW_ANT_FOOD_COST == 0) {
			hill.getAnts().add(breedAntOrWarrior(hill));
		}
	}

	public void decrementFood(final Hill hill, final int count) {
		hill.setFood(Math.max(hill.getFood() - count, 0));
		System.out.println(
				"The food in hill '" + hill.getName() + "' descreased by " + count + ". Food amount is now " + hill.getFood());
	}
}
