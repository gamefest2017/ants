package com.ibm.sk.dto;

import java.io.Serializable;
import java.util.List;

public class Step implements Serializable {

    private int turn;
    private List<IWorldObject> worldObjectsInTurn;
    
    public Step(int turn, List<IWorldObject> worldObjectsInTurn) {
        this.turn = turn;
        this.worldObjectsInTurn = worldObjectsInTurn;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

	public List<IWorldObject> getWorldObjectsInTurn() {
		return worldObjectsInTurn;
	}

	public void setWorldObjectsInTurn(List<IWorldObject> worldObjectsInTurn) {
		this.worldObjectsInTurn = worldObjectsInTurn;
	}

}
