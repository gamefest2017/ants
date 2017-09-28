package com.ibm.sk.engine.exceptions;

public class InvalidWorldPositionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public InvalidWorldPositionException(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
