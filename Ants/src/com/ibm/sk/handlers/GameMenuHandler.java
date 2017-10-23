package com.ibm.sk.handlers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.ibm.sk.dto.matchmaking.Match;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.StartGameData;
import com.ibm.sk.dto.qualification.QualificationCandidate;
import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.engine.matchmaking.NoMoreMatchesException;
import com.ibm.sk.engine.matchmaking.Qualification;
import com.ibm.sk.engine.matchmaking.SingleElimination;
import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.client.ReplayFileHelper;
import com.ibm.sk.ff.gui.common.events.GuiEvent;
import com.ibm.sk.ff.gui.common.events.GuiEventListener;
import com.ibm.sk.ff.gui.common.events.GuiEvent.EventTypes;
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
			final StartGameData data = Mapper.INSTANCE.jsonToPojo(event.getData(), StartGameData.class);
			this.menuData.setRunInBackground(data.isRunInBackground());
			this.facade.setRender(!data.isRunInBackground());
			executor = new ProcessExecutor(this.facade);
			final Map<String, Integer> results = executor.run(data.getPlayers().get(0), null);
			final String winner = results.entrySet().stream()
					.max((one, other) -> one.getValue().compareTo(other.getValue()))
					.orElseGet(() -> results.entrySet().iterator().next()).getKey();
			this.facade.showResult(winner);
		} else if (GuiEvent.EventTypes.DOUBLE_PLAY_START.name().equals(event.getType().name())) {
			final StartGameData data = Mapper.INSTANCE.jsonToPojo(event.getData(), StartGameData.class);
			this.menuData.setRunInBackground(data.isRunInBackground());
			this.facade.setRender(!data.isRunInBackground());
			executor = new ProcessExecutor(this.facade);
			final Map<String, Integer> results = executor.run(data.getPlayers().get(0),
					data.getPlayers().get(1));
			final String winner = results.entrySet().stream()
					.max((one, other) -> one.getValue().compareTo(other.getValue()))
					.orElseGet(() -> results.entrySet().iterator().next()).getKey();
			this.facade.showResult(winner);
		} else if (GuiEvent.EventTypes.QUALIFICATION_START.name().equals(event.getType().name())) {
			final StartGameData data = Mapper.INSTANCE.jsonToPojo(event.getData(), StartGameData.class);
			this.menuData.setRunInBackground(data.isRunInBackground());
			this.facade.setRender(!data.isRunInBackground());
			if (this.qualification == null) {
				final AtomicInteger index = new AtomicInteger();
				final List<Player> players = data.getPlayers().stream()
						.map(s -> new Player(Integer.valueOf(index.incrementAndGet()), s)).collect(Collectors.toList());
				this.qualification = new Qualification(this.facade, players);
			}

			if (!this.qualification.getWinner().isPresent()) {
				Match m = null;
				try {
					m = this.qualification.resolveNextMatch();
				} catch (final NoMoreMatchesException e) {
					e.printStackTrace();
				}
				
				this.menuData.setQualification(this.qualification.getQualificationTable());
				this.facade.showResult(m == null ? "" : m.getWinners().get(0).getName());
			} else {
				actionPerformed(new GuiEvent(EventTypes.TOURNAMENT_PLAY_START, event.getData()));
			}
			
		} else if (GuiEvent.EventTypes.TOURNAMENT_PLAY_START.name().equals(event.getType().name())) {
			final StartGameData data = Mapper.INSTANCE.jsonToPojo(event.getData(), StartGameData.class);
			this.facade.setRender(!data.isRunInBackground());
			this.menuData.setRunInBackground(data.isRunInBackground());

			// if qualification is not done, do nothing
			if (this.qualification.getWinner().isPresent()) {

				// take results from qualification, start tournament
				if (this.tournament == null) {
					this.tournament = new SingleElimination(this.facade,
							this.qualification.getQualificationTable().getCandidates().stream()
							.filter(QualificationCandidate::isQualified)
							.map(qc -> new Player(qc.getId(), qc.getName()))
							.collect(Collectors.toList()));
				}

				if (!this.tournament.getWinner().isPresent()) {
					Match m = null;
					try {
						m = this.tournament.resolveNextMatch();
					} catch (final NoMoreMatchesException e) {
						e.printStackTrace();
					}
					
					this.menuData.setTournament(this.tournament.getTournamentTable());
					this.facade.showResult(m == null ? "" : m.getWinners().get(0).getName());
				}
			}
		} else if (GuiEvent.EventTypes.START_REPLAY.equals(event.getType())) {
			System.out.println("Replay starts : " + event.getData());
			ReplayFileHelper.read(event.getData()).play(this.facade);
		} else if (GuiEvent.EventTypes.RESULT_CLOSE.equals(event.getType())) {
			this.menuData.setReplays(ReplayFileHelper.getAvailableReplays());
			this.facade.showInitMenu(this.menuData);
		}
	}

}
