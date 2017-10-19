package com.ibm.sk.ff.gui.client;

import com.ibm.sk.ff.gui.common.GUIOperations;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObject;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.ResultData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;

public class Step {
    private GUIOperations operation;
    private GUIObject[] guiData;
    private ScoreData scoreData;
    private CreateGameData createGame;
    private ResultData resultData;

    public Step(GUIOperations operation, GUIObject[] operationData) {
    	this(operation, operationData, null, null, null);
    }
    
    public Step(GUIOperations operation, ScoreData scoreData) {
    	this(operation, null, scoreData, null, null);
    }
    
    public Step(GUIOperations operation, CreateGameData createGame) {
    	this(operation, null, null, createGame, null);
    }
    
    public Step(GUIOperations operation, ResultData resultData) {
    	this(operation, null, null, null, resultData);
    }
    
    private Step(GUIOperations operation, GUIObject[] guiData, ScoreData scoreData, CreateGameData createGame, ResultData resultData) {
    	this.operation = operation;
    	this.guiData = guiData;
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

    public GUIObject[] getOperationData() {
        return guiData;
    }

    public GUIOperations getOperation() {
        return operation;
    }
}