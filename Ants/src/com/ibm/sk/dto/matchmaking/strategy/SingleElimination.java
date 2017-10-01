package com.ibm.sk.dto.matchmaking.strategy;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ibm.sk.dto.matchmaking.Match;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.PlayerStatus;
import com.ibm.sk.dto.matchmaking.Tournament;
import com.ibm.sk.dto.matchmaking.comparator.PlayerScoreComparator;
/**
 * @author Vladimir Martinka (vladimir.martinka@gmail.com)
 * @see <a href="http://denegames.ca/tournaments/index.html">http://denegames.ca/tournaments/index.html</a>
 */
public class SingleElimination extends Tournament {
	
	
	private static final int PLAYERS_PER_MATCH = 2;
	private List<Player> eliminatedPlayers = new ArrayList<>();
	
	/**
	 * List of players, ideally sorted by their ranking ascending.
	 * @param players Sorted list of players. Pairs will be made starting from first element.
	 */
	public SingleElimination(List<Player> players) {
		super(players);
		
		/*
		 * Limit first round matches to get power of PLAYERS_PER_MATCH
		 */
		int nextPowerOfTwo = (int) Math.ceil((Math.log(players.size()) / Math.log(PLAYERS_PER_MATCH)));
		int targetPlayers = (int) Math.pow(PLAYERS_PER_MATCH, nextPowerOfTwo - 1 );
		int firstRoundPlayers = (players.size() - targetPlayers) * PLAYERS_PER_MATCH;
		
		getMatches().addAll(createMatches(players.stream().limit(firstRoundPlayers).collect(Collectors.toList())));
	}
	
	@Override
	public Match resolveNextMatch() throws NoMoreMatchesException {
		Match match = super.resolveNextMatch();
		List<Player> losers = new ArrayList<>(match.getPlayers());
		losers.removeAll(match.getWinners());
		eliminatedPlayers.addAll(losers);
		return match;
	}

	@Override
	public Optional<Match> getNextMatch() {
		Optional<Match> nextMatch = getMatches().stream().filter(m -> !m.isFinished()).findFirst();
		if (!nextMatch.isPresent() && !getWinner().isPresent()) {
			//round finished, generate next batch
			List<Player> actualPlayers = new ArrayList<>(getPlayers());
			actualPlayers.removeAll(eliminatedPlayers);
			getMatches().addAll(createMatches(actualPlayers));
			nextMatch = getMatches().stream().filter(m -> !m.isFinished()).findFirst();
		}
		return nextMatch;
	}

	@Override
	public Optional<Player> getWinner() {
		if (getPlayers().size() - eliminatedPlayers.size() == 1) {
			return getPlayers().stream().filter(p -> !eliminatedPlayers.contains(p)).findFirst(); 
		} 
		return Optional.empty();
	}

	@Override
	public List<PlayerStatus> getRanking() {
		List<PlayerStatus> ranking = getPlayers().stream().map(PlayerStatus::new).collect(Collectors.toList());
		
		getMatches().stream().forEach(m -> {
			ranking.stream().filter(ps -> m.getWinners().contains(ps.getPlayer())).findFirst().get().addScore(1);
		});
		
		if (getWinner().isPresent()) {
			ranking.stream().filter(ps -> getWinner().get().equals(ps.getPlayer())).findFirst().get().addScore(1);
		}
		
		ranking.sort(new PlayerScoreComparator().reversed());
		return ranking;
	}

	@Override
	public List<Match> getMatchStatus() {
		return getMatches();
	}
	
	private static List<Match> createMatches(List<Player> players) {
		int counter = 0;
		List<Player> matchPlayers = new ArrayList<>();
		List<Match> matches = new ArrayList<>();
		
		for (Player player : players) {
			matchPlayers.add(player);
			if (++counter == PLAYERS_PER_MATCH) {
				//flush
				counter = 0;
				matches.add(new Match(new ArrayList<>(matchPlayers)));
				matchPlayers.clear();
			}
		}
		
		
		return matches;
	}



}
