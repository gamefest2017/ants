package com.ibm.sk.ff.gui.common.objects.gui;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GUIObjectTypes {

	ANT("ant"),
	FOOD("food"),
	ANT_FOOD("antfood"),
	HILL("hill"), WARRIOR("warrior"),
	;

	private static Map<String, GUIObjectTypes> VALUES = new HashMap<>();

	static {
		for (final GUIObjectTypes it : values()) {
			VALUES.put(it.NAME, it);
		}
	}

	private final String NAME;

	private GUIObjectTypes(final String name) {
		this.NAME = name;
	}

	@JsonCreator
	public static GUIObjectTypes forValue(final String value) {
		return VALUES.get(value);
	}

	@Override
	@JsonValue
	public String toString() {
		return this.NAME;
	}

}
