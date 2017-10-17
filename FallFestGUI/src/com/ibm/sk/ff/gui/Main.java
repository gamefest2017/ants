package com.ibm.sk.ff.gui;

import java.io.IOException;

import com.ibm.sk.ff.gui.server.Server;
import com.ibm.sk.ff.gui.simple.SimpleGUI;

public class Main {
	
	public static void main(final String [] args) {
		try {
			final SimpleGUI gui = new SimpleGUI();
			final Facade f = new Facade(gui);
			gui.addGuiEventListener(f);
			new Server(f);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
}
