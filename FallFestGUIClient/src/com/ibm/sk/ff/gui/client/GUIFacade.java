package com.ibm.sk.ff.gui.client;

import static com.ibm.sk.ff.gui.client.FileHelper.write;
import static com.ibm.sk.ff.gui.common.GUIOperations.*;
import static com.ibm.sk.ff.gui.common.mapper.Mapper.INSTANCE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.objects.gui.GAntFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectCrate;
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

	private List<Step> steps;

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
			finalGUIObject[] objects= map(o);

			final GUIObjectCrate crate = new GUIObjectCrate();
			crate.sortOut(objects);
				this.CLIENT.postMessage(GUIOperations.SET.toString(), Mapper.INSTANCE.pojoToJson(crate));
			}

			sendNotYetRenderedData(o);
			}

	private void sendNotYetRenderedData (final GUIObject [] o) {
				if (this.notRenderedYet.size() > 0){
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
					write(new Step(SET, type));
		}
	}

<<<<<<< HEAD
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
=======
			// Send objects to server
			for (final GUIObjectTypes type : objectsByType.keySet()) {
				this.CLIENT.postMessage(SET.toString() + "/" + type.toString(),
										INSTANCE.pojoToJson(objectsByType.get(type).toArray()));
				steps.add(new Step(SET, type));
>>>>>>> Add support for read replay
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
	public GAntFoodObject join(finalGAntObject ant, finalGFoodObject food) {
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
	public GUIObject[] separate(finalGAntObject ant) {
		final List<GUIObject> retList = new ArrayList<>();
			finalGAntFoodObject gafo = getMapped(ant);
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

<<<<<<< HEAD
	public void remove(final GUIObject[] objects) {
		final GUIObjectCrate crate = new GUIObjectCrate();
		final GUIObject[] mapped = map(objects);
		crate.sortOut(mapped);
		this.CLIENT.postMessage(GUIOperations.REMOVE.toString() + "/", Mapper.INSTANCE.pojoToJson(crate));
		write(new Step(REMOVE, data));
=======
	public void remove(final GUIObject[] data) {
		if (data.length > 0) {
			this.CLIENT.postMessage(REMOVE.toString() + "/" + data[0].getType().toString(), INSTANCE.pojoToJson(data));
			steps.add(new Step(REMOVE, data));
		}
>>>>>>> Add support for read replay
	}

	public void showScore(final ScoreData data) {
		String scoreDataJson = INSTANCE.pojoToJson(data);
		this.CLIENT.postMessage(SCORE.toString(), scoreDataJson);
	}

	//
	// Operations
	//
	public void showInitMenu(final InitMenuData data) {
		String initMenuDataJson = INSTANCE.pojoToJson(data);
		this.CLIENT.postMessage(SHOW_INIT_MENU.toString(), initMenuDataJson);
	}

	public void createGame(final CreateGameData data) {
		String createGameDataJson = INSTANCE.pojoToJson(data);
		this.CLIENT.postMessage(CREATE_GAME.toString(), createGameDataJson);
		steps = new ArrayList<>();
	}

	public void showResult(final ResultData data) {
		String resultDataJson = INSTANCE.pojoToJson(data);
		this.CLIENT.postMessage(SHOW_RESULT.toString(), resultDataJson);
		steps.add(new Step(SHOW_RESULT, resultDataJson));
	}

	public void close(final CloseData data) {
		String closeDataJson = INSTANCE.pojoToJson(data);
		this.CLIENT.postMessage(CLOSE.toString(), closeDataJson);
		write(steps);
	}

	// Poll for events
	private void checkEvents() {
		final String events = this.CLIENT.getMessage(EVENT_POLL.toString());
		if (events != null && events.length() > 0) {
			final GuiEvent[] swp = INSTANCE.jsonToPojo(events, GuiEvent[].class);
			castEvent(swp);
			steps.add(new Step(EVENT_POLL, swp));
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
