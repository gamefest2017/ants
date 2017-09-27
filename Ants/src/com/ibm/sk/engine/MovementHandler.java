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

	public void makeMove(final IAnt ant, final Direction direction) throws MoveException {
		final double newXPos = ant.getPosition().getX() + direction.getPositionChange().getX();
		final double newYPos = ant.getPosition().getY() + direction.getPositionChange().getY();
		if (newXPos < 0 || newXPos > X_BOUNDRY || newYPos < 0 || newYPos > Y_BOUNDRY) {
			throw new MoveException("Can't go to empty space! I am not an astronaut, I am just a little ant!");
		}
		final Point position = new Point();
		position.setLocation(newXPos, newYPos);

		if (ant.getMyHill().getPosition().equals(position)) {
			moveHome(ant.getMyHill(), ant);
		}
		final Object worldObject = World.getWorldObject(position);

		if (worldObject == null) {
			System.out.println("I'm moving to " + direction.name() + ", out of my way!");
			World.removeObject(ant.getPosition());
			ant.setPosition(position);
			World.placeObject(ant);
		} else if (worldObject instanceof Food && ant instanceof AbstractAnt) {
			World.removeObject(ant.getPosition());
			FoodHandler.pickUpFood(ant, position);
			ant.setPosition(position);
			World.placeObject(ant);
		} else if (worldObject instanceof IAnt && ant instanceof AbstractWarrior && ant.isEnemy((IAnt) worldObject)) {
			final AbstractWarrior warrior = (AbstractWarrior) ant;
			World.removeObject(warrior.getPosition());
			moveToEnemyAndKill(warrior, (IAnt) worldObject);
			warrior.setPosition(position);
			World.placeObject(warrior);
		} else {
			System.out.println("I will not move to " + direction.name() + "! The place is occupied.");
		}
	}

	private void moveHome(final Hill hill, final IAnt ant) {
		final Food droppedFood = ant.dropFood();
		if (droppedFood != null && droppedFood.getAmount() > 0) {
			hill.incrementPopulation(droppedFood.getAmount());
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
