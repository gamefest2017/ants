package com.ibm.sk.engine.matchmaking;

import static com.ibm.sk.WorldConstants.TURNS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ibm.sk.ant.AntLoader;
import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.IAnt;
import com.ibm.sk.dto.enums.HillOrder;
import com.ibm.sk.dto.matchmaking.Match;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.PlayerStatus;
import com.ibm.sk.engine.FoodHandler;
import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.engine.World;
import com.ibm.sk.ff.gui.client.GUIFacade;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;

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
	private List<Match> matches = new ArrayList<>();
	
	@Override
	public Match resolveNextMatch() throws NoMoreMatchesException {
		Match match = getNextMatch().orElseThrow(NoMoreMatchesException::new);

		boolean singlePlayer = match.getPlayers().contains(AI);
		
		match.startMatch();

		ProcessExecutor executor = new ProcessExecutor(new GUIFacade(), AntLoader.getImplementations());
		final Map<String, Integer> results = executor.run(match.getPlayer(0).getName(), match.getPlayer(1).getName());

		match.getPlayerStatus(0).addScore(results.get(match.getPlayer(0).getName()).intValue());
		if (!singlePlayer) {
			match.getPlayerStatus(1).addScore(results.get(match.getPlayer(1).getName()).intValue());
		}
		
		match.endMatch();
		
		return match;
	}
	
	@Override
	public ITournament fastForward() {
		while (getNextMatch().isPresent()) {
			try {
				resolveNextMatch();
			} catch (NoMoreMatchesException e) {
				e.printStackTrace();//cannot happen
			}
		}
		return this;
	}
	
	
}