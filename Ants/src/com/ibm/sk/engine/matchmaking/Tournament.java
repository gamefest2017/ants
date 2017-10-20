package com.ibm.sk.engine.matchmaking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ibm.sk.ant.AntLoader;
import com.ibm.sk.dto.matchmaking.Match;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.ff.gui.client.GUIFacade;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public abstract class Tournament implements ITournament {
	private final List<Player> players;
	protected static final Player AI = new Player(null, "AI");
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.PROTECTED)
	private final List<Match> matches = new ArrayList<>();
	
	@Override
	public Match resolveNextMatch() throws NoMoreMatchesException {
		final Match match = getNextMatch().orElseThrow(NoMoreMatchesException::new);

		final boolean singlePlayer = match.getPlayers().contains(AI);
		
		match.startMatch();

		final ProcessExecutor executor = new ProcessExecutor(new GUIFacade());
		String secondPlayerName = null;
		if (!singlePlayer) {
			secondPlayerName = match.getPlayer(1).getName();
		}
		final Map<String, Integer> results = executor.run(match.getPlayer(0).getName(), secondPlayerName);

		match.getPlayerStatus(0).addScore(results.get(match.getPlayer(0).getName()).intValue());
		if (!singlePlayer) {
			match.getPlayerStatus(1).addScore(results.get(secondPlayerName).intValue());
		}
		
		if (singlePlayer) {
			ProcessExecutor.guiConnector.showResult(match.getPlayer(0).getName());
		} else {
			ProcessExecutor.guiConnector.showResult(match.getWinners().get(0).getName());
		}
		
		match.endMatch();
		
		return match;
	}
	
	@Override
	public ITournament fastForward() {
		while (getNextMatch().isPresent()) {
			try {
				resolveNextMatch();
			} catch (final NoMoreMatchesException e) {
				e.printStackTrace();//cannot happen
			}
		}
		return this;
	}
	
	
}