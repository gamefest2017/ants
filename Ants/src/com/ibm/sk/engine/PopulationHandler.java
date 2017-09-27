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

public class PopulationHandler {
	private static int antcounter = 1;

	public AbstractAnt breedAnt(final Hill hill) {
		System.out.println("Welcome new creature of this world! From now on you belong to "
				+ hill.getName() + "! But don't be affraid, you are not alone, he has other " + hill.getPopulation() + " ants.");
		final Point homePosition = new Point(hill.getPosition());
		final Ant ant = new Ant(antcounter++, homePosition, hill);
		World.placeObject(ant);
		hill.incrementPopulation(1);
		return ant;
	}

	public static void killAnt(final IAnt ant) {
		System.out.println("You shall be no more in this world! Good bye forewer dear ant " + ant.getId());
		ant.getMyHill().decrementPopulation(1);
		final Food remains = ant.dropFood();
		if (remains != null && World.getWorldObject(remains.getPosition()) == null) {
			placeObject(remains);
			System.out.println("Dropped: " + remains);
		}
		World.removeObject(ant.getPosition());
		ant.getMyHill().getAnts().remove(ant);
	}

	public AbstractWarrior breedWarrior(final Hill hill) {
		System.out.println("Welcome new creature of this world! From now on you belong to " + hill.getName()
		+ "! But don't be affraid, you are not alone, he has other " + hill.getPopulation() + " ants.");
		final Point homePosition = new Point(hill.getPosition());
		final AbstractWarrior warrior = new Warrior(antcounter++, homePosition, hill);
		World.placeObject(warrior);
		hill.incrementPopulation(1);
		return warrior;
	}
}
