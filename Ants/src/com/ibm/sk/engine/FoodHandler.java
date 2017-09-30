package com.ibm.sk.engine;

import static com.ibm.sk.Main.getTurn;
import static com.ibm.sk.WorldConstans.FOOD_REFILL_FREQUENCY;
import static com.ibm.sk.WorldConstans.X_BOUNDRY;
import static com.ibm.sk.WorldConstans.Y_BOUNDRY;
import static com.ibm.sk.engine.World.placeObject;
import static com.ibm.sk.engine.World.removeObject;

import java.awt.Point;
import java.util.Random;

import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.IAnt;
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
				row = RANDOMIZER.nextInt(X_BOUNDRY);
				coll = RANDOMIZER.nextInt(Y_BOUNDRY);
			} while (World.isPositionOccupied(new Point(row, coll)));
			
			final Food newFood = new Food(World.idSequence++, 1, new Point(row, coll));
			
			try {
				placeObject(newFood);
				System.out.println("Dropped: " + newFood);
			} catch (InvalidWorldPositionException e) {
				System.out.println("Position had not space, food was not dropped. Position was: " + newFood.getPosition());
			}
		}
	}

	public static void pickUpFood(final IAnt ant, final Point position) {
		final Object object = World.getWorldObject(position);
		if (object instanceof Food) {
			ant.pickUpFood((Food) object);
		} else {
			System.out.println("I'm not picking that unknown thing!");
		}
	}
	
	public static void pickUpFood(final IAnt ant, final Food food) {
		ant.pickUpFood(food);
		removeObject(food);
		System.out.println("I'm not picking that unknown thing!");
	}
}
