package com.ibm.sk.engine;

import java.awt.Point;
import java.util.Random;

import com.ibm.sk.dto.Food;

public final class FoodHandler {
	
	private static final Random RANDOMIZER = new Random();

	public static void dropFood() {
		Point position = null;

		do {
			int posX = RANDOMIZER.nextInt(World.X_BOUNDRY) + 1;
			int posY = RANDOMIZER.nextInt(World.Y_BOUNDRY) + 1;
			position = new Point(posX, posY);
		} while (World.getWorldObject(position) != null);
		
		Food food = new Food(RANDOMIZER.nextInt(World.FOOD_BOUNDRY) + 1, position);
		World.placeObject(food);
		
		System.out.println("Dropped: " + food);
	}
	
	public static void pickUpFood(final Point position) {
		Object object = World.getWorldObject(position);
		if (object instanceof Food) {
			System.out.println("Picked: " + object);
			World.removeObject(position);
		} else {
			System.out.println("I'm not picking that unknown thing!");
		}
	}
}
