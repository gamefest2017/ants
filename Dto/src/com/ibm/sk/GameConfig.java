package com.ibm.sk;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public enum GameConfig {
	
	WIDTH("game.width", "40"),
	HEIGHT("game.height", "20"),
	TURNS("turns", "500"),
	;
	
	private static final Properties PROPS = new Properties();
	
	static {
		File f = new File("ant.singlejar.properties");
		if (f.exists() && f.isFile()) {
			try {
				PROPS.load(new FileInputStream(f));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private final String KEY;
	private final String DEFAULT_VALUE;
	
	private GameConfig(String key, String defaultValue) {
		this.KEY = key;
		this.DEFAULT_VALUE = defaultValue;
	}
	
	@Override
	public String toString() {
		return PROPS.getProperty(KEY, DEFAULT_VALUE);
	}

}
