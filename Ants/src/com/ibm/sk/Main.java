package com.ibm.sk;

import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.handlers.GameMenuHandler;

public class Main extends AbstractMain {

	private static int turn;

	public static void main(final String args[]) {

		final GameMenuHandler menuHandler = new GameMenuHandler(new ProcessExecutor(FACADE));
		if (args.length == 0) {
			menuHandler.startSinglePlayer("Majstri Svetla");
		} else if (args.length == 1) {
			menuHandler.startSinglePlayer(args[0]);
		} else if (args.length == 2) {
			menuHandler.startDoublePlayer(args[0], args[1]);
		}
	}

	public static int getTurn() {
		return turn;
	}
}
