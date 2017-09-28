package com.ibm.sk;

public class WorldConstans {

	public static final int X_BOUNDRY = 15;
	public static final int Y_BOUNDRY = 10;
	/**
	 * number of TURNS to evaluate game.
	 */
	public static final int TURNS = 50;
	/**
	 * the amount of ants in one anthill at the beginning of the world.
	 */
	public static final int INITIAL_ANT_COUNT = 5;
	/**
	 * frequency of food adding.
	 */
	public static final int FOOD_REFILL_FREQUENCY = 5;
	/**
	 * number of ants in the hill at the beginning of the game.
	 */
	public static final int ANTS_START_POPULATION = 5;
	public static final double POPULATION_WAR_FACTOR = 2.0 / 5.0;
	/**
	 * File where to serialize the world per step.
	 */
	public static final String FILE_NAME = "traciking.ser";
}