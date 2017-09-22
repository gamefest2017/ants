package com.ibm.sk.engine;

import java.awt.Point;

import com.ibm.sk.dto.Ant;
import com.ibm.sk.dto.Hill;

public class PopulationHandler {
	private static int antcounter = 1;

	public Ant breedAnt(final Hill hill) {
		System.out.println("Welcome new creature of this world! From now on you belong to " 
				+ hill.getName() + "! But don't be affraid, you are not alone, he has other " + hill.getPopulation() + " ants.");
		Point homePosition = new Point(hill.getPosition());
		Ant ant = new Ant(antcounter++, homePosition, hill);
		World.placeObject(ant);
		hill.incrementPopulation(1);
		return ant;
	}
	
	public void killAnt(final Ant ant) {
		System.out.println("You shall be no more in this world! Good bye forewer dear ant " + ant.getId());
		ant.getMyHill().decrementPopulation(1);
		World.removeObject(ant.getPosition());
		ant.getMyHill().getAnts().remove(ant);
	}
}
