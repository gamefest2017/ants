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
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectTypes;
import com.ibm.sk.ff.gui.common.objects.gui.GWarriorObject;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.ff.gui.config.Config;

public class SimpleCanvas extends JComponent {
	private static final long serialVersionUID = 1L;


	private final Color[] COLORS = {Color.BLACK, Color.RED};

	private final boolean DEV_MODE = Boolean.parseBoolean(Config.DEV_MODE.toString());
	
	private static long MIN_RETURN_INTERVAL = Long.parseLong(Config.GUI_MIN_RETURN_INTERVAL.toString());

	private static Image[] IMAGES_ANT = null;
	private static Image[] IMAGES_WARRIOR = null;
	private static Image IMAGES_FOOD = null;
	private static Image IMAGES_HILL = null;
	private static Image[] IMAGES_ANT_FOOD = null;
	private static Image IMAGES_BORDER = null;

	private boolean finishedRedraw = true;
	
	private boolean fastForward = false;

	static {
		try {
			IMAGES_ANT = new Image[] {ImageIO.read(new File("res/ant_left.png")), ImageIO.read(new File("res/ant_right.png"))};
			IMAGES_WARRIOR = new Image[] { ImageIO.read(new File("res/warrior_left_2.png")), ImageIO.read(new File("res/warrior_right_2.png")) };
			IMAGES_FOOD = ImageIO.read(new File("res/food.png"));
			IMAGES_BORDER = ImageIO.read(new File("res/water.jpg"));
			IMAGES_HILL = ImageIO.read(new File("res/hill.png"));
			IMAGES_ANT_FOOD = new Image [] {ImageIO.read(new File("res/antWithCookie_left.png")), ImageIO.read(new File("res/antWithCookie_right.png"))};
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private final Map<GUIObject, SimpleGUIComponent> OBJECTS = new HashMap<>();

	private final int WIDTH, HEIGHT;

	private final String [] teams;

	private final JFrame parentFrame;

	public SimpleCanvas(final int width, final int height, final String[] teams, final JFrame parent) {
		this.WIDTH = width;
		this.HEIGHT = height;

		this.teams = teams;
		this.parentFrame = parent;

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(final KeyEvent e) {
				System.out.println("asdfasdf");
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_SPACE) {
					SimpleCanvas.this.parentFrame.setVisible(false);
					SimpleCanvas.this.parentFrame.dispose();
				}
			}
			@Override
			public void keyPressed(final KeyEvent e) {}
			@Override
			public void keyReleased(final KeyEvent e) {}
		});
	}

	@Override
	public void paintComponent(final Graphics g) {
		if (this.DEV_MODE) {
			paintGrid(g);
		}

		this.finishedRedraw = true;

		for (final SimpleGUIComponent it : this.OBJECTS.values().stream().toArray(SimpleGUIComponent[]::new)) {
			if (!it.paint(g)) {
				this.finishedRedraw = false;
			}
		}
	}

	private void paintGrid(final Graphics g) {
		g.setColor(Color.GRAY);
		for (int i = 0; i < this.WIDTH; i++) {
			g.drawLine(i * MAGNIFICATION_X(), 0, i * MAGNIFICATION_X(), this.HEIGHT * MAGNIFICATION_Y());
		}
		for (int i = 0; i < this.HEIGHT; i++) {
			g.drawLine(0, i * MAGNIFICATION_Y(), this.WIDTH * MAGNIFICATION_X(), i * MAGNIFICATION_Y());
		}
	}

	private void performRepaint() {
		do {
			paintImmediately(0, 0, getWidth(), getHeight());
		} while (!finishedRedraw);
	}

	public void set(final GUIObject[] objects) {
		for (final GUIObject it : objects) {
			SimpleGUIComponent toAdd = null;
			if (this.OBJECTS.containsKey(it)) {
				toAdd = this.OBJECTS.remove(it);
				if (fastForward) {
					toAdd.setLocation(it.getLocation().getX(), it.getLocation().getY());
				} else {
					toAdd.moveToLocation(it.getLocation().getX(), it.getLocation().getY());
				}
			} else {
				toAdd = new SimpleGUIComponent(MAGNIFICATION_X(), MAGNIFICATION_Y(), getImage(it.getType()), null, getTeamColor(it));
				toAdd.setLocation(it.getLocation().getX(), it.getLocation().getY());
			}
			this.OBJECTS.put(it, toAdd);
		}

		long before = System.currentTimeMillis(), after;
		
		performRepaint();
		
		after = System.currentTimeMillis();
		if (after - before < MIN_RETURN_INTERVAL) {
			try {Thread.sleep(MIN_RETURN_INTERVAL - (after - before));} catch (Exception e) {}
		}
	}

	private Image[] getImage(final GUIObjectTypes type) {
		Image [] ret = null;
		switch (type) {
		case ANT: ret = IMAGES_ANT; break;
		case WARRIOR:
			ret = IMAGES_WARRIOR;
			break;
		case ANT_FOOD: ret = IMAGES_ANT_FOOD; break;
		case FOOD: ret = new Image[] {IMAGES_FOOD}; break;
		case BORDER:
			ret = new Image[] { IMAGES_BORDER };
			break;
		case HILL: ret = new Image[] {IMAGES_HILL};	break;
		}
		return ret;
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
		if (go instanceof GWarriorObject) {
			final GWarriorObject swp = (GWarriorObject) go;
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

	public void remove(final GUIObject object) {
		remove (new GUIObject[] {object});
	}

	public void remove(final GUIObject[] object) {
		boolean changed = false;
		for (final GUIObject it : object) {
			if (this.OBJECTS.containsKey(it)) {
				this.OBJECTS.remove(it);
				changed = true;
			}
		}
		if (changed) {
			performRepaint();
		}
	}

	public void reset(final InitMenuData data) {
		this.OBJECTS.clear();

		performRepaint();
	}

	public boolean contains(final GUIObject go) {
		return this.OBJECTS.containsKey(go.getId());
	}
	
	public void setFastForward(boolean fastForward) {
		this.fastForward = fastForward;
	}

	private int magnificationX = -1;
	private int MAGNIFICATION_X() {
		if (this.magnificationX == -1) {
			this.magnificationX = getWidth() / this.WIDTH;
		}
		return this.magnificationX;
	}

	private int magnificationY = -1;
	private int MAGNIFICATION_Y() {
		if (this.magnificationY == -1) {
			this.magnificationY = getHeight() / this.HEIGHT;
		}
		return this.magnificationY;
	}

}
