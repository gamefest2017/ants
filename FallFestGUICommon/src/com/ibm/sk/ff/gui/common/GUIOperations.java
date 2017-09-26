package com.ibm.sk.ff.gui.common;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GUIOperations {

	SHOW_INIT_MENU("show_menu"),
	
	CREATE_GAME("create"),
	
	SET("set"),
	REMOVE("remove"),
	SCORE("score"),

	SHOW_RESULT("show_result"),
	
	CLOSE("close"),
	
	EVENT_POLL("events"),
	;
	
	private static Map<String, GUIOperations> VALUES = new HashMap<>();
	
	static {
		for (GUIOperations it : values()) {
			VALUES.put(it.operation, it);
		}
	}
	
	private String operation;
	
	private GUIOperations(String oper) {
		this.operation = oper;
	}
	
	@JsonCreator
	public static GUIOperations forValue(String value) {
		return VALUES.get(value);
	}
	
	@Override
	@JsonValue
	public String toString() {
		return operation;
	}

}
