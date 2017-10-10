package com.ibm.sk.ff.gui.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;

public class UniversalGuiTester extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public static UniversalGuiTester INSTANCE = new UniversalGuiTester();
	
	private JTabbedPane tabbedPane;
	private GUIFacade facade;
	
	public DataHolder<GAntObject> ants = new DataHolder<>();
	public DataHolder<GFoodObject> foods = new DataHolder<>();
	
	private void init() {
		facade = new GUIFacade();
		
		setLayout(new BorderLayout());
		
		tabbedPane = new JTabbedPane();
		
		tabbedPane.addTab("Creation", new CreationPanel(facade));
		tabbedPane.addTab("Ants", new AntsPanel(facade));
		tabbedPane.addTab("Foods", new FoodsPanel(facade));
		tabbedPane.addTab("Ant-Food", new AntFoodsPanel(facade));
		tabbedPane.addTab("Hills", new HillsPanel(facade));
		
		add(tabbedPane, BorderLayout.CENTER);
		
		pack();
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String [] args) {
		INSTANCE.init();
	}

}
