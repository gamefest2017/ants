package com.ibm.sk.ff.gui.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.objects.operations.CloseData;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;

public class CreationPanel extends JPanel implements GuiEventListener {

	private static final long serialVersionUID = 2196289264725789277L;

	private GUIFacade facade;

	private JButton create = new JButton("Create");
	private JButton close = new JButton("Close");
	private JButton init = new JButton("Init");

	private JTextField sizex = new JTextField("20");
	private JTextField sizey = new JTextField("15");

	private JTextArea textEvents = new JTextArea("Events:");
	private JScrollPane scrollEvents = new JScrollPane(textEvents);

	public CreationPanel(GUIFacade facade) {
		this.facade = facade;

		JPanel north = new JPanel();
		north.setLayout(new FlowLayout());

		setEvents();

		sizex.setPreferredSize(new Dimension(40, 25));
		sizey.setPreferredSize(new Dimension(40, 25));

		north.add(sizex);
		north.add(sizey);

		north.add(create);
		north.add(close);
		north.add(init);

		setLayout(new BorderLayout());
		add(north, BorderLayout.NORTH);
		add(scrollEvents, BorderLayout.CENTER);

		facade.addGuiEventListener(this);
	}

	private void setEvents() {
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CreateGameData data = new CreateGameData();
				data.setWidth(Integer.parseInt(sizex.getText()));
				data.setHeight(Integer.parseInt(sizey.getText()));
				data.setTeams(new String[] { "Team1", "Team2" });
				facade.createGame(data);
			}
		});

		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CloseData data = new CloseData();
				facade.close(data);
			}
		});

		init.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InitMenuData imd = new InitMenuData();

				imd.setCompetitors(new String[] { "Competitor1", "Competitor2", "Competitor3", "Competitor4" });
				imd.setReplay(new String[] { "Replay1", "Replay2", "Replay3", "Replay4" });

				facade.showInitMenu(imd);
			}
		});
	}

	@Override
	public void actionPerformed(GuiEvent event) {
		textEvents.setText(textEvents.getText() + "\n" + event.getType().toString() + " - " + event.getData());
	}

}
