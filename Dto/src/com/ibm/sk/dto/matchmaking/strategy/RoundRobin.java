package com.ibm.sk.dto.matchmaking.strategy;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ibm.sk.dto.matchmaking.Match;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.PlayerStatus;

import lombok.Data;

//TODO consider just shoving all this stuff into Tournament class, that one seems pretty redundant now
@Data
public class RoundRobin implements MatchmakingStrategy {
	
	private static final int PLAYERS_PER_MATCH = 2;
	
	private List<Player> players;
	private List<Match> matches;
	
	/**
	 * Pairs up random players until all have match.
	 */
	@Override
	public List<Match> initialize(List<Player> players) {
		//pairs of random players until all have match
		//TODO - refactor, I'm being sloppy here
		matches = new ArrayList<>();
		for (Player player : players) {
			for (Player player2 : players) {
				if (!player.equals(player2)) {
					PlayerStatus ps1 = new PlayerStatus(player);
					PlayerStatus ps2 = new PlayerStatus(player2);
					matches.add(new Match(ps1, ps2)); 
				}
			}
		}
		
		return matches;
	}

	@Override
	public List<Match> update(Match finishedMatch) {
		return Collections.emptyList();
	}

	@Override
	public Optional<Player> getWinner() {
		//TODO guy with highest score wins? or with highest number of victories?
		if (matches.stream().anyMatch(m -> !m.isFinished())) {
			return Optional.empty();
		}
		return Optional.of(getRanking().get(0).getPlayer());
	}

	@Override
	public List<PlayerStatus> getRanking() {
		Map<Player, Integer> playerScore = matches.stream()
		.flatMap(m -> m.getPlayers().stream())
		.collect(groupingBy(PlayerStatus::getPlayer, 
				summingInt(PlayerStatus::getScore)));
		
		List<PlayerStatus> ranking = playerScore.keySet().stream().map(p -> {
			PlayerStatus status = new PlayerStatus(p); 
			status.addScore(playerScore.get(p));
			return status;
		}).collect(Collectors.toList());
		
		Collections.sort(ranking);
		
		return ranking;
	}

}
