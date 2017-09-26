package com.ibm.sk.dto;

import java.awt.Point;

public class Food extends WorldObject {

	private final int amount;

	public Food(final int amount, final Point position) {
		this.amount = amount;
		this.position = position;
	}

	public int getAmount() {
		return this.amount;
	}

	@Override
	public String toString() {
		return "Food amount: " + this.amount + " on position: [" + this.position.x + ", " + this.position.y + "]";
	}
}
