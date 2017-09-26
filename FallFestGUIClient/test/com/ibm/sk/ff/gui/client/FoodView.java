package com.ibm.sk.ff.gui.client;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.Location;

public class FoodView extends JPanel {
	private static final long serialVersionUID = 509105871717918619L;
	
	private JTextField textID = new JTextField();
	private JTextField textPositionX = new JTextField();
	private JTextField textPositionY = new JTextField();

	public FoodView() {
		setLayout(new GridLayout(3, 1, 5, 5));
		
		add(textID);
		add(textPositionX);
		add(textPositionY);
	}
	
	public void setFoodObject(GFoodObject ant) {
		if (ant != null) {
			textID.setText(ant.getId() + "");
			Location l = ant.getLocation();
			textPositionX.setText(l.getX() + "");
			textPositionY.setText(l.getY() + "");
		} else {
			clear();
		}
	}
	
	public void clear() {
		textID.setText("");
		textPositionX.setText("");
		textPositionY.setText("");
	}
	
	public GFoodObject createFoodObject() {
		GFoodObject ret = new GFoodObject();
		
		try {
			ret.setId(Long.parseLong(textID.getText()));
			ret.setLocation(Integer.parseInt(textPositionX.getText()), Integer.parseInt(textPositionY.getText()));
		} catch (Exception e) {
			e.printStackTrace();
			ret = null;
		}
		
		return ret;
	}

}
