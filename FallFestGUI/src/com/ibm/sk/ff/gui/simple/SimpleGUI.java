package com.ibm.sk.ff.gui.simple;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ibm.sk.ff.gui.GUI;
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
import com.ibm.sk.ff.gui.config.Config;

public class SimpleGUI implements GUI {

	private static final int MAGNIFICATION = Integer.parseInt(Config.GUI_MAGNIFICATION.toString());

	private JFrame frame = null;
	
	private SimpleCanvas canvas = null;
	private ScoreboardSmall scoreboard = null;
	
	private InitMenuEventListener listener = null;
	
	public SimpleGUI() { 
	}
	
	@Override
	public void set(GAntObject it) {
		canvas.set(it);
	}

	@Override
	public void set(GFoodObject food) {
		canvas.set(food);
	}

	@Override
	public void set(GHillObject hill) {
		canvas.set(hill);
	}
	
	@Override
	public void set(GAntFoodObject afo) {
		canvas.set(afo);
	}

	@Override
	public void createGame(CreateGameData data) {
		if (frame != null) {
			frame.dispose();
			frame = null;
		}
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLayout(new BorderLayout());
		
		canvas = new SimpleCanvas(data.getWidth(), data.getHeight(), MAGNIFICATION, data.getTeams());
		scoreboard = new ScoreboardSmall(data);
		
		JPanel swpPanel = new JPanel();
		swpPanel.setLayout(new GridLayout(1, 1));
		swpPanel.add(canvas);
		
		frame.add(swpPanel, BorderLayout.CENTER);
		frame.add(scoreboard, BorderLayout.NORTH);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void remove(GAntObject ant) {
		canvas.remove(ant);
	}

	@Override
	public void remove(GFoodObject food) {
		canvas.remove(food);
	}

	@Override
	public void remove(GHillObject hill) {
		canvas.remove(hill);
	}
	
	@Override
	public void remove(GAntFoodObject antfood) {
		canvas.remove(antfood);
	}

	@Override
	public void close(CloseData data) {
		System.exit(0);
	}

	@Override
	public void showResult(ResultData data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void createMenu(InitMenuData data) {
		if (frame != null) {
			frame.dispose();
			canvas = null;
			scoreboard = null;
			frame = null;
		}
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(new Menu(data, listener), BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void score(ScoreData score) {
		scoreboard.setScore(score);
	}

	@Override
	public void addInitMenuEventListener(InitMenuEventListener listener) {
		this.listener = listener;
	}

}
