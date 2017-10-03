package com.ibm.sk.ff.gui.common.events;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class GuiEvent {
	
	public static enum EventTypes {
		SINGLE_PLAY_START("singlePlayStart"),
		DOUBLE_PLAY_START("doublePlayStart"),
		TOURNAMENT_PLAY_START("tournamentPlayStart"),
		PLAYER_1_SELECTED("playerOneSelected"),
		PLAYER_2_SELECTED("playerTwoSelected"),
		START_REPLAY("startReplay"),
		REPLAY_SELECTED("replaySelected"),
		;
		
		private static final Map<String, EventTypes> MAP = new HashMap<>();
		
		static {
			for (EventTypes it : values()) {
				MAP.put(it.NAME, it);
			}
		}
		
		private final String NAME;
		private EventTypes(String name) {
			this.NAME = name;
		}
		
		@JsonCreator
		public static EventTypes forName(String name) {
			return MAP.get(name);
		}
		
		@Override
		@JsonValue
		public String toString() {
			return NAME;
		}
	}
	
	private EventTypes type = null;
	private String data = null;;
	
	public GuiEvent() {
		
	}
	
	public GuiEvent(EventTypes type, String data) {
		this.type = type;
		this.data = data;
	}
	
	public void setType(EventTypes type) {
		this.type = type;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public EventTypes getType() {
		return type;
	}
	
	public String getData() {
		return data;
	}

}
