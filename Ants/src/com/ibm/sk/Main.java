package com.ibm.sk;

import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.handlers.GameMenuHandler;

public class Main extends AbstractMain {

	public static void main(final String args[]) {

		final GameMenuHandler menuHandler = new GameMenuHandler(FACADE, null);
		if (args.length == 0) {
			menuHandler.startSinglePlayer("Majstri Svetla");
		} else if (args.length == 1) {
			menuHandler.startSinglePlayer(args[0]);
		} else if (args.length == 2) {
			menuHandler.startDoublePlayer(args[0], args[1]);
		}
	}

}
