package com.ibm.sk.ff.gui.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public enum Config {
	
	HOSTNAME("gui.server.hostname", "localhost"),
	PORT("gui.server.port", "60065"),
	GUI_MAGNIFICATION("gui.magnification", "5"),
	GUI_MOVE_INTERVAL("gui.move.interval", "500"),
	;
	
	private static final Properties PROPS = new Properties();
	
	static {
		try {
			PROPS.load(new FileInputStream(new File("gui.properties")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private final String KEY;
	private final String DEF;
	
	private Config(String key, String def) {
		KEY = key;
		DEF = def;
	}
	
	public String toString() {
		return PROPS.getProperty(KEY, DEF);
	}

}
