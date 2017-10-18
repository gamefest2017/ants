package com.ibm.sk.dto.tournament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.sk.dto.matchmaking.Match;

public class TournamentTable {

	Map<Integer, List<Match>> matches = new HashMap<>();
	
	public void addMatch(int round, Match match) {
		if (!matches.containsKey(round)) {
			matches.put(round, new ArrayList<>());
		}
		getMatches(round).add(match);
	}
	
	public List<Match> getMatches(int round) {
		return round >= matches.size() ? null : matches.get(round);
	}
	
	@JsonIgnore
	public int getRounds() {
		return matches.keySet().size();
	}
}
