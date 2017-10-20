package com.ibm.sk.engine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Food;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IWorldObject;
import com.ibm.sk.dto.WorldObject;
import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GBorderObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.gui.GWarriorObject;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;
import com.ibm.sk.models.WorldBorder;

public class GuiConnector {

	public final GUIFacade FACADE;

	public GuiConnector(final GUIFacade FACADE) {
		this.FACADE = FACADE;
	}

	public void initGame(final CreateGameData data) {
		this.FACADE.createGame(data);
	}

	public GUIFacade getFacade() {
		return this.FACADE;
	}

	public void placeGuiObjects(final List<IWorldObject> worldObjects) {
		final List<GUIObject> guiObjects = new ArrayList<>();
		final List<GAntObject> guiAntObjectsToSplit = new ArrayList<>();

		for (final IWorldObject worldObject : worldObjects) {
			if (worldObject instanceof WorldBorder) {
				final WorldBorder border = (WorldBorder) worldObject;
				final GBorderObject gBorderObject = createGBorderObject(border);
				System.out.println("Placing to GUI: " + border);
				guiObjects.add(gBorderObject);
			} else if (worldObject instanceof Food) {
				final Food food = (Food) worldObject;
				final GFoodObject gFoodObject = createGFoodObject(food);
				System.out.println("Placing to GUI: " + food);
				guiObjects.add(gFoodObject);
			} else if (worldObject instanceof AbstractWarrior) {
				final AbstractWarrior warrior = (AbstractWarrior) worldObject;
				final GWarriorObject gWarriorObject = createGWarriorObject(warrior);
				System.out.println("Placing to GUI: " + warrior);
				guiObjects.add(gWarriorObject);
			} else if (worldObject instanceof AbstractAnt) {
				final AbstractAnt ant = (AbstractAnt) worldObject;
				GAntObject gAntObject;
				if (ant.hasFood()) {
					gAntObject = createGAntFoodObject(ant);
					System.out.println("Placing AntFood to GUI: " + ant);
				} else {
					gAntObject = createGAntObject(ant);
					if (ant.getMyHill().getPosition().equals(ant.getPosition())) {
						System.out.println("Splitting food from ant: " + ant);
						guiAntObjectsToSplit.add(gAntObject);
					}
					System.out.println("Placing to GUI: " + ant);
				}
				guiObjects.add(gAntObject);
			}
		}

		this.FACADE.set(guiObjects.toArray(new GUIObject[guiObjects.size()]));
		for (final GAntObject gAntObject : guiAntObjectsToSplit) {
			this.FACADE.separate(gAntObject);
		}
	}

	private GBorderObject createGBorderObject(final WorldBorder border) {
		final GBorderObject result = new GBorderObject();
		result.setId(border.getId());
		final Point position = border.getPosition();
		result.setLocation(position.x, position.y);
		return result;
	}

	private GAntObject createGAntFoodObject(final AbstractAnt ant) {
		final GAntObject gAntObject = createGAntObject(ant);
		this.FACADE.join(gAntObject, createGFoodObject(ant.getFood()));
		return gAntObject;
	}

	private GAntObject createGAntObject(final AbstractAnt ant) {
		final GAntObject result = new GAntObject();
		result.setId(ant.getId());
		result.setTeam(ant.getMyHillName());
		final Point position = ant.getPosition();
		result.setLocation(position.x, position.y);
		return result;
	}

	private GWarriorObject createGWarriorObject(final AbstractWarrior warrior) {
		final GWarriorObject result = new GWarriorObject();
		result.setId(warrior.getId());
		result.setTeam(warrior.getMyHillName());
		final Point position = warrior.getPosition();
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
			// final GFoodObject gFoodObject = createGFoodObject((Food)
			// worldObject);
			// this.FACADE.remove(gFoodObject);
			// System.out.println("Removed from GUI object: " + worldObject);
		} else if (worldObject instanceof AbstractAnt) {
			final AbstractAnt ant = (AbstractAnt) worldObject;
			final GAntObject gAntObject = createGAntObject(ant);
			System.out.println("Removing from GUI: " + ant);
			this.FACADE.remove(gAntObject);
		}
	}

	public void removeGuiObjects(final List<IWorldObject> list) {
		final List<GUIObject> ants = new ArrayList<>();
		for (final IWorldObject object : list) {
			if (object instanceof AbstractAnt) {
				final AbstractAnt ant = (AbstractAnt) object;
				final GAntObject gAntObject = createGAntObject(ant);
				if (ant.hasFood()) {
					final GUIObject[] food = this.FACADE.separate(gAntObject);
					this.FACADE.set(food);
				}
				ants.add(gAntObject);
			}
			if (object instanceof AbstractWarrior) {
				ants.add(createGWarriorObject((AbstractWarrior) object));
			}
		}
		if (ants.size() > 0) {
			this.FACADE.remove(ants.toArray(new GUIObject[ants.size()]));
		}
	}

	public void showScore(final String teamName, final int points, final int currentTurn, final int turns) {
		final ScoreData data = new ScoreData();
		data.setMessage(teamName);
		data.setScore(points);
		data.setCurrentTurn(currentTurn);
		data.setTurns(turns);
		this.FACADE.showScore(data);
	}

}
