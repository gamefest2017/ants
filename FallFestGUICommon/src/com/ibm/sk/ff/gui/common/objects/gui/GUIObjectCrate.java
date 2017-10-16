package com.ibm.sk.ff.gui.common.objects.gui;

import java.util.ArrayList;
import java.util.List;

public class GUIObjectCrate {
	private final List<GHillObject> hills = new ArrayList<>();
	private final List<GFoodObject> foods = new ArrayList<>();
	private final List<GAntObject> ants = new ArrayList<>();
	private final List<GAntFoodObject> antFoods = new ArrayList<>();
	private final List<GWarriorObject> warriors = new ArrayList<>();
	private final List<GBorderObject> borders = new ArrayList<>();

	public List<GAntFoodObject> getAntFoods() {
		return this.antFoods;
	}

	public List<GHillObject> getHills() {
		return this.hills;
	}

	public List<GAntObject> getAnts() {
		return this.ants;
	}

	public List<GFoodObject> getFoods() {
		return this.foods;
	}

	public GUIObject[] dump() {
		final int totalSize = getHills().size() + getFoods().size() + getAnts().size() + getWarriors().size()
				+ getAntFoods().size() + getBorders().size();
		final List<GUIObject> objects = new ArrayList<>(totalSize);
		objects.addAll(getBorders());
		objects.addAll(getHills());
		objects.addAll(getFoods());
		objects.addAll(getAnts());
		objects.addAll(getWarriors());
		objects.addAll(getAntFoods());
		return objects.toArray(new GUIObject[totalSize]);
	}

	public List<GWarriorObject> getWarriors() {
		return this.warriors;
	}

	public void sortOut(final GUIObject[] objects) {
		for (final GUIObject guiObject : objects) {
			if (guiObject instanceof GHillObject) {
				final GHillObject hill = (GHillObject) guiObject;
				getHills().add(hill);
			}
			if (guiObject instanceof GBorderObject) {
				final GBorderObject border = (GBorderObject) guiObject;
				getBorders().add(border);
			}
			if (guiObject instanceof GAntObject) {
				final GAntObject ant = (GAntObject) guiObject;
				getAnts().add(ant);
			}
			if (guiObject instanceof GWarriorObject) {
				final GWarriorObject warrior = (GWarriorObject) guiObject;
				getWarriors().add(warrior);
			}
			if (guiObject instanceof GFoodObject) {
				final GFoodObject food = (GFoodObject) guiObject;
				getFoods().add(food);
			}
			if (guiObject instanceof GAntFoodObject) {
				final GAntFoodObject antFood = (GAntFoodObject) guiObject;
				getAntFoods().add(antFood);
			}
		}
	}

	public List<GBorderObject> getBorders() {
		return this.borders;
	}

}
