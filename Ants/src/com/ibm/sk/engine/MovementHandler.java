package com.ibm.sk.engine;

import static com.ibm.sk.WorldConstans.X_BOUNDRY;
import static com.ibm.sk.WorldConstans.Y_BOUNDRY;

import java.awt.Point;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.engine.exceptions.MoveException;

public final class MovementHandler {

	private static MovementHandler movementHandler;
	private static PopulationHandler populationHandler;

	private MovementHandler() {
	}

	public static synchronized MovementHandler getInstance() {
		if (movementHandler == null) {
			movementHandler = new MovementHandler();
			populationHandler = PopulationHandler.getInstance();
		}
		return movementHandler;
	}

	public IAnt makeMove(final IAnt ant, final Direction direction) throws MoveException {
		final double newXPos = ant.getPosition().getX() + direction.getPositionChange().getX();
		final double newYPos = ant.getPosition().getY() + direction.getPositionChange().getY();
		if (newXPos < 0 || newXPos > X_BOUNDRY - 1 || newYPos < 0 || newYPos > Y_BOUNDRY - 1) {
			throw new MoveException("Can't go to empty space! I am not an astronaut, I am just a little ant!");
		}
		final Point position = new Point();
		position.setLocation(newXPos, newYPos);

		if (ant instanceof AbstractAnt && ant.getMyHill().getPosition().equals(position)) {
			moveHome(ant.getMyHill(), (AbstractAnt) ant);
		}

		final Object worldObject = World.getWorldObject(position);

		if (worldObject == null || ant.getMyHill().getPosition().equals(position)) {
			System.out.println("I'm moving from [" + ant.getPosition().x + ", " + ant.getPosition().y + "] to "
					+ direction.name() + "[" + position.x + ", " + position.y + "]" + ", out of my way!");
			ant.setPosition(position);
		} else if (worldObject instanceof Food && ant instanceof AbstractAnt) {
			FoodHandler.pickUpFood((AbstractAnt) ant, (Food) worldObject);
			ant.setPosition(position);
		} else if (worldObject instanceof IAnt
				&& ant instanceof AbstractWarrior && ant.isEnemy((IAnt) worldObject)) {
			final AbstractWarrior warrior = (AbstractWarrior) ant;
			moveToEnemyAndKill(warrior, (IAnt) worldObject);
			warrior.setPosition(position);
		} else {
			System.out.println("I will not move to " + direction.name() + "! The place is occupied.");
		}

		return ant;
	}

	private void moveHome(final Hill hill, final AbstractAnt ant) {
		final Food droppedFood = ant.dropFood();
		if (droppedFood != null && droppedFood.getAmount() > 0) {
			populationHandler.incrementFood(hill, droppedFood.getAmount());
		}
	}

	private void moveToEnemyAndDie() {
		// TODO
	}

	private void moveToEnemyAndKill(final AbstractWarrior warrior, final IAnt enemy) {
		PopulationHandler.killAnt(enemy);
		warrior.killed(enemy);
	}
}
