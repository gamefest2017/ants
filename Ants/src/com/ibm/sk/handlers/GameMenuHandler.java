package com.ibm.sk.handlers;

import static com.ibm.sk.WorldConstans.TURNS;
import static com.ibm.sk.engine.World.createHill;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.ibm.sk.MenuMain;
import com.ibm.sk.WorldConstans;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.enums.HillOrder;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.engine.FoodHandler;
import com.ibm.sk.engine.GuiConnector;
import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.engine.World;
import com.ibm.sk.engine.matchmaking.NoMoreMatchesException;
import com.ibm.sk.engine.matchmaking.Qualification;
import com.ibm.sk.engine.matchmaking.SingleElimination;
import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;

public class GameMenuHandler implements GuiEventListener {

	private static int turn;
	private final ProcessExecutor executor;
	private final InitMenuData menuData;
	private final GUIFacade facade;
	
	private Qualification qualification = null;
	private SingleElimination tournament = null;
	

	public GameMenuHandler(final GUIFacade facade, InitMenuData menuData) {
		this.executor = new ProcessExecutor(facade);
		this.facade = facade;
		this.menuData = menuData;
	}

	@Override
	public void actionPerformed(final GuiEvent event) {
		if (GuiEvent.EventTypes.SINGLE_PLAY_START.name().equals(event.getType().name())) {
			startSinglePlayer(event.getData());
		} else if (GuiEvent.EventTypes.DOUBLE_PLAY_START.name().equals(event.getType().name())) {
			final String hillNames = event.getData();
			final int separatorPos = hillNames.indexOf(GuiEvent.HLL_NAMES_SEPARATOR);
			startDoublePlayer(hillNames.substring(0, separatorPos) + "1", hillNames.substring(separatorPos + 1) + "2");
		} else if (GuiEvent.EventTypes.QUALIFICATION_START.name().equals(event.getType().name()) ) {

			//lazy initialize qualification on first run
			if (qualification == null) {
				AtomicInteger index = new AtomicInteger();
				List<Player> players = Arrays.asList(event.getData().split(",")).stream().map(s -> new Player(index.incrementAndGet(), s)).collect(Collectors.toList());
				qualification = new Qualification(players, executor);
			}
			
			try {
				qualification.resolveNextMatch();
			} catch (NoMoreMatchesException e) {
				e.printStackTrace();
			}
			
			//TODO - repeated matches do not reset game state, leftover objects cause exceptions after a while
			
			menuData.setQualification(qualification.getQualificationTable());
			
			facade.showInitMenu(menuData);
			
		} else if (GuiEvent.EventTypes.TOURNAMENT_PLAY_START.name().equals(event.getType().name()) ) {
			//take results from qualification, start tournament
			if (tournament == null) {
				tournament = new SingleElimination(
						qualification.getQualificationTable().getCandidates().stream().filter(qc -> qc.isQualified()).collect(Collectors.toList()), 
						executor);
			}
			
			try {
				tournament.resolveNextMatch();
			} catch (NoMoreMatchesException e) {
				e.printStackTrace();
			}
			
			menuData.setTournament(this.tournament.getTournamentTable());
			
			facade.showInitMenu(menuData);
		} else if (GuiEvent.EventTypes.RESULT_CLOSE.equals(event.getType())) {
			//TODO - show init menu
			MenuMain.showMainWindow();
		}
	}

	public void startSinglePlayer(final String hillName) {
		System.out.println("Single player game starting...");
		System.out.println("World size: " + WorldConstans.X_BOUNDRY + " x " + WorldConstans.Y_BOUNDRY);
		System.out.println("Turns: " + TURNS);

		World.createWorldBorder();
		final Hill hill = createHill(HillOrder.FIRST, hillName);
		this.executor.initGame(hill, null);
		final long startTime = System.currentTimeMillis();
		for (turn = 0; turn < TURNS; turn++) {
			ProcessExecutor.execute(hill, null, turn);
			FoodHandler.dropFood(turn);
		}
		final long endTime = System.currentTimeMillis();
		System.out.println(hill.getName() + " earned score: " + hill.getFood());
		for (final IAnt ant : hill.getAnts()) {
			System.out.println(ant);
		}
		executor.finishGame(hill);
		System.out.println("Game duration: " + turn + " turns, in " + (endTime - startTime) + " ms");
	}

	public void startDoublePlayer(final String firstHillName, final String secondHillName) {
		System.out.println("Duel starting...");
		System.out.println("World size: " + WorldConstans.X_BOUNDRY + " x " + WorldConstans.Y_BOUNDRY);
		System.out.println("Turns: " + TURNS);

		World.createWorldBorder();
		final Hill firstHill = createHill(HillOrder.FIRST, firstHillName);
		final Hill secondHill = createHill(HillOrder.SECOND, secondHillName);
		this.executor.initGame(firstHill, secondHill);

		final long startTime = System.currentTimeMillis();
		for (turn = 0; turn < TURNS; turn++) {
			ProcessExecutor.execute(firstHill, secondHill, turn);
			FoodHandler.dropFood(turn);
		}
		final long endTime = System.currentTimeMillis();
		printScore(firstHill);
		printScore(secondHill);
		executor.finishGame((firstHill.getFood() > secondHill.getFood()) ? firstHill : secondHill);
		System.out.println("Game duration: " + turn + " turns, in " + (endTime - startTime) + " ms");
	}

	private static void printScore(final Hill hill) {
		System.out.println(hill.getName() + " earned score: " + hill.getFood());
	}

}
