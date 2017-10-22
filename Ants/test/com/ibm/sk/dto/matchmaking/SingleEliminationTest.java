package com.ibm.sk.dto.matchmaking;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.sk.engine.matchmaking.ITournament;
import com.ibm.sk.engine.matchmaking.SingleElimination;
import com.ibm.sk.engine.matchmaking.TournamentTest;
import com.ibm.sk.ff.gui.client.GUIFacade;

public class SingleEliminationTest extends TournamentTest {

	@Override
	public ITournament createTournament() {
		return new SingleElimination(new GUIFacade(), PLAYERS);
	}

	@Test
	public void testWinnerPresent() {
		assertTrue(getTournament().getWinner().isPresent());
	}

}