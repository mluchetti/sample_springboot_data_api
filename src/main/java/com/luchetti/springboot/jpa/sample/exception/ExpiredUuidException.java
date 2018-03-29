package com.luchetti.springboot.jpa.sample.exception;

public class ExpiredUuidException extends Exception {

	public ExpiredUuidException(String message) {
		super(message);
	}

	private static final long serialVersionUID = -2573415758549362320L;

}
