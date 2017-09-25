package com.ibm.sk.ff.gui;

import com.ibm.sk.ff.gui.common.events.InitMenuEventListener;
import com.ibm.sk.ff.gui.common.objects.gui.GAntFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.operations.CloseData;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.ff.gui.common.objects.operations.ResultData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;

public interface GUI {
	
	public void createMenu(InitMenuData data);
	
	public void createGame(CreateGameData data);
	
	public void set(GAntObject ants);
	public void set(GAntFoodObject antfood);
	public void set(GFoodObject foods);
	public void set(GHillObject hills);
	
	public void remove(GAntObject ants);
	public void remove(GAntFoodObject antfood);
	public void remove(GFoodObject foods);
	public void remove(GHillObject hills);
	
	public void score(ScoreData score);
	
	public void showResult(ResultData data);
	
	public void close(CloseData data);
	
	public void addInitMenuEventListener(InitMenuEventListener listener);
	
}
