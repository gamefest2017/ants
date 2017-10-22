package com.ibm.sk.ff.gui.client;

import com.ibm.sk.ff.gui.common.GUIOperations;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectCrate;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.ResultData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;

public class Step {
	private GUIOperations operation;
	private GUIObjectCrate guiCrate;
	private ScoreData scoreData;
	private CreateGameData createGame;
	private ResultData resultData;

	public Step(GUIOperations operation, GUIObjectCrate guiCrate) {
		this(operation, guiCrate, null, null, null);
	}

	public Step(GUIOperations operation, ScoreData scoreData) {
		this(operation, new GUIObjectCrate(), scoreData, null, null);
	}

	public Step(GUIOperations operation, CreateGameData createGame) {
		this(operation, new GUIObjectCrate(), null, createGame, null);
	}

	public Step(GUIOperations operation, ResultData resultData) {
		this(operation, new GUIObjectCrate(), null, null, resultData);
	}

	private Step(GUIOperations operation, GUIObjectCrate guiCrate, ScoreData scoreData, CreateGameData createGame,
			ResultData resultData) {
		this.operation = operation;
		this.guiCrate = guiCrate;
		this.scoreData = scoreData;
		this.createGame = createGame;
		this.resultData = resultData;
	}

	public Step() {
	}

	public ResultData getResultData() {
		return resultData;
	}

	public CreateGameData getCreateGame() {
		return createGame;
	}

	public ScoreData getScoreData() {
		return scoreData;
	}

	public GUIObjectCrate getGuiCrate() {
		return guiCrate;
	}

	public GUIOperations getOperation() {
		return operation;
	}
}