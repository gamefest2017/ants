package com.ibm.sk.dto.matchmaking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.ibm.sk.dto.matchmaking.strategy.MatchmakingStrategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

@Data
public class Tournament {
	private final MatchmakingStrategy strategy;
	private final List<Player> players;
	@Setter(AccessLevel.NONE) private List<Match> matches = new ArrayList<>();
	
	public void initialize() {
		matches.addAll(getStrategy().initialize(getPlayers()));
	}
	
	public void runNextMatch() {
		Match match = getNextMatch().orElse(null);//TODO erh
		
		match.startMatch();
		for (PlayerStatus player : match.getPlayers()) {
			player.addScore(new Random().nextInt(100));
		}
		match.endMatch();
		
		matches.addAll(getStrategy().update(match));
	}
	
	public Optional<Match> getNextMatch() {
		return matches.stream().filter(m -> !m.isFinished()).findFirst();
	}
}
