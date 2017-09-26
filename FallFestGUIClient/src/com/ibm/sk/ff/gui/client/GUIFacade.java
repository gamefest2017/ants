package com.ibm.sk.ff.gui.client;

import java.util.ArrayList;
import java.util.List;

import com.ibm.sk.ff.gui.common.GUIOperations;
import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.mapper.Mapper;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.operations.CloseData;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.ff.gui.common.objects.operations.ResultData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;

public class GUIFacade {

	private final Client CLIENT;

	private List<GuiEventListener> guiEventListeners = new ArrayList<>();

	public GUIFacade() {
		this.CLIENT = new Client();
		new Thread(new Runnable() {
			@Override
			public void run() {
				int sleepTime = Integer.parseInt(Config.SERVER_POLL_INTERVAL.toString());
				while (true) {
					try {
						Thread.sleep(sleepTime);
						checkEvents();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void set(GUIObject object) {
		CLIENT.postMessage(GUIOperations.SET.toString() + "/" + object.getType().toString(),
				Mapper.INSTANCE.pojoToJson(object));
	}

	public void remove(GUIObject data) {
		CLIENT.postMessage(GUIOperations.REMOVE.toString() + "/" + data.getType().toString(),
				Mapper.INSTANCE.pojoToJson(data));
	}

	public void showScore(ScoreData data) {
		CLIENT.postMessage(GUIOperations.SHOW_RESULT.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	//
	// Operations
	//
	public void showInitMenu(InitMenuData data) {
		CLIENT.postMessage(GUIOperations.SHOW_INIT_MENU.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	public void createGame(CreateGameData data) {
		CLIENT.postMessage(GUIOperations.CREATE_GAME.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	public void showResult(ResultData data) {
		CLIENT.postMessage(GUIOperations.SHOW_RESULT.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	public void close(CloseData data) {
		CLIENT.postMessage(GUIOperations.CLOSE.toString(), Mapper.INSTANCE.pojoToJson(data));
	}

	// Poll for events
	private void checkEvents() {
		String events = CLIENT.getMessage(GUIOperations.EVENT_POLL.toString());
		if (events != null && events.length() > 0) {
			GuiEvent[] swp = Mapper.INSTANCE.jsonToPojo(events, GuiEvent[].class);
			castEvent(swp);
		}
	}

	//
	// EVENTS
	//
	public void addGuiEventListener(GuiEventListener listener) {
		guiEventListeners.add(listener);
	}

	public void removeGuiEventListener(GuiEventListener listener) {
		guiEventListeners.remove(listener);
	}

	private void castEvent(GuiEvent[] event) {
		for (GuiEvent it : event)
			guiEventListeners.stream().forEach(l -> l.actionPerformed(it));
	}

}
