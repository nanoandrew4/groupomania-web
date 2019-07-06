package com.greenapper.exceptions;

import org.springframework.validation.Errors;

public class ValidationException extends RuntimeException {
	private Errors errors;

	public ValidationException(final String message, final Errors errors) {
		super(message);
		this.errors = errors;
	}

	public Errors getErrors() {
		return errors;
	}

	public void setErrors(Errors errors) {
		this.errors = errors;
	}
}
