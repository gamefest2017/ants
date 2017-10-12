package com.ibm.sk.dto.qualification;

import java.util.ArrayList;
import java.util.List;

public class QualificationTable {
	
	List<QualificationCandidate> candidates = new ArrayList<>();
	
	public QualificationTable() {
		
	}

	public QualificationTable(List<QualificationCandidate> candidates) {
		this.candidates = candidates;
	}

	public List<QualificationCandidate> getCandidates() {
		return candidates;
	}
	
	public QualificationCandidate getCandidate(int i) {
		if (candidates == null || candidates.size() <= i) {
			return null;
		}
		
		return candidates.get(i);
	}

	public void setCandidates(List<QualificationCandidate> candidates) {
		this.candidates = candidates;
	}
	
	public void addCandidate(QualificationCandidate candidate) {
		this.candidates.add(candidate);
	}
	
}
