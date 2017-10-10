package com.ibm.sk.dto.qualification;

import java.util.ArrayList;
import java.util.List;

public class Qualification {
	
	List<Candidate> candidates = new ArrayList<>();
	
	public Qualification() {
		
	}

	public Qualification(List<Candidate> candidates) {
		this.candidates = candidates;
	}

	public List<Candidate> getCandidates() {
		return candidates;
	}
	
	public Candidate getCandidate(int i) {
		if (candidates == null || candidates.size() <= i) {
			return null;
		}
		
		return candidates.get(i);
	}

	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}
	
	public void addCandidate(Candidate candidate) {
		this.candidates.add(candidate);
	}
	
}
