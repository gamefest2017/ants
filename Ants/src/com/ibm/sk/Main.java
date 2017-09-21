package com.ibm.sk;

import com.ibm.sk.engine.FoodHandler;
import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.engine.World;

public class Main {

	public static void main(String args[]) {
		World.createHill("King of ants");
		
		boolean endGame = false;
		do {
			ProcessExecutor.execute();
			if (Math.random() > 0.5) {
				FoodHandler.dropFood();
			}
			
			endGame = true;
		} while (!endGame);
	}
}
