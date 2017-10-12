package com.ibm.sk.engine;

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
		final Point destination = new Point((int) newXPos, (int) newYPos);
		if (World.isPositionOccupiedByBorder(destination)) {
			throw new MoveException("Can't go to border of the world grid at position " + destination);
		}
		final Point position = new Point();
		position.setLocation(newXPos, newYPos);

		if (ant.getMyHill().getPosition().equals(position)) {
			moveHome(ant.getMyHill(), ant);
		}

		final Object worldObject = World.getWorldObject(position);

		if (worldObject == null || ant.getMyHill().getPosition().equals(position)) {
			System.out.println("I'm moving from [" + ant.getPosition().x + ", " + ant.getPosition().y + "] to "
					+ direction.name() + "[" + position.x + ", " + position.y + "]" + ", out of my way!");
			ant.setPosition(position);
		} else if (worldObject instanceof Food && ant instanceof AbstractAnt) {
			FoodHandler.pickUpFood(ant, (Food) worldObject);
			ant.setPosition(position);
		} else if (worldObject instanceof IAnt && ant instanceof AbstractWarrior && ant.isEnemy((IAnt) worldObject)) {
			final AbstractWarrior warrior = (AbstractWarrior) ant;
			moveToEnemyAndKill(warrior, (IAnt) worldObject);
			warrior.setPosition(position);
		} else {
			System.out.println("I will not move to " + direction.name() + "! The place is occupied.");
		}

		return ant;
	}

	private void moveHome(final Hill hill, final IAnt ant) {
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
