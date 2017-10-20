package com.ibm.sk;

import java.util.stream.Stream;

import com.ibm.sk.ant.AntLoader;
import com.ibm.sk.ant.facade.AntFactory;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.handlers.GameMenuHandler;

public class MenuMain extends AbstractMain {

	public MenuMain() {
		super();
	}

	public static void main(final String args[]) {
		final AntFactory[] implementations = AntLoader.getImplementations();
		final InitMenuData initData = new InitMenuData();

		initData.setCompetitors(Stream.of(implementations).map(AntFactory::getTeamName).toArray(String[]::new));
		showMainWindow(initData);
	}

	public static void showMainWindow(final InitMenuData initData) {
		FACADE.addGuiEventListener(new GameMenuHandler(FACADE, initData));
		FACADE.showInitMenu(initData);
	}

}
