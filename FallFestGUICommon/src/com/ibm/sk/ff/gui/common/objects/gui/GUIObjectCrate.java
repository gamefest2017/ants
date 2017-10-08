package com.ibm.sk.ff.gui.common.objects.gui;

import java.util.ArrayList;
import java.util.List;

public class GUIObjectCrate {
	private final List<GHillObject> hills = new ArrayList<>();
	private final List<GFoodObject> foods = new ArrayList<>();
	private final List<GAntObject> ants = new ArrayList<>();
	private final List<GAntFoodObject> antFoods = new ArrayList<>();

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
		final int totalSize = getHills().size() + getFoods().size() + getAnts().size()
				+ getAntFoods().size();
		final List<GUIObject> objects = new ArrayList<>(totalSize);
		objects.addAll(getHills());
		objects.addAll(getFoods());
		objects.addAll(getAnts());
		objects.addAll(getAntFoods());
		return objects.toArray(new GUIObject[totalSize]);
	}

}
