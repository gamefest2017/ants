package com.ibm.sk.engine;

import java.awt.Point;
import java.util.EnumMap;
import java.util.Map;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.IWorldObject;
import com.ibm.sk.dto.Vision;
import com.ibm.sk.dto.enums.Direction;
import com.ibm.sk.dto.enums.ObjectType;
import com.ibm.sk.engine.exceptions.MoveException;
import com.ibm.sk.models.WorldBorder;

public final class MovementHandler {

	private final PopulationHandler populationHandler;
	private final World world;
	private final FoodHandler foodHandler;

	public MovementHandler(final World world, final PopulationHandler populationHandler) {
		this.world = world;
		this.populationHandler = populationHandler;
		this.foodHandler = new FoodHandler(this.world);
	}

	public IAnt move(final IAnt ant, final Direction direction) throws MoveException {
		final double newXPos = ant.getPosition().getX() + direction.getPositionChange().getX();
		final double newYPos = ant.getPosition().getY() + direction.getPositionChange().getY();
		final Point destination = new Point((int) newXPos, (int) newYPos);
		if (this.world.isPositionOccupiedByBorder(destination)) {
			throw new MoveException("Can't go to border of the world grid at position " + destination);
		}
		final Point position = new Point();
		position.setLocation(newXPos, newYPos);

		if (ant instanceof AbstractAnt && ant.getMyHill().getPosition().equals(position)) {
			moveHome(ant.getMyHill(), (AbstractAnt) ant);
		}

		final Object worldObject = this.world.getWorldObject(position);

		if (worldObject == null || ant.getMyHill().getPosition().equals(position)) {
			System.out.println("I'm moving from [" + ant.getPosition().x + ", " + ant.getPosition().y + "] to "
					+ direction.name() + "[" + position.x + ", " + position.y + "]" + ", out of my way!");
			ant.setPosition(position);
		} else if (worldObject instanceof Food && ant instanceof AbstractAnt) {
			final AbstractAnt worker = (AbstractAnt) ant;
			final Food food = (Food) worldObject;
			if (!worker.hasFood()) {
				worker.pickUpFood(food);
				this.world.removeObject(food);
			}
			worker.setPosition(position);
		} else if (worldObject instanceof IAnt && ant instanceof AbstractWarrior && ant.isEnemy((IAnt) worldObject)) {
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
			this.populationHandler.incrementFood(hill, droppedFood.getAmount());
		}
	}

	private void moveToEnemyAndKill(final AbstractWarrior warrior, final IAnt enemy) {
		this.populationHandler.killAnt(enemy);
		warrior.killed(enemy);
	}

	ObjectType checkField(final Direction direction, final IAnt ant) {
		ObjectType result = ObjectType.EMPTY_SQUARE;
		final Point point = new Point(ant.getPosition());
		point.translate(direction.getPositionChange().x, direction.getPositionChange().y);
		final IWorldObject foundObject = this.world.getWorldObject(point);
		if (foundObject instanceof AbstractAnt) {
			final AbstractAnt otherAnt = (AbstractAnt) foundObject;
			if (ant.isEnemy(otherAnt) && otherAnt.hasFood()) {
				result = ObjectType.ENEMY_ANT_WITH_FOOD;
			} else if (ant.isEnemy(otherAnt)) {
				result = ObjectType.ENEMY_ANT;
			} else if (otherAnt.hasFood()) {
				result = ObjectType.ANT_WITH_FOOD;
			} else {
				result = ObjectType.ANT;
			}
		} else if (foundObject instanceof AbstractWarrior) {
			final AbstractWarrior warrior = (AbstractWarrior) foundObject;
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

	Vision createVisionGrid(final IAnt ant) {
		final Map<Direction, ObjectType> visionGrid = new EnumMap<>(Direction.class);

		for (final Direction visionDirection : Direction.values()) {
			visionGrid.put(visionDirection, checkField(visionDirection, ant));
		}

		return new Vision(visionGrid);
	}
}
