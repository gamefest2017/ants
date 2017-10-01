package com.ibm.sk.dto.matchmaking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

/**
 * Helper structure to store player score, for easier manipulation + sorting.
 * @author Vladimir Martinka (vladimir.martinka@gmail.com)
 *
 */
@Data
public class PlayerStatus {
	private final Player player;
	@Setter(AccessLevel.NONE) private Integer score = 0;
	
	public void addScore(@NonNull Integer score) {
		this.score += score;
	}
}
