package com.ibm.sk.dto.matchmaking;

public class PlayerStatus {
	private Player player;
	private Integer score = 0;
	
	public PlayerStatus() {
		//dummy
	}
	
	public PlayerStatus(Player player) {
		this(player, 0);
	}

	public PlayerStatus(Player player, Integer score) {
		this.player = player;
		this.score = score;
	}
	
	public void addScore(int score) {
		this.score = this.score == null ? score : this.score + score;
	}
	
	public Integer getScore() {
		return this.score;
	}

	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return player.getName() + " : " + getScore();
	}
	
}
