package com.ibm.sk.engine;

import static com.ibm.sk.Main.getTurn;
import static com.ibm.sk.WorldConstans.FOOD_REFILL_FREQUENCY;
import static com.ibm.sk.WorldConstans.X_BOUNDRY;
import static com.ibm.sk.WorldConstans.Y_BOUNDRY;
import static com.ibm.sk.engine.World.getWorldObject;
import static com.ibm.sk.engine.World.placeObject;

import java.awt.Point;
import java.util.Random;

import com.ibm.sk.dto.Ant;
import com.ibm.sk.dto.Food;

public final class FoodHandler {

	private FoodHandler() {
	}

	private static final Random RANDOMIZER = new Random();

	public static void dropFood() {
		if (getTurn() % FOOD_REFILL_FREQUENCY == 0) {
			int row;
			int coll;
			do {
				row = RANDOMIZER.nextInt(X_BOUNDRY);
				coll = RANDOMIZER.nextInt(Y_BOUNDRY);
			} while (getWorldObject(new Point(row, coll)) != null);
			Food newFood = new Food(1, new Point(row, coll));
			placeObject(newFood);
			System.out.println("Dropped: " + newFood);
		}
	}

	public static void pickUpFood(final Ant ant, final Point position) {
		Object object = World.getWorldObject(position);
		if (object instanceof Food) {
			System.out.println("Picked: " + object);
			ant.pickUpFood((Food) object);
			// World.removeObject(position);
		} else {
			System.out.println("I'm not picking that unknown thing!");
		}
	}
}
