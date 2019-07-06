package com.greenapper.dtos;

public class ErrorDTO {
	private String exception;

	private String message;

	private ErrorDTO() {

	}

	public ErrorDTO(final Exception e) {
		this.exception = e.getClass().getSimpleName();
		this.message = e.getMessage();
	}

	public String getException() {
		return exception;
	}

	public String getMessage() {
		return message;
	}
}
