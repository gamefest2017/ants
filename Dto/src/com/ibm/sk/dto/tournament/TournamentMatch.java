package com.ibm.sk.dto.tournament;

import java.util.List;
import java.util.stream.Collectors;

import com.ibm.sk.dto.matchmaking.Player;
import com.ibm.sk.dto.matchmaking.PlayerStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentMatch {
	
	private boolean finished;
	private List<Player> players;
	private Player winner;
	private List<PlayerStatus> playerStatus;
	
	public String printScore() {
		if (playerStatus == null || !isFinished()) {
			return "";
		} else {
			return playerStatus.stream().map(playerScore -> String.valueOf(playerScore.getScore())).collect(Collectors.joining(" : "));
		}
	}
	
	
	
	
}
