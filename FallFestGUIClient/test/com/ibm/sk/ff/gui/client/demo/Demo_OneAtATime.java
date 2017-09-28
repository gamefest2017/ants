package com.ibm.sk.ff.gui.client.demo;

import java.util.ArrayList;
import java.util.List;

import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.Location;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;

public class Demo_OneAtATime implements Runnable {
	
	private GUIFacade facade = new GUIFacade();
	
	private List<GAntObject> teamA = new ArrayList<>();
	private List<GAntObject> teamB = new ArrayList<>();
	
	private int width = 20, height = 15;
	
	public Demo_OneAtATime() {
		
		for (int i = 0; i < 5; i++) {
			teamA.add(createAntObject("teamA", i, 0));
		}
		
		for (int i = 0; i < 5; i++) {
			teamB.add(createAntObject("teamB", i, 14));
		}
		
		CreateGameData cgd = new CreateGameData();
		cgd.setHeight(height);
		cgd.setWidth(width);
		cgd.setTeams(new String [] {"TeamA", "TeamB"});
		facade.createGame(cgd);
		
		teamA.stream().forEach(t -> facade.set(t));
		teamB.stream().forEach(t -> facade.set(t));
	}
	
	@Override
	public void run() {
		while (true) {
			teamA.stream().forEach(t -> {
				Location [] swp = possibleMoves(t);
				Location step = swp[(int)(Math.random() * swp.length)];
				t.setLocation(step);
				facade.set(t);
			});
		}
	}
	
	private GAntObject createAntObject(String team, int x, int y) {
		GAntObject ret = new GAntObject();
		ret.setTeam(team);
		ret.setLocation(x, y);
		return ret;
	}
	
	private Location [] possibleMoves(GAntObject ant) {
		int x = ant.getLocation().getX();
		int y = ant.getLocation().getY();
		List<Location> ret = new ArrayList<>();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if ((x + i) >= 0 && (x + i) <= width) {
					if ((y + j) >= 0 && (y + j) <= height) {
						ret.add(new Location(i, j));
					}
				}
			}
		}
		
		return ret.stream().toArray(Location[]::new);
	}
	
	

}
