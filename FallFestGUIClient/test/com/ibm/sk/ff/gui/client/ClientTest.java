package com.ibm.sk.ff.gui.client;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClientTest {
	
	private static Client CLIENT;
	
	@BeforeClass
	public static void prepare() {
		CLIENT = new Client();
	}
	
	@Test
	public void sendMessage() {
		boolean result = CLIENT.postMessage("/", "Hello, World");
		
		assertTrue(result);
	}
	
	@AfterClass
	public static void destroy() {
		//nothing to do
	}

}
