package com.ibm.sk.dto.enums;

public enum HillOrder {
	FIRST(0, 1), SECOND(1, -2);

	private final int order;
	private final int xOffset;

	HillOrder(final int order, final int xOffset) {
		this.order = order;
		this.xOffset = xOffset;
	}

	public int getOrder() {
		return this.order;
	}

	public int getXOffset() {
		return this.xOffset;
	}
}
