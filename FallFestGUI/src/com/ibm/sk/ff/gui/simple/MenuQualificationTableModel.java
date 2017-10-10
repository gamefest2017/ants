package com.ibm.sk.ff.gui.simple;

import javax.swing.table.DefaultTableModel;

import com.ibm.sk.dto.qualification.Candidate;
import com.ibm.sk.dto.qualification.Qualification;

public class MenuQualificationTableModel extends DefaultTableModel {

	private Qualification qualification;

	MenuQualificationTableModel(Qualification qualification) {
		this.qualification = qualification == null ? new Qualification() : qualification;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "ID";
		case 1:
			return "Name";
		case 2:
			return "Qualified?";
		case 3:
			return "#1";
		case 4:
			return "#2";
		case 5:
			return "#3";
		case 6:
			return "AVG";
		default:
			return "";
		}
	}

	@Override
	public int getRowCount() {
		if (qualification == null || qualification.getCandidates() == null) {
			return 0;
		}
		
		return qualification.getCandidates().size();
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public Object getValueAt(int row, int column) {

		Candidate candidate = qualification.getCandidate(row);

		switch (column) {
		case 0:
			return candidate.getId();
		case 1:
			return candidate.getName();
		case 2:
			return candidate.isQualified() ? "Y" : "N";
		case 3:
			Long score1 = candidate.getScore(0);
			return score1 == null ? "" : score1;
		case 4:
			Long score2 = candidate.getScore(1);
			return score2 == null ? "" : score2;
		case 5:
			Long score3 = candidate.getScore(2);
			return score3 == null ? "" : score3;
		case 6:
			Long scoreAvg = candidate.getAvgScore();
			return scoreAvg == null ? "" : scoreAvg;
		}

		return "???";
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
}
