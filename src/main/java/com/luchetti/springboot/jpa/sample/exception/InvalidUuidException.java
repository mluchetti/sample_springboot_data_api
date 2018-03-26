package com.luchetti.springboot.jpa.sample.exception;

public class InvalidUuidException extends Exception {

	public InvalidUuidException(String message) {
		super(message);
	}

	private static final long serialVersionUID = -1013346917279601459L;

}
