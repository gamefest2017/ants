package com.ibm.sk.ff.gui.simple;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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

public class SimpleGUI implements GUI {

	private JFrame frame = null;

	private SimpleCanvas canvas = null;
	private ScoreboardSmall scoreboard = null;

	private GuiEventListener listener = null;

	public SimpleGUI() {
	}

	@Override
	public void set(final GAntObject[] ants) {
		this.canvas.set(ants);
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
		final JLabel label = new JLabel(new ImageIcon(loadBackgroundImage()));
		this.frame.setContentPane(label);

		this.scoreboard = new ScoreboardSmall(data);
		this.canvas = new SimpleCanvas(data.getWidth(), data.getHeight(), data.getTeams(), this.frame);

		this.frame.setLayout(new BorderLayout());
		this.frame.add(this.canvas, BorderLayout.CENTER);
		this.frame.add(this.scoreboard, BorderLayout.NORTH);

		this.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.frame.setUndecorated(true);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}

	private BufferedImage loadBackgroundImage() {
		BufferedImage ret = null;
		try {
			ret = ImageIO.read(new File("res/grass2.jpg"));
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return ret;
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

	@Override
	public void remove(final GUIObject[] objects) {
		this.canvas.remove(objects);
	}

}
