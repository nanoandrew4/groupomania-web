package com.greenapper.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnknownIdentifierException extends RuntimeException {
	public UnknownIdentifierException(final String message) {
		super(message);
	}
}
