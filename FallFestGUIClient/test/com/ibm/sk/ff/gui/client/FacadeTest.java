package com.ibm.sk.ff.gui.client;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.sk.ff.gui.common.objects.operations.CloseData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;
import com.ibm.sk.ff.gui.common.objects.operations.ResultData;

public class FacadeTest {
	
	public static GUIFacade FACADE;
	
	@BeforeClass
	public static void prepare() {
		FACADE = new GUIFacade();
	}
	
	@Test
	public void testCreateSplashscreen() {
		FACADE.showScore(new ScoreData());
	}
	
	@Test
	public void testCreateResultData() {
		FACADE.showResult(new ResultData());
	}
	
	@Test
	public void testCloseData() {
		FACADE.close(new CloseData());
	}
	
	@AfterClass
	public static void destroy() {
		//nothing to do
	}

}
