package com.ibm.sk.dto.matchmaking;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.sk.dto.qualification.QualificationTable;
import com.ibm.sk.dto.tournament.TournamentTable;
import com.ibm.sk.engine.matchmaking.ITournament;
import com.ibm.sk.engine.matchmaking.Qualification;
import com.ibm.sk.engine.matchmaking.SingleElimination;
import com.ibm.sk.engine.matchmaking.TournamentTest;
import com.ibm.sk.ff.gui.client.GUIFacade;

public class AntTournamentTest extends TournamentTest {

	@Override
	public ITournament createTournament() {
		final ITournament qualification = new Qualification(new GUIFacade(), PLAYERS).fastForward();
		//		List<Player> rankedPlayers = qualification.getRanking().stream().sorted(new PlayerScoreComparator().reversed())
		//				.map(ps -> ps.getPlayer()).collect(Collectors.toList());
		printTournament(qualification);
		final QualificationTable qt = ((Qualification)qualification).getQualificationTable();
		//		return new SingleElimination(
		//				qt.getCandidates().stream().filter(c -> c.isQualified()).collect(Collectors.toList()));
		return null;
	}

	@Test
	public void testAntTournament() {
		final ITournament t = getTournament();
		assertTrue(t.getWinner().isPresent());

		final TournamentTable tt = ((SingleElimination)t).getTournamentTable();
		assertNotNull(tt);
	}


}