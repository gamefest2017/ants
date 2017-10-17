package com.ibm.sk.engine.matchmaking;

import static com.ibm.sk.WorldConstans.TURNS;
import static com.ibm.sk.engine.World.createHill;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ibm.sk.dto.Hill;
import com.ibm.sk.dto.enums.HillOrder;
import com.ibm.sk.dto.matchmaking.Match;
import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.PlayerStatus;
import com.ibm.sk.engine.FoodHandler;
import com.ibm.sk.engine.ProcessExecutor;
import com.ibm.sk.engine.World;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public abstract class Tournament implements ITournament {
	private final List<Player> players;
	private final ProcessExecutor executor;
	protected static final Player AI = new Player(null, "AI");
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.PROTECTED)
	private List<Match> matches = new ArrayList<>();
	
	@Override
	public Match resolveNextMatch() throws NoMoreMatchesException {
		Match match = getNextMatch().orElseThrow(NoMoreMatchesException::new);
		
		
		boolean singlePlayer = match.getPlayers().contains(AI);
		
		match.startMatch();
		
		World.createWorldBorder();
		final Hill firstHill = createHill(HillOrder.FIRST, match.getPlayer(0).getName());
		Hill secondHill = null;
		if (!singlePlayer) {
			secondHill = createHill(HillOrder.SECOND, match.getPlayer(1).getName());
		}
		executor.initGame(firstHill, secondHill);

		for (int turn = 0; turn < TURNS; turn++) {
//			ProcessExecutor.execute(firstHill, secondHill);
//			FoodHandler.dropFood();
		}
		match.getPlayerStatus(0).addScore(firstHill.getFood());
		if (!singlePlayer) {
			match.getPlayerStatus(1).addScore(secondHill.getFood());
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