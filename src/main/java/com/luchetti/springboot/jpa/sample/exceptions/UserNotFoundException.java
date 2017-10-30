package com.luchetti.springboot.jpa.sample.exceptions;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(Long userId) {
		super("could not find user '" + userId + "'.");
	}

}
