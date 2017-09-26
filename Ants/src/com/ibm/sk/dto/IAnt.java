package com.ibm.sk.dto;

import com.ibm.sk.dto.enums.Direction;

public interface IAnt extends IWorldObject {

	int getId();

	void setId(int id);

	Direction move(Vision vision);

	void pickUpFood(Food food);

	Food dropFood();

	Hill getMyHill();

	String getMyHillName();

	boolean isEnemy(IAnt otherAnt);

}
