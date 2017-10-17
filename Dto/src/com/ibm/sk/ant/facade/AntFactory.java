package com.ibm.sk.ant.facade;

import java.awt.Point;

import com.ibm.sk.dto.AbstractAnt;
import com.ibm.sk.dto.AbstractWarrior;
import com.ibm.sk.dto.Hill;

public abstract class AntFactory {
	
	public abstract String getTeamName();
	
	public abstract AbstractAnt createWorker(long id, Point position, Hill hill);
	
	public abstract AbstractWarrior createWarrior(long id, Point position, Hill hill);

}
