package com.ibm.sk.ff.gui.common.objects.operations;

import java.util.Arrays;

import com.ibm.sk.dto.matchmaking.Tournament;
import com.ibm.sk.dto.qualification.Qualification;

public class InitMenuData {
	
	public String[] competitors = {};
	
	public String[] replays = {};
	
	public Qualification qualification;
	
	public Tournament tournament;

	public Qualification getQualification() {
		return qualification;
	}

	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public void addCompetitor(String competitor) {
		competitors = Arrays.copyOf(competitors, competitors.length + 1);
		competitors[competitors.length - 1] = competitor;
	}
	
	public void setCompetitors(String [] competitors) {
		this.competitors = competitors;
	}
	
	public String[] getCompetitors() {
		return competitors;
	}
	
	public void addReplay(String replay) {
		replays = Arrays.copyOf(replays, replays.length + 1);
		replays[replays.length -1] = replay;
	}
	
	public void setReplay(String[] replay) {
		replays = replay;
	}
	
	public String[] getReplays() {
		return replays;
	}

}
