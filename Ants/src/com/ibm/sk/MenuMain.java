package com.ibm.sk;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.ibm.sk.ant.AntLoader;
import com.ibm.sk.ant.facade.AntFactory;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.handlers.GameMenuHandler;

public class MenuMain extends AbstractMain {

	public MenuMain() {
		super();
	}

	public static void main(final String args[]) {
		showMainWindow();
	}
	
	public static void showMainWindow() {
		final AntFactory[] implementations = AntLoader.getImplementations();
		final InitMenuData initData = new InitMenuData();

		initData.setCompetitors(Arrays.asList(implementations).stream().map(AntFactory::getTeamName)
				.collect(Collectors.toList()).stream().toArray(String[]::new));
		FACADE.showInitMenu(initData);
		FACADE.addGuiEventListener(new GameMenuHandler(FACADE, initData, implementations));
	}

}
