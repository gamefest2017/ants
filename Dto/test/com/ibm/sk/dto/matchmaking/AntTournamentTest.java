package com.ibm.sk.dto.matchmaking;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.ibm.sk.dto.matchmaking.comparator.PlayerScoreComparator;
import com.ibm.sk.dto.matchmaking.strategy.Qualification;
import com.ibm.sk.dto.matchmaking.strategy.SingleElimination;
import com.ibm.sk.dto.qualification.QualificationTable;
import com.ibm.sk.dto.tournament.TournamentTable;

public class AntTournamentTest extends TournamentTest {

	@Override
	public ITournament createTournament() {
		ITournament qualification = new Qualification(PLAYERS).fastForward();
//		List<Player> rankedPlayers = qualification.getRanking().stream().sorted(new PlayerScoreComparator().reversed())
//				.map(ps -> ps.getPlayer()).collect(Collectors.toList());
		printTournament(qualification);
		QualificationTable qt = ((Qualification)qualification).getQualificationTable();
		return new SingleElimination(qt.getCandidates().stream().filter(c -> c.isQualified()).collect(Collectors.toList()));
	}
	
	@Test
	public void testAntTournament() {
		ITournament t = getTournament();
		assertTrue(t.getWinner().isPresent());
		
		TournamentTable tt = ((SingleElimination)t).getTournamentTable();
		assertNotNull(tt);
	}
	

}