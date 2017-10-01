package com.ibm.sk.dto.matchmaking.comparator;

import java.util.Comparator;

import com.ibm.sk.dto.matchmaking.PlayerStatus;

public class PlayerScoreComparator implements Comparator<PlayerStatus>{

	@Override
	public int compare(PlayerStatus o1, PlayerStatus o2) {
		return o1.getScore().compareTo(o2.getScore());
	}
	
}
