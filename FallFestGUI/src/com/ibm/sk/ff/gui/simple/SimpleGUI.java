package com.ibm.sk.ff.gui.simple;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ibm.sk.ff.gui.GUI;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.objects.gui.GAntFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.operations.CloseData;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.ff.gui.common.objects.operations.ResultData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;
import com.ibm.sk.ff.gui.config.Config;

public class SimpleGUI implements GUI {

	private static final int MAGNIFICATION = Integer.parseInt(Config.GUI_MAGNIFICATION.toString());

	private JFrame frame = null;
	private final Menu menu = null;

	private SimpleCanvas canvas = null;
	private ScoreboardSmall scoreboard = null;

	private GuiEventListener listener = null;

	public SimpleGUI() {
	}

	@Override
	public void set(final GAntObject[] it) {
		this.canvas.set(it);
	}

	@Override
	public void set(final GFoodObject[] food) {
		this.canvas.set(food);
	}

	@Override
	public void set(final GHillObject[] hill) {
		this.canvas.set(hill);
	}

	@Override
	public void set(final GAntFoodObject[] afo) {
		//		List<GAntFoodObject> antsList = new ArrayList<>();
		//		for (int i = 0; i < afo.length; i++) {
		//			GAntFoodObject swp = afo[i];
		//			swp.setLocation(afo[i].getAnt().getLocation());
		//			antsList.add(swp);
		//		}
		//		if (antsList.size() > 0) {
		//			canvas.set(antsList.stream().toArray(GAntFoodObject[]::new));
		//		}
		this.canvas.set(afo);
	}

	@Override
	public void createGame(final CreateGameData data) {
		if (this.frame != null) {
			this.frame.dispose();
			this.frame = null;
		}
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.frame.setLayout(new BorderLayout());

		this.canvas = new SimpleCanvas(data.getWidth(), data.getHeight(), MAGNIFICATION, data.getTeams());
		this.scoreboard = new ScoreboardSmall(data);

		final JPanel swpPanel = new JPanel();
		swpPanel.setLayout(new GridLayout(1, 1));
		swpPanel.add(this.canvas);

		this.frame.add(swpPanel, BorderLayout.CENTER);
		this.frame.add(this.scoreboard, BorderLayout.NORTH);
		this.frame.pack();
		this.frame.setVisible(true);
	}

	@Override
	public void remove(final GAntObject[] ant) {
		this.canvas.remove(ant);
	}

	@Override
	public void remove(final GFoodObject[] food) {
		this.canvas.remove(food);
	}

	@Override
	public void remove(final GHillObject[] hill) {
		this.canvas.remove(hill);
	}

	@Override
	public void remove(final GAntFoodObject[] antfood) {
		this.canvas.remove(antfood);
	}

	@Override
	public void close(final CloseData data) {
		System.exit(0);
	}

	@Override
	public void showResult(final ResultData data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void createMenu(final InitMenuData data) {
		if (this.frame != null) {
			this.frame.dispose();
			this.canvas = null;
			this.scoreboard = null;
			this.frame = null;
		}

		//Create and set up the window.
		this.frame = new JFrame("IBM Slovakia / Fall Fest 2017 / Anthill");
		this.frame.setExtendedState(this.frame.getExtendedState()|Frame.MAXIMIZED_BOTH);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());

		//Set up the content pane.
		this.frame.add(new Menu(data, this.listener, this.frame));

		//Display the window.
		this.frame.pack();
		this.frame.setVisible(true);
	}

	@Override
	public void score(final ScoreData score) {
		this.scoreboard.setScore(score);
	}

	@Override
	public void addGuiEventListener(final GuiEventListener listener) {
		this.listener = listener;
	}

	@Override
	public void set(final GUIObject[] objects) {
		this.canvas.set(objects);
	}

}
