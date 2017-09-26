package com.ibm.sk.engine;

import java.awt.Point;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ibm.sk.dto.Ant;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.Vision;
import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.engine.exceptions.MoveException;

public final class ProcessExecutor {

	private ProcessExecutor() {}


	public static void execute() {

		for (final Entry<Point, Object> entry : World.getWorld().entrySet()) {
			if (entry.getValue() instanceof Hill) {
				for (final Ant ant : ((Hill) entry.getValue()).getAnts()) {
					singleStep(ant);
				}

			} else if (entry.getValue() instanceof Ant) {
				singleStep((Ant) entry.getValue());

			}
		}
	}

	private static void singleStep(final Ant ant) {
		System.out.println("Ant " + ant.getId() + " said:");
		final Vision vision = new Vision(createVisionGrid(ant.getPosition()));
		final Direction direction = ant.move(vision);
		final MovementHandler movementHandler = new MovementHandler();

		if (Direction.NO_MOVE.equals(direction)) {
			System.out.println("I'm not moving. I like this place!");
		} else {
			try {
				movementHandler.makeMove(ant, direction);
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
		System.out.println("I see on position [" + point.x + ", " + point.y + "] "
				+ (foundObject == null ? "nothing" : foundObject));
		return foundObject;
	}
}
