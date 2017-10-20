package com.ibm.sk.ant.singlejar;

import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.ff.gui.Facade;
import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.simple.SimpleGUI;

public class GameSimulator implements Runnable {
	
	private GUIFacade facade = new GUIFacade(new DirectClient(new Facade(new SimpleGUI())));
	
	
	@Override
	public void run() {
		String competitor = Config.COMPETITOR.toString();
		if (competitor.length() == 0) {
			competitor = null;
		}
		new ProcessExecutor(this.facade).run(Config.TEAM.toString(), competitor);
	}

}
