package com.ibm.sk.ant.implementation;

import java.awt.Point;

import com.ibm.sk.ant.facade.AntFactory;
import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Hill;

public class StripedFactory extends AntFactory {

	@Override
	public String getTeamName() {
		return "Striped Team";
	}

	@Override
	public AbstractAnt createWorker(long id, Point position, Hill hill) {
		return new StripedAnt(id, position, hill);
	}

	@Override
	public AbstractWarrior createWarrior(long id, Point position, Hill hill) {
		return null;
	}

}
