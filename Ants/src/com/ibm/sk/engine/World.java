package com.ibm.sk.engine;

import java.awt.Point;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.sk.WorldConstans;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.WorldObject;
import com.ibm.sk.dto.enums.HillOrder;

public final class World {

	private static final Map<Point, Object> GRID = new ConcurrentHashMap<>();

	private World() {
	}

	protected static Map<Point, Object> getWorld() {
		return GRID;
	}

	protected static Object getWorldObject(final Point position) {
		return GRID.get(position);
	}

	protected static void placeObject(final WorldObject worldObject) {
		GRID.put(worldObject.getPosition(), worldObject);
	}

	protected static void removeObject(final Point position) {
		GRID.remove(position);
	}

	public static Hill createHill(final HillOrder order, final String name) {
		final Hill hill = new Hill(WorldConstans.ANTS_START_POPULATION, name,
				new Point(order.getOrder() * WorldConstans.X_BOUNDRY, WorldConstans.Y_BOUNDRY / 2));
		placeObject(hill);
		return hill;
	}

}
