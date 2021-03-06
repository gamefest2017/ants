package com.ibm.sk;

public class WorldConstants {

	public static final int X_BOUNDRY = Integer.parseInt(GameConfig.WIDTH.toString());
	public static final int Y_BOUNDRY = Integer.parseInt(GameConfig.HEIGHT.toString());
	/**
	 * number of TURNS to evaluate game.
	 */
	public static final int TURNS = Integer.parseInt(GameConfig.TURNS.toString());
	/**
	 * the amount of ants in one anthill at the beginning of the world.
	 */
	public static final int INITIAL_ANT_COUNT = 5;
	/**
	 * frequency of food adding.
	 */
	public static final int FOOD_REFILL_FREQUENCY = 5;
	public static final double POPULATION_WAR_FACTOR = 2.0 / 5.0;

	/**
	 * Number of collected foods to breed new ant
	 */
	public static final int NEW_ANT_FOOD_COST = 3;
}