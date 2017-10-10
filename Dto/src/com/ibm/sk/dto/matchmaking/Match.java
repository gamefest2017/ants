package com.ibm.sk.dto.matchmaking;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
	
	private static synchronized Calendar now() {
		Calendar cal = Calendar.getInstance();//TODO set default timezone
		cal.setTimeInMillis(System.currentTimeMillis());
		return cal;
	}
	
}
