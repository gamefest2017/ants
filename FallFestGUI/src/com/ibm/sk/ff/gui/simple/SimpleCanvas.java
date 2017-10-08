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
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private final int MAGNIFICATION;

	private final Map<GUIObject, SimpleGUIComponent> objects = new HashMap<>();

	private final int w, h;

	private final String [] teams;

	public SimpleCanvas(final int width, final int height, final int magnification, final String[] teams) {
		this.MAGNIFICATION = magnification;
		this.w = width;
		this.h = height;
		setSize(width * magnification, height * magnification);
		setPreferredSize(new Dimension(width * magnification, height * magnification));
		this.teams = teams;
	}

	@Override
	public void paint(final Graphics g) {
		try {
			g.drawImage(IMAGES_GRASS, 0, 0, this.w * this.MAGNIFICATION, this.h * this.MAGNIFICATION, Color.GREEN, null);
			if (this.DEV_MODE) {
				paintGrid(g);
			}
		} catch (final Exception e) {
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		this.finishedRedraw = true;

		for (final SimpleGUIComponent it : this.objects.values().stream().toArray(SimpleGUIComponent[]::new)) {
			if (!it.paint(g)) {
				this.finishedRedraw = false;
			}
		}
	}

	private void paintGrid(final Graphics g) {
		g.setColor(Color.GRAY);
		for (int i = 0; i < this.w; i++) {
			g.drawLine(i * this.MAGNIFICATION, 0, i * this.MAGNIFICATION, this.h * this.MAGNIFICATION);
		}
		for (int i = 0; i < this.h; i++) {
			g.drawLine(0, i * this.MAGNIFICATION, this.w * this.MAGNIFICATION, i * this.MAGNIFICATION);
		}
	}

	private void performRepaint() {
		do {
			paintImmediately(0, 0, getWidth(), getHeight());
			try {
				Thread.sleep(SLEEP_INTERVAL);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		} while (!this.finishedRedraw);
	}

	public void set(final GUIObject[] gos) {
		for (final GUIObject go : gos) {
			final GUIObject key = go;
			SimpleGUIComponent toAdd = null;
			if (this.objects.containsKey(go)) {
				toAdd = this.objects.get(go);
				this.objects.remove(go);
				toAdd.moveToLocation(go.getLocation().getX(), go.getLocation().getY());
			} else {
				if (GUIObjectTypes.ANT_FOOD.equals(go.getType())) {
					final GAntFoodObject gafo = (GAntFoodObject)go;
					toAdd = this.objects.get(gafo.getAnt());
					this.objects.remove(gafo.getAnt());
					toAdd.moveToLocation(gafo.getAnt().getLocation().getX(), gafo.getAnt().getLocation().getY());
					this.objects.put(gafo.getAnt(), toAdd);
					performRepaint();
				}
				//				else if (GUIObjectTypes.ANT.equals(go.getType())) {
				//					GAntObject gao = (GAntObject)go;
				//					toAdd = objects.get(gao);
				//					objects.remove(gao);
				//					toAdd.moveToLocation(gao.getLocation().getX(), gao.getLocation().getY());
				//					this.objects.put(gao, toAdd);
				//					performRepaint();
				//				}
				toAdd = new SimpleGUIComponent(this.MAGNIFICATION, getImage(go.getType()), getTeamColor(go));
				toAdd.setLocation(go.getLocation().getX(), go.getLocation().getY());
			}

			if (go.getType().equals(GUIObjectTypes.ANT_FOOD)) {
				final GAntFoodObject gafo = (GAntFoodObject)go;
				this.objects.remove(gafo.getAnt());
				this.objects.remove(gafo.getFood());
			} else
				if (go.getType().equals(GUIObjectTypes.ANT)){
					final GAntFoodObject swp = findInAntFood(go);
					if (swp != null) {
						this.objects.remove(swp);
						//					toAdd = this.objects.remove(swp);
						//					toAdd.moveToLocation(go.getLocation().getX(), go.getLocation().getY());
						//					key = swp;
					}
					//			} else
					//			if (go.getType().equals(GUIObjectTypes.FOOD)) {
					//				GAntFoodObject swp = findInAntFood(go);
					//				if (swp != null) {
					//					this.objects.put(swp.getAnt(), new SimpleGUIComponent(MAGNIFICATION, getImage(GUIObjectTypes.ANT), getTeamColor(go)));
					//					this.objects.remove(swp);
					//				}
				}

			this.objects.put(key, toAdd);
		}

		performRepaint();
	}

	private GAntFoodObject findInAntFood(final GUIObject object) {
		return this.objects.keySet().stream()
				.filter(o -> o.getType().equals(GUIObjectTypes.ANT_FOOD))
				.map(o -> (GAntFoodObject)o)
				.filter(af -> af.getAnt().getId() == object.getId() || af.getFood().getId() == object.getId())
				.findAny()
				.orElse(null);
	}

	private Color getTeamColor(final GUIObject go) {
		Color ret = null;
		if (go instanceof GAntObject) {
			final GAntObject swp = (GAntObject)go;
			for (int i = 0; i < this.teams.length; i++) {
				if (this.teams[i].equals(swp.getTeam())) {
					ret = this.COLORS[i];
				}
			}
		}
		if (go instanceof GHillObject) {
			final GHillObject swp = (GHillObject)go;
			for (int i = 0; i < this.teams.length; i++) {
				if (this.teams[i].equals(swp.getTeam())) {
					ret = this.COLORS[i];
				}
			}
		}
		if (go instanceof GAntFoodObject) {
			final GAntFoodObject swp = (GAntFoodObject)go;
			for (int i = 0; i < this.teams.length; i++) {
				if (swp.getAnt() != null && this.teams[i].equals(swp.getAnt().getTeam())) {
					ret = this.COLORS[i];
				}
			}
		}
		return ret;
	}

	public void remove(final GUIObject[] object) {
		boolean changed = false;
		for (final GUIObject it : object) {
			if (this.objects.containsKey(it.getId())) {
				this.objects.remove(it.getId());
				changed = true;
			}
		}
		if (changed) {
			performRepaint();
		}
	}

	private Image[] getImage(final GUIObjectTypes type) {
		Image[] ret = null;
		switch (type) {
		case ANT: ret = IMAGES_ANT;	break;
		case ANT_FOOD: ret = IMAGES_ANT_FOOD; break;
		case FOOD: ret = new Image [] {IMAGES_FOOD}; break;
		case HILL: ret = new Image [] {IMAGES_HILL}; break;
		}
		return ret;
	}

	public void reset(final InitMenuData data) {
		this.objects.clear();

		performRepaint();
	}

	public boolean contains(final GUIObject go) {
		return this.objects.containsKey(go.getId());
	}

}
