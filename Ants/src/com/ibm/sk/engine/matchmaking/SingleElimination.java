package com.ibm.sk.engine.matchmaking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ibm.sk.dto.matchmaking.Match;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.PlayerStatus;
import com.ibm.sk.dto.matchmaking.comparator.PlayerScoreComparator;
import com.ibm.sk.dto.tournament.TournamentMatch;
/**
 * @author Vladimir Martinka (vladimir.martinka@gmail.com)
 * @see <a href="http://denegames.ca/tournaments/index.html">http://denegames.ca/tournaments/index.html</a>
 */
import com.ibm.sk.dto.tournament.TournamentTable;
import com.ibm.sk.ff.gui.client.GUIFacade;
public class SingleElimination extends Tournament {


	private static final int PLAYERS_PER_MATCH = 2;
	private final List<Player> eliminatedPlayers = new ArrayList<>();
	private final TournamentTable tournamentTable = new TournamentTable();
	private int tournamentRound = 0;

	/**
	 * List of players, ideally sorted by their ranking ascending.
	 * @param players Sorted list of players. Pairs will be made starting from first element.
	 */
	public SingleElimination(final GUIFacade facade, final List<Player> players) {
		super(facade, players);

		/*
		 * Limit first round matches to get power of PLAYERS_PER_MATCH
		 */
		final int nextPowerOfTwo = (int) Math.ceil(Math.log(players.size()) / Math.log(PLAYERS_PER_MATCH));
		int targetPlayers = (int) Math.pow(PLAYERS_PER_MATCH, nextPowerOfTwo);
		int firstRoundPlayers = targetPlayers;
		if (targetPlayers > players.size()) {
			//need to bye some players
			targetPlayers = (int) Math.pow(PLAYERS_PER_MATCH, nextPowerOfTwo - 1);
			firstRoundPlayers = (players.size() - targetPlayers) * PLAYERS_PER_MATCH;
		}

		getMatches().addAll(createMatches(players.stream().limit(firstRoundPlayers).collect(Collectors.toList())));
	}
	
	@Override
	public Match resolveNextMatch() throws NoMoreMatchesException {
		final Match match = super.resolveNextMatch();
		final List<Player> losers = new ArrayList<>(match.getPlayers());
		losers.removeAll(match.getWinners());
		this.eliminatedPlayers.addAll(losers);
		
		for (TournamentMatch tm : this.tournamentTable.getMatches(tournamentRound - 1)) {
			if (tm.getPlayers().containsAll(match.getPlayers())) {
				//update tournament data with result
				tm.setFinished(match.isFinished());
				tm.setPlayerStatus(match.getPlayerStatus());
				tm.setWinner(match.getWinners().isEmpty() ? null : match.getWinners().get(0));
			}
		}
		
		
		return match;
	}

	@Override
	public Optional<Match> getNextMatch() {
		Optional<Match> nextMatch = getMatches().stream().filter(m -> !m.isFinished()).findFirst();
		if (!nextMatch.isPresent() && !getWinner().isPresent()) {
			//round finished, generate next batch
			final List<Player> actualPlayers = new ArrayList<>(getPlayers());
			actualPlayers.removeAll(this.eliminatedPlayers);
			getMatches().addAll(createMatches(actualPlayers));
			nextMatch = getMatches().stream().filter(m -> !m.isFinished()).findFirst();
		}
		return nextMatch;
	}

	@Override
	public Optional<Player> getWinner() {
		if (getPlayers().size() - this.eliminatedPlayers.size() == 1) {
			return getPlayers().stream().filter(p -> !this.eliminatedPlayers.contains(p)).findFirst();
		}
		return Optional.empty();
	}

	@Override
	public List<PlayerStatus> getRanking() {
		final List<PlayerStatus> ranking = getPlayers().stream().map(PlayerStatus::new).collect(Collectors.toList());

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

	private List<Match> createMatches(final List<Player> players) {
		int counter = 0;
		final List<Player> matchPlayers = new ArrayList<>();
		final List<Match> matches = new ArrayList<>();

		for (final Player player : players) {
			matchPlayers.add(player);
			if (++counter == PLAYERS_PER_MATCH) {
				//flush
				counter = 0;
				matches.add(new Match(new ArrayList<>(matchPlayers)));
				matchPlayers.clear();
			}
		}


		for (final Match m : matches) {

			final TournamentMatch tm = new TournamentMatch();
			tm.setFinished(m.isFinished());
			tm.setPlayers(m.getPlayers());
			tm.setPlayerStatus(m.getPlayerStatus());
			tm.setWinner(m.getWinners().isEmpty() ? null : m.getWinners().get(0));
			//TODO will need to update on the fly, resolved matches will not be updated like this...

			this.tournamentTable.addMatch(this.tournamentRound, tm);
		}

		this.tournamentRound++;

		return matches;
	}

	public TournamentTable getTournamentTable() {
		return this.tournamentTable;
	}



}