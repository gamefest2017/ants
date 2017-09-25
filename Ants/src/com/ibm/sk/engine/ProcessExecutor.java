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

        for (Entry<Point, Object> entry : World.getWorld().entrySet()) {
            if (entry.getValue() instanceof Hill) {
                for (Ant ant : ((Hill) entry.getValue()).getAnts()) {
                    singleStep(ant);
                }

            } else if (entry.getValue() instanceof Ant) {
                singleStep((Ant) entry.getValue());

            }
        }
    }

    private static void singleStep(final Ant ant) {
		System.out.println("Ant " + ant.getId() + " said:");
		Vision vision = new Vision(createVisionGrid(ant.getPosition()));
		Direction direction = ant.move(vision);
		MovementHandler movementHandler = new MovementHandler();

		if (Direction.NO_MOVE.equals(direction)) {
			System.out.println("I'm not moving. I like this place!");
		} else {
			try {
				movementHandler.makeMove(ant, direction);
			} catch (MoveException e) {
				System.out.println("I cannot move to " + direction.name() + "! That would hurt me!");
			}
		}
	}

    private static Map<Direction, Object> createVisionGrid(final Point visionPosition) {
        Map<Direction, Object> visionGrid = new EnumMap<>(Direction.class);

        for (Direction visionDirection : Direction.values()) {
            visionGrid.put(visionDirection, checkField(visionDirection, visionPosition));
        }

        return visionGrid;
    }

    private static Object checkField(final Direction direction, final Point point) {
        double newXPos = point.getX() + direction.getPositionChange().getX();
        double newYPos = point.getY() + direction.getPositionChange().getY();

        Point visionPosition = new Point(0, 0);
        visionPosition.setLocation(newXPos, newYPos);

        Object foundObject = World.getWorldObject(visionPosition);

        System.out.println("I see on position " + newXPos + "," + newYPos + (foundObject == null ? " nothing" : foundObject));

        return foundObject;
    }
}
