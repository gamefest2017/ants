package com.ibm.sk.ff.gui.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ibm.sk.ff.gui.common.GUIOperations;
import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.mapper.Mapper;
import com.ibm.sk.ff.gui.common.objects.gui.GAntFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectCrate;
import com.ibm.sk.ff.gui.common.objects.operations.CloseData;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.ff.gui.common.objects.operations.ResultData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;

public class GUIFacade {

	private final Client CLIENT;

	private final List<GuiEventListener> guiEventListeners = new ArrayList<>();

	private final Map<GAntObject, GAntFoodObject> afoMap = new HashMap<>();

	private final Set<GUIObject> accumulator = new HashSet<>();

	public GUIFacade() {
		this.CLIENT = new Client();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final int sleepTime = Integer.parseInt(Config.SERVER_POLL_INTERVAL.toString());
				while (true) {
					try {
						Thread.sleep(sleepTime);
						checkEvents();
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void set(final GUIObject object) {
		set(new GUIObject[] {object});
	}

	public void set(final GUIObject[] objects) {
		if (objects != null && objects.length > 0) {
			final GUIObject[] mappedObjects = mapAntsToAntfoodObjects(objects);

			this.accumulator.clear();

			final GUIObjectCrate crate = new GUIObjectCrate();
			for (final GUIObject guiObject : mappedObjects) {
				if (guiObject instanceof GHillObject) {
					final GHillObject hill = (GHillObject) guiObject;
					crate.getHills().add(hill);
				}
				if (guiObject instanceof GAntObject) {
					final GAntObject ant = (GAntObject) guiObject;
					crate.getAnts().add(ant);
				}
				if (guiObject instanceof GFoodObject) {
					final GFoodObject food = (GFoodObject) guiObject;
					crate.getFoods().add(food);
				}
				if (guiObject instanceof GAntFoodObject) {
					final GAntFoodObject antFood = (GAntFoodObject) guiObject;
					crate.getAntFoods().add(antFood);
				}
			}
			// Send objects to server
			// for (final GUIObjectTypes type : objectsByType.keySet()) {
			this.CLIENT.postMessage(GUIOperations.SET.toString() + "/", Mapper.INSTANCE.pojoToJson(crate));
			// }
		}
	}

	private GUIObject[] mapAntsToAntfoodObjects(final GUIObject[] objects) {
		final GUIObject[] mappedObjects = new GUIObject[objects.length];
		for (int i = 0; i < objects.length; i++){
			if (this.afoMap.containsKey(objects[i])) {
				final GAntFoodObject swp = this.afoMap.remove(objects[i]);
				swp.setLocation(objects[i].getLocation());
				this.afoMap.put((GAntObject)objects[i], swp);
				mappedObjects[i] = swp;
			} else {
				mappedObjects[i] = objects[i];
			}
		}
		return mappedObjects;
	}

	public void join(final GAntObject ant, final GFoodObject food) {
		if (!this.afoMap.containsKey(ant)) {
			final GAntFoodObject swp = new GAntFoodObject();
			swp.setAnt(ant);
			swp.setFood(food);
			swp.setLocation(ant.getLocation());
			this.afoMap.put(ant, swp);
		}
	}

	public void split(final GAntObject ant) {
		if (this.afoMap.containsKey(ant)) {
			final GAntFoodObject swp = this.afoMap.get(ant);
			//			accumulator.add(swp.getFood());
			this.afoMap.remove(ant);
			set(ant);
		}
	}

	public void remove(final GUIObject data) {
		remove(new GUIObject [] {data});
	}

	public void remove(final GUIObject[] data) {
		if (data.length > 0) {
			this.CLIENT.postMessage(GUIOperations.REMOVE.toString() + "/" + data[0].getType().toString(), Mapper.INSTANCE.pojoToJson(data));
		}
	}

	public void showScore(final ScoreData data) {
		this.CLIENT.postMessage(GUIOperations.SCORE.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	//
	// Operations
	//
	public void showInitMenu(final InitMenuData data) {
		this.CLIENT.postMessage(GUIOperations.SHOW_INIT_MENU.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	public void createGame(final CreateGameData data) {
		this.CLIENT.postMessage(GUIOperations.CREATE_GAME.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	public void showResult(final ResultData data) {
		this.CLIENT.postMessage(GUIOperations.SHOW_RESULT.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	public void close(final CloseData data) {
		this.CLIENT.postMessage(GUIOperations.CLOSE.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	// Poll for events
	private void checkEvents() {
		final String events = this.CLIENT.getMessage(GUIOperations.EVENT_POLL.toString());
		if (events != null && events.length() > 0) {
			final GuiEvent[] swp = Mapper.INSTANCE.jsonToPojo(events, GuiEvent[].class);
			castEvent(swp);
		}
	}

	//
	// EVENTS
	//
	public void addGuiEventListener(final GuiEventListener listener) {
		this.guiEventListeners.add(listener);
	}

	public void removeGuiEventListener(final GuiEventListener listener) {
		this.guiEventListeners.remove(listener);
	}

	private void castEvent(final GuiEvent[] event) {
		for (final GuiEvent it : event) {
			this.guiEventListeners.stream().forEach(l -> l.actionPerformed(it));
		}
	}

}
