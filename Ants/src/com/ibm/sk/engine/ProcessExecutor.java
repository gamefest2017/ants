package com.ibm.sk.engine;

import static com.ibm.sk.engine.World.getWorldObjects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import com.ibm.sk.Main;
import com.ibm.sk.WorldConstans;
import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.Vision;
import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.engine.exceptions.MoveException;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;

public final class ProcessExecutor {

	private ProcessExecutor() {}
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
		guiConnector.initGame(gameData);
		guiConnector.placeGuiObject(team1);
		guiConnector.placeGuiObjects(new ArrayList<>(team1.getAnts()));
		if (team2 != null) {
			guiConnector.placeGuiObject(team2);
			guiConnector.placeGuiObjects(new ArrayList<>(team2.getAnts()));
		}
	}

	private static void singleStep(final IAnt ant) {
		System.out.println("Turn:" + Main.getTurn() + "Ant " + ant.getId() + " said:");
		final Vision vision = new Vision(createVisionGrid(ant.getPosition()));
		final Direction direction = ant.move(vision);
		final MovementHandler movementHandler = new MovementHandler();

		if (Direction.NO_MOVE.equals(direction)) {
			System.out.println("I'm not moving. I like this place!");
		} else {
			try {
//				boolean hadFood = false;

//				if (ant instanceof AbstractAnt) {
//					hadFood = ant.hasFood();
//				}

				movementHandler.makeMove(ant, direction);

//				if (ant instanceof AbstractAnt) {
//					if (!hadFood && ant.hasFood()) {
//						guiConnector.removeGuiObject(ant.getFood());
//					}
//				}
			} catch (final MoveException e) {
				System.out.println("I cannot move to " + direction.name() + "! That would hurt me!");
			}
		}
	}

	private static Map<Direction, Object> createVisionGrid(final Point visionPosition) {
		final Map<Direction, Object> visionGrid = new EnumMap<>(Direction.class);

		for (final Direction visionDirection : Direction.values()) {
			visionGrid.put(visionDirection, checkField(visionDirection, new Point(visionPosition)));
		}

		return visionGrid;
	}

	private static Object checkField(final Direction direction, final Point point) {
		point.translate(direction.getPositionChange().x, direction.getPositionChange().y);
		final Object foundObject = World.getWorldObject(point);
//		System.out.println("I see on position [" + point.x + ", " + point.y + "] "
//				+ (foundObject == null ? "nothing" : foundObject));
		return foundObject;
	}
}
