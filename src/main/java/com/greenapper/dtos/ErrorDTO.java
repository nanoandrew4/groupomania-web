package com.greenapper.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "DTO that represents exceptions that occur internally", subTypes = ValidationErrorDTO.class)
public class ErrorDTO {
	@ApiModelProperty(value = "Class name of the exception thrown internally", required = true)
	private String exception;

	@ApiModelProperty(value = "Message associated with the thrown exception", required = true)
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
