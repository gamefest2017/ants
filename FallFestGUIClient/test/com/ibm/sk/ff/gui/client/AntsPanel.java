package com.ibm.sk.ff.gui.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;

public class AntsPanel extends JPanel {
	
	private static final long serialVersionUID = -8780098469519172008L;
	
	private DataHolder<GAntObject> listModel = UniversalGuiTester.INSTANCE.ants;
	private JList<GAntObject> list = new JList<GAntObject>(listModel);
	
	private AntView antView = new AntView();
	
	private JPanel panel_buttons = new JPanel();
	private JButton button_set = new JButton("Set");
	private JButton button_remove = new JButton("Remove");
	
	private GUIFacade facade;
	
	public AntsPanel(GUIFacade facade) {
		this.facade = facade;
		
		setLayout(new BorderLayout());
		
		setEvents();
		
		panel_buttons.setLayout(new FlowLayout());
		panel_buttons.add(button_set);
		panel_buttons.add(button_remove);
		
		list.setPreferredSize(new Dimension(70, 200));
		
		add(list, BorderLayout.WEST);
		add(antView, BorderLayout.CENTER);
		add(panel_buttons, BorderLayout.SOUTH);
	}
	
	private void setEvents() {
		button_set.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GAntObject gad = antView.createAntObject();
				if (listModel.contains(gad)) {
					listModel.set(listModel.indexOf(gad), gad);
				} else {
					listModel.addElement(gad);
				}
				facade.set(gad);
			}
		});
		
		button_remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GAntObject gad = listModel.getElementAt(list.getSelectedIndex());
				listModel.removeElement(gad);
				facade.remove(gad);
				antView.clear();
			}
		});
		
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selected = list.getSelectedIndex();
				if (selected > 0 && selected < listModel.size()) {
					antView.setAntObject(listModel.getElementAt(list.getSelectedIndex()));
				}
			}
		});
	}

}
