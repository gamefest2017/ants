package com.ibm.sk.dto.matchmaking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ibm.sk.dto.matchmaking.comparator.PlayerScoreComparator;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString(exclude= {"startTime", "endTime", "players"})
public class Match {
	private final List<Player> players;
	private final List<PlayerStatus> playerStatus;
	
	@Setter(AccessLevel.NONE) private Optional<Calendar> startTime = Optional.empty();
	@Setter(AccessLevel.NONE) private Optional<Calendar> endTime = Optional.empty();
	
	public Match(List<Player> players) {
		this.players = players;
		this.playerStatus = players.stream().map(PlayerStatus::new).collect(Collectors.toList());
	}
	
	public Match(PlayerStatus playerStatus1, PlayerStatus playerStatus2) {
		playerStatus = new ArrayList<>();
		playerStatus.add(playerStatus1);
		playerStatus.add(playerStatus2);
		players = new ArrayList<>();
		players.add(playerStatus1.getPlayer());
		players.add(playerStatus2.getPlayer());
	}
	
	public Match(PlayerStatus playerStatus1, PlayerStatus playerStatus2, boolean finished) {
		playerStatus = new ArrayList<>();
		playerStatus.add(playerStatus1);
		playerStatus.add(playerStatus2);
		players = new ArrayList<>();
		players.add(playerStatus1.getPlayer());
		players.add(playerStatus2.getPlayer());
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
		return getPlayerStatus().stream()
				.filter(ps -> ps.getScore().equals(highestScore()))
				.map(ps -> ps.getPlayer())
				.collect(Collectors.toList());
	}
	
	public Integer highestScore() {
		return getPlayerStatus().stream().sorted(new PlayerScoreComparator().reversed()).findFirst().get().getScore();
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
		if (playerStatus == null || !isFinished()) {
			return "";
		} else {
			return playerStatus.stream().map(playerScore -> String.valueOf(playerScore.getScore())).collect(Collectors.joining(" : "));
		}
	}
	
	public PlayerStatus getPlayerStatus(int index) {
		
		if (index < 0 || playerStatus == null || playerStatus.size() < index) {
			return null;
		}
		
		return playerStatus.get(index);
	}
	
	public Player getPlayer(int index) {
		
		if (getPlayerStatus(index) == null) {
			return null;
		}
		
		return getPlayerStatus(index).getPlayer();
	}
	
	private static synchronized Calendar now() {
		Calendar cal = Calendar.getInstance();//TODO set default timezone
		cal.setTimeInMillis(System.currentTimeMillis());
		return cal;
	}
	
}