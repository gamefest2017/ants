package com.ibm.sk.engine.matchmaking;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.ibm.sk.dto.matchmaking.Match;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.PlayerStatus;
import com.ibm.sk.dto.matchmaking.comparator.PlayerScoreComparator;
import com.ibm.sk.dto.qualification.QualificationCandidate;
import com.ibm.sk.dto.qualification.QualificationTable;

/**
 * Each player has 3 rounds with AI and gets ranking based on his score.
 *
 * Victories are not taken into account, as it is assumed AI is passive (cannot win).
 * @author Vladimir Martinka (vladimir.martinka@gmail.com)
 *
 */
public class Qualification extends Tournament {

	private static final Integer MATCHES_PER_PLAYER = 3;


	public Qualification(final List<Player> players) {
		super(players);
		getPlayers().stream().forEach(player -> {
			IntStream.range(0, MATCHES_PER_PLAYER).forEach(i -> {
				getMatches().add(new Match(Arrays.asList(player, AI)));
			});
		});
	}

	@Override
	public Optional<Match> getNextMatch() {
		return getMatches().stream().filter(m -> !m.isFinished()).findFirst();
	}

	@Override
	public Optional<Player> getWinner() {
		if (getMatches().stream().anyMatch(m -> !m.isFinished())) {
			return Optional.empty();
		}
		return Optional.of(getRanking().get(0).getPlayer());
	}

	@Override
	public List<PlayerStatus> getRanking() {
		final Map<Player, Integer> playerScore = getMatches().stream()
				.flatMap(m -> m.getPlayerStatus().stream())
				.collect(groupingBy(PlayerStatus::getPlayer,
						summingInt(PlayerStatus::getScore)));

		playerScore.remove(AI);

		final List<PlayerStatus> ranking = playerScore.keySet().stream().map(p -> {
			final PlayerStatus status = new PlayerStatus(p);
			status.addScore(playerScore.get(p));
			return status;
		}).sorted(new PlayerScoreComparator().reversed())
				.collect(Collectors.toList());

		return ranking;
	}

	@Override
	public List<Match> getMatchStatus() {
		// TODO return copy / snapshot of current state
		return getMatches();
	}

	public QualificationTable getQualificationTable() {
		final QualificationTable table = new QualificationTable();
		int index = 0;

		final int nextPowerOfTwo = (int) Math.ceil(Math.log(getPlayers().size()) / Math.log(2));
		final int maxQualifiedPlayers = (int) Math.pow(2, nextPowerOfTwo - 1 );

		for (final PlayerStatus ps : getRanking()) {
			table.addCandidate(new QualificationCandidate(
					ps.getPlayer().getId(),
					ps.getPlayer().getName(),
					++index > maxQualifiedPlayers ? false : true,
							//					ps.getScore().longValue())
							getScoresForPlayer(ps.getPlayer())));

		}
		return table;
	}

	private List<Long> getScoresForPlayer(final Player player) {
		return getMatches().stream()
				.flatMap(m -> m.getPlayerStatus().stream())
				.filter(ps -> player.equals(ps.getPlayer()))
				.map(ps -> ps.getScore().longValue())
				.collect(Collectors.toList());
	}

}