package com.ibm.sk.ff.gui.common.objects.operations;

import java.util.Arrays;

import com.ibm.sk.dto.qualification.QualificationTable;
import com.ibm.sk.dto.tournament.TournamentTable;

public class InitMenuData {
	private boolean runInBackground;

	private String[] competitors = {};

	private String[] replays = {};

	private QualificationTable qualification;

	private TournamentTable tournament;

	public QualificationTable getQualification() {
		return this.qualification;
	}

	public void setQualification(final QualificationTable qualification) {
		this.qualification = qualification;
	}

	public TournamentTable getTournament() {
		return this.tournament;
	}

	public void setTournament(final TournamentTable tournament) {
		this.tournament = tournament;
	}

	public void addCompetitor(final String competitor) {
		this.competitors = Arrays.copyOf(this.competitors, this.competitors.length + 1);
		this.competitors[this.competitors.length - 1] = competitor;
	}

	public void setCompetitors(final String [] competitors) {
		this.competitors = competitors;
	}

	public String[] getCompetitors() {
		return this.competitors;
	}

	public void addReplay(final String replay) {
		this.replays = Arrays.copyOf(this.replays, this.replays.length + 1);
		this.replays[this.replays.length -1] = replay;
	}

	public void setReplays(final String[] replays) {
		this.replays = replays;
	}

	public String[] getReplays() {
		return this.replays;
	}

	public boolean isRunInBackground() {
		return this.runInBackground;
	}

	public void setRunInBackground(final boolean runInBackground) {
		this.runInBackground = runInBackground;
	}

}
