package com.greenapper.handlers;

import com.greenapper.dtos.ErrorDTO;
import com.greenapper.dtos.ValidationErrorDTO;
import com.greenapper.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity handle(RuntimeException ex) {
		try {
			final HttpStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class).code();
			if (ex instanceof ValidationException)
				return new ResponseEntity<>(new ValidationErrorDTO((ValidationException) ex), responseStatus);
			return new ResponseEntity<>(new ErrorDTO(ex), responseStatus);
		} catch (NullPointerException e) {
			LOG.error("Unhandled exception", ex);
			return new ResponseEntity<>(new ErrorDTO(ex), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}