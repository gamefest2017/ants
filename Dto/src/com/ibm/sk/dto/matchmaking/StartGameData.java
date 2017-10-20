package com.ibm.sk.dto.matchmaking;

import java.util.List;

import lombok.Data;

@Data
public class StartGameData {
	
	private boolean runInBackground;
	private List<String> players;

}
