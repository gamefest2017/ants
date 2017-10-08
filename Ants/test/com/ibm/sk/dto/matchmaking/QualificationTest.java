package com.ibm.sk.dto.matchmaking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.sk.dto.matchmaking.strategy.Qualification;

public class QualificationTest extends TournamentTest {

	@Override
	public ITournament createTournament() {
		return new Qualification(PLAYERS);
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
