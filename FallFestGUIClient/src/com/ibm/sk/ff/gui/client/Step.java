package com.ibm.sk.ff.gui.client;

import com.ibm.sk.ff.gui.common.GUIOperations;

public class Step {
    private GUIOperations operation;
    private Object operationData;

    public Step(GUIOperations operation, Object operationData) {
        this.operation = operation;
        this.operationData = operationData;
    }

    public Step() {
    }

    public Object getOperationData() {
        return operationData;
    }

    public GUIOperations getOperation() {
        return operation;
    }
}