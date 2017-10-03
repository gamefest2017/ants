package com.ibm.sk.ff.gui.client.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.gui.Location;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;

public class Demo_OneMoreATime implements Runnable {
	
	private static long counter = 0;
	
	private GUIFacade facade = new GUIFacade();
	
	private List<GAntObject> teamA = new ArrayList<>();
	private List<GAntObject> teamB = new ArrayList<>();
	
	private int width = 20, height = 15;
	
	private Random random = new Random();
	
	public Demo_OneMoreATime() {
		
		for (int i = 0; i < 5; i++) {
			teamA.add(createAntObject("TeamA", i, 0));
		}
		
		for (int i = 0; i < 5; i++) {
			teamB.add(createAntObject("TeamB", i, 14));
		}
		
		CreateGameData cgd = new CreateGameData();
		cgd.setHeight(height);
		cgd.setWidth(width);
		cgd.setTeams(new String [] {"TeamA", "TeamB"});
		facade.createGame(cgd);
		
		facade.set(antsToObjects());
	}
	
	public static void main(String [] args) {
		new Thread(new Demo_OneMoreATime()).start();
	}
	
	@Override
	public void run() {
		while (true) {
			
			teamA.stream().forEach(t -> {
				Location [] swp = possibleMoves(t);
				Location step = swp[random.nextInt(swp.length)];
				t.setLocation(mergeLocations(t.getLocation(), step));
			});
			teamB.stream().forEach(t -> {
				Location [] swp = possibleMoves(t);
				Location step = swp[random.nextInt(swp.length)];
				t.setLocation(mergeLocations(t.getLocation(), step));
			});
			
			facade.set(antsToObjects());
		}
	}
	
	private Location mergeLocations(Location a, Location b) {
		int x = a.getX() + b.getX();
		int y = a.getY() + b.getY();
		
		return new Location(x, y);
	}
	
	private GUIObject[] antsToObjects() {
		GUIObject[] objects = new GUIObject[teamA.size() + teamB.size()];
		int i = 0;
		for (int j = 0; j < teamA.size(); j++) {
			objects[i++] = teamA.get(j);
		}
		for (int j = 0; j < teamB.size(); j++) {
			objects[i++] = teamB.get(j);
		}
		return objects;
	}
	
	private GAntObject createAntObject(String team, int x, int y) {
		GAntObject ret = new GAntObject();
		ret.setId(counter++);
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
				if ((x + i) >= 0 && (x + i) < width && (y + j) >= 0 && (y + j) < height) {
					ret.add(new Location(i, j));
				}
			}
		}
		
		return ret.stream().toArray(Location[]::new);
	}
	
}
