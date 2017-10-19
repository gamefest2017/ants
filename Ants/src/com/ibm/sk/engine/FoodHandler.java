package com.ibm.sk.engine;

import static com.ibm.sk.WorldConstants.FOOD_REFILL_FREQUENCY;
import static com.ibm.sk.WorldConstants.X_BOUNDRY;
import static com.ibm.sk.WorldConstants.Y_BOUNDRY;

import java.awt.Point;
import java.util.Random;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.Food;
import com.ibm.sk.engine.exceptions.InvalidWorldPositionException;

public final class FoodHandler {

	private final World world;

	public FoodHandler(final World world) {
		this.world = world;
	}

	private static final Random RANDOMIZER = new Random();

	public void dropFood(final int turn) {
		if (turn % FOOD_REFILL_FREQUENCY == 0) {
			int row;
			int column;
			final Point position = new Point(0, 0);
			do {
				row = RANDOMIZER.nextInt(X_BOUNDRY - 1);
				column = RANDOMIZER.nextInt(Y_BOUNDRY - 1);
				position.setLocation(row, column);
			} while (this.world.isPositionOccupied(position) || this.world.isHillPosition(position));

			final Food newFood = new Food(this.world.idSequence++, 1, position);

			try {
				this.world.placeObject(newFood);
			} catch (final InvalidWorldPositionException e) {
				System.out.println("Position had not space, food was not dropped. Position was: " + newFood.getPosition());
			}
		}
	}
}
