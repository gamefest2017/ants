package com.ibm.sk.handlers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.ibm.sk.ant.facade.AntFactory;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.engine.matchmaking.NoMoreMatchesException;
import com.ibm.sk.engine.matchmaking.Qualification;
import com.ibm.sk.engine.matchmaking.SingleElimination;
import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;

public class GameMenuHandler implements GuiEventListener {

	private final InitMenuData menuData;

	private Qualification qualification = null;
	private SingleElimination tournament = null;
	private final GUIFacade facade;
	private final AntFactory[] implementations;


	public GameMenuHandler(final GUIFacade facade, final InitMenuData menuData, final AntFactory[] implementations) {
		this.menuData = menuData;
		this.facade = facade;
		this.implementations = implementations;
	}

	@Override
	public void actionPerformed(final GuiEvent event) {
		ProcessExecutor executor;
		if (GuiEvent.EventTypes.SINGLE_PLAY_START.name().equals(event.getType().name())) {
			executor = new ProcessExecutor(this.facade, this.implementations);
			executor.run(event.getData(), null);
		} else if (GuiEvent.EventTypes.DOUBLE_PLAY_START.name().equals(event.getType().name())) {
			final String hillNames = event.getData();
			final int separatorPos = hillNames.indexOf(GuiEvent.HLL_NAMES_SEPARATOR);
			executor = new ProcessExecutor(this.facade, this.implementations);
			executor.run(hillNames.substring(0, separatorPos), hillNames.substring(separatorPos + 1));
		} else if (GuiEvent.EventTypes.QUALIFICATION_START.name().equals(event.getType().name()) ) {

			//lazy initialize qualification on first run
			if (this.qualification == null) {
				final AtomicInteger index = new AtomicInteger();
				final List<Player> players = Arrays.asList(event.getData().split(",")).stream().map(s -> new Player(index.incrementAndGet(), s)).collect(Collectors.toList());
				this.qualification = new Qualification(players);
			}

			try {
				this.qualification.resolveNextMatch();
			} catch (final NoMoreMatchesException e) {
				e.printStackTrace();
			}

			//TODO - repeated matches do not reset game state, leftover objects cause exceptions after a while

			this.menuData.setQualification(this.qualification.getQualificationTable());

			this.facade.showInitMenu(this.menuData);

		} else if (GuiEvent.EventTypes.TOURNAMENT_PLAY_START.name().equals(event.getType().name()) ) {
			//take results from qualification, start tournament
			if (this.tournament == null) {
				this.tournament = new SingleElimination(
						this.qualification.getQualificationTable().getCandidates().stream()
						.filter(qc -> qc.isQualified()).collect(Collectors.toList()));
			}

			try {
				this.tournament.resolveNextMatch();
			} catch (final NoMoreMatchesException e) {
				e.printStackTrace();
			}

			this.menuData.setTournament(this.tournament.getTournamentTable());

			this.facade.showInitMenu(this.menuData);
		}
	}

}
