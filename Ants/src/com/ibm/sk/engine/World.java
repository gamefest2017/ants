package com.ibm.sk.engine;

import java.awt.Point;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.WorldObject;

public final class World {
	protected static int X_BOUNDRY = 100;
	protected static int Y_BOUNDRY = 100;
	protected static int FOOD_BOUNDRY = 10;
	
	private static final Map<Point, Object> GRID = new ConcurrentHashMap<Point, Object>();
	
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
	
	public static void createHill(final String name) {
		Hill hill = new Hill(5, name, new Point(50,50));
		placeObject(hill);
	}
}
