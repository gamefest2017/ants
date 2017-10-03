package com.ibm.sk.ff.gui.simple;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;

public class ScoreboardSmall extends JPanel {

	private static final long serialVersionUID = 8475229618454645422L;

	private JLabel[] labels;
	private JTextField[] texts;

	public ScoreboardSmall(final CreateGameData cgd) {
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(100, 30));

		if (cgd.getTeams() != null) {
			this.labels = new JLabel[cgd.getTeams().length];
			this.texts = new JTextField[cgd.getTeams().length];

			int i = 0;
			for (final String it : cgd.getTeams()) {
				this.labels[i] = new JLabel(it);
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
		for (int i = 0; i < this.labels.length; i++) {
			if (this.labels[i].getText().equals(sd.getMessage())) {
				this.texts[i].setText(sd.getScore() + "");
			}
		}
	}

}
