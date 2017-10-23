package com.ibm.sk.ant.implementation;

import java.awt.Point;

import com.ibm.sk.ant.facade.AntFactory;
import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.Warrior;

public class CloneFactory extends AntFactory {

	@Override
	public String getTeamName() {
		return "Clones";
	}

	@Override
	public AbstractAnt createWorker(final long id, final Point position, final Hill hill) {
		return new StripedAnt(id, position, hill);
	}

	@Override
	public AbstractWarrior createWarrior(final long id, final Point position, final Hill hill) {
		return new Warrior(id, position, hill);
	}

}
