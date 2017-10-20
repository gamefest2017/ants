package com.ibm.sk.ant.singlejar;

import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.ff.gui.Facade;
import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.simple.SimpleGUI;

public class GameSimulator implements Runnable {
	
	private Facade facade_gui = new Facade(new SimpleGUI());
	private DirectClient client = new DirectClient(facade_gui);
	private GUIFacade facade = new GUIFacade(client);
	
	
	@Override
	public void run() {
		String competitor = Config.COMPETITOR.toString();
		if (competitor.length() == 0) {
			competitor = null;
		}
		new ProcessExecutor(this.facade).run(Config.TEAM.toString(), competitor);
	}

}
