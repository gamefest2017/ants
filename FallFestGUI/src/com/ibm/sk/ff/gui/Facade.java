package com.ibm.sk.ff.gui;

import java.util.ArrayList;
import java.util.List;

import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.mapper.Mapper;
import com.ibm.sk.ff.gui.common.objects.gui.GAntFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectCrate;
import com.ibm.sk.ff.gui.common.objects.gui.GWarriorObject;
import com.ibm.sk.ff.gui.common.objects.operations.CloseData;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.ff.gui.common.objects.operations.ResultData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;
import com.sun.istack.internal.logging.Logger;

public class Facade implements GuiEventListener {

	private final GUI gui;

	private final Logger LOG = Logger.getLogger(Facade.class);

	public Facade(final GUI gui) {
		this.gui = gui;
	}

	public void set(final GAntObject[] ants) {
		this.gui.set(ants);
	}

	public void set(final GWarriorObject[] warriors) {
		this.gui.set(warriors);
	}

	public void set(final GFoodObject[] foods) {
		this.gui.set(foods);
	}

	public void set(final GHillObject[] hills) {
		this.gui.set(hills);
	}


	public void set(final GAntFoodObject[] antfood) {
		this.gui.set(antfood);
	}

	public void remove(final GAntObject[] ants) {
		this.gui.remove(ants);
	}

	public void remove(final GAntFoodObject[] antfood) {
		this.gui.remove(antfood);
	}

	public void remove(final GFoodObject[] foods) {
		this.gui.remove(foods);
	}

	public void remove(final GHillObject[] hills) {
		this.gui.remove(hills);
	}

	public void initMenu(final InitMenuData imData) {
		this.gui.createMenu(imData);
	}

	public void createGame(final CreateGameData cgData) {
		this.gui.createGame(cgData);
	}

	public void score(final ScoreData score) {
		this.gui.score(score);
	}

	public void showResults(final ResultData result) {
		this.gui.showResult(result);
	}

	public void close(final CloseData data) {
		this.gui.close(data);
	}

	public String getEvents() {
		final GuiEvent[] eventsArray = this.events.stream().toArray(GuiEvent[]::new);
		final String ret = Mapper.INSTANCE.pojoToJson(eventsArray);
		this.events.clear();
		return ret;
	}

	private final List<GuiEvent> events = new ArrayList<>();

	@Override
	public void actionPerformed(final GuiEvent event) {
		this.LOG.info("Gui Event occured: " + event.getType().toString() + ", " + event.getData());
		this.events.add(event);
	}

	public void set(final GUIObjectCrate crate) {
		this.gui.set(crate.dump());
	}

	public void remove(final GUIObjectCrate crate) {
		this.gui.remove(crate.dump());
	}
	
	public boolean isWindowShowing() {
		return gui.isWindowShowing();
	}
}
