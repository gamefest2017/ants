package com.ibm.sk;

import com.ibm.sk.ant.AntLoader;
import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.handlers.GameMenuHandler;

public class Main extends AbstractMain {

	public static void main(final String args[]) {

		final InitMenuData imd = new InitMenuData();
		imd.setCompetitors(competitors);
		final GameMenuHandler menuHandler = new GameMenuHandler(
				new ProcessExecutor(FACADE, AntLoader.getImplementations()), imd);
		if (args.length == 0) {
			menuHandler.startSinglePlayer("Homesick");
		} else if (args.length == 1) {
			menuHandler.startSinglePlayer(args[0]);
		} else if (args.length == 2) {
			menuHandler.startDoublePlayer(args[0], args[1]);
		}
	}

}
