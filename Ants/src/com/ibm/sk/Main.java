package com.ibm.sk;

import static com.ibm.sk.WorldConstans.TURNS;
import static com.ibm.sk.engine.World.createHill;

import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.enums.HillOrder;
import com.ibm.sk.engine.FoodHandler;
import com.ibm.sk.engine.ProcessExecutor;

public class Main {

	private static int turn;

	public static void main(final String args[]) {
		System.out.println("Game starting...");
		System.out.println("World size: " + WorldConstans.X_BOUNDRY + " x " + WorldConstans.Y_BOUNDRY);
		System.out.println("Turns: " + TURNS);
		final Hill hill = createHill(HillOrder.FIRST, "King of ants");
		final long startTime = System.currentTimeMillis();
		for (turn = 0; turn < TURNS; turn++) {
			ProcessExecutor.execute();
			FoodHandler.dropFood();
		}
		final long endTime = System.currentTimeMillis();
		System.out.println(hill.getName() + " earned score: " + hill.getPopulation());
		System.out.println("Game duration: " + turn + " turns, in " + (endTime - startTime) + " ms");
	}

	public static int getTurn() {
		return turn;
	}
}
