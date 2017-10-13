package com.ibm.sk;

import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.handlers.GameMenuHandler;

public class MenuMain extends AbstractMain {

	public MenuMain() {
		super();
	}

	public static void main(final String args[]) {
		InitMenuData imd = new InitMenuData();
		getGuiFacade().showInitMenu(imd);
		getGuiFacade().addGuiEventListener(new GameMenuHandler(new ProcessExecutor(FACADE)));
	}

}
