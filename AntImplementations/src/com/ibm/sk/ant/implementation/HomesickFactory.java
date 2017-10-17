package com.ibm.sk.ant.implementation;

import java.awt.Point;

import com.ibm.sk.ant.facade.AntFactory;
import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Hill;

public class HomesickFactory extends AntFactory {

	@Override
	public String getTeamName() {
		return "Homesick";
	}

	@Override
	public AbstractAnt createWorker(long id, Point position, Hill hill) {
		return new HomesickAnt(id, position, hill);
	}

	@Override
	public AbstractWarrior createWarrior(long id, Point position, Hill hill) {
		return null;
	}

}
