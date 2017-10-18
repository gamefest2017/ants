package com.ibm.sk.ff.gui.common.objects.operations;

public class ScoreData {

	private String team;
	private int score;
	private int currentTurn;
	private int turns;

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

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}

	public int getTurns() {
		return this.turns;
	}

}
