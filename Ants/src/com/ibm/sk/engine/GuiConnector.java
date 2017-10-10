package com.ibm.sk.engine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.IWorldObject;
import com.ibm.sk.dto.WorldObject;
import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectTypes;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;

public class GuiConnector {
	private final GUIFacade FACADE = new GUIFacade();

	public void initGame(final CreateGameData data) {
		this.FACADE.createGame(data);
	}

	public void placeGuiObjects(final List<IWorldObject> worldObjects) {
		final List<GUIObject> guiObjects = new ArrayList<>();
		final List<GUIObject> guiFoodObjects = new ArrayList<>();
		final List<GUIObject> guiAntObjects = new ArrayList<>();
		final List<GAntObject> guiAntObjectsToSplit = new ArrayList<>();
		
		for (final IWorldObject worldObject : worldObjects) {
			if (worldObject instanceof Food) {
				final Food food = (Food) worldObject;
				final GFoodObject gFoodObject = createGFoodObject(food);
				System.out.println("Placing to GUI: " + food);
				guiFoodObjects.add(gFoodObject);
			} else if (worldObject instanceof AbstractAnt) {
				final AbstractAnt ant = (AbstractAnt) worldObject;
				GAntObject gAntObject;
				if (ant.hasFood()) {
					gAntObject = createGAntFoodObject(ant);
					System.out.println("Changing type in GUI old object: " + GUIObjectTypes.ANT + " with object: " + GUIObjectTypes.ANT_FOOD);
				} else {
					gAntObject = createGAntObject(ant);
					if (ant.getMyHill().getPosition().equals(ant.getPosition())) {
						System.out.println("Splitting food from ant: " + ant);
						guiAntObjectsToSplit.add(gAntObject);
					}
					System.out.println("Placing to GUI: " + ant);
				}
				guiAntObjects.add(gAntObject);
			}
		}
		
		guiObjects.addAll(guiFoodObjects);
		guiObjects.addAll(guiAntObjects);
		
		this.FACADE.set(guiObjects.toArray(new GUIObject[guiObjects.size()]));
		for (GAntObject gAntObject : guiAntObjectsToSplit) {
			FACADE.separate(gAntObject);
		}
	}

	private GAntObject createGAntFoodObject(final IAnt ant) {
		GAntObject gAntObject = createGAntObject(ant);
		FACADE.join(gAntObject, createGFoodObject(ant.getFood()));
		return gAntObject;
	}

	private GAntObject createGAntObject(final IAnt ant) {
		final GAntObject result = new GAntObject();
		result.setId(ant.getId());
		result.setTeam(ant.getMyHillName());
		final Point position = ant.getPosition();
		result.setLocation(position.x, position.y);
		return result;
	}

	private GFoodObject createGFoodObject(final Food food) {
		final GFoodObject result = new GFoodObject();
		result.setId(food.getId());
		final Point position = food.getPosition();
		result.setLocation(position.x, position.y);
		return result;
	}

	public void placeGuiObject(final WorldObject worldObject) {
		if (worldObject instanceof Hill) {
			final Hill hill = (Hill) worldObject;
			final GHillObject gHillObject = createGHillObject(hill);
			System.out.println("Placing to GUI: " + hill);
			this.FACADE.set(gHillObject);
		} else if (worldObject instanceof Food) {
			final Food food = (Food) worldObject;
			final GFoodObject gFoodObject = createGFoodObject(food);
			System.out.println("Placing to GUI: " + food);
			this.FACADE.set(gFoodObject);
		} else if (worldObject instanceof AbstractAnt) {
			final AbstractAnt ant = (AbstractAnt) worldObject;
			final GAntObject gAntbject = new GAntObject();
			System.out.println("Placing to GUI: " + ant);
			this.FACADE.set(gAntbject);
		}
	}


	private GHillObject createGHillObject(final Hill hill) {
		final GHillObject result = new GHillObject();
		result.setId(hill.getId());
		result.setTeam(hill.getName());
		final Point position = hill.getPosition();
		result.setLocation(position.x, position.y);
		return result;
	}

	public void removeGuiObject(final WorldObject worldObject) {
		if (worldObject instanceof Food) {
//			final GFoodObject gFoodObject = createGFoodObject((Food) worldObject);
//			this.FACADE.remove(gFoodObject);
//			System.out.println("Removed from GUI object: " + worldObject);
		} else if (worldObject instanceof AbstractAnt) {
			final AbstractAnt ant = (AbstractAnt) worldObject;
			final GAntObject gAntObject = createGAntObject(ant);
			System.out.println("Removing from GUI: " + ant);
			this.FACADE.remove(gAntObject);
		}
	}

	public void showScore(final String teamName, final int points) {
		final ScoreData data = new ScoreData();
		data.setMessage(teamName);
		data.setScore(points);
		this.FACADE.showScore(data);
	}
}
