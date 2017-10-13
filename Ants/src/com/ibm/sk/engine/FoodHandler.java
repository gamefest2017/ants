package com.ibm.sk.engine;

import static com.ibm.sk.Main.getTurn;
import static com.ibm.sk.WorldConstans.FOOD_REFILL_FREQUENCY;
import static com.ibm.sk.WorldConstans.X_BOUNDRY;
import static com.ibm.sk.WorldConstans.Y_BOUNDRY;
import static com.ibm.sk.engine.World.placeObject;
import static com.ibm.sk.engine.World.removeObject;

import java.awt.Point;
import java.util.Random;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.Food;
import com.ibm.sk.engine.exceptions.InvalidWorldPositionException;

public final class FoodHandler {

	private FoodHandler() {
	}

	private static final Random RANDOMIZER = new Random();

	public static void dropFood() {
		if (getTurn() % FOOD_REFILL_FREQUENCY == 0) {
			int row;
			int coll;
			do {
				row = RANDOMIZER.nextInt(X_BOUNDRY - 1);
				coll = RANDOMIZER.nextInt(Y_BOUNDRY - 1);
			} while (World.isPositionOccupied(new Point(row, coll)));

			final Food newFood = new Food(World.idSequence++, 1, new Point(row, coll));

			try {
				placeObject(newFood);
			} catch (final InvalidWorldPositionException e) {
				System.out.println("Position had not space, food was not dropped. Position was: " + newFood.getPosition());
			}
		}
	}

	public static void pickUpFood(final AbstractAnt ant, final Food food) {
		if (!ant.hasFood()) {
			ant.pickUpFood(food);
			removeObject(food);
		}
	}
}
