package com.ibm.sk.engine;

import static com.ibm.sk.engine.World.placeObject;

import java.awt.Point;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Ant;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.Warrior;
import com.ibm.sk.engine.exceptions.InvalidWorldPositionException;

public class PopulationHandler {

	public AbstractAnt breedAnt(final Hill hill) {
		System.out.println("Welcome new creature of this world! From now on you belong to " + hill.getName()
		+ "! But don't be affraid, you are not alone, he has other " + hill.getPopulation() + " ants.");
		final Point homePosition = new Point(hill.getPosition());
		final AbstractAnt ant = new Ant(World.idSequence++, homePosition, hill);
		try {
			placeObject(ant);
		} catch (final InvalidWorldPositionException e) {
			System.out.println("Invalid position.");
		}

		return ant;
	}

	public static void killAnt(final IAnt ant) {
		System.out.println("You shall be no more in this world! Good bye forewer dear ant " + ant.getId());
		final Food remains = ant.dropFood();
		if (remains != null && World.getWorldObject(remains.getPosition()) == null) {
			try {
				placeObject(remains);
			} catch (final InvalidWorldPositionException e) {
				System.out.println("Invalid position.");
			}
			System.out.println("Dropped: " + remains);
		}
		ant.getMyHill().getAnts().remove(ant);
	}

	public AbstractWarrior breedWarrior(final Hill hill) {
		System.out.println("Welcome new creature of this world! From now on you belong to " + hill.getName()
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
}
