package com.ibm.sk.ff.gui.common.objects.operations;

public class ScoreData {

	private String team;
	private int score;
	private int turn;

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

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getTurn() {
		return turn;
	}

}
