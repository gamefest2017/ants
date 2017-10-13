package com.ibm.sk.dto;

import com.ibm.sk.dto.enums.Direction;

public interface IAnt extends IWorldObject {

	Direction move(Vision vision);

	Hill getMyHill();

	String getMyHillName();

	boolean isEnemy(IAnt otherAnt);
}
