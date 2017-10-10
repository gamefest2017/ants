package com.ibm.sk.dto.enums;

public enum HillOrder {
	FIRST(0), SECOND(1);

	private final int order;

	HillOrder(final int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}
}
