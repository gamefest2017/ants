package com.ibm.sk.dto.matchmaking;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.sk.engine.matchmaking.TournamentTest;
import com.ibm.sk.engine.matchmaking.ITournament;
import com.ibm.sk.engine.matchmaking.SingleElimination;

public class SingleEliminationTest extends TournamentTest {
	
	@Override
	public ITournament createTournament() {
		return new SingleElimination(PLAYERS, null);
	}

	@Test
	public void testWinnerPresent() {
		assertTrue(getTournament().getWinner().isPresent());
	}
	
}