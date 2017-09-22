package com.ibm.sk.dto;

import java.awt.Point;

public class Food extends WorldObject {

	private int amount;

	public Food(final int amount, final Point position) {
		this.amount = amount;
		this.position = position;
	}

	public int getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "Food amount: " + this.amount + " on position: " + (int) this.position.getX() + ", " + (int) this.position.getY();
	}
}
