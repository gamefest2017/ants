package com.ibm.sk.handlers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.ibm.sk.MenuMain;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.StartGameData;
import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.engine.matchmaking.NoMoreMatchesException;
import com.ibm.sk.engine.matchmaking.Qualification;
import com.ibm.sk.engine.matchmaking.SingleElimination;
import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.client.ReplayFileHelper;
import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.mapper.Mapper;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;

public class GameMenuHandler implements GuiEventListener {

	private final InitMenuData menuData;

	private Qualification qualification = null;
	private SingleElimination tournament = null;
	private final GUIFacade facade;

	public GameMenuHandler(final GUIFacade facade, final InitMenuData menuData) {
		this.menuData = menuData;
		this.facade = facade;
	}

	@Override
	public void actionPerformed(final GuiEvent event) {
		ProcessExecutor executor;
		if (GuiEvent.EventTypes.SINGLE_PLAY_START.name().equals(event.getType().name())) {
			facade.setRender(true);
			executor = new ProcessExecutor(this.facade);
			executor.run(event.getData(), null);
		} else if (GuiEvent.EventTypes.DOUBLE_PLAY_START.name().equals(event.getType().name())) {
			facade.setRender(true);
			final String hillNames = event.getData();
			final int separatorPos = hillNames.indexOf(GuiEvent.HLL_NAMES_SEPARATOR);
			executor = new ProcessExecutor(this.facade);
			executor.run(hillNames.substring(0, separatorPos), hillNames.substring(separatorPos + 1));
		} else if (GuiEvent.EventTypes.QUALIFICATION_START.name().equals(event.getType().name())) {
			StartGameData data = Mapper.INSTANCE.jsonToPojo(event.getData(), StartGameData.class);
			facade.setRender(!data.isRunInBackground());
			menuData.setRunInBackground(data.isRunInBackground());
			// lazy initialize qualification on first run
			if (this.qualification == null) {
				final AtomicInteger index = new AtomicInteger();
				final List<Player> players = data.getPlayers().stream()
						.map(s -> new Player(Integer.valueOf(index.incrementAndGet()), s)).collect(Collectors.toList());
				this.qualification = new Qualification(players);
			}

			if (!this.qualification.getWinner().isPresent()) {
				try {
					this.qualification.resolveNextMatch(facade);
				} catch (final NoMoreMatchesException e) {
					e.printStackTrace();
				}

				this.menuData.setQualification(this.qualification.getQualificationTable());

				this.facade.showInitMenu(this.menuData);
			}

		} else if (GuiEvent.EventTypes.TOURNAMENT_PLAY_START.name().equals(event.getType().name())) {
			StartGameData data = Mapper.INSTANCE.jsonToPojo(event.getData(), StartGameData.class);
			facade.setRender(!data.isRunInBackground());
			menuData.setRunInBackground(data.isRunInBackground());

			// if qualification is not done, do nothing
			if (this.qualification.getWinner().isPresent()) {

				// take results from qualification, start tournament
				if (this.tournament == null) {
					this.tournament = new SingleElimination(this.qualification.getQualificationTable().getCandidates()
							.stream().filter(qc -> qc.isQualified()).map(qc -> new Player(qc.getId(), qc.getName()))
							.collect(Collectors.toList()));
				}

				if (!tournament.getWinner().isPresent()) {
					try {
						this.tournament.resolveNextMatch(facade);
					} catch (final NoMoreMatchesException e) {
						e.printStackTrace();
					}

					this.menuData.setTournament(this.tournament.getTournamentTable());

					this.facade.showInitMenu(this.menuData);
				}
			}
		} else if (GuiEvent.EventTypes.START_REPLAY.equals(event.getType())) {
			System.out.println("Replay starts : " + event.getData());
			ReplayFileHelper.read(event.getData()).play(facade);
		} else if (GuiEvent.EventTypes.RESULT_CLOSE.equals(event.getType())) {
			// TODO - show init menu
			MenuMain.showMainWindow(this.menuData);
		}
	}

}
