package com.ibm.sk.ff.gui.client;

import java.util.ArrayList;
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
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectCrate;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectTypes;
import com.ibm.sk.ff.gui.common.objects.gui.GWarriorObject;
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
			final GUIObject [] objects = map(o);

			final GUIObjectCrate crate = new GUIObjectCrate();
			for (final GUIObject guiObject : objects) {
				if (guiObject instanceof GHillObject) {
					final GHillObject hill = (GHillObject) guiObject;
					crate.getHills().add(hill);
				}
				if (guiObject instanceof GAntObject) {
					final GAntObject ant = (GAntObject) guiObject;
					crate.getAnts().add(ant);
				}
				if (guiObject instanceof GWarriorObject) {
					final GWarriorObject warrior = (GWarriorObject) guiObject;
					crate.getWarriors().add(warrior);
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
			this.CLIENT.postMessage(GUIOperations.SET.toString(), Mapper.INSTANCE.pojoToJson(crate));
		}

		sendNotYetRenderedData(o);
	}

	private void sendNotYetRenderedData(final GUIObject[] o) {
		if (this.notRenderedYet.size() > 0) {
			final List<GAntObject> antsToRemove = new ArrayList<>();
			final List<GFoodObject> foodsToRemove = new ArrayList<>();

			this.notRenderedYet.values().stream().forEach(af -> {
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

	private GAntFoodObject[] mapNotYetRendered(final GUIObject[] o) {
		final List<GAntFoodObject> ret = new ArrayList<>();

		for (final GUIObject it : o) {
			if (it.getType() == GUIObjectTypes.ANT) {
				final GAntObject swp = (GAntObject)it;
				if (this.notRenderedYet.containsKey(swp)) {
					final GAntFoodObject toAdd = this.notRenderedYet.remove(swp);
					toAdd.setLocation(swp.getLocation());
					ret.add(toAdd);
				}
			}
		}

		return ret.stream().toArray(GAntFoodObject[]::new);
	}

	private GUIObject[] map(final GUIObject[] orig) {
		final List<GUIObject> ret = new ArrayList<>(orig.length);
		for (final GUIObject it : orig) {
			if (it.getType() == GUIObjectTypes.ANT) {
				final GAntFoodObject mapped = getMapped((GAntObject)it);
				if (mapped != null && !this.notRenderedYet.containsValue(mapped)) {
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

	private GAntFoodObject getMapped(final GAntObject ant) {
		return this.antFoodObjects.stream().filter(af -> af.getAnt().equals(ant)).findFirst().orElse(null);
	}

	public GAntFoodObject join(final GAntObject ant, final GFoodObject food) {
		GAntFoodObject gafo = getMapped(ant);

		if (!this.antFoodObjects.contains(gafo)) {
			gafo = new GAntFoodObject();
			gafo.setAnt(ant);
			gafo.setFood(food);

			this.antFoodObjects.add(gafo);
			this.notRenderedYet.put(ant, gafo);
		}

		return gafo;
	}

	public GUIObject[] separate(final GAntObject ant) {
		final List<GUIObject> retList = new ArrayList<>();

		final GAntFoodObject gafo = getMapped(ant);
		if (this.antFoodObjects.contains(gafo)) {
			this.antFoodObjects.stream()
			.filter(afo -> afo.getAnt().equals(ant))
			.forEach(afo -> retList.add(afo.getFood()));
			this.antFoodObjects.remove(gafo);
			remove(gafo);
			set(ant);
		}

		return retList.stream().toArray(GUIObject[]::new);
	}

	public void separate(final GFoodObject food) {
		//TODO
	}

	public void separate(final GAntObject ant, final GFoodObject food) {
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
