package com.ibm.sk.dto;

import java.awt.Point;
import java.io.Serializable;

public interface IWorldObject extends Serializable {
	
	Point getPosition();

	void setPosition(Point position);
	
	long getId();

	void setId(long id);
}
