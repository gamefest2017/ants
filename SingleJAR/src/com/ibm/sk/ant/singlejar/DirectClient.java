package com.ibm.sk.ant.singlejar;

import com.ibm.sk.ff.gui.Facade;
import com.ibm.sk.ff.gui.client.IClient;
import com.ibm.sk.ff.gui.common.GUIOperations;
import com.ibm.sk.ff.gui.server.RequestHandler;

public class DirectClient extends RequestHandler implements IClient {
	
	public DirectClient(Facade facade) {
		super(facade);
	}
	
	public boolean postMessage(String url, String message) {
		String[] swp = url.split("/");
		GUIOperations operation = createGUIOperation(swp);
		
		handle(operation, swp.length > 1 ? swp[1] : null, message);
		
		return true;
	}
	
	@Override
	public String getMessage(String url) {
	    
		return "";
	}
	

}
