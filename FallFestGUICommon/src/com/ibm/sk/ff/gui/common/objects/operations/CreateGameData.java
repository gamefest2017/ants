package com.ibm.sk.ff.gui.common.objects.operations;

public class CreateGameData {
	
	private int width;
	private int height;
	
	private String[] teams;
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setTeams(String[] teams) {
		this.teams = teams;
	}
	
	public String[] getTeams() {
		return teams;
	}

}
