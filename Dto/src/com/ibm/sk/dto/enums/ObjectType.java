package com.ibm.sk.dto.enums;

public enum ObjectType {
	ANT, ENEMY_ANT, WARRIOR, ENEMY_WARRIOR, ANT_WITH_FOOD, ENEMY_ANT_WITH_FOOD, HILL, ENEMY_HILL, FOOD, EMPTY_SQUARE, BORDER;

	private long id;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
