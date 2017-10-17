package com.ibm.sk.ff.gui.common.objects.operations;

public class ScoreData {

	private String team;
	private int score;
	private int turnsRemaining;

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

	public void setTurnsRemaining(int turnsRemaining) {
		this.turnsRemaining = turnsRemaining;
	}

	public int getTurnsRemaining() {
		return turnsRemaining;
	}

}
