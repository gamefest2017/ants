package com.ibm.sk.dto.matchmaking;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;

import com.ibm.sk.engine.matchmaking.ITournament;

import lombok.Getter;

public abstract class TournamentTest {

	@Getter	
	private ITournament tournament;
	
	public static Player VLADO = new Player(1 , "Vlado");
	public static List<Player> PLAYERS = Arrays.asList(
			VLADO,
			new Player(2 , "Robo"),
			new Player(3 , "Gabo"),
			new Player(4 , "Lenka"),
			new Player(5 , "Robo"),// 2 guys with same name
			new Player(6 , "Peto")
			);
	
	@Before
	public void setUp() {
		tournament = createTournament().fastForward();
		printTournament(getTournament());
	}
	
	public abstract ITournament createTournament();
	
	public static void printTournament(ITournament t) {
		System.out.println("Tournament " + t.getClass().getSimpleName());
		System.out.println("-----------------");
		
		System.out.println("Ranking: ");
		t.getRanking().forEach(System.out::println);
		
		System.out.println("Matches: ");
		t.getMatchStatus().forEach(System.out::println);
		
		System.out.println("Winner is: " + t.getWinner().get());
		
		System.out.println("\n\n");
	}
}