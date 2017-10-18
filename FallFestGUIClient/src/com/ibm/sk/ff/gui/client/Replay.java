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
}