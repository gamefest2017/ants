package com.ibm.sk.ff.gui.common.objects.operations;

import java.util.Arrays;
import java.util.List;

import com.ibm.sk.dto.qualification.QualificationTable;
import com.ibm.sk.dto.tournament.TournamentTable;
import com.ibm.sk.ff.gui.client.Replay;

public class InitMenuData {
	
	public String[] competitors = {};
	
	public Replay[] replays = {};
	
	public QualificationTable qualification;
	
	public TournamentTable tournament;

	public QualificationTable getQualification() {
		return qualification;
	}

	public void setQualification(QualificationTable qualification) {
		this.qualification = qualification;
	}

	public TournamentTable getTournament() {
		return tournament;
	}

	public void setTournament(TournamentTable tournament) {
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
	
	public void addReplay(Replay replay) {
		replays = Arrays.copyOf(replays, replays.length + 1);
		replays[replays.length -1] = replay;
	}
	
	public void setReplay(Replay[] replays) {
		this.replays = replays;
	}
	
	public Replay[] getReplays() {
		return replays;
	}

}
