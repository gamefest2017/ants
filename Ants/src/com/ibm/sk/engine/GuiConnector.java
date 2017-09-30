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

public class GuiConnector {
	private final GUIFacade FACADE = new GUIFacade();
	
	public void initGame(final CreateGameData data) {
		FACADE.createGame(data);
	}
	
	public void placeGuiObjects(final List<IWorldObject> worldObjects) {
		List<GUIObject> guiObjects = new ArrayList<>();
		for (IWorldObject worldObject : worldObjects) {
			if (worldObject instanceof Food) {
				Food food = (Food) worldObject;
				GFoodObject gFoodObject = new GFoodObject();
				
				gFoodObject.setId(food.getId());
				gFoodObject.setLocation(food.getPosition().x, food.getPosition().y);
				System.out.println("Placing to GUI: " + food);
				guiObjects.add(gFoodObject);
			} else if (worldObject instanceof AbstractAnt) {
				AbstractAnt ant = (AbstractAnt) worldObject;
				GAntObject gAntbject = new GAntObject();
				
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
		FACADE.set(guiObjects.toArray(new GUIObject[guiObjects.size()]));
		
	}
	
	public void placeGuiObject(final WorldObject worldObject) {
		if (worldObject instanceof Hill) {
			Hill hill = (Hill) worldObject;
			GHillObject gHillObject = new GHillObject();
			
			gHillObject.setId(hill.getId());
			gHillObject.setTeam(hill.getName());
			gHillObject.setLocation(new Location(hill.getPosition().x, hill.getPosition().y));
			System.out.println("Placing to GUI: " + hill);
			FACADE.set(gHillObject);
		} else if (worldObject instanceof Food) {
			Food food = (Food) worldObject;
			GFoodObject gFoodObject = new GFoodObject();
			
			gFoodObject.setId(food.getId());
			gFoodObject.setLocation(food.getPosition().x, food.getPosition().y);
			System.out.println("Placing to GUI: " + food);
			FACADE.set(gFoodObject);
		} else if (worldObject instanceof AbstractAnt) {
			AbstractAnt ant = (AbstractAnt) worldObject;
			GAntObject gAntbject = new GAntObject();
			
			gAntbject.setId(ant.getId());
			gAntbject.setLocation(ant.getPosition().x, ant.getPosition().y);
			System.out.println("Placing to GUI: " + ant);
			FACADE.set(gAntbject);
		}
	}
	
	public void removeGuiObject(final WorldObject worldObject) {
		if (worldObject instanceof Food) {
			GFoodObject gFoodObject = new GFoodObject();
			gFoodObject.setId(worldObject.getId());
			gFoodObject.setLocation(worldObject.getPosition().x, worldObject.getPosition().y);
			FACADE.remove(gFoodObject);
			
			System.out.println("Removed from GUI object: " + worldObject);
		} else if (worldObject instanceof AbstractAnt) {
			AbstractAnt ant = (AbstractAnt) worldObject;
			GAntObject gAntObject = new GAntObject();
			
			gAntObject.setId(ant.getId());
			gAntObject.setLocation(ant.getPosition().x, ant.getPosition().y);
			System.out.println("Removing from GUI: " + ant);
			FACADE.remove(gAntObject);
		}
	}
}
