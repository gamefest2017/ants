package com.ibm.sk.dto.matchmaking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString(exclude= {"startTime","endTime"})
public class Match {
	private final List<PlayerStatus> players;
	//private final String map; //TODO not part of the match?
	//TODO history? reference to game state?
	
	@Setter(AccessLevel.NONE) private Optional<Calendar> startTime = Optional.empty();
	@Setter(AccessLevel.NONE) private Optional<Calendar> endTime = Optional.empty();
	
	public Match(PlayerStatus playerStatus1, PlayerStatus playerStatus2) {
		players = new ArrayList<>();
		players.add(playerStatus1);
		players.add(playerStatus2);
	}
	
	public Match(PlayerStatus playerStatus1, PlayerStatus playerStatus2, boolean finished) {
		players = new ArrayList<>();
		players.add(playerStatus1);
		players.add(playerStatus2);
		if (finished) {
			startMatch();
			endMatch();
		}
	}

	public Boolean isFinished() {
		return endTime.isPresent();
	}
	
	public Boolean isDraw() {
		return getWinners().size() > 1;
	}
	
	public List<Player> getWinners() {
		if (!isFinished()) {
			return Collections.emptyList();//TODO consider exception
		}
		return players.stream()
				.filter(ps -> ps.getScore().equals(highestScore()))
				.map(ps -> ps.getPlayer())
				.collect(Collectors.toList());
	}
	
	public Integer highestScore() {
		return players.stream().sorted().findFirst().get().getScore();
	}
	
	public void startMatch() {
		if (!startTime.isPresent()) {
			startTime = Optional.of(now());
		}
	}
	
	public void endMatch() {
		if (!endTime.isPresent()) {
			endTime = Optional.of(now());
		}
	}
	
	public String printScore() {
		if (players == null || !isFinished()) {
			return "";
		} else {
			return players.stream().map(playerScore -> String.valueOf(playerScore.getScore())).collect(Collectors.joining(" : "));
		}
	}
	private static synchronized Calendar now() {
		Calendar cal = Calendar.getInstance();//TODO set default timezone
		cal.setTimeInMillis(System.currentTimeMillis());
		return cal;
	}
	
	public PlayerStatus getPlayerStatus(int index) {
		
		if (index < 0 || players == null || players.size() < index) {
			return null;
		}
		
		return players.get(index);
	}
	
	public Player getPlayer(int index) {
		
		if (getPlayerStatus(index) == null) {
			return null;
		}
		
		return getPlayerStatus(index).getPlayer();
	}
}
