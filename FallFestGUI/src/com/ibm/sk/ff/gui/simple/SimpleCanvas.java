package com.ibm.sk.ff.gui.simple;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import com.ibm.sk.ff.gui.common.objects.gui.GAntFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectTypes;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.ff.gui.config.Config;

public class SimpleCanvas extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private final Color[] COLORS = {Color.BLACK, Color.RED};
	
	private final boolean DEV_MODE = Boolean.parseBoolean(Config.DEV_MODE.toString());
	
	private static long SLEEP_INTERVAL = Long.parseLong(Config.GUI_MOVE_INTERVAL.toString()) / Long.parseLong(Config.GUI_MAGNIFICATION.toString());
	
	private static Image IMAGES_GRASS = null;
	private static Image[] IMAGES_ANT = null;
	private static Image IMAGES_FOOD = null;
	private static Image IMAGES_HILL = null;
	private static Image[] IMAGES_ANT_FOOD = null;
	
	private static final Color BACKGROUND_COLOR = Color.GREEN;
	
	private boolean finishedRedraw = true;
	
	static {
		try {
			IMAGES_GRASS = ImageIO.read(new File("res/grass2.jpg"));
			IMAGES_ANT = new Image[] {ImageIO.read(new File("res/ant_left.png")), ImageIO.read(new File("res/ant_right.png"))};
			IMAGES_FOOD = ImageIO.read(new File("res/food.png"));
			IMAGES_HILL = ImageIO.read(new File("res/hill.png"));
			IMAGES_ANT_FOOD = new Image [] {ImageIO.read(new File("res/antWithCookie_left.png")), ImageIO.read(new File("res/antWithCookie_right.png"))};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final int MAGNIFICATION;
	
	private Map<GUIObject, SimpleGUIComponent> objects = new HashMap<>();
	
	private int w, h;
	
	private String [] teams;
	
	public SimpleCanvas(int width, int height, int magnification, String[] teams) {
		this.MAGNIFICATION = magnification;
		this.w = width;
		this.h = height;
		setSize(width * magnification, height * magnification);
		setPreferredSize(new Dimension(width * magnification, height * magnification));
		this.teams = teams;
	}
	
	public void paint(Graphics g) {
		try {
			g.drawImage(IMAGES_GRASS, 0, 0, w * MAGNIFICATION, h * MAGNIFICATION, Color.GREEN, null);
			if (DEV_MODE) {
				paintGrid(g);
			}
		} catch (Exception e) {
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		finishedRedraw = true;
			
		for (SimpleGUIComponent it : objects.values().stream().toArray(SimpleGUIComponent[]::new)) {
			if (!it.paint(g)) {
				finishedRedraw = false;
			}
		}
	}
	
	private void paintGrid(Graphics g) {
		g.setColor(Color.GRAY);
		for (int i = 0; i < w; i++) {
			g.drawLine(i * MAGNIFICATION, 0, i * MAGNIFICATION, h * MAGNIFICATION);
		}
		for (int i = 0; i < h; i++) {
			g.drawLine(0, i * MAGNIFICATION, w * MAGNIFICATION, i * MAGNIFICATION);
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
	
	public void set(GUIObject[] gos) {
		for (GUIObject go : gos) {
			SimpleGUIComponent toAdd = null;
			if (objects.containsKey(go)) {
				toAdd = objects.get(go);
				objects.remove(go);
				toAdd.moveToLocation(go.getLocation().getX(), go.getLocation().getY());
			} else {
				toAdd = new SimpleGUIComponent(MAGNIFICATION, getImage(go.getType()), getTeamColor(go));
				toAdd.setLocation(go.getLocation().getX(), go.getLocation().getY());
			}
			
			if (go.getType().equals(GUIObjectTypes.ANT_FOOD)) {
				GAntFoodObject gafo = (GAntFoodObject)go;
				objects.remove(gafo.getAnt());
				objects.remove(gafo.getFood());
//			} else
//			if (go.getType().equals(GUIObjectTypes.ANT)){
//				GAntFoodObject swp = findInAntFood(go);
//				if (swp != null) {
//					this.objects.put(swp.getFood(), new SimpleGUIComponent(MAGNIFICATION, getImage(GUIObjectTypes.FOOD), getTeamColor(go)));
//					this.objects.remove(swp);
//				}
//			} else
//			if (go.getType().equals(GUIObjectTypes.FOOD)) {
//				GAntFoodObject swp = findInAntFood(go);
//				if (swp != null) {
//					this.objects.put(swp.getAnt(), new SimpleGUIComponent(MAGNIFICATION, getImage(GUIObjectTypes.ANT), getTeamColor(go)));
//					this.objects.remove(swp);
//				}
			}
	
			this.objects.put(go, toAdd);
		}
		
		performRepaint();
	}
	
	private GAntFoodObject findInAntFood(GUIObject object) {
		return objects.keySet().stream()
			.filter(o -> o.getType().equals(GUIObjectTypes.ANT_FOOD))
			.map(o -> (GAntFoodObject)o)
			.filter(af -> af.getAnt().getId() == object.getId() || af.getFood().getId() == object.getId())
			.findAny()
			.orElse(null);
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

	public void remove(GUIObject[] object) {
		boolean changed = false;
		for (GUIObject it : object) {
			if (this.objects.containsKey(it.getId())) {
				this.objects.remove(it.getId());
				changed = true;
			}
		}
		if (changed) {
			performRepaint();
		}
	}
	
	private Image[] getImage(GUIObjectTypes type) {
		Image[] ret = null;
		switch (type) {
		case ANT: ret = IMAGES_ANT;	break;
		case ANT_FOOD: ret = IMAGES_ANT_FOOD; break;
		case FOOD: ret = new Image [] {IMAGES_FOOD}; break;
		case HILL: ret = new Image [] {IMAGES_HILL}; break;
		}
		return ret;
	}
	
	public void reset(InitMenuData data) {
		objects.clear();

		performRepaint();
	}
	
	public boolean contains(GUIObject go) {  
		return objects.containsKey(go.getId());
	}

}
