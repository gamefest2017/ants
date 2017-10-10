package com.ibm.sk.ff.gui.client.demo;

import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.common.objects.gui.GAntFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.Location;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;

public class TestAntFood {
	
	private GUIFacade sg = new GUIFacade();
	
	private GAntObject gao = new GAntObject();
	private GFoodObject gfo = new GFoodObject();
	private GAntFoodObject gafo = new GAntFoodObject();
	
	public static void main(String [] args) {
		new TestAntFood().run();
	}
	
	public TestAntFood() {
		
	}
	
	public void run() {
		CreateGameData cgd = new CreateGameData();
		cgd.setHeight(15);
		cgd.setWidth(20);
		cgd.setTeams(new String [] {"Team1", "Team2"});
		sg.createGame(cgd);
		
		try {Thread.sleep(1000);} catch (Exception e) {}
		
		gao.setLocation(new Location(2, 2));
		gao.setTeam("Team1");
		gfo.setLocation(new Location(3, 3));
		gafo.setLocation(new Location(3, 3));
		
		// Add ANT
		sg.set(new GAntObject[] {gao});
		try {Thread.sleep(1000);} catch (Exception e) {}
		
		// Add FOOD
		sg.set(new GFoodObject[] {gfo});
		try {Thread.sleep(1000);} catch (Exception e) {}
		
		gafo.setAnt(gao);
		gafo.setFood(gfo);
		
		// Move ANT to FOOD
		gao.setLocation(new Location(3, 3));
		sg.set(new GAntObject[] {gao});
		try {Thread.sleep(1000);} catch (Exception e) {}
		
		// Remove ANT and FOOD
		sg.remove(new GAntObject[] {gao});
		sg.remove(new GFoodObject[] {gfo});

		// Add ANTFOOD
		sg.set(new GAntFoodObject[] {gafo});
		try {Thread.sleep(1000);} catch (Exception e) {}
		
		// Move ANTFOOD
		gafo.setLocation(3, 4);
		sg.set(new GAntFoodObject[] {gafo});
		try {Thread.sleep(1000);} catch (Exception e) {}
	}

}
