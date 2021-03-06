package com.ibm.sk.ff.gui.client;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public enum Config {

	HOSTNAME("gui.server.hostname", "localhost"),
	PORT("gui.server.port", "60065"),
	SERVER_POLL_INTERVAL("server.poll.interval", "100"),
	REPLAY_FOLDER("replay.folder", "./replays"),
	;

	private static final Properties PROPS = new Properties();

	static {
		try {
			PROPS.load(new FileInputStream(new File("gui.client.properties")));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private final String KEY;
	private final String DEF;

	private Config(final String key, final String def) {
		this.KEY = key;
		this.DEF = def;
	}

	@Override
	public String toString() {
		return PROPS.getProperty(this.KEY, this.DEF);
	}

}
