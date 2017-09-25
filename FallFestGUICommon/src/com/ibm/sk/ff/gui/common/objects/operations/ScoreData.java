package com.ibm.sk.ff.gui.common.objects.operations;

public class ScoreData {
	
	private String team;
	
	private int score;
	
	
	public void setMessage(String message) {
		this.team = message;
	}
	
	public String getMessage() {
		return team;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}

}
