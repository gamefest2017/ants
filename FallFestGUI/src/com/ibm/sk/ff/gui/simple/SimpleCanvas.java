package com.ibm.sk.ff.gui.simple;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.ibm.sk.ff.gui.common.objects.gui.GAntFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.ff.gui.config.Config;

public class SimpleCanvas extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private final Color[] COLORS = {Color.BLACK, Color.RED};
	
	private final boolean DEV_MODE = Boolean.parseBoolean(Config.DEV_MODE.toString());
	
	private static long SLEEP_INTERVAL = 10; //TODO - the transition interval should be set correctly!
	
	private static Image[] IMAGES_ANT = null;
	private static Image IMAGES_FOOD = null;
	private static Image IMAGES_HILL = null;
	private static Image[] IMAGES_ANT_FOOD = null;
	
	private boolean finishedRedraw = true;
	
	static {
		try {
			IMAGES_ANT = new Image[] {ImageIO.read(new File("res/ant_left.png")), ImageIO.read(new File("res/ant_right.png"))};
			IMAGES_FOOD = ImageIO.read(new File("res/food.png"));
			IMAGES_HILL = ImageIO.read(new File("res/hill.png"));
			IMAGES_ANT_FOOD = new Image [] {ImageIO.read(new File("res/antWithCookie_left.png")), ImageIO.read(new File("res/antWithCookie_right.png"))};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final Map<GUIObject, SimpleGUIComponent> OBJECTS = new HashMap<>();
	
	private final int WIDTH, HEIGHT;
	
	private String [] teams;
	
	private JFrame parentFrame;
	
	public SimpleCanvas(int width, int height, String[] teams, JFrame parent) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.teams = teams;
		this.parentFrame = parent;
		
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("asdfasdf");
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_SPACE) {
					parentFrame.setVisible(false);
					parentFrame.dispose();
				}
			}
		});
	}
	
	public void paint(Graphics g) {
		if (DEV_MODE) {
			paintGrid(g);
		}
		
		finishedRedraw = true;
			
		for (SimpleGUIComponent it : OBJECTS.values().stream().toArray(SimpleGUIComponent[]::new)) {
			if (!it.paint(g)) {
				finishedRedraw = false;
			}
		}
	}
	
	private void paintGrid(Graphics g) {
		g.setColor(Color.GRAY);
		for (int i = 0; i < WIDTH; i++) {
			g.drawLine(i * MAGNIFICATION_X(), 0, i * MAGNIFICATION_X(), HEIGHT * MAGNIFICATION_Y());
		}
		for (int i = 0; i < HEIGHT; i++) {
			g.drawLine(0, i * MAGNIFICATION_Y(), WIDTH * MAGNIFICATION_X(), i * MAGNIFICATION_Y());
		}
	}
	
	private void performRepaint() {
		do {
			paintImmediately(0, 0, getWidth(), getHeight());
			try {
				Thread.sleep(SLEEP_INTERVAL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (!finishedRedraw);
	}
	
	private void animate() {
		//TODO
	}
	
	public void set(GHillObject[] hills) {
		for (GHillObject it : hills) {
			SimpleGUIComponent toAdd = null;
			if (OBJECTS.containsKey(it)) {
				OBJECTS.remove(it);
			}
			toAdd = new SimpleGUIComponent(MAGNIFICATION_X(), MAGNIFICATION_Y(), new Image[] {IMAGES_HILL}, null, getTeamColor(it));
			toAdd.setLocation(it.getLocation().getX(), it.getLocation().getY());
			OBJECTS.put(it, toAdd);
		}
		performRepaint();
	}
	
	public void set(GFoodObject[] foods) {
		for (GFoodObject it : foods) {
			SimpleGUIComponent toAdd = null;
			if (OBJECTS.containsKey(it)) {
				OBJECTS.remove(it);
			}
			toAdd = new SimpleGUIComponent(MAGNIFICATION_X(), MAGNIFICATION_Y(), new Image[] {IMAGES_FOOD}, null, getTeamColor(it));
			toAdd.setLocation(it.getLocation().getX(), it.getLocation().getY());
			OBJECTS.put(it, toAdd);
		}
		performRepaint();
	}
	
	public void set(GAntObject[] ants) {
		for (GAntObject it : ants) {
			SimpleGUIComponent toAdd = null;
			if (OBJECTS.containsKey(it)) {
				toAdd = OBJECTS.remove(it);
				toAdd.moveToLocation(it.getLocation().getX(), it.getLocation().getY());
			} else {
				toAdd = new SimpleGUIComponent(MAGNIFICATION_X(), MAGNIFICATION_Y(), IMAGES_ANT, null, getTeamColor(it));
				toAdd.setLocation(it.getLocation().getX(), it.getLocation().getY());
			}
			OBJECTS.put(it, toAdd);
		}
		performRepaint();
	}
	
	public void set(GAntFoodObject[] gafos) {
		for (GAntFoodObject it : gafos) {
			SimpleGUIComponent toAdd = null;
			if (OBJECTS.containsKey(it)) {
				toAdd = OBJECTS.remove(it);
				toAdd.moveToLocation(it.getLocation().getX(), it.getLocation().getY());
			} else {
				toAdd = new SimpleGUIComponent(MAGNIFICATION_X(), MAGNIFICATION_Y(), IMAGES_ANT_FOOD, null, getTeamColor(it));
				toAdd.moveToLocation(it.getLocation().getX(), it.getLocation().getY());
			}
			OBJECTS.put(it, toAdd);
		}
		performRepaint();
	}
	
	private Color getTeamColor(GUIObject go) {
		Color ret = null;
		if (go instanceof GAntObject) {
			GAntObject swp = (GAntObject)go;
			for (int i = 0; i < teams.length; i++) {
				if (teams[i].equals(swp.getTeam())) {
					ret = COLORS[i];
				}
			}
		}
		if (go instanceof GHillObject) {
			GHillObject swp = (GHillObject)go;
			for (int i = 0; i < teams.length; i++) {
				if (teams[i].equals(swp.getTeam())) {
					ret = COLORS[i];
				}
			}
		}
		if (go instanceof GAntFoodObject) {
			GAntFoodObject swp = (GAntFoodObject)go;
			for (int i = 0; i < teams.length; i++) {
				if (swp.getAnt() != null && teams[i].equals(swp.getAnt().getTeam())) {
					ret = COLORS[i];
				}
			}
		}
		return ret;
	}
	
	public void remove(GUIObject object) {
		remove (new GUIObject[] {object});
	}
	
	public void remove(GUIObject[] object) {
		boolean changed = false;
		for (GUIObject it : object) {
			if (this.OBJECTS.containsKey(it.getId())) {
				this.OBJECTS.remove(it.getId());
				changed = true;
			}
		}
		if (changed) {
			performRepaint();
		}
	}
	
	public void reset(InitMenuData data) {
		OBJECTS.clear();

		performRepaint();
	}
	
	public boolean contains(GUIObject go) {  
		return OBJECTS.containsKey(go.getId());
	}
	
	private int magnificationX = -1;
	private int MAGNIFICATION_X() {
		if (magnificationX == -1) {
			magnificationX = getWidth() / WIDTH;
		}
		return magnificationX;
	}
	
	private int magnificationY = -1;
	private int MAGNIFICATION_Y() {
		if (magnificationY == -1) {
			magnificationY = getHeight() / HEIGHT;
		}
		return magnificationY;
	}

}
