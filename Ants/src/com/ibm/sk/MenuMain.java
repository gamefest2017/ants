package com.ibm.sk;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.ibm.sk.ant.AntLoader;
import com.ibm.sk.ant.facade.AntFactory;
import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.ff.gui.client.ReplayFileHelper;
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
		AntFactory[] implementations = AntLoader.getImplementations();
		InitMenuData imd = new InitMenuData();
		
		imd.setCompetitors(Arrays.asList(implementations).stream().map(i -> i.getTeamName()).collect(Collectors.toList()).stream().toArray(String[]::new));
		imd.setReplay(ReplayFileHelper.getAvailableReplays());
		
		getGuiFacade().showInitMenu(imd);
		getGuiFacade().addGuiEventListener(new GameMenuHandler(FACADE, imd));
	}

}
