package com.ibm.sk.engine;

import static com.ibm.sk.WorldConstans.INITIAL_ANT_COUNT;
import static com.ibm.sk.WorldConstans.POPULATION_WAR_FACTOR;
import static com.ibm.sk.engine.World.getWorldObjects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import com.ibm.sk.Main;
import com.ibm.sk.WorldConstans;
import com.ibm.sk.dto.Ant;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.IWorldObject;
import com.ibm.sk.dto.Vision;
import com.ibm.sk.dto.Warrior;
import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.dto.enums.ObjectType;
import com.ibm.sk.engine.exceptions.MoveException;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.models.WorldBorder;

public final class ProcessExecutor {

	private ProcessExecutor() {
	}

	private static final GuiConnector guiConnector = new GuiConnector();

	public static void execute(final Hill hill) {

		for (final IAnt ant : hill.getAnts()) {
			singleStep(ant);
		}
		guiConnector.placeGuiObjects(getWorldObjects());
		guiConnector.showScore(hill.getName(), hill.getFood());
	}

	public static void initGame(final Hill team1, final Hill team2) {
		final CreateGameData gameData = new CreateGameData();
		gameData.setWidth(WorldConstans.X_BOUNDRY);
		gameData.setHeight(WorldConstans.Y_BOUNDRY);
		gameData.setTeams(new String[] { team1.getName(), team2 != null ? team2.getName() : "" });
		initAnts(team1);
		guiConnector.initGame(gameData);
		guiConnector.placeGuiObject(team1);
		guiConnector.placeGuiObjects(new ArrayList<>(team1.getAnts()));
		if (team2 != null) {
			initAnts(team2);
			guiConnector.placeGuiObject(team2);
			guiConnector.placeGuiObjects(new ArrayList<>(team2.getAnts()));
		}
	}

	private static void initAnts(final Hill hill) {
		for (int i = 0; i < Math.ceil(INITIAL_ANT_COUNT * (1.0 - POPULATION_WAR_FACTOR)); i++) {
			hill.getAnts().add(PopulationHandler.breedAnt(hill));
		}
		for (int i = 0; i < Math.floor(INITIAL_ANT_COUNT * POPULATION_WAR_FACTOR); i++) {
			hill.getAnts().add(PopulationHandler.breedWarrior(hill));
		}
	}

	private static void singleStep(final IAnt ant) {
		System.out.println("Turn:" + Main.getTurn() + "Ant " + ant.getId() + " said:");
		final Vision vision = new Vision(createVisionGrid(ant));
		final Direction direction = ant.move(vision);
		final MovementHandler movementHandler = MovementHandler.getInstance();

		if (Direction.NO_MOVE.equals(direction)) {
			System.out.println("I'm not moving. I like this place!");
		} else {
			try {
				// boolean hadFood = false;

				// if (ant instanceof AbstractAnt) {
				// hadFood = ant.hasFood();
				// }

				movementHandler.makeMove(ant, direction);

				// if (ant instanceof AbstractAnt) {
				// if (!hadFood && ant.hasFood()) {
				// guiConnector.removeGuiObject(ant.getFood());
				// }
				// }
			} catch (final MoveException e) {
				System.out.println("I cannot move to " + direction.name() + "! That would hurt me!");
			}
		}
	}

	private static Map<Direction, ObjectType> createVisionGrid(final IAnt ant) {
		final Map<Direction, ObjectType> visionGrid = new EnumMap<>(Direction.class);

		for (final Direction visionDirection : Direction.values()) {
			visionGrid.put(visionDirection, checkField(visionDirection, ant));
		}

		return visionGrid;
	}

	private static ObjectType checkField(final Direction direction, final IAnt ant) {
		ObjectType result = ObjectType.EMPTY_SQUARE;
		final Point point = new Point(ant.getPosition());
		point.translate(direction.getPositionChange().x, direction.getPositionChange().y);
		final IWorldObject foundObject = World.getWorldObject(point);
		if (foundObject instanceof Ant) {
			final Ant otherAnt = (Ant) foundObject;
			if (ant.isEnemy(otherAnt) && otherAnt.hasFood()) {
				result = ObjectType.ENEMY_ANT_WITH_FOOD;
			} else if (ant.isEnemy(otherAnt)) {
				result = ObjectType.ENEMY_ANT;
			} else if (otherAnt.hasFood()) {
				result = ObjectType.ANT_WITH_FOOD;
			} else {
				result = ObjectType.ANT;
			}
		} else if (foundObject instanceof Warrior) {
			final Warrior warrior = (Warrior) foundObject;
			if (ant.isEnemy(warrior)) {
				result = ObjectType.ENEMY_WARRIOR;
			} else {
				result = ObjectType.WARRIOR;
			}
		} else if (foundObject instanceof Hill) {
			final Hill hill = (Hill) foundObject;
			if (hill.equals(ant)) {
				result = ObjectType.HILL;
			} else {
				result = ObjectType.ENEMY_HILL;
			}
		} else if (foundObject instanceof Food) {
			result = ObjectType.FOOD;
		} else if (foundObject instanceof WorldBorder) {
			result = ObjectType.BORDER;
		}
		return result;
	}
}
