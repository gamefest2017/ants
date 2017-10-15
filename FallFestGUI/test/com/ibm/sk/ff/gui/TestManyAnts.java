package com.ibm.sk.ff.gui;

import java.util.ArrayList;
import java.util.List;

import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.Location;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.simple.SimpleGUI;

public class TestManyAnts {
	
	private SimpleGUI sg = new SimpleGUI();
	private GAntObject[][] ants = new GAntObject[50][40];
	
	public TestManyAnts() {
		
	}
	
	public void run() {
		createGame();
		
		try {Thread.sleep(1000);}catch (Exception e) {}
		
		createAnts();
		
		int direction = -1;
		while (true) {
			renderAnts();
			
			direction *= -1;
			
			moveAnts(direction);
		}
	}
	
	private void renderAnts() {
		List<GAntObject> toAdd = new ArrayList<>();
		for (int i = 0; i < ants.length; i++)
			for (int j = 0; j < ants[i].length; j++)
				toAdd.add(ants[i][j]);
		
		sg.set(toAdd.stream().toArray(GAntObject[]::new));
	}
	
	private void moveAnts(int direction) {
		for (int i = 0; i < ants.length; i++) {
			for (int j = 0; j < ants[i].length; j++) {
				GAntObject swp = ants[i][j];
				Location l = swp.getLocation();
				l.setY(l.getY() + direction);
				ants[i][j] = swp;
			}
		}
	}
	
	private void createAnts() {
		for (int i = 0; i < ants.length; i++) {
			for (int j = 0; j < ants[i].length; j++) {
				GAntObject ant = new GAntObject();
				ant.setId((long)i * 1000 + (long)j);
				ant.setLocation(i, j);
				ants[i][j] = ant;
			}
		}
	}
	
	private void createGame() {
		CreateGameData cgd = new CreateGameData();
		cgd.setHeight(41);
		cgd.setWidth(51);
		cgd.setTeams(new String [] {"Team1", "Team2"});
		sg.createGame(cgd);
	}
	
	public static void main(String [] args) {
		new TestManyAnts().run();
	}

}
