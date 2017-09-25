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
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectTypes;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;

public class SimpleCanvas extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private static Image IMAGES_GRASS = null;
	private static Image IMAGES_ANT = null;
	private static Image IMAGES_FOOD = null;
	private static Image IMAGES_HILL = null;
	private static Image IMAGES_ANT_FOOD = null;
	
	private static final Color BACKGROUND_COLOR = Color.GREEN;
	
	static {
		try {
			IMAGES_GRASS = ImageIO.read(new File("res/grass2.jpg"));
			IMAGES_ANT = ImageIO.read(new File("res/ant2.png"));
			IMAGES_FOOD = ImageIO.read(new File("res/food.png"));
			IMAGES_HILL = ImageIO.read(new File("res/hill.png"));
			IMAGES_ANT_FOOD = ImageIO.read(new File("res/antWithCookie.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final int MAGNIFICATION;
	
	private Map<Long, SimpleGUIComponent> objects = new HashMap<>();
	
	private int w, h;

	public SimpleCanvas(int width, int height, int magnification) {
		this.MAGNIFICATION = magnification;
		this.w = width;
		this.h = height;
		setSize(width * magnification, height * magnification);
		setPreferredSize(new Dimension(width * magnification, height * magnification));
	}
	
	public void paintComponent(Graphics g) {
		try {
			g.drawImage(IMAGES_GRASS, 0, 0, w * MAGNIFICATION, h * MAGNIFICATION, Color.GREEN, null);
		} catch (Exception e) {
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		objects.values().stream().forEach(o -> o.paint(g));
	}
	
	public void set(GUIObject go) {
		SimpleGUIComponent toAdd = null;
		if (objects.containsKey(go.getId())) {
			toAdd = objects.get(go.getId());
			objects.remove(go.getId());
			toAdd.setLocation(go.getLocation().getX(), go.getLocation().getY());
		} else {
			toAdd = new SimpleGUIComponent(MAGNIFICATION, Color.BLACK, getImage(go.getType()));
			toAdd.moveToLocation(go.getLocation().getX(), go.getLocation().getY());
		}
		
		if (go.getType().equals(GUIObjectTypes.ANT_FOOD)) {
			GAntFoodObject gafo = (GAntFoodObject)go;
			objects.remove(gafo.getAnt().getId());
			objects.remove(gafo.getFood().getId());
		}

		this.objects.put(go.getId(), toAdd);
		
		repaint();
	}

	public void remove(GUIObject object) {
		if (this.objects.containsKey(object.getId())) {
			this.objects.remove(object.getId());
			repaint();
		}
	}
	
	private Image getImage(GUIObjectTypes type) {
		Image ret = null;
		switch (type) {
		case ANT: ret = IMAGES_ANT;	break;
		case ANT_FOOD: ret = IMAGES_ANT_FOOD; break;
		case FOOD: ret = IMAGES_FOOD; break;
		case HILL: ret = IMAGES_HILL; break;
		}
		return ret;
	}
	
	public void reset(InitMenuData data) {
		objects.clear();

		repaint();
	}

}
