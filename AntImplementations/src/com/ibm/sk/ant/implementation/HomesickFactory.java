package com.ibm.sk.ant.implementation;

import java.awt.Point;

import com.ibm.sk.ant.facade.AntFactory;
import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.Warrior;

public class HomesickFactory extends AntFactory {

	@Override
	public String getTeamName() {
		return "Homesick";
	}

	@Override
	public AbstractAnt createWorker(final long id, final Point position, final Hill hill) {
		return new HomesickAnt(id, position, hill);
	}

	@Override
	public AbstractWarrior createWarrior(final long id, final Point position, final Hill hill) {
		return new Warrior(id, position, hill);
	}

}
