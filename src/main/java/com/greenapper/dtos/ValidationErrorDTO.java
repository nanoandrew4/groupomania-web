package com.greenapper.dtos;

import com.greenapper.exceptions.ValidationException;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationErrorDTO extends ErrorDTO {

	private List<String> validationErrors;

	private ValidationErrorDTO(final Exception e) {
		super(e);
	}

	public ValidationErrorDTO(final ValidationException e) {
		super(e);
		this.validationErrors = e.getErrors().getAllErrors().stream().map(ObjectError::getCode).collect(Collectors.toList());
	}

	public List<String> getValidationErrors() {
		return validationErrors;
	}
}