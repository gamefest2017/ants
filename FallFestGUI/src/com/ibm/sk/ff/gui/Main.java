package com.ibm.sk.ff.gui;

import java.io.IOException;

import com.ibm.sk.ff.gui.server.Server;
import com.ibm.sk.ff.gui.simple.SimpleGUI;

public class Main {
	
	public static void main(String [] args) {
		try {
			SimpleGUI gui = new SimpleGUI();
			Facade f = new Facade(gui);
			gui.addGuiEventListener(f);
			new Server(f);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
}
