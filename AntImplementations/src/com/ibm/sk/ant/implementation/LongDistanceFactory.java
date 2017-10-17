package com.ibm.sk.ant.implementation;

import java.awt.Point;

import com.ibm.sk.ant.facade.AntFactory;
import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Hill;

public class LongDistanceFactory extends AntFactory {

	@Override
	public String getTeamName() {
		return "Long distance";
	}

	@Override
	public AbstractAnt createWorker(long id, Point position, Hill hill) {
		return new LongDistanceAnt(id, position, hill);
	}

	@Override
	public AbstractWarrior createWarrior(long id, Point position, Hill hill) {
		return null;
	}

}
