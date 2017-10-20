package com.ibm.sk.dto.qualification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibm.sk.dto.matchmaking.Player;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QualificationCandidate {
	private Integer id;
	private String name;
	private List<Long> score = new ArrayList<>();
	private boolean qualified = false;
	
	public QualificationCandidate(Integer id, String name, boolean qualified, Long... scoreInQualificationRounds) {
		this.id = id;
		this.name = name;
		this.qualified = qualified;
		this.score = Arrays.asList(scoreInQualificationRounds);
	}
	
	public QualificationCandidate(Integer id, String name, boolean qualified, List<Long> scoreInQualificationRounds) {
		this.id = id;
		this.name = name;
		this.qualified = qualified;
		this.score = scoreInQualificationRounds;
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
	
	public void setScore(List<Long> score) {
		this.score = score;
	}

	@JsonIgnore
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
