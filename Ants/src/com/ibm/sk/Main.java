package com.ibm.sk;

import static com.ibm.sk.WorldConstans.TURNS;
import static com.ibm.sk.engine.World.createHill;

import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.enums.HillOrder;
import com.ibm.sk.engine.FoodHandler;
import com.ibm.sk.engine.ProcessExecutor;

public class Main {

	private static int turn;

	public static void main(final String args[]) {
		System.out.println("Game starting...");
		System.out.println("World size: " + WorldConstans.X_BOUNDRY + " x " + WorldConstans.Y_BOUNDRY);
		System.out.println("Turns: " + TURNS);
		final String firstHillName = args.length > 1 ? args[1] : "King of ants";
		final Hill firstHill = createHill(HillOrder.FIRST, firstHillName);
		Hill secondHill = null;
		if (args.length > 2 || true) { // TODO remove true from condition and
										// replace constant with argument on the
										// next line.
			final String secondHillName = "Queen of pants"; // args[2];
			secondHill = createHill(HillOrder.SECOND, secondHillName);
		}
		ProcessExecutor.initGame(firstHill, secondHill);
		final long startTime = System.currentTimeMillis();
		for (turn = 0; turn < TURNS; turn++) {
			ProcessExecutor.execute(firstHill, secondHill);
			FoodHandler.dropFood();
		}
		final long endTime = System.currentTimeMillis();
		System.out.println(firstHill.getName() + " earned score: " + firstHill.getFood());
		for (final IAnt ant : firstHill.getAnts()) {
			System.out.println(ant);
		}
		if (secondHill != null) {
			System.out.println(secondHill.getName() + " earned score: " + secondHill.getFood());
			for (final IAnt ant : secondHill.getAnts()) {
				System.out.println(ant);
			}
		}
		System.out.println("Game duration: " + turn + " turns, in " + (endTime - startTime) + " ms");

	}

	public static int getTurn() {
		return turn;
	}
}
