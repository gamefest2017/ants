package com.ibm.sk.dto.matchmaking.strategy;

import java.util.List;
import java.util.Optional;

import com.ibm.sk.dto.matchmaking.Match;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.PlayerStatus;

/**
 * @author Vladimir Martinka (vladimir.martinka@gmail.com)
 * @see <a href="http://denegames.ca/tournaments/index.html">http://denegames.ca/tournaments/index.html</a> 
 */
public interface MatchmakingStrategy {
	public List<Match> initialize(List<Player> players);
	public List<Match> update(Match finishedMatch);
	public Optional<Player> getWinner();
	public List<PlayerStatus> getRanking();
}
