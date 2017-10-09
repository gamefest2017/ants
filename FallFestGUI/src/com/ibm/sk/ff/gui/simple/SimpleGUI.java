package com.ibm.sk.ff.gui.simple;

import java.awt.BorderLayout;
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
	public void set(GAntObject[] ants) {
		canvas.set(ants);
	}

	@Override
	public void set(GFoodObject[] food) {
		canvas.set(food);
	}

	@Override
	public void set(GHillObject[] hill) {
		canvas.set(hill);
	}
	
	@Override
	public void set(GAntFoodObject[] afo) {
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
		frame.setContentPane(new JLabel(new ImageIcon(loadBackgroundImage())));
		
		scoreboard = new ScoreboardSmall(data);
		canvas = new SimpleCanvas(data.getWidth(), data.getHeight(), data.getTeams(), frame);
		
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.add(scoreboard, BorderLayout.NORTH);
		
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private BufferedImage loadBackgroundImage() {
		BufferedImage ret = null;
		try {
			ret = ImageIO.read(new File("res/grass2.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public void remove(GAntObject[] ant) {
		canvas.remove(ant);
	}

	@Override
	public void remove(GFoodObject[] food) {
		canvas.remove(food);
	}

	@Override
	public void remove(GHillObject[] hill) {
		canvas.remove(hill);
	}
	
	@Override
	public void remove(GAntFoodObject[] antfood) {
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

		//Create and set up the window.
		frame = new JFrame("IBM Slovakia / Fall Fest 2017 / Anthill");
        frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
        //Set up the content pane.
		frame.add(new Menu(data, listener, frame.getContentPane()));
		
        //Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void score(ScoreData score) {
		scoreboard.setScore(score);
	}

	@Override
	public void addGuiEventListener(GuiEventListener listener) {
		this.listener = listener;
	}

}
