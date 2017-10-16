package com.ibm.sk.engine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.ibm.sk.WorldConstans;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.IWorldObject;
import com.ibm.sk.dto.enums.HillOrder;
import com.ibm.sk.engine.exceptions.InvalidWorldPositionException;
import com.ibm.sk.models.WorldBorder;

public final class World {
	protected static long idSequence = 0L;
	private static List<IWorldObject> worldObjects = new ArrayList<>();
	private static List<IWorldObject> deadObjects = new ArrayList<>();
	private static List<Hill> hills = new ArrayList<>();

	private World() {
	}

	protected static List<IWorldObject> getWorldObjects() {
		return worldObjects;
	}

	protected static List<IWorldObject> getDeadObjects() {
		return deadObjects;
	}

	protected static IWorldObject getWorldObject(final Point position) {
		IWorldObject result = null;

		for (final IWorldObject worldObject : worldObjects) {
			if (worldObject.getPosition().equals(position)) {
				result = worldObject;
				System.out.println("Found world object: " + result + " on position: " + position);
				break;
			}
		}

		return result;
	}

	protected static void placeObject(final IWorldObject worldObject) throws InvalidWorldPositionException {
		if (worldObject.getPosition() == null) {
			throw new InvalidWorldPositionException("Position for given world object is not set" + worldObject);
		}

		System.out.println("New object added to world: " + worldObject);
		worldObjects.add(worldObject);
	}

	protected static void removeObject(final IWorldObject worldObject) {
		if (!worldObjects.remove(worldObject)) {
			System.out.println("Nothing was removed. Object is unknown in the world: " + worldObject);
		}
	}

	protected static void removeObject(final Point position) {
		for (final IWorldObject worldObject : worldObjects) {
			if (worldObject.getPosition().equals(position)) {
				worldObjects.remove(worldObject);
				if (worldObject instanceof IAnt) {
					deadObjects.add(worldObject);
				}
				break;
			}
		}

		System.out.println("No object on that position, nothing was removed.");
	}

	public static boolean isPositionOccupiedByBorder(final Point position) {
		boolean isOccupied = false;

		for (final IWorldObject worldObject : worldObjects) {
			if (worldObject instanceof WorldBorder && worldObject.getPosition().equals(position)) {
				isOccupied = true;
				System.out.println("Position is occupied: " + position);
				break;
			}
		}

		return isOccupied;
	}

	public static void createWorldBorder() {
		try {
			for (int i = 0; i < WorldConstans.X_BOUNDRY; i++) {
				placeObject(new WorldBorder(World.idSequence++, new Point(i, 0)));
				placeObject(new WorldBorder(World.idSequence++, new Point(i, WorldConstans.Y_BOUNDRY - 1)));
			}
			for (int i = 1; i < WorldConstans.Y_BOUNDRY - 1; i++) {
				placeObject(new WorldBorder(World.idSequence++, new Point(0, i)));
				placeObject(new WorldBorder(World.idSequence++, new Point(WorldConstans.X_BOUNDRY - 1, i)));
			}
		} catch (final InvalidWorldPositionException e) {
			System.out.println("Invalid position.");
		}
	}

	public static boolean isPositionOccupied(final Point position) {
		boolean isOccupied = false;

		for (final IWorldObject worldObject : worldObjects) {
			if (worldObject.getPosition().equals(position)) {
				isOccupied = true;
				System.out.println("Position is occupied: " + position);
				break;
			}
		}

		return isOccupied;
	}

	public static boolean isHillPosition(final Point position) {
		boolean isHillPosition = false;

		for (final IWorldObject worldObject : hills) {
			if (worldObject.getPosition().equals(position)) {
				isHillPosition = true;
				System.out.println("Found hill on given position: " + position);
				break;
			}
		}

		return isHillPosition;
	}

	public static Hill createHill(final HillOrder order, final String name) {
		final long hillId = idSequence++;
		final Hill hill = new Hill(name, new Point(order.getOrder() * WorldConstans.X_BOUNDRY + order.getXOffset(),
				WorldConstans.Y_BOUNDRY / 2));
		hill.setId(hillId);

		hills.add(hill);

		return hill;
	}

	public static List<IWorldObject> getStaticObjects() {
		final List<IWorldObject> result = new ArrayList<>();

		for (final IWorldObject worldObject : worldObjects) {
			if (worldObject instanceof Food || worldObject instanceof WorldBorder) {
				result.add(worldObject);
			}
		}

		return result;
	}

	public static List<IWorldObject> getWorldObjectsToMove() {
		final List<IWorldObject> result = new ArrayList<>();

		for (final IWorldObject worldObject : worldObjects) {
			if (worldObject instanceof IAnt) {
				result.add(worldObject);
			}
		}

		return result;
	}
}
