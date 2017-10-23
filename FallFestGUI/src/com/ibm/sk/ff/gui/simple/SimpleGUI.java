package com.ibm.sk.ff.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.ibm.sk.ff.gui.GUI;
import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEvent.EventTypes;
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
	private JCheckBox fastForward = null;

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
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						frame.dispose();
					}
				});
			} catch (InvocationTargetException | InterruptedException e1) {
				e1.printStackTrace();
			}
			this.frame = null;
		}

		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JLabel label = new JLabel(new ImageIcon(loadBackgroundImage()));
		this.frame.setContentPane(label);

		this.canvas = new SimpleCanvas(data.getWidth(), data.getHeight(), data.getTeams(), this.frame);

		final JPanel panelNorth = new JPanel();
		panelNorth.setLayout(new FlowLayout());
		this.scoreboard = new ScoreboardSmall(data);
		this.scoreboard.setPreferredSize(new Dimension(600, 25));
		this.fastForward = new JCheckBox("Fast forward");
		this.fastForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				SimpleGUI.this.canvas.setFastForward(SimpleGUI.this.fastForward.isSelected());
			}
		});
		panelNorth.add(this.scoreboard);
		panelNorth.add(this.fastForward);

		this.frame.setLayout(new BorderLayout());
		this.frame.add(this.canvas, BorderLayout.CENTER);
		this.frame.add(panelNorth, BorderLayout.NORTH);

		this.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.frame.setUndecorated(true);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);

		try {
			Thread.sleep(2000);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private BufferedImage loadBackgroundImage() {
		return loadImage("res/grass2.jpg");
	}

	private BufferedImage loadGameOverImage() {
		return loadImage("res/game-over.jpg");
	}

	private BufferedImage loadImage(final String resource) {
		BufferedImage ret = null;
		try {
			ret = ImageIO.read(new File(resource));
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
		if (this.frame  != null) {
			final ImageIcon img = new ImageIcon(loadGameOverImage());

			final JPanel panel_center = new JPanel() {
				private static final long serialVersionUID = 2005550217855285811L;

				@Override
				protected void paintComponent(final Graphics g) {
					super.paintComponent(g);
					g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), this);
				}

				@Override
				public Dimension getPreferredSize() {
					final Dimension size = super.getPreferredSize();
					size.width = Math.max(img.getIconWidth(), size.width);
					size.height = Math.max(img.getIconHeight(), size.height);

					return size;
				}
			};

			final JDialog dialog = new JDialog(this.frame, data.getWinner() + " is the winner!", true);

			dialog.addWindowListener(new WindowListener() {
				@Override
				public void windowOpened(final WindowEvent e) {
				}
				@Override
				public void windowClosing(final WindowEvent e) {
					SimpleGUI.this.listener.actionPerformed(new GuiEvent(EventTypes.RESULT_CLOSE, ""));
				}
				@Override
				public void windowClosed(final WindowEvent e) {
				}
				@Override
				public void windowIconified(final WindowEvent e) {
				}
				@Override
				public void windowDeiconified(final WindowEvent e) {
				}
				@Override
				public void windowActivated(final WindowEvent e) {
				}
				@Override
				public void windowDeactivated(final WindowEvent e) {
				}
			});

			dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			dialog.setLayout(new BorderLayout());
			dialog.add(panel_center, BorderLayout.CENTER);
			dialog.pack();
			final int w = this.frame.getWidth() / 2 - dialog.getWidth() / 2;
			final int h = this.frame.getHeight() / 2 - dialog.getHeight() / 2;
			dialog.setLocation(this.frame.getLocation().x + w, this.frame.getLocation().y + h);
			dialog.setVisible(true);
		}
	}

	@Override
	public void createMenu(final InitMenuData data) {
		if (this.frame != null) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						frame.dispose();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.canvas = null;
			this.scoreboard = null;
			this.frame = null;
		}

		// Create and set up the window.
		this.frame = new JFrame("IBM Slovakia / Fall Fest 2017 / Anthill");
		this.frame.setExtendedState(this.frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());

		// Set up the content pane.
		this.frame.add(new Menu(data, this.listener, this.frame));

		// Display the window.
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
