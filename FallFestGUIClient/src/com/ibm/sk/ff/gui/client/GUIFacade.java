package com.ibm.sk.ff.gui.client;

import java.util.ArrayList;
import java.util.EnumMap;
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
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectTypes;
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
			GUIObject[] mappedObjects = mapAntsToAntfoodObjects(objects);
			
			final Map<GUIObjectTypes, List<GUIObject>> objectsByType = new EnumMap<>(GUIObjectTypes.class);
			for (final GUIObjectTypes type : GUIObjectTypes.values()) {
				objectsByType.put(type, new ArrayList<>());
			}
			
			// Add objects received
			for (final GUIObject guiObject : mappedObjects) {
				objectsByType.get(guiObject.getType()).add(guiObject);
			}
			
			// Accumulator contains the splitted food objects previously held in the antfood object
			for (final GUIObject guiObject : accumulator) {
				objectsByType.get(guiObject.getType()).add(guiObject);
			}
			accumulator.clear();
			
			// Send objects to server
			for (final GUIObjectTypes type : objectsByType.keySet()) {
				this.CLIENT.postMessage(GUIOperations.SET.toString() + "/" + type.toString(),
						Mapper.INSTANCE.pojoToJson(objectsByType.get(type).toArray()));
			}
		}
	}
	
	private GUIObject[] mapAntsToAntfoodObjects(GUIObject[] objects) {
		GUIObject[] mappedObjects = new GUIObject[objects.length];
		for (int i = 0; i < objects.length; i++){
			if (afoMap.containsKey(objects[i])) {
				GAntFoodObject swp = afoMap.remove(objects[i]);
				(swp).setLocation(objects[i].getLocation());
				afoMap.put((GAntObject)objects[i], swp);
				mappedObjects[i] = swp;
			} else {
				mappedObjects[i] = objects[i];
			}
		}
		return mappedObjects;
	}
	
	public void join(GAntObject ant, GFoodObject food) {
		if (!afoMap.containsKey(ant)) {
			GAntFoodObject swp = new GAntFoodObject();
			swp.setAnt(ant);
			swp.setFood(food);
			swp.setLocation(ant.getLocation());
			afoMap.put(ant, swp);
		}
	}
	
	public void split(GAntObject ant) {
		if (afoMap.containsKey(ant)) {
			GAntFoodObject swp = afoMap.get(ant);
//			accumulator.add(swp.getFood());
			afoMap.remove(ant);
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
