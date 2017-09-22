package com.ibm.sk.engine.exceptions;

public class MoveException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public MoveException(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
