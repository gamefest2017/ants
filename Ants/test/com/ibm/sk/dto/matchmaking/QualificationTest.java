package com.ibm.sk.dto.matchmaking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.sk.engine.matchmaking.TournamentTest;
import com.ibm.sk.engine.matchmaking.ITournament;
import com.ibm.sk.engine.matchmaking.Qualification;

public class QualificationTest extends TournamentTest {

	@Override
	public ITournament createTournament() {
		return new Qualification(PLAYERS, null);
	}
	
	@Test
	public void testWinnerPresent() {
		assertTrue(getTournament().getWinner().isPresent());
	}
	
	@Test
	public void testNumberOfMatches() {
		assertEquals(3L, getTournament().getMatchStatus().stream().filter(m -> m.getPlayers().contains(VLADO)).count());
	}
	

	
	
	
}