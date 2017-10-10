package com.ibm.sk.ff.gui.client;

import java.awt.GridLayout;

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
	private JTextField textPositionX = new JTextField();
	private JTextField textPositionY = new JTextField();
	private DataHolder<GAntObject> comboModelAnt = UniversalGuiTester.INSTANCE.ants;
	private DataHolder<GFoodObject> comboModelFood = UniversalGuiTester.INSTANCE.foods;
	private JComboBox<GAntObject> comboBoxAnt = new JComboBox<>(comboModelAnt);
	private JComboBox<GFoodObject> comboBoxFood = new JComboBox<>(comboModelFood);

	public AntFoodView() {
		setLayout(new GridLayout(6, 1, 5, 5));
		
		add(textID);
		add(comboBoxAnt);
		add(comboBoxFood);
		add(textPositionX);
		add(textPositionY);
	}
	
	public void setAntFoodObject(GAntFoodObject antfood) {
		if (antfood != null) {
			textID.setText(antfood.getId() + "");
			Location l = antfood.getLocation();
			textPositionX.setText(l.getX() + "");
			textPositionY.setText(l.getY() + "");
			comboBoxAnt.setSelectedItem(antfood.getAnt());
			comboBoxFood.setSelectedItem(antfood.getFood());
		} else {
			clear();
		}
	}
	
	public void clear() {
		textID.setText("");
		textPositionX.setText("");
		textPositionY.setText("");
	}
	
	public GAntFoodObject createAntFoodObject() {
		GAntFoodObject ret = new GAntFoodObject();
		
		try {
			ret.setId(Long.parseLong(textID.getText()));
			ret.setLocation(Integer.parseInt(textPositionX.getText()), Integer.parseInt(textPositionY.getText()));
			ret.setAnt(comboModelAnt.getElementAt(comboBoxAnt.getSelectedIndex()));
			ret.setFood(comboModelFood.getElementAt(comboBoxFood.getSelectedIndex()));
		} catch (Exception e) {
			e.printStackTrace();
			ret = null;
		}
		
		return ret;
	}

}
