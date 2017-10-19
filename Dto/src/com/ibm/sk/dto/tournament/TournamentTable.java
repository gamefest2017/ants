package com.ibm.sk.dto.tournament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.sk.dto.matchmaking.Match;

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
	public List<TournamentMatch> getMatches(int round) {
		return round >= matches.size() ? null : matches.get(round);
	}
	
	@JsonIgnore
	public int getRounds() {
		return matches.keySet().size();
	}
}
