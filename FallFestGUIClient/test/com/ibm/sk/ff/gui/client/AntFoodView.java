package com.ibm.sk.ff.gui.client;

import java.awt.GridLayout;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.sk.ff.gui.common.objects.gui.GAntFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.Location;

public class AntFoodView extends JPanel {
	private static final long serialVersionUID = 509105871717918619L;
	
	private JTextField textID = new JTextField();
	private JTextField textAntId = new JTextField();
	private JTextField textFoodId = new JTextField();
	private JTextField textPositionX = new JTextField();
	private JTextField textPositionY = new JTextField();
	private ComboBoxModel<String> comboModel = new DefaultComboBoxModel<>(new String [] {"Team1", "Team2"});
	private JComboBox<String> comboBox = new JComboBox<>(comboModel);

	public AntFoodView() {
		setLayout(new GridLayout(6, 1, 5, 5));
		
		add(textID);
		add(textAntId);
		add(comboBox);
		add(textFoodId);
		add(textPositionX);
		add(textPositionY);
	}
	
	public void setAntFoodObject(GAntFoodObject ant) {
		if (ant != null) {
			textID.setText(ant.getId() + "");
			Location l = ant.getLocation();
			textPositionX.setText(l.getX() + "");
			textPositionY.setText(l.getY() + "");
			textAntId.setText(ant.getAnt().getId() + "");
			textFoodId.setText(ant.getFood().getId() + "");
		} else {
			clear();
		}
	}
	
	public void clear() {
		textID.setText("");
		textAntId.setText("");
		textFoodId.setText("");
		textPositionX.setText("");
		textPositionY.setText("");
	}
	
	public GAntFoodObject createAntFoodObject() {
		GAntFoodObject ret = new GAntFoodObject();
		
		try {
			ret.setId(Long.parseLong(textID.getText()));
			ret.setLocation(Integer.parseInt(textPositionX.getText()), Integer.parseInt(textPositionY.getText()));
			GAntObject a = new GAntObject();
			a.setId(Long.parseLong(textAntId.getText()));
			a.setTeam(comboBox.getSelectedItem().toString());
			a.setLocation(ret.getLocation());
			GFoodObject f = new GFoodObject();
			f.setId(Long.parseLong(textFoodId.getText()));
			ret.setAnt(a);
			ret.setFood(f);
		} catch (Exception e) {
			e.printStackTrace();
			ret = null;
		}
		
		return ret;
	}

}
