package com.ibm.sk;

import java.util.Arrays;

import com.ibm.sk.ant.AntLoader;
import com.ibm.sk.ant.facade.AntFactory;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.handlers.GameMenuHandler;

public class Main extends AbstractMain {

	public static void main(final String args[]) {

		final AntFactory[] implementations = AntLoader.getImplementations();
		final InitMenuData imd = new InitMenuData();
		imd.setCompetitors(Arrays.asList(implementations).stream().map(AntFactory::getTeamName).toArray(String[]::new));
		final GameMenuHandler menuHandler = new GameMenuHandler(
				FACADE, imd);
		// if (args.length == 0) {
		// menuHandler.startSinglePlayer(menuHandler, "Homesick");
		// } else if (args.length == 1) {
		// menuHandler.startSinglePlayer(menuHandler, args[0]);
		// } else if (args.length == 2) {
		// menuHandler.startDoublePlayer(menuHandler, args[0], args[1]);
		// }
	}

}
