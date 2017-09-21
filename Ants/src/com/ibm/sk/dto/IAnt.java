package com.ibm.sk.dto;

import java.awt.Point;

import com.ibm.sk.dto.enums.Direction;

public interface IAnt {

	int getId();

	void setId(int id);

	Point getPosition();

	void setPosition(Point position);

	Direction move(Vision vision);

	void pickUpFood(int count);

	int dropFood();

	Hill getMyHill();

	String getMyHillName();

	boolean isEnemy(IAnt otherAnt);

}
