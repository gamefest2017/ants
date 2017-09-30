package com.ibm.sk.engine;

import static com.ibm.sk.engine.World.placeObject;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Ant;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.Warrior;
import com.ibm.sk.dto.enums.Direction;

public class PopulationHandler {
	private static int antcounter = 1;

	public AbstractAnt breedAnt(final Hill hill) {
		System.out.println("Welcome new creature of this world! From now on you belong to " + hill.getName()
				+ "! But don't be affraid, you are not alone, he has other " + hill.getPopulation() + " ants.");
		final Point homePosition = new Point(hill.getPosition());
		final Ant ant = new Ant(antcounter++, homePosition, hill);
		World.placeObject(ant);
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
			final List<Direction> directions = Arrays.asList(Direction.values());
			Collections.shuffle(directions);
			final Iterator<Direction> randomDirections = directions.iterator();
			Point dropPosition = null;
			do {
				final Point randomDirection = randomDirections.next().getPositionChange();
				dropPosition = new Point(here);
				dropPosition.translate(randomDirection.x, randomDirection.y);
			} while (randomDirections.hasNext() && World.getWorldObject(dropPosition) != null);
			if (World.getWorldObject(dropPosition) == null) {
				remains.setPosition(dropPosition);
				placeObject(remains);
				System.out.println("Dropped: " + remains);
			}
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
		return warrior;
	}
}
