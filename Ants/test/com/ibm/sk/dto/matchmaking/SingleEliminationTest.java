package com.ibm.sk.dto.matchmaking;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.sk.dto.matchmaking.strategy.SingleElimination;

public class SingleEliminationTest extends TournamentTest {
	
	@Override
	public ITournament createTournament() {
		return new SingleElimination(PLAYERS);
	}

	@Test
	public void testWinnerPresent() {
		assertTrue(getTournament().getWinner().isPresent());
	}
	
}
