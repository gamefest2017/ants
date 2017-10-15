package com.ibm.sk;

import com.ibm.sk.ff.gui.client.GUIFacade;

public abstract class AbstractMain {

	public static final GUIFacade FACADE = new GUIFacade();

	public AbstractMain() {
		super();
	}

	protected static GUIFacade getGuiFacade() {
		return FACADE;
	}

}
