package com.ibm.sk.ff.gui.client;

import java.util.List;

public class Replay {
	private List<Step> steps;
	private String replayName;

	public Replay(List<Step> steps, String replayName) {
		this.steps = steps;
		this.replayName = replayName;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	public String getReplayName() {
		return replayName;
	}

	public void setReplayName(String replayName) {
		this.replayName = replayName;
	}

	public void play(GUIFacade facade) {
		facade.setRecord(false);
		for (Step it : steps) {
			switch (it.getOperation()) {
			case CLOSE:
				break;
			case CREATE_GAME:
				facade.createGame(it.getCreateGame());
				break;
			case EVENT_POLL:
				break;
			case REMOVE:
				facade.remove(it.getGuiCrate().dump());
				break;
			case SCORE:
				facade.showScore(it.getScoreData());
				break;
			case SET:
				facade.set(it.getGuiCrate().dump());
				break;
			case SHOW_INIT_MENU:
				break;
			case SHOW_RESULT:
				facade.showResult(it.getResultData());
				break;
			default:
				break;
			}
		}
		facade.setRecord(true);
	}
}