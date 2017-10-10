package com.ibm.sk.ff.gui.client;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private final List<GAntFoodObject> antFoodObjects = new ArrayList<>();
	
	private final Map<GAntObject, GAntFoodObject> notRenderedYet = new HashMap<>();
	
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

	public void set(final GUIObject[] o) {
		if (o != null && o.length > 0) {
			GUIObject [] objects = map(o);
			final Map<GUIObjectTypes, List<GUIObject>> objectsByType = new EnumMap<>(GUIObjectTypes.class);
			for (final GUIObjectTypes type : GUIObjectTypes.values()) {
				objectsByType.put(type, new ArrayList<>());
			}
			for (final GUIObject guiObject : objects) {
				objectsByType.get(guiObject.getType()).add(guiObject);
			}
			for (final GUIObjectTypes type : objectsByType.keySet()) {
				this.CLIENT.postMessage(
					GUIOperations.SET.toString() + "/" + type.toString(),
					Mapper.INSTANCE.pojoToJson(objectsByType.get(type).toArray())
				);
			}
		}
		
		sendNotYetRenderedData(o);
	}
	
	private void sendNotYetRenderedData(GUIObject[] o) {
		if (notRenderedYet.size() > 0) {
			List<GAntObject> antsToRemove = new ArrayList<>();
			List<GFoodObject> foodsToRemove = new ArrayList<>();
			
			notRenderedYet.values().stream().forEach(af -> {
				antsToRemove.add(af.getAnt());
				foodsToRemove.add(af.getFood());
				}
			);
			
			remove(antsToRemove.stream().toArray(GAntObject[]::new));
			remove(foodsToRemove.stream().toArray(GFoodObject[]::new));
			
			this.CLIENT.postMessage(
				GUIOperations.SET.toString() + "/" + GUIObjectTypes.ANT_FOOD.toString(), 
				Mapper.INSTANCE.pojoToJson(mapNotYetRendered(o))
			);
		}
	}
	
	private GAntFoodObject[] mapNotYetRendered(GUIObject[] o) {
		List<GAntFoodObject> ret = new ArrayList<>();
		
		for (GUIObject it : o) {
			if (it.getType() == GUIObjectTypes.ANT) {
				GAntObject swp = (GAntObject)it;
				if (notRenderedYet.containsKey(swp)) {
					GAntFoodObject toAdd = notRenderedYet.remove(swp);
					toAdd.setLocation(swp.getLocation());
					ret.add(toAdd);
				}
			}
		}
		
		return ret.stream().toArray(GAntFoodObject[]::new);
	}
	
	private GUIObject[] map(GUIObject[] orig) {
		List<GUIObject> ret = new ArrayList<>(orig.length);
		for (GUIObject it : orig) {
			if (it.getType() == GUIObjectTypes.ANT) {
				GAntFoodObject mapped = getMapped((GAntObject)it);
				if (mapped != null && !notRenderedYet.containsValue(mapped)) {
					mapped.setLocation(it.getLocation());
					ret.add(mapped);
				} else {
					ret.add(it);
				}
			} else {
				ret.add(it);
			}
		}
		return ret.stream().toArray(GUIObject[]::new);
	}
	
	private GAntFoodObject getMapped(GAntObject ant) {
		return antFoodObjects.stream().filter(af -> af.getAnt().equals(ant)).findFirst().orElse(null);
	}

	public GAntFoodObject join(GAntObject ant, GFoodObject food) {
		GAntFoodObject gafo = new GAntFoodObject();
		gafo.setAnt(ant);
		gafo.setFood(food);
		antFoodObjects.add(gafo);
		notRenderedYet.put(ant, gafo);
		return gafo;
	}
	
	public GUIObject[] separate(GAntObject ant) {
		List<GUIObject> retList = new ArrayList<>();
		//TODO
		
		antFoodObjects.stream()
			.filter(afo -> afo.getAnt().equals(ant))
			.forEach(afo -> retList.add(afo.getFood()));
		
		return retList.stream().toArray(GUIObject[]::new);
	}
	
	public void separate(GFoodObject food) {
		//TODO
	}
	
	public void separate(GAntObject ant, GFoodObject food) {
		//TODO
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
		this.CLIENT.postMessage(GUIOperations.SHOW_RESULT.toString(), Mapper.INSTANCE.pojoToJson(data));
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
