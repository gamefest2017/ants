package com.ibm.sk.engine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.ibm.sk.WorldConstants;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.IWorldObject;
import com.ibm.sk.dto.enums.HillOrder;
import com.ibm.sk.engine.exceptions.InvalidWorldPositionException;
import com.ibm.sk.models.WorldBorder;

public final class World {
	protected long idSequence = 0L;
	private final List<IWorldObject> worldObjects = new ArrayList<>();
	private final List<IWorldObject> deadObjects = new ArrayList<>();
	private final List<Hill> hills = new ArrayList<>();

	public World() {
	}

	protected List<IWorldObject> getWorldObjects() {
		return this.worldObjects;
	}

	protected List<IWorldObject> getDeadObjects() {
		return this.deadObjects;
	}

	protected IWorldObject getWorldObject(final Point position) {
		IWorldObject result = null;

		for (final IWorldObject worldObject : this.worldObjects) {
			if (worldObject.getPosition().equals(position)) {
				result = worldObject;
				System.out.println("Found world object: " + result + " on position: " + position);
				break;
			}
		}

		return result;
	}

	protected void placeObject(final IWorldObject worldObject) throws InvalidWorldPositionException {
		if (worldObject.getPosition() == null) {
			throw new InvalidWorldPositionException("Position for given world object is not set" + worldObject);
		}

		System.out.println("New object added to world: " + worldObject);
		this.worldObjects.add(worldObject);
	}

	protected void removeObject(final IWorldObject worldObject) {
		if (!this.worldObjects.remove(worldObject)) {
			System.out.println("Nothing was removed. Object is unknown in the world: " + worldObject);
		}
	}

	protected void removeObject(final Point position) {
		for (final IWorldObject worldObject : this.worldObjects) {
			if (worldObject.getPosition().equals(position)) {
				this.worldObjects.remove(worldObject);
				if (worldObject instanceof IAnt) {
					this.deadObjects.add(worldObject);
				}
				break;
			}
		}

		System.out.println("No object on that position, nothing was removed.");
	}

	public boolean isPositionOccupiedByBorder(final Point position) {
		boolean isOccupied = false;

		for (final IWorldObject worldObject : this.worldObjects) {
			if (worldObject instanceof WorldBorder && worldObject.getPosition().equals(position)) {
				isOccupied = true;
				System.out.println("Position is occupied: " + position);
				break;
			}
		}

		return isOccupied;
	}

	public void createWorldBorder() {
		try {
			for (int i = 0; i < WorldConstants.X_BOUNDRY; i++) {
				placeObject(new WorldBorder(this.idSequence++, new Point(i, 0)));
				placeObject(new WorldBorder(this.idSequence++, new Point(i, WorldConstants.Y_BOUNDRY - 1)));
			}
			for (int i = 1; i < WorldConstants.Y_BOUNDRY - 1; i++) {
				placeObject(new WorldBorder(this.idSequence++, new Point(0, i)));
				placeObject(new WorldBorder(this.idSequence++, new Point(WorldConstants.X_BOUNDRY - 1, i)));
			}
		} catch (final InvalidWorldPositionException e) {
			System.out.println("Invalid position.");
		}
	}

	public boolean isPositionOccupied(final Point position) {
		boolean isOccupied = false;

		for (final IWorldObject worldObject : this.worldObjects) {
			if (worldObject.getPosition().equals(position)) {
				isOccupied = true;
				System.out.println("Position is occupied: " + position);
				break;
			}
		}

		return isOccupied;
	}

	public boolean isHillPosition(final Point position) {
		boolean isHillPosition = false;

		for (final IWorldObject worldObject : this.hills) {
			if (worldObject.getPosition().equals(position)) {
				isHillPosition = true;
				System.out.println("Found hill on given position: " + position);
				break;
			}
		}

		return isHillPosition;
	}

	public Hill createHill(final HillOrder order, final String name) {
		final long hillId = this.idSequence++;
		final Hill hill = new Hill(name, new Point(order.getOrder() * WorldConstants.X_BOUNDRY + order.getXOffset(),
				WorldConstants.Y_BOUNDRY / 2));
		hill.setId(hillId);

		this.hills.add(hill);

		return hill;
	}

	public List<IWorldObject> getAllFoods() {
		final List<IWorldObject> result = new ArrayList<>();

		for (final IWorldObject worldObject : this.worldObjects) {
			if (worldObject instanceof Food) {
				result.add(worldObject);
			}
		}

		return result;
	}

	public List<IWorldObject> getWorldObjectsToMove() {
		final List<IWorldObject> result = new ArrayList<>();

		for (final IWorldObject worldObject : this.worldObjects) {
			if (worldObject instanceof IAnt) {
				result.add(worldObject);
			}
		}

		return result;
	}

}
