package com.ibm.sk.engine.matchmaking;

import java.util.List;
import java.util.Optional;

import com.ibm.sk.dto.matchmaking.Match;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.PlayerStatus;

public interface ITournament {
	/**
	 * Peek at the next match in line to be resolved.
	 * @return Next Match to be resolved.
	 */
	public Optional<Match> getNextMatch();

	/**
	 * Play the next match if applicable.
	 * @return Resulting match after execution.
	 * @throws NoMoreMatchesException In case all matches are resolved and no more matches can be executed.
	 */
	public Match resolveNextMatch() throws NoMoreMatchesException;

	/**
	 * Returns the tournament winner if there is one.
	 * @return Reference to Player that won the tournament.
	 */
	public Optional<Player> getWinner();

	/**
	 * Return a list of current player score in descending order.
	 * @return List of PlayerStatus objects sorted by PlayerStatus.score descending
	 */
	public List<PlayerStatus> getRanking();

	/**
	 * TODO already resolved and remaining matches - input for visualization
	 * @return
	 */
	public List<Match> getMatchStatus();

	/**
	 * Resolves all remaining matches until winner is found.
	 * @return Reference to current tournament.
	 */
	public ITournament fastForward();
}