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
import com.ibm.sk.ff.gui.common.objects.operations.CloseData;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.ff.gui.common.objects.operations.ResultData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;
import com.sun.istack.internal.logging.Logger;

public class Facade implements GuiEventListener {
	
	private GUI gui;
	
	private Logger LOG = Logger.getLogger(Facade.class);
	
	public Facade(GUI gui) {
		this.gui = gui;
	}
	
	public void set(GAntObject ants) {
		gui.set(ants);
	}
	
	public void set(GFoodObject foods) {
		gui.set(foods);
	}
	
	public void set(GHillObject hills) {
		gui.set(hills);
	}
	
	public void set(GAntFoodObject antfood) {
		gui.set(antfood);
	}
	
	public void remove(GAntObject ants) {
		gui.remove(ants);
	}
	
	public void remove(GAntFoodObject antfood) {
		gui.remove(antfood);
	}
	
	public void remove(GFoodObject foods) {
		gui.remove(foods);
	}
	
	public void remove(GHillObject hills) {
		gui.remove(hills);
	}
	
	public void initMenu(InitMenuData imData) {
		gui.createMenu(imData);
	}
	
	public void createGame(CreateGameData cgData) {
		gui.createGame(cgData);
	}
	
	public void score(ScoreData score) {
		gui.score(score);
	}
	
	public void showResults(ResultData result) {
		gui.showResult(result);
	}
	
	public void close(CloseData data) {
		gui.close(data);
	}
	
	public String getEvents() {
		GuiEvent[] eventsArray = events.stream().toArray(GuiEvent[]::new);
		String ret = Mapper.INSTANCE.pojoToJson(eventsArray);
		events.clear();
		return ret;
	}
	
	private List<GuiEvent> events = new ArrayList<>();

	@Override
	public void actionPerformed(GuiEvent event) {
		LOG.info("Gui Event occured: " + event.getType().toString() + ", " + event.getData());
		events.add(event);
	}
	
}
