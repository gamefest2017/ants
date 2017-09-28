package com.ibm.sk.dto;

import java.awt.*;
import java.io.Serializable;
import java.util.Map;

public class Step implements Serializable {

    private int turn;
    private Map<Point, IWorldObject> grid;

    public Step(int turn, Map<Point, IWorldObject> grid) {
        this.turn = turn;
        this.grid = grid;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Map<Point, IWorldObject> getGrid() {
        return grid;
    }

    public void setGrid(Map<Point, IWorldObject> grid) {
        this.grid = grid;
    }
}
