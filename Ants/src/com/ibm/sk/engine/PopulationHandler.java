package com.ibm.sk.engine;

import static com.ibm.sk.engine.World.placeObject;

import java.awt.Point;
import java.util.Random;

import com.ibm.sk.WorldConstans;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Ant;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.Warrior;
import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.engine.exceptions.InvalidWorldPositionException;

public class PopulationHandler {
	
	private static final Random RANDOMIZER = new Random();

	private static PopulationHandler populationHandler;

	private PopulationHandler() {
	}

	public static synchronized PopulationHandler getInstance() {
		if (populationHandler == null) {
			populationHandler = new PopulationHandler();
		}
		return populationHandler;
	}

	public IAnt breedAnt(final Hill hill) {
		System.out.println("Welcome new worker of this world! From now on you belong to " + hill.getName()
				+ "! But don't be affraid, you are not alone, he has other " + hill.getPopulation() + " ants.");
		final Point homePosition = new Point(hill.getPosition());
		final IAnt ant = new Ant(World.idSequence++, homePosition, hill);
		try {
			placeObject(ant);
		} catch (final InvalidWorldPositionException e) {
			System.out.println("Invalid position.");
		}

		return ant;
	}

	public static void killAnt(final IAnt ant) {
		System.out.println("You shall be no more in this world! Good bye forewer dear ant " + ant.getId());
		leaveFood(ant);
		World.removeObject(ant.getPosition());
		ant.getMyHill().getAnts().remove(ant);
	}

	private static void leaveFood(final IAnt ant) {
		final Food remains = ant.dropFood();
		if (remains != null) {
			final Point here = ant.getPosition();
			Point dropPosition = null;
			do {
				final Point randomDirection = Direction.random().getPositionChange();
				dropPosition = new Point(here);
				dropPosition.translate(randomDirection.x, randomDirection.y);
			} while (World.getWorldObject(dropPosition) != null);
			if (World.getWorldObject(dropPosition) == null) {
				remains.setPosition(dropPosition);
				try {
					placeObject(remains);
				} catch (final InvalidWorldPositionException e) {
					System.err.println("Invalid position to drop food from killed ant." + e);
				}
				System.out.println("Dropped: " + remains);
			}
		}
		World.removeObject(ant.getPosition());
		ant.getMyHill().getAnts().remove(ant);
	}

	public AbstractWarrior breedWarrior(final Hill hill) {
		System.out.println("Welcome new warrior of this world! From now on you belong to " + hill.getName()
				+ "! But don't be affraid, you are not alone, he has other " + hill.getPopulation() + " ants.");
		final Point homePosition = new Point(hill.getPosition());
		final AbstractWarrior warrior = new Warrior(World.idSequence++, homePosition, hill);
		try {
			placeObject(warrior);
		} catch (final InvalidWorldPositionException e) {
			System.out.println("Invalid position.");
		}

		return warrior;
	}

	public IAnt breedAntOrWarrior(final Hill hill) {
		IAnt result;
		if (RANDOMIZER.nextDouble() < WorldConstans.POPULATION_WAR_FACTOR) {
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
