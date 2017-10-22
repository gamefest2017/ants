package com.ibm.sk.dto.tournament;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.PlayerStatus;

public class TournamentTable {

	public Map<Integer, List<TournamentMatch>> matches = new HashMap<>();

	@JsonIgnore
	public void addMatch(int round, TournamentMatch match) {
		if (!matches.containsKey(round)) {
			matches.put(round, new ArrayList<>());
		}
		getMatches(round).add(match);
	}
	
	@JsonIgnore
	public void addMatch(int round, Player player1, Integer score1, Player player2, Integer score2) {
		// Convert intput parameters
		PlayerStatus playerStatus1 = new PlayerStatus(player1, score1);
		PlayerStatus playerStatus2 = new PlayerStatus(player2, score2);
		boolean finished = player1 != null && player2 != null && score1 != null && score2 != null;
		final Player winner;
		if (finished) {
			winner = score1 >= score2 ? player1 : player2;
		} else {
			winner = null;
		}
		TournamentMatch match = new TournamentMatch(finished, Arrays.asList(player1, player2), winner, Arrays.asList(playerStatus1, playerStatus2));
		
		if (!matches.containsKey(round)) {
			matches.put(round, new ArrayList<>());
		}
		getMatches(round).add(match);
	}

	@JsonIgnore
	public List<TournamentMatch> getMatches(int round) {
		return round >= matches.size() ? null : matches.get(round);
	}
	
	@JsonIgnore
	public int getRounds() {
		return matches.keySet().size();
	}
}
