package com.ibm.sk.engine;

import static com.ibm.sk.WorldConstants.TURNS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import com.ibm.sk.WorldConstants;
import com.ibm.sk.ant.AntLoader;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.Vision;
import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.dto.enums.HillOrder;
import com.ibm.sk.engine.exceptions.MoveException;
import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;

public final class ProcessExecutor {

	public static GuiConnector guiConnector;
	final World world;
	private final MovementHandler movementHandler;
	private final PopulationHandler populationHandler;
	private final FoodHandler foodHandler;

	public ProcessExecutor(final GUIFacade FACADE) {
		guiConnector = new GuiConnector(FACADE);
		this.world = new World();
		this.populationHandler = new PopulationHandler(this.world, AntLoader.getImplementations());
		this.movementHandler = new MovementHandler(this.world, this.populationHandler);
		this.foodHandler = new FoodHandler(this.world);
	}

	private void singleTurn(final Hill firstHill, final Hill secondHill, final int turn) {
		System.out.println("Turn: " + turn);
		final Iterator<IAnt> first = firstHill.getAnts().iterator();
		final Iterator<IAnt> second = secondHill == null ? Collections.emptyIterator()
				: secondHill.getAnts().iterator();
		guiConnector.placeGuiObjects(this.world.getAllFoods());

		while (first.hasNext() || second.hasNext()) {
			IAnt ant = null;
			if (first.hasNext()) {
				ant = first.next();
				singleStep(ant);
			}
			if (second.hasNext()) {
				ant = second.next();
				singleStep(ant);
			}
		}
		guiConnector.placeGuiObjects(this.world.getWorldObjectsToMove());
		guiConnector.removeGuiObjects(this.world.getDeadObjects());
		this.world.getDeadObjects().clear();
		guiConnector.showScore(firstHill.getName(), firstHill.getFood(), turn + 1, WorldConstants.TURNS);
		if (secondHill != null) {
			guiConnector.showScore(secondHill.getName(), secondHill.getFood(), turn + 1, WorldConstants.TURNS);
		}
	}

	private void initGame(final Hill team1, final Hill team2) {
		final CreateGameData gameData = new CreateGameData();
		gameData.setWidth(WorldConstants.X_BOUNDRY);
		gameData.setHeight(WorldConstants.Y_BOUNDRY);
		gameData.setTeams(new String[] { team1.getName(), team2 != null ? team2.getName() : "" });
		guiConnector.initGame(gameData);
		guiConnector.placeGuiObjects(this.world.getWorldObjects());
		this.populationHandler.init(team1, WorldConstants.INITIAL_ANT_COUNT, WorldConstants.POPULATION_WAR_FACTOR);
		guiConnector.placeGuiObject(team1);
		guiConnector.placeGuiObjects(new ArrayList<>(team1.getAnts()));
		if (team2 != null) {
			this.populationHandler.init(team2, WorldConstants.INITIAL_ANT_COUNT, WorldConstants.POPULATION_WAR_FACTOR);
			guiConnector.placeGuiObject(team2);
			guiConnector.placeGuiObjects(new ArrayList<>(team2.getAnts()));
		}
	}

	private void singleStep(final IAnt ant) {
		System.out.println("Ant " + ant.getId() + " said:");
		final Vision vision = this.movementHandler.createVisionGrid(ant);
		final Direction direction = ant.move(vision);

		if (Direction.NO_MOVE.equals(direction)) {
			System.out.println("I'm not moving. I like this place!");
		} else {
			try {
				this.movementHandler.move(ant, direction);
			} catch (final MoveException e) {
				System.out.println("I cannot move to " + direction.name() + "! That would hurt me!");
			}
		}
	}

	public Map<String, Integer> run(final String firstHillName, final String secondHillName) {
		System.out.println("Duel starting...");
		System.out.println("World size: " + WorldConstants.X_BOUNDRY + " x " + WorldConstants.Y_BOUNDRY);
		System.out.println("Turns: " + TURNS);

		this.world.createWorldBorder();
		final Hill firstHill = this.world.createHill(HillOrder.FIRST, firstHillName);
		Hill secondHill = null;
		if (secondHillName != null) {
			secondHill = this.world.createHill(HillOrder.SECOND, secondHillName);
		}
		initGame(firstHill, secondHill);

		final long startTime = System.currentTimeMillis();
		for (int turn = 0; turn < TURNS; turn++) {
			singleTurn(firstHill, secondHill, turn);
			this.foodHandler.dropFood(turn);
		}
		final long endTime = System.currentTimeMillis();
		final HashMap<String, Integer> result = new HashMap<>();
		System.out.println(firstHill.getName() + " earned score: " + firstHill.getFood());
		result.put(firstHill.getName(), Integer.valueOf(firstHill.getFood()));
		if (secondHillName != null) {
			System.out.println(secondHill.getName() + " earned score: " + secondHill.getFood());
			result.put(secondHill.getName(), Integer.valueOf(secondHill.getFood()));
			if (firstHill.getFood() == secondHill.getFood()) {
				final Function<Hill, Stream<IAnt>> itsAnts = hill -> hill.getAnts().stream();
				final Function<Stream<IAnt>, Stream<AbstractWarrior>> onlyWarriors = ants -> ants.filter(AbstractWarrior.class::isInstance)
						.map(AbstractWarrior.class::cast);
				final Function<Stream<AbstractWarrior>, Integer> sumOfKills = warriors -> Integer
						.valueOf(warriors.mapToInt(AbstractWarrior::getKills).sum());
				final Function<Hill, Integer> killsBy = hill -> itsAnts.andThen(onlyWarriors).andThen(sumOfKills)
						.apply(hill);
				final Comparator<Hill> byKills = (one, other) -> killsBy.apply(one).compareTo(killsBy.apply(other));
				final String winner = Stream.of(firstHill, secondHill)
						.max(byKills).orElse(secondHill)
						.getName();
				result.put(winner, Integer.valueOf(result.get(winner).intValue() + 1));
			}

		}
		System.out.println("Game duration: " + TURNS + " turns, in " + (endTime - startTime) + " ms");
		return result;
	}

}
