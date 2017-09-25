package com.ibm.sk.ff.gui.common.objects.operations;

import java.util.Arrays;

public class InitMenuData {
	
	public String[] competitors = {};
	
	public String[] replays = {};
	
	public void addCompetitor(String competitor) {
		competitors = Arrays.copyOf(competitors, competitors.length + 1);
		competitors[competitors.length - 1] = competitor;
	}
	
	public void setCompetitors(String [] competitors) {
		this.competitors = competitors;
	}
	
	public String[] getCompetitors() {
		return competitors;
	}
	
	public void addReplay(String replay) {
		replays = Arrays.copyOf(replays, replays.length + 1);
		replays[replays.length -1] = replay;
	}
	
	public void setReplay(String[] replay) {
		replays = replay;
	}
	
	public String[] getReplays() {
		return replays;
	}

}
