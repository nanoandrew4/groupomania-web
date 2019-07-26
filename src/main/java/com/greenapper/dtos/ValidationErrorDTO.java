package com.greenapper.dtos;

import com.greenapper.exceptions.ValidationException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel(description = "Error DTO used when validation errors are found in forms sent to the server", parent = ErrorDTO.class)
public class ValidationErrorDTO extends ErrorDTO {

	@ApiModelProperty(value = "List of validation errors, converted to the users locale")
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

	public void setValidationErrors(final List<String> validationErrors) {
		this.validationErrors = validationErrors;
	}
}