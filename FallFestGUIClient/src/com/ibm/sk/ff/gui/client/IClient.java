package com.ibm.sk.ff.gui.client;

public interface IClient {
	
	public boolean postMessage(String url, String message);
	public String getMessage(String url);

}
