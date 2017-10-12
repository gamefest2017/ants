package com.ibm.sk.dto.matchmaking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ibm.sk.dto.matchmaking.strategy.NoMoreMatchesException;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public abstract class Tournament implements ITournament {
	private final List<Player> players;
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.PROTECTED)
	private List<Match> matches = new ArrayList<>();
	
	@Override
	public Match resolveNextMatch() throws NoMoreMatchesException {
		Match match = getNextMatch().orElseThrow(NoMoreMatchesException::new);
		
		match.startMatch();
		//TODO add actual implementation of running the game when done
		for (PlayerStatus player : match.getPlayerStatus()) {
			player.addScore(new Random().nextInt(1000));
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