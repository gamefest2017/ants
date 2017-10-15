package com.ibm.sk.handlers;

import static com.ibm.sk.WorldConstans.TURNS;
import static com.ibm.sk.engine.World.createHill;

import com.ibm.sk.WorldConstans;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.enums.HillOrder;
import com.ibm.sk.engine.FoodHandler;
import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.engine.World;
import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;

public class GameMenuHandler implements GuiEventListener {

	private static int turn;
	private final ProcessExecutor executor;

	public GameMenuHandler(final ProcessExecutor executor) {
		this.executor = executor;
	}

	@Override
	public void actionPerformed(final GuiEvent event) {
		if (GuiEvent.EventTypes.SINGLE_PLAY_START.name().equals(event.getType().name())) {
			startSinglePlayer(event.getData());
		} else if (GuiEvent.EventTypes.DOUBLE_PLAY_START.name().equals(event.getType().name())) {
			final String hillNames = event.getData();
			final int separatorPos = hillNames.indexOf(GuiEvent.HLL_NAMES_SEPARATOR);
			startDoublePlayer(hillNames.substring(0, separatorPos) + "1", hillNames.substring(separatorPos + 1) + "2");
		}
	}

	public void startSinglePlayer(final String hillName) {
		System.out.println("Single player game starting...");
		System.out.println("World size: " + WorldConstans.X_BOUNDRY + " x " + WorldConstans.Y_BOUNDRY);
		System.out.println("Turns: " + TURNS);

		World.createWorldBorder();
		final Hill hill = createHill(HillOrder.FIRST, hillName);
		this.executor.initGame(hill, null);
		final long startTime = System.currentTimeMillis();
		for (turn = 0; turn < TURNS; turn++) {
			ProcessExecutor.execute(hill, null);
			FoodHandler.dropFood();
		}
		final long endTime = System.currentTimeMillis();
		System.out.println(hill.getName() + " earned score: " + hill.getFood());
		for (final IAnt ant : hill.getAnts()) {
			System.out.println(ant);
		}
		System.out.println("Game duration: " + turn + " turns, in " + (endTime - startTime) + " ms");
	}

	public void startDoublePlayer(final String firstHillName, final String secondHillName) {
		System.out.println("Duel starting...");
		System.out.println("World size: " + WorldConstans.X_BOUNDRY + " x " + WorldConstans.Y_BOUNDRY);
		System.out.println("Turns: " + TURNS);

		World.createWorldBorder();
		final Hill firstHill = createHill(HillOrder.FIRST, firstHillName);
		final Hill secondHill = createHill(HillOrder.SECOND, secondHillName);
		this.executor.initGame(firstHill, secondHill);

		final long startTime = System.currentTimeMillis();
		for (turn = 0; turn < TURNS; turn++) {
			ProcessExecutor.execute(firstHill, secondHill);
			FoodHandler.dropFood();
		}
		final long endTime = System.currentTimeMillis();
		printScore(firstHill);
		printScore(secondHill);
		System.out.println("Game duration: " + turn + " turns, in " + (endTime - startTime) + " ms");
	}

	private static void printScore(final Hill hill) {
		System.out.println(hill.getName() + " earned score: " + hill.getFood());
	}

}
