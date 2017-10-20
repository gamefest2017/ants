package com.ibm.sk.ant.singlejar;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public enum Config {
	
	SIZE_X("size.x", "20"),
	SIZE_Y("size.y", "10"),
	ROUNDS("rounds", "20"),
	TEAM("team.name", "Homesick"),
	COMPETITOR("competitor.name", ""),
	;
	
	private static final Properties PROPS = new Properties();
	
	static {
		try {
			File toLoad = new File("ant.singlejar.properties");
			if (toLoad.exists() && toLoad.isFile()) {
				PROPS.load(new FileInputStream(toLoad));
			}
		} catch (Exception e) {
			System.err.println("Error when loading ant.singlejar.properties");
		}
	}
	
	private final String KEY;
	private final String DEFAULT;
	
	private Config(String key, String defaultValue) {
		this.KEY = key;
		this.DEFAULT = defaultValue;
	}
	
	public String toString() {
		return PROPS.getProperty(KEY, DEFAULT);
	}

}
