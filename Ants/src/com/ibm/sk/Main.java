package com.ibm.sk;

import static com.ibm.sk.WorldConstans.TURNS;
import static com.ibm.sk.engine.World.createHill;

import com.ibm.sk.engine.FoodHandler;
import com.ibm.sk.engine.ProcessExecutor;

public class Main {

    private static int turn;

	public static void main(String args[]) {
		createHill("King of ants");
		
		for (turn = 0; turn < TURNS; turn++) {
			ProcessExecutor.execute();
			FoodHandler.dropFood();
		}
	}

    public static int getTurn() {
        return turn;
    }
}
