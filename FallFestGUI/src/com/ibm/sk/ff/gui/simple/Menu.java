package com.ibm.sk.ff.gui.simple;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;

public class Menu extends JPanel {

	private static final long serialVersionUID = 6974035742626961138L;
	
	private Image backgroundImage = null;
	
	private ComboBoxModel<String> replays_model = null;
	private JComboBox<String> combo_replays = null;
	
	private ComboBoxModel<String> implementations = null;
	private JComboBox<String> combo_implementations1 = null;
	private ComboBoxModel<String> implementations2 = null;
	private JComboBox<String> combo_implementations2 = null;
	
	private JButton button_singlePlayer = null;
	private JButton button_twoPlayer = null;
	private JButton button_replay = null;
	
	private GuiEventListener listener;
	
	public Menu(InitMenuData data, GuiEventListener listener) {
		this.listener = listener;
		try {
			backgroundImage = ImageIO.read(new File("res/grass2.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		replays_model = new DefaultComboBoxModel<>(data.getReplays());
		combo_replays = new JComboBox<>(replays_model);
		
		implementations = new DefaultComboBoxModel<>(data.getCompetitors());
		combo_implementations1 = new JComboBox<>(implementations);
		implementations2 = new DefaultComboBoxModel<>(data.getCompetitors());
		combo_implementations2 = new JComboBox<>(implementations2);
		
		button_singlePlayer = new JButton("Single player");
		button_twoPlayer = new JButton("Two players");
		button_replay = new JButton("Replay");
		
		setLayout(null);
		
		setLocation(0, 0);
		setSize(800, 600);
		setPreferredSize(new Dimension(800, 600));
		
		int w = 280;
		int h = 30;
		int x = (getWidth() / 2) - (w / 2);
		int y = 300;
		
		combo_replays.setSize(w, h);
		combo_implementations1.setSize(w/2, h);
		combo_implementations2.setSize(w/2, h);
		
		button_singlePlayer.setSize(w, h);
		button_twoPlayer.setSize(w, h);
		button_replay.setSize(w, h);
		
		/////////////////////////////////////////////
		
		/////////////////////////////////////////////
		button_singlePlayer.setLocation(x, y);
		button_twoPlayer.setLocation(x, y + h);
		combo_implementations1.setLocation(x, y + (2 * h));
		combo_implementations2.setLocation(x + (w / 2), y + (2 * h));
		button_replay.setLocation(x, y + (3 * h));
		combo_replays.setLocation(x, y + (4 * h));
		
		add(button_singlePlayer);
		add(button_twoPlayer);
		add(combo_implementations1);
		add(combo_implementations2);
		add(button_replay);
		add(combo_replays);
		
		setupEvents();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
	}
	
	private void setupEvents() {
		//TODO
		button_singlePlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.SINGLE_PLAY_START, ""));
			}
		});
		button_twoPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.DOUBLE_PLAY_START, ""));
			}
			
		});
		button_replay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.REPLAY_SELECTED, ""));
			}
		});
		combo_replays.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.REPLAY_SELECTED, combo_replays.getSelectedItem().toString()));
			}
		});
		combo_implementations1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.PLAYER_1_SELECTED, combo_implementations1.getSelectedItem().toString()));
			}
		});
		combo_implementations2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new GuiEvent(GuiEvent.EventTypes.PLAYER_2_SELECTED, combo_implementations2.getSelectedItem().toString()));
			}
		});
	}
	
}
