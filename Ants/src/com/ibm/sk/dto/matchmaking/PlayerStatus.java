package com.ibm.sk.dto.matchmaking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@Data
public class PlayerStatus implements Comparable<PlayerStatus>{
	private final Player player;
	@Setter(AccessLevel.NONE) private Integer score = 0;
	
	public void addScore(@NonNull Integer score) {
		this.score += score;
	}
	
	@Override
	public int compareTo(PlayerStatus o) {
		return Integer.compare(o.score, this.score);
	}
	
}
