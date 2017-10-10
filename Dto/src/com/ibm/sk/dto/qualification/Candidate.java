package com.ibm.sk.dto.qualification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ibm.sk.dto.matchmaking.Player;

public class Candidate extends Player {

	private List<Long> score = new ArrayList<>();
	private boolean qualified = false;

	public Candidate(Integer id, String name, boolean qualified, Long... scoreInQualificationRounds) {
		super(id, name);
		this.qualified = qualified;
		this.score = Arrays.asList(scoreInQualificationRounds);
	}

	public List<Long> getScore() {
		return score;
	}

	public Long getScore(int i) {
		if (score == null || score.size() <= i) {
			return null;
		}
		return score.get(i);
	}

	public void addScore(long scoreInOneRound) {
		score.add(scoreInOneRound);
	}

	public long getAvgScore() {
		return score == null || score.isEmpty() ? 0 : Math.round(score.stream().mapToLong(Long::longValue).average().getAsDouble());
	}

	public boolean isQualified() {
		return qualified;
	}

	public void setQualified(boolean qualified) {
		this.qualified = qualified;
	}
}
