package com.ibm.sk.dto.matchmaking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.ibm.sk.dto.matchmaking.strategy.RoundRobin;

public class TournamentTest {

	Tournament t;
	
	@Before
	public void setUp() {
		t = new Tournament(new RoundRobin(), Arrays.asList(
				new Player(1 , "Vlado"),
				new Player(2 , "Robo"),
				new Player(3 , "Gabo"),
				new Player(4 , "Lenka"),
				new Player(5 , "Robo"),// 2 guys with same name
				new Player(6 , "Peto")
				));
		
		t.initialize();
		
		while (!t.getStrategy().getWinner().isPresent()) {
			t.runNextMatch();
		}
	}
	
	@Test
	public void testRanking() {
		printTournament();
		assertTrue(t.getStrategy().getRanking().size() > 0);
	}
	
	@Test
	public void testMatchList() {
		printTournament();
		assertTrue(t.getMatches().size() > 0);
	}
	
	@Test
	public void testWinner() {
		
		printTournament();
		
		assertEquals(t.getStrategy().getRanking().get(0).getPlayer(), t.getStrategy().getWinner().get());
		
		// :)
		//assertEquals("Vlado", t.getStrategy().getWinner().get().getName());
	}
	
	
	private void printTournament() {
		System.out.println("Tournament");
		System.out.println("-----------------");
		
		System.out.println("Ranking: ");
		t.getStrategy().getRanking().forEach(System.out::println);
		
		System.out.println("Matches: ");
		t.getMatches().forEach(System.out::println);
		
		System.out.println("Winner is: " + t.getStrategy().getWinner().get());
		
		System.out.println("\n\n");
	}
	
	
	
}
