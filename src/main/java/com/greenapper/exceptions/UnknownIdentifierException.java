package com.greenapper.exceptions;

public class UnknownIdentifierException extends RuntimeException {
	public UnknownIdentifierException(final String message) {
		super(message);
	}
}
