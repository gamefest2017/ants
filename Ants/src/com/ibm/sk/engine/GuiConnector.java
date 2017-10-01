package com.ibm.sk.engine;

import java.util.ArrayList;
import java.util.List;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IWorldObject;
import com.ibm.sk.dto.WorldObject;
import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectTypes;
import com.ibm.sk.ff.gui.common.objects.gui.Location;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;

public class GuiConnector {
	private final GUIFacade FACADE = new GUIFacade();

	public void initGame(final CreateGameData data) {
		this.FACADE.createGame(data);
	}

	public void placeGuiObjects(final List<IWorldObject> worldObjects) {
		final List<GUIObject> guiObjects = new ArrayList<>();
		for (final IWorldObject worldObject : worldObjects) {
			if (worldObject instanceof Food) {
				final Food food = (Food) worldObject;
				final GFoodObject gFoodObject = new GFoodObject();

				gFoodObject.setId(food.getId());
				gFoodObject.setLocation(food.getPosition().x, food.getPosition().y);
				System.out.println("Placing to GUI: " + food);
				guiObjects.add(gFoodObject);
			} else if (worldObject instanceof AbstractAnt) {
				final AbstractAnt ant = (AbstractAnt) worldObject;
				final GAntObject gAntbject = new GAntObject();

				gAntbject.setId(ant.getId());
				gAntbject.setTeam(ant.getMyHillName());
				gAntbject.setLocation(ant.getPosition().x, ant.getPosition().y);

				if (ant.hasFood()) {
					gAntbject.setType(GUIObjectTypes.ANT_FOOD);
					System.out.println("Changing type in GUI old object: " + GUIObjectTypes.ANT + " with object: " + GUIObjectTypes.ANT_FOOD);
				} else {
					System.out.println("Placing to GUI: " + ant);
					guiObjects.add(gAntbject);
				}
			}
		}
		this.FACADE.set(guiObjects.toArray(new GUIObject[guiObjects.size()]));

	}

	public void placeGuiObject(final WorldObject worldObject) {
		if (worldObject instanceof Hill) {
			final Hill hill = (Hill) worldObject;
			final GHillObject gHillObject = new GHillObject();

			gHillObject.setId(hill.getId());
			gHillObject.setTeam(hill.getName());
			gHillObject.setLocation(new Location(hill.getPosition().x, hill.getPosition().y));
			System.out.println("Placing to GUI: " + hill);
			this.FACADE.set(gHillObject);
		} else if (worldObject instanceof Food) {
			final Food food = (Food) worldObject;
			final GFoodObject gFoodObject = new GFoodObject();

			gFoodObject.setId(food.getId());
			gFoodObject.setLocation(food.getPosition().x, food.getPosition().y);
			System.out.println("Placing to GUI: " + food);
			this.FACADE.set(gFoodObject);
		} else if (worldObject instanceof AbstractAnt) {
			final AbstractAnt ant = (AbstractAnt) worldObject;
			final GAntObject gAntbject = new GAntObject();

			gAntbject.setId(ant.getId());
			gAntbject.setLocation(ant.getPosition().x, ant.getPosition().y);
			System.out.println("Placing to GUI: " + ant);
			this.FACADE.set(gAntbject);
		}
	}

	public void removeGuiObject(final WorldObject worldObject) {
		if (worldObject instanceof Food) {
			final GFoodObject gFoodObject = new GFoodObject();
			gFoodObject.setId(worldObject.getId());
			gFoodObject.setLocation(worldObject.getPosition().x, worldObject.getPosition().y);
			this.FACADE.remove(gFoodObject);

			System.out.println("Removed from GUI object: " + worldObject);
		} else if (worldObject instanceof AbstractAnt) {
			final AbstractAnt ant = (AbstractAnt) worldObject;
			final GAntObject gAntObject = new GAntObject();

			gAntObject.setId(ant.getId());
			gAntObject.setLocation(ant.getPosition().x, ant.getPosition().y);
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
