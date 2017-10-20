package com.ibm.sk.ant.singlejar;

public class Main {
	
	private Main() {
	}
	
	public static void main(String [] args) {
		new Main().run();
	}
	
	private void run() {
		new Thread(new GameSimulator()).start();
	}

}
