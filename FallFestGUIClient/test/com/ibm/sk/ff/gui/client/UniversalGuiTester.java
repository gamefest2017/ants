package com.ibm.sk.ff.gui.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class UniversalGuiTester extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
	private GUIFacade facade;
	
	public UniversalGuiTester() {
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
		new UniversalGuiTester();
	}

}
