package com.ibm.sk.ff.gui.client;

import static com.ibm.sk.ff.gui.common.GUIOperations.CLOSE;
import static com.ibm.sk.ff.gui.common.GUIOperations.CREATE_GAME;
import static com.ibm.sk.ff.gui.common.GUIOperations.EVENT_POLL;
import static com.ibm.sk.ff.gui.common.GUIOperations.REMOVE;
import static com.ibm.sk.ff.gui.common.GUIOperations.SCORE;
import static com.ibm.sk.ff.gui.common.GUIOperations.SET;
import static com.ibm.sk.ff.gui.common.GUIOperations.SHOW_INIT_MENU;
import static com.ibm.sk.ff.gui.common.GUIOperations.SHOW_RESULT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.mapper.Mapper;
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
	
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmm");

	private final IClient CLIENT;

	private final List<GuiEventListener> guiEventListeners = new ArrayList<>();

	private final List<GAntFoodObject> antFoodObjects = new ArrayList<>();

	private final Map<GAntObject, GAntFoodObject> notRenderedYet = new HashMap<>();

	private final List<Step> steps = new ArrayList<>();
	
	private boolean render = true;
	private boolean record = true;
	
	private long recordStartTime;

	public GUIFacade() {
		this(new Client());
	}
	
	public GUIFacade(IClient client) {
		this.CLIENT = client;
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
			crate.sortOut(objects);
			if (render) {
				this.CLIENT.postMessage(SET.toString(), Mapper.INSTANCE.pojoToJson(crate));
			}
			if (record) {
				steps.add(new Step(SET, o));
			}
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

			GUIObject[] toBeRendered = mapNotYetRendered(o);
			if (render) {
				this.CLIENT.postMessage(
					SET.toString() + "/" + GUIObjectTypes.ANT_FOOD.toString(),
					Mapper.INSTANCE.pojoToJson(toBeRendered)
					);
			}
			if (record) {
				steps.add(new Step(SET, toBeRendered));
			}
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

	public void remove(final GUIObject[] objects) {
		final GUIObjectCrate crate = new GUIObjectCrate();
		final GUIObject[] mapped = map(objects);
		crate.sortOut(mapped);
		if (render) {
			this.CLIENT.postMessage(REMOVE.toString() + "/", Mapper.INSTANCE.pojoToJson(crate));
		}
		if (record) {
			steps.add(new Step(REMOVE, objects));
		}
	}

	public void showScore(final ScoreData data) {
		if (render) {
			this.CLIENT.postMessage(SCORE.toString(), Mapper.INSTANCE.pojoToJson(data));
		}
		if (record) {
			steps.add(new Step(SCORE, data));
		}
	}

	//
	// Operations
	//
	public void showInitMenu(final InitMenuData data) {
		this.CLIENT.postMessage(SHOW_INIT_MENU.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	public void createGame(final CreateGameData data) {
		steps.clear();
		notRenderedYet.clear();
		antFoodObjects.clear();
		
		if (render) {
			this.CLIENT.postMessage(CREATE_GAME.toString(), Mapper.INSTANCE.pojoToJson(data));
		}
		if (record) {
			recordStartTime = System.currentTimeMillis();
			steps.add(new Step(CREATE_GAME, data));
		}
	}

	public void showResult(final ResultData data) {
		if (render) {
			this.CLIENT.postMessage(SHOW_RESULT.toString(), Mapper.INSTANCE.pojoToJson(data));
		}
		if (record) {
			steps.add(new Step(SHOW_RESULT, data));
			CreateGameData cgd = steps.get(0).getCreateGame();
			String [] teams = cgd.getTeams();
			StringBuilder recordName = new StringBuilder();
			for (String it : teams) {
				if (recordName.length() > 0) {
					recordName.append("_");
				}
				recordName.append(it);
			}
			recordName
				.append("_")
				.append(DATE_FORMAT.format(new Date(recordStartTime)))
				.append(".replay");
			Replay replay = new Replay(steps, recordName.toString());
			ReplayFileHelper.write(replay);
		}
	}

	public void close(final CloseData data) {
		this.CLIENT.postMessage(CLOSE.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	// Poll for events
	private void checkEvents() {
		final String events = this.CLIENT.getMessage(EVENT_POLL.toString());
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
	
	//
	// setters
	//
	public void setRender(boolean render) {
		this.render = render;
	}
	
	public void setRecord(boolean record) {
		this.record = record;
	}

}
