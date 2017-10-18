package com.ibm.sk.ff.gui.simple;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.sk.WorldConstans;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;

public class ScoreboardSmall extends JPanel {

	private static final long serialVersionUID = 8475229618454645422L;
	private static final String SCORE_FONT = "Verdana";
	private static final int SCORE_FONT_SIZE = 14;
	private static final List<Color> colors = Arrays.asList(Color.BLACK, Color.RED);
	private static final String TURN_LABEL = "Turn";
	private JLabel[] labels;
	private JTextField[] texts;
	private JLabel turnsLabel;
	private JTextField turnsText;

	public ScoreboardSmall(final CreateGameData cgd) {
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(250, 30));

		if (cgd.getTeams() != null) {
			createTurnsArea();
			this.labels = new JLabel[cgd.getTeams().length];
			this.texts = new JTextField[cgd.getTeams().length];

			int i = 0;
			for (final String it : cgd.getTeams()) {
				this.labels[i] = new JLabel(it);
				this.labels[i].setFont(new Font(SCORE_FONT, Font.BOLD, SCORE_FONT_SIZE));
				this.labels[i].setBorder(BorderFactory.createLineBorder(colors.get(i % colors.size()), 1));
				this.texts[i] = new JTextField();
				this.texts[i].setColumns(5);
				this.texts[i].setEditable(false);
				add(this.labels[i]);
				add(this.texts[i]);
				i++;
			}
		}
	}

	public void setScore(final ScoreData sd) {
		this.turnsText.setText(sd.getCurrentTurn() + " / " + WorldConstans.TURNS);
		for (int i = 0; i < this.labels.length; i++) {
			if (this.labels[i].getText().equals(sd.getMessage())) {
				this.texts[i].setText(sd.getScore() + "");
			}
		}
	}

	private void createTurnsArea() {
		this.turnsLabel = new JLabel(TURN_LABEL);
		this.turnsText = new JTextField(5);
		this.turnsText.setEditable(false);
		add(this.turnsLabel);
		add(this.turnsText);
	}

}
