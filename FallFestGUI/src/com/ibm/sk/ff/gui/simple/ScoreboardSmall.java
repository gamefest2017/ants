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
	
	public ScoreboardSmall(CreateGameData cgd) {
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(100, 30));
		
		if (cgd.getTeams() != null) {
			labels = new JLabel[cgd.getTeams().length];
			texts = new JTextField[cgd.getTeams().length];
			
			int i = 0;
			for (String it : cgd.getTeams()) {
				labels[i] = new JLabel(it);
				texts[i] = new JTextField();
				texts[i].setColumns(5);
				texts[i].setEditable(false);
				add(labels[i]);
				add(texts[i]);
			}
		}
	}
	
	public void setScore(ScoreData sd) {
		for (int i = 0; i < labels.length; i++) {
			if (labels[i].getText().equals(sd.getMessage())) {
				texts[i].setText(sd.getScore() + "");
			}
		}
	}

}
