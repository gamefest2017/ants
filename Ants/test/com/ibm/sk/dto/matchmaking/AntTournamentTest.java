package com.ibm.sk.dto.matchmaking;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.ibm.sk.dto.matchmaking.comparator.PlayerScoreComparator;
import com.ibm.sk.dto.matchmaking.strategy.Qualification;
import com.ibm.sk.dto.matchmaking.strategy.SingleElimination;

public class AntTournamentTest extends TournamentTest {

	@Override
	public ITournament createTournament() {
		ITournament qualification = new Qualification(PLAYERS).fastForward();
		List<Player> rankedPlayers = qualification.getRanking().stream().sorted(new PlayerScoreComparator())
				.map(ps -> ps.getPlayer()).collect(Collectors.toList());
		printTournament(qualification);
		return new SingleElimination(rankedPlayers);
	}
	
	@Test
	public void testAntTournament() {
		assertTrue(getTournament().getWinner().isPresent());
	}
	

}
